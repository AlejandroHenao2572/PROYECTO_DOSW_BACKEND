package com.sirha.proyecto_sirha_dosw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security con JWT.
 * 
 * <p>Esta clase configura:</p>
 * <ul>
 *   <li>Autenticación basada en JWT (sin sesiones)</li>
 *   <li>Autorización por roles (ADMIN, DECANO, ESTUDIANTE)</li>
 *   <li>Protección de endpoints según roles</li>
 *   <li>Codificación de contraseñas con BCrypt</li>
 * </ul>
 * 
 * <h3>Endpoints públicos (sin autenticación):</h3>
 * <ul>
 *   <li>POST /api/auth/login - Login de usuarios</li>
 *   <li>POST /api/usuarios/crear - Registro de usuarios</li>
 * </ul>
 * 
 * <h3>Endpoints protegidos por rol:</h3>
 * <ul>
 *   <li>ADMIN: /api/admin/** - Solo administradores</li>
 *   <li>DECANO: /api/decano/** - Solo decanos</li>
 *   <li>ESTUDIANTE: /api/estudiante/** - Solo estudiantes</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Bean que proporciona el codificador de contraseñas BCrypt.
     * 
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación que utiliza UserDetailsService y PasswordEncoder.
     * 
     * @return el proveedor de autenticación configurado
     */
    @Bean
    @SuppressWarnings("deprecation")
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean del AuthenticationManager para gestionar la autenticación.
     * 
     * @param config configuración de autenticación
     * @return el AuthenticationManager
     * @throws Exception si ocurre un error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP con JWT.
     * 
     * <h3>Configuración de seguridad:</h3>
     * <ul>
     *   <li>CSRF deshabilitado (no es necesario para APIs REST con JWT)</li>
     *   <li>Sesiones deshabilitadas (autenticación stateless con JWT)</li>
     *   <li>Filtro JWT antes del filtro de autenticación estándar</li>
     *   <li>Autorización basada en roles</li>
     * </ul>
     * 
     * @param http el objeto {@link HttpSecurity} para configurar
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (no es necesario para APIs REST stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar autorización de requests
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación) - Login y Registro
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                
                // Documentación Swagger (opcional, si lo usas)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // Endpoints solo para ADMINISTRADOR
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                
                // Endpoints solo para DECANO
                .requestMatchers("/api/decano/**").hasRole("DECANO")
                
                // Endpoints solo para ESTUDIANTE
                .requestMatchers("/api/estudiante/**").hasRole("ESTUDIANTE")
                
                // Endpoints que requieren autenticación (cualquier rol autenticado)
                .requestMatchers("/api/usuarios/**").authenticated()
                .requestMatchers("/api/carreras/**").authenticated()
                .requestMatchers("/api/materias/**").authenticated()
                .requestMatchers("/api/grupos/**").authenticated()
                .requestMatchers("/api/reportes/**").authenticated()
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            
            // Configurar sesiones como STATELESS (sin sesiones, solo JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configurar el proveedor de autenticación
            .authenticationProvider(authenticationProvider())
            
            // Agregar el filtro JWT antes del filtro de autenticación estándar
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
