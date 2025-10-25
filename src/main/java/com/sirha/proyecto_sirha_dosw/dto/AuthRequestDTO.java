package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la petición de login (autenticación).
 * 
 * <p>Este DTO se utiliza cuando un usuario intenta iniciar sesión.</p>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * POST /api/auth/login
 * {
 *   "email": "usuario@ejemplo.com",
 *   "password": "password123"
 * }
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {
    
    /**
     * Email del usuario (usado como username).
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;
    
    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
