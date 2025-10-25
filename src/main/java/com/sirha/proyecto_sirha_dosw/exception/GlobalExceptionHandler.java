package com.sirha.proyecto_sirha_dosw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación.
 * 
 * <p>Captura y formatea las excepciones más comunes para retornar
 * respuestas consistentes y amigables al cliente.</p>
 * 
 * <p>Maneja:</p>
 * <ul>
 *   <li>Errores de validación de campos (MethodArgumentNotValidException)</li>
 *   <li>Excepciones personalizadas del sistema (SirhaException)</li>
 *   <li>Excepciones generales del sistema</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de validación de campos en los DTOs.
     * 
     * <p>Cuando un campo no cumple con las validaciones definidas en el DTO
     * (como @NotBlank, @ValidPassword, etc.), esta excepción es lanzada.</p>
     * 
     * <p>Ejemplo de respuesta:</p>
     * <pre>
     * {
     *   "timestamp": "2025-10-19T18:30:45.123",
     *   "status": 400,
     *   "error": "Error de validación",
     *   "message": "Errores en los campos enviados",
     *   "errores": {
     *     "password": "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula...",
     *     "nombre": "El nombre no puede estar vacío"
     *   }
     * }
     * </pre>
     *
     * @param ex la excepción de validación capturada
     * @return ResponseEntity con los detalles del error y status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errores = new HashMap<>();
        
        // Extraer todos los errores de validación
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Error de validación");
        response.put("message", "Errores en los campos enviados");
        response.put("errores", errores);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja las excepciones personalizadas del sistema SIRHA.
     * 
     * <p>Ejemplo de respuesta:</p>
     * <pre>
     * {
     *   "timestamp": "2025-10-19T18:30:45.123",
     *   "status": 409,
     *   "error": "Error en la operación",
     *   "message": "El email ya está registrado"
     * }
     * </pre>
     *
     * @param ex la excepción personalizada capturada
     * @return ResponseEntity con los detalles del error y status 409 (Conflict)
     */
    @ExceptionHandler(SirhaException.class)
    public ResponseEntity<Map<String, Object>> handleSirhaException(SirhaException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Error en la operación");
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Maneja cualquier otra excepción no capturada específicamente.
     * 
     * <p>Ejemplo de respuesta:</p>
     * <pre>
     * {
     *   "timestamp": "2025-10-19T18:30:45.123",
     *   "status": 500,
     *   "error": "Error interno del servidor",
     *   "message": "Ha ocurrido un error inesperado"
     * }
     * </pre>
     *
     * @param ex la excepción general capturada
     * @return ResponseEntity con los detalles del error y status 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Error interno del servidor");
        response.put("message", "Ha ocurrido un error inesperado: " + ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
