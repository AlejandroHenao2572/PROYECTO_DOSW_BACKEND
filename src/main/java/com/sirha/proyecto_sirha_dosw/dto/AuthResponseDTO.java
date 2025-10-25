package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de autenticación exitosa.
 * 
 * <p>Este DTO se retorna cuando un usuario inicia sesión correctamente.</p>
 * <p>Contiene el token JWT que el cliente debe incluir en las siguientes peticiones.</p>
 * 
 * <p>Ejemplo de respuesta:</p>
 * <pre>
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "email": "usuario@ejemplo.com",
 *   "rol": "ESTUDIANTE",
 *   "nombre": "Juan Pérez"
 * }
 * </pre>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    
    /**
     * Token JWT generado para la sesión.
     * El cliente debe incluir este token en el header Authorization como "Bearer {token}".
     */
    private String token;
    
    /**
     * Email del usuario autenticado.
     */
    private String email;
    
    /**
     * Rol del usuario (ADMIN, DECANO, ESTUDIANTE).
     */
    private Rol rol;
    
    /**
     * Nombre completo del usuario.
     */
    private String nombre;
}
