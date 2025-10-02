package com.sirha.proyecto_sirha_dosw.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Clase de pruebas unitarias para la configuración de seguridad de la aplicación.
 * Verifica que los beans de seguridad se configuren correctamente sin cargar el contexto completo.
 */
class SecurityConfigUnitTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    @DisplayName("Debe crear el bean PasswordEncoder correctamente")
    void testPasswordEncoderBean() {
        // Given & When
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(encoder, "El PasswordEncoder no debe ser null");
        assertTrue(encoder.encode("test").startsWith("$2a$"), 
                "Debe usar BCryptPasswordEncoder que genera hashes con prefijo $2a$");
    }

    @Test
    @DisplayName("Debe crear el bean UserDetailsService con usuario admin configurado")
    void testUserDetailsServiceBean() {
        // Given & When
        InMemoryUserDetailsManager manager = securityConfig.userDetailsService();

        // Then
        assertNotNull(manager, "El UserDetailsManager no debe ser null");
        assertTrue(manager.userExists("admin"), "El usuario 'admin' debe existir");
    }

    @Test
    @DisplayName("Debe cargar el usuario admin con las credenciales correctas")
    void testLoadUserByUsername() {
        // Given
        String username = "admin";
        InMemoryUserDetailsManager userDetailsManager = securityConfig.userDetailsService();

        // When
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // Then
        assertNotNull(userDetails, "Los detalles del usuario no deben ser null");
        assertEquals(username, userDetails.getUsername(), "El nombre de usuario debe ser 'admin'");
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")), 
                "El usuario debe tener el rol 'ROLE_USER'");
        assertTrue(userDetails.isEnabled(), "El usuario debe estar habilitado");
        assertTrue(userDetails.isAccountNonExpired(), "La cuenta no debe estar expirada");
        assertTrue(userDetails.isAccountNonLocked(), "La cuenta no debe estar bloqueada");
        assertTrue(userDetails.isCredentialsNonExpired(), "Las credenciales no deben estar expiradas");
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar usuario inexistente")
    void testLoadUserByUsernameNotFound() {
        // Given
        String nonExistentUser = "usuario_inexistente";
        InMemoryUserDetailsManager userDetailsManager = securityConfig.userDetailsService();

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
                () -> userDetailsManager.loadUserByUsername(nonExistentUser),
                "Debe lanzar UsernameNotFoundException para usuarios inexistentes");
        
        assertNotNull(exception, "La excepción no debe ser null");
    }

    @Test
    @DisplayName("Debe validar que la contraseña del usuario admin se encripta correctamente")
    void testUserPasswordEncryption() {
        // Given
        String rawPassword = "admin";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        InMemoryUserDetailsManager userDetailsManager = securityConfig.userDetailsService();
        UserDetails userDetails = userDetailsManager.loadUserByUsername("admin");

        // When & Then
        assertTrue(passwordEncoder.matches(rawPassword, userDetails.getPassword()),
                "La contraseña encriptada debe coincidir con la contraseña en texto plano");
        assertNotEquals(rawPassword, userDetails.getPassword(),
                "La contraseña almacenada debe estar encriptada, no en texto plano");
    }

    @Test
    @DisplayName("Debe verificar que la configuración de SecurityConfig puede ser instanciada")
    void testSecurityConfigInstantiation() {
        // Given & When & Then
        assertNotNull(securityConfig, "La instancia de SecurityConfig no debe ser null");
        assertDoesNotThrow(() -> {
            securityConfig.passwordEncoder();
            securityConfig.userDetailsService();
        }, "Los métodos de SecurityConfig deben ejecutarse sin lanzar excepciones");
    }

    @Test
    @DisplayName("Debe verificar que PasswordEncoder funciona correctamente para diferentes contraseñas")
    void testPasswordEncoderFunctionality() {
        // Given
        String password1 = "password123";
        String password2 = "differentPassword";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);

        // Then
        assertNotNull(encoded1, "La contraseña encriptada no debe ser null");
        assertNotNull(encoded2, "La contraseña encriptada no debe ser null");
        assertNotEquals(encoded1, encoded2, "Contraseñas diferentes deben generar hashes diferentes");
        
        assertTrue(passwordEncoder.matches(password1, encoded1),
                "La primera contraseña debe coincidir con su hash");
        assertTrue(passwordEncoder.matches(password2, encoded2),
                "La segunda contraseña debe coincidir con su hash");
        
        assertFalse(passwordEncoder.matches(password1, encoded2),
                "La primera contraseña no debe coincidir con el hash de la segunda");
        assertFalse(passwordEncoder.matches(password2, encoded1),
                "La segunda contraseña no debe coincidir con el hash de la primera");
    }

    @Test
    @DisplayName("Debe verificar que el usuario admin tiene exactamente un rol")
    void testUserRoles() {
        // Given
        InMemoryUserDetailsManager userDetailsManager = securityConfig.userDetailsService();
        UserDetails userDetails = userDetailsManager.loadUserByUsername("admin");

        // When & Then
        assertEquals(1, userDetails.getAuthorities().size(),
                "El usuario admin debe tener exactamente un rol");
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())),
                "El usuario admin debe tener el rol ROLE_USER");
    }

    @Test
    @DisplayName("Debe verificar que solo existe un usuario en el sistema")
    void testOnlyAdminUserExists() {
        // Given
        InMemoryUserDetailsManager manager = securityConfig.userDetailsService();

        // When & Then
        assertTrue(manager.userExists("admin"), "El usuario 'admin' debe existir");
        assertFalse(manager.userExists("user"), "No debe existir el usuario 'user'");
        assertFalse(manager.userExists("root"), "No debe existir el usuario 'root'");
        assertFalse(manager.userExists("guest"), "No debe existir el usuario 'guest'");
    }

    @Test
    @DisplayName("Debe verificar que BCryptPasswordEncoder genera hashes únicos para la misma contraseña")
    void testPasswordEncoderGeneratesUniqueHashes() {
        // Given
        String password = "samePassword";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // Then
        assertNotEquals(hash1, hash2, 
                "BCryptPasswordEncoder debe generar hashes únicos incluso para la misma contraseña");
        assertTrue(passwordEncoder.matches(password, hash1),
                "El primer hash debe ser válido para la contraseña");
        assertTrue(passwordEncoder.matches(password, hash2),
                "El segundo hash debe ser válido para la contraseña");
    }

    @Test
    @DisplayName("Debe verificar la integración entre PasswordEncoder y UserDetailsService")
    void testPasswordEncoderIntegrationWithUserDetailsService() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        InMemoryUserDetailsManager userDetailsManager = securityConfig.userDetailsService();
        UserDetails adminUser = userDetailsManager.loadUserByUsername("admin");
        String rawPassword = "admin";

        // When & Then
        assertTrue(passwordEncoder.matches(rawPassword, adminUser.getPassword()),
                "El PasswordEncoder debe poder validar la contraseña del usuario admin");
        
        // Verificar que una contraseña incorrecta no funciona
        assertFalse(passwordEncoder.matches("wrongPassword", adminUser.getPassword()),
                "Una contraseña incorrecta no debe ser válida");
    }

    @Test
    @DisplayName("Debe verificar que la configuración crea beans independientes")
    void testBeansAreIndependent() {
        // Given & When
        PasswordEncoder encoder1 = securityConfig.passwordEncoder();
        PasswordEncoder encoder2 = securityConfig.passwordEncoder();
        InMemoryUserDetailsManager manager1 = securityConfig.userDetailsService();
        InMemoryUserDetailsManager manager2 = securityConfig.userDetailsService();

        // Then
        assertNotSame(encoder1, encoder2, "Cada llamada debe crear una nueva instancia de PasswordEncoder");
        assertNotSame(manager1, manager2, "Cada llamada debe crear una nueva instancia de UserDetailsManager");
        
        // Pero ambos deben funcionar de la misma manera
        String password = "testPassword";
        String hash1 = encoder1.encode(password);
        String hash2 = encoder2.encode(password);
        
        assertTrue(encoder1.matches(password, hash1), "El primer encoder debe validar su propio hash");
        assertTrue(encoder2.matches(password, hash2), "El segundo encoder debe validar su propio hash");
        assertTrue(encoder1.matches(password, hash2), "Los encoders deben ser intercambiables");
        assertTrue(encoder2.matches(password, hash1), "Los encoders deben ser intercambiables");
    }
}