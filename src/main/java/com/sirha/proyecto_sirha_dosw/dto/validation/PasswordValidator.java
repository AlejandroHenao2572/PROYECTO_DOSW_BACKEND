package com.sirha.proyecto_sirha_dosw.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para contraseñas seguras.
 * 
 * <p>Valida que una contraseña cumpla con los siguientes requisitos:</p>
 * <ul>
 *   <li>Mínimo 8 caracteres de longitud</li>
 *   <li>Al menos una letra mayúscula (A-Z)</li>
 *   <li>Al menos una letra minúscula (a-z)</li>
 *   <li>Al menos un dígito (0-9)</li>
 *   <li>Al menos un carácter especial (@$!%*?&.#-_)</li>
 *   <li>Solo caracteres alfanuméricos y especiales permitidos</li>
 * </ul>
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * Patrón de expresión regular para validar contraseñas.
     * 
     * <p>Explicación del patrón:</p>
     * <ul>
     *   <li>^                      - Inicio de la cadena</li>
     *   <li>(?=.*[a-z])            - Al menos una minúscula (lookahead positivo)</li>
     *   <li>(?=.*[A-Z])            - Al menos una mayúscula (lookahead positivo)</li>
     *   <li>(?=.*\d)               - Al menos un dígito (lookahead positivo)</li>
     *   <li>(?=.*[@$!%*?&.#\-_])   - Al menos un carácter especial (lookahead positivo)</li>
     *   <li>[A-Za-z\d@$!%*?&.#\-_]{8,} - Solo caracteres válidos, mínimo 8</li>
     *   <li>$                      - Fin de la cadena</li>
     * </ul>
     */
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#\\-_])[A-Za-z\\d@$!%*?&.#\\-_]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // No se requiere inicialización especial
    }

    /**
     * Valida si la contraseña cumple con los requisitos de seguridad.
     *
     * @param password La contraseña a validar
     * @param context El contexto de validación
     * @return true si la contraseña es válida, false en caso contrario
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Si la contraseña es nula o vacía, se delega a @NotNull/@NotBlank
        if (password == null || password.isEmpty()) {
            return true;
        }

        // Validación del patrón
        if (!pattern.matcher(password).matches()) {
            // Personalizar el mensaje de error con detalles específicos
            context.disableDefaultConstraintViolation();
            
            StringBuilder errorMessage = new StringBuilder("La contraseña no cumple con los requisitos: ");
            boolean hasErrors = false;
            
            if (password.length() < 8) {
                errorMessage.append("debe tener mínimo 8 caracteres");
                hasErrors = true;
            }
            
            if (!password.matches(".*[a-z].*")) {
                if (hasErrors) errorMessage.append(", ");
                errorMessage.append("debe contener al menos una minúscula");
                hasErrors = true;
            }
            
            if (!password.matches(".*[A-Z].*")) {
                if (hasErrors) errorMessage.append(", ");
                errorMessage.append("debe contener al menos una mayúscula");
                hasErrors = true;
            }
            
            if (!password.matches(".*\\d.*")) {
                if (hasErrors) errorMessage.append(", ");
                errorMessage.append("debe contener al menos un número");
                hasErrors = true;
            }
            
            if (!password.matches(".*[@$!%*?&.#\\-_].*")) {
                if (hasErrors) errorMessage.append(", ");
                errorMessage.append("debe contener al menos un carácter especial (@$!%*?&.#-_)");
                hasErrors = true;
            }
            
            if (!password.matches("^[A-Za-z\\d@$!%*?&.#\\-_]+$")) {
                if (hasErrors) errorMessage.append(", ");
                errorMessage.append("contiene caracteres no permitidos");
            }
            
            context.buildConstraintViolationWithTemplate(errorMessage.toString())
                   .addConstraintViolation();
            
            return false;
        }

        return true;
    }
}