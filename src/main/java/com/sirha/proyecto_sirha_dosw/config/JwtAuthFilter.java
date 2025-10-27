package com.sirha.proyecto_sirha_dosw.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada petición HTTP para validar el token JWT.
 * 
 * <p>Este filtro:</p>
 * <ul>
 *   <li>Extrae el token JWT del header Authorization</li>
 *   <li>Valida el token y extrae el email del usuario</li>
 *   <li>Carga los detalles del usuario desde la base de datos</li>
 *   <li>Establece la autenticación en el contexto de seguridad de Spring</li>
 * </ul>
 * 
 * <p>Se ejecuta una vez por cada petición HTTP.</p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Filtra cada petición HTTP para validar el token JWT.
     * 
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de I/O
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Obtener la ruta de la petición
        final String requestPath = request.getRequestURI();
        
        // Lista de endpoints públicos que no requieren autenticación
        if (requestPath.equals("/api/auth/login") || 
            requestPath.equals("/api/usuarios/login") ||
            requestPath.startsWith("/swagger-ui") ||
            requestPath.startsWith("/v3/api-docs") ||
            requestPath.startsWith("/swagger-resources") ||
            requestPath.startsWith("/webjars")) {
            // Permitir acceso sin validar token
            filterChain.doFilter(request, response);
            return;
        }
        
        // Obtener el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Si no hay header Authorization o no comienza con "Bearer ", continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT (eliminar "Bearer " del inicio)
        jwt = authHeader.substring(7);
        
        try {
            // Extraer el email del usuario del token
            userEmail = jwtService.extractEmail(jwt);

            // Si el email existe y el usuario no está autenticado aún
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Cargar los detalles del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Si el token es válido, establecer la autenticación
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si hay cualquier error (token inválido, expirado, etc.), simplemente no autenticar
            // y dejar que Spring Security maneje el acceso no autorizado
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
