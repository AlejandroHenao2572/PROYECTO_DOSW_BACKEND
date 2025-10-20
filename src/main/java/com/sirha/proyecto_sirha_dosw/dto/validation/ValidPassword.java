package com.sirha.proyecto_sirha_dosw.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Anotación para validar contraseñas seguras.
 * 
 * <p>Requisitos de la contraseña:</p>
 * <ul>
 *   <li>Mínimo 8 caracteres</li>
 *   <li>Al menos una letra mayúscula</li>
 *   <li>Al menos una letra minúscula</li>
 *   <li>Al menos un número</li>
 *   <li>Al menos un carácter especial (@$!%*?&.#-_)</li>
 * </ul>
 * 
 * <p>Ejemplo de uso:</p>
 * <pre>
 * {@code
 * public class UsuarioDTO {
 *     @ValidPassword
 *     private String password;
 * }
 * }
 * </pre>
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    
    /**
     * Mensaje de error por defecto.
     */
    String message() default "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&.#-_)";
    
    /**
     * Grupos de validación.
     */
    Class<?>[] groups() default {};
    
    /**
     * Payload adicional.
     */
    Class<? extends Payload>[] payload() default {};
}
