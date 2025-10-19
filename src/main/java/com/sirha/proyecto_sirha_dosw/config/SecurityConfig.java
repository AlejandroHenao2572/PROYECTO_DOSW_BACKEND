package com.sirha.proyecto_sirha_dosw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * <p>
 * Esta clase configura los aspectos de seguridad de la aplicación, incluyendo:
 * <ul>
 *   <li>El codificador de contraseñas (BCrypt)</li>
 *   <li>La cadena de filtros de seguridad HTTP</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <b>Nota: </b> Por el momento, la configuración permite todas las peticiones sin autenticación.
 * Para producción, se debe configurar adecuadamente la autorización y autenticación.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean que proporciona el codificador de contraseñas BCrypt.
     * 
     * <p>
     * BCrypt es un algoritmo de hashing adaptativo que incluye un salt automático
     * y es resistente a ataques de fuerza bruta debido a su naturaleza computacionalmente costosa.
     * </p>
     * 
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * 
     * <p>
     * <b>Configuración actual:</b> Todas las peticiones están permitidas sin autenticación.
     * Esto es útil para desarrollo inicial.
     * </p>
     * 
     * <p>
     * <b>Para producción:</b> Se debe configurar adecuadamente la autorización por roles,
     * la autenticación JWT, y proteger los endpoints sensibles.
     * </p>
     * 
     * @param http el objeto {@link HttpSecurity} para configurar
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permitir todas las peticiones (solo para desarrollo)
            );
        
        return http.build();
    }
}
