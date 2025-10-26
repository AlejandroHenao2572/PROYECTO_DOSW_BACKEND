package com.sirha.proyecto_sirha_dosw.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para la generación y validación de tokens JWT.
 * 
 * <p>Este servicio maneja:</p>
 * <ul>
 *   <li>Generación de tokens JWT con información del usuario</li>
 *   <li>Extracción de información (email, fecha de expiración) del token</li>
 *   <li>Validación de tokens (firma y expiración)</li>
 * </ul>
 */
@Service
public class JwtService {
    
    /**
     * Clave secreta para firmar los tokens JWT.
     * En producción, esta clave debe estar en variables de entorno.
     * La clave debe tener al menos 256 bits (32 caracteres).
     */
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    
    /**
     * Tiempo de expiración del token: 24 horas en milisegundos.
     */
    private static final long JWT_EXPIRATION = 1000L * 60 * 60 * 24; // 24 horas

    /**
     * Extrae el email (subject) del token JWT.
     * 
     * @param token el token JWT
     * @return el email del usuario
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae una claim específica del token usando una función.
     * 
     * @param token el token JWT
     * @param claimsResolver función para extraer la claim deseada
     * @param <T> tipo de la claim
     * @return el valor de la claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para el usuario.
     * 
     * @param userDetails detalles del usuario autenticado
     * @return el token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Agregar el rol como claim personalizado
        if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
            // Obtiene el primer rol (Spring lo guarda como "ROLE_ADMINISTRADOR", etc.)
            String authority = userDetails.getAuthorities().iterator().next().getAuthority();
            // Elimina el prefijo "ROLE_" si existe
            String role = authority.startsWith("ROLE_") ? authority.substring(5) : authority;
            claims.put("role", role);
        }
        return generateToken(claims, userDetails);
    }
    /**
     * Extrae el rol del usuario desde el token JWT.
     * @param token el token JWT
     * @return el rol del usuario (ADMINISTRADOR, DECANO, ESTUDIANTE, etc.) o null si no existe
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        Object roleObj = claims.get("role");
        return roleObj != null ? roleObj.toString() : null;
como e    }

    /**
     * Genera un token JWT con claims adicionales.
     * 
     * @param extraClaims claims adicionales a incluir en el token
     * @param userDetails detalles del usuario autenticado
     * @return el token JWT generado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // El email del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Fecha de expiración
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Firma con HMAC-SHA256
                .compact();
    }

    /**
     * Valida si el token es válido para el usuario.
     * Verifica que el email coincida y que el token no haya expirado.
     * 
     * @param token el token JWT
     * @param userDetails detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token ha expirado.
     * 
     * @param token el token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     * 
     * @param token el token JWT
     * @return la fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todas las claims del token JWT.
     * 
     * @param token el token JWT
     * @return las claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT.
     * 
     * @return la clave de firma
     */
    private Key getSignInKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
