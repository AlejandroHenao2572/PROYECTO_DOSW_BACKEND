package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.AuthRequestDTO;
import com.sirha.proyecto_sirha_dosw.dto.AuthResponseDTO;
import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación y registro de usuarios.
 * 
 * <p>Endpoints disponibles:</p>
 * <ul>
 *   <li>POST /api/auth/register - Registro de nuevos usuarios</li>
 *   <li>POST /api/auth/login - Login de usuarios</li>
 * </ul>
 * 
 * <h3>Ejemplo de registro:</h3>
 * <pre>
 * POST /api/auth/register
 * Content-Type: application/json
 * 
 * {
 *   "nombre": "Juan",
 *   "apellido": "Pérez",
 *   "email": "juan.perez@estudiantes.edu.co",
 *   "password": "password123",
 *   "rol": "ESTUDIANTE",
 *   "facultad": "INGENIERIA"
 * }
 * </pre>
 * 
 * <h3>Ejemplo de login:</h3>
 * <pre>
 * POST /api/auth/login
 * Content-Type: application/json
 * 
 * {
 *   "email": "juan.perez@estudiantes.edu.co",
 *   "password": "password123"
 * }
 * 
 * Respuesta exitosa (200 OK):
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "email": "juan.perez@estudiantes.edu.co",
 *   "rol": "ESTUDIANTE",
 *   "nombre": "Juan Pérez"
 * }
 * </pre>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para registro y autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     * 
     * <p>Registra un usuario con rol de ESTUDIANTE, DECANO o ADMINISTRADOR.</p>
     * <p>La contraseña se encripta automáticamente con BCrypt.</p>
     * 
     * @param usuarioDTO DTO con los datos del nuevo usuario
     * @return ResponseEntity con el token JWT y la información del usuario registrado
     */
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema SIRHA. El usuario puede tener roles de ESTUDIANTE, " +
                     "DECANO o ADMINISTRADOR. La contraseña se encripta automáticamente. " +
                     "Retorna un token JWT para usar inmediatamente después del registro.",
        tags = {"Autenticación"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponseDTO.class),
                examples = @ExampleObject(
                    name = "Registro exitoso",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE",
                        "nombre": "Juan Pérez"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = "{\"error\": \"Los datos proporcionados no son válidos\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email ya registrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Email duplicado",
                    value = "{\"error\": \"El email ya está registrado en el sistema\"}"
                )
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(
        @Parameter(
            description = "Datos del usuario a registrar. Incluye nombre, apellido, email, password, rol y facultad (opcional).",
            required = true,
            schema = @Schema(implementation = UsuarioDTO.class)
        )
        @Valid @RequestBody UsuarioDTO usuarioDTO
    ) {
        try {
            AuthResponseDTO response = authService.register(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SirhaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error al registrar usuario: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para iniciar sesión (login).
     * 
     * <p>Autentica al usuario con email y contraseña, y retorna un token JWT.</p>
     * <p>El token debe incluirse en las siguientes peticiones en el header: Authorization: Bearer {token}</p>
     * 
     * @param request DTO con el email y contraseña del usuario
     * @return ResponseEntity con el token JWT y la información del usuario
     */
    @Operation(
        summary = "Iniciar sesión (Login)",
        description = "Autentica a un usuario con email y contraseña. Si las credenciales son correctas, " +
                     "retorna un token JWT que debe incluirse en el header Authorization como 'Bearer {token}' " +
                     "en las siguientes peticiones a endpoints protegidos.",
        tags = {"Autenticación"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AuthResponseDTO.class),
                examples = @ExampleObject(
                    name = "Login exitoso",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE",
                        "nombre": "Juan Pérez"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Error de autenticación",
                    value = "{\"error\": \"Credenciales inválidas\"}"
                )
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(
        @Parameter(
            description = "Credenciales de acceso (email y contraseña)",
            required = true,
            schema = @Schema(implementation = AuthRequestDTO.class)
        )
        @Valid @RequestBody AuthRequestDTO request
    ) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuario no encontrado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al procesar la solicitud"));
        }
    }

    /**
     * Clase interna para las respuestas de error.
     */
    private record ErrorResponse(String error) {}
}
