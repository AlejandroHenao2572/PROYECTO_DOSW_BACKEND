package com.sirha.proyecto_sirha_dosw.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWPATTERN = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#\\-_])[A-Za-z\\d@$!%*?&.#\\-_]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWPATTERN);

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
            String errorMessage = buildErrorMessage(password);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                   .addConstraintViolation();
            return false;
        }

        return true;
    }

    /**
     * Construye un mensaje de error detallado basado en los requisitos no cumplidos.
     *
     * @param password La contraseña a validar
     * @return Mensaje de error personalizado
     */
    private String buildErrorMessage(String password) {
        StringBuilder errorMessage = new StringBuilder("La contraseña no cumple con los requisitos: ");
        StringBuilder errors = new StringBuilder();

        appendErrorIfNeeded(errors, password.length() < 8, "debe tener mínimo 8 caracteres");
        appendErrorIfNeeded(errors, !hasLowercase(password), "debe contener al menos una minúscula");
        appendErrorIfNeeded(errors, !hasUppercase(password), "debe contener al menos una mayúscula");
        appendErrorIfNeeded(errors, !hasDigit(password), "debe contener al menos un número");
        appendErrorIfNeeded(errors, !hasSpecialChar(password), "debe contener al menos un carácter especial (@$!%*?&.#-_)");
        appendErrorIfNeeded(errors, !hasOnlyAllowedChars(password), "contiene caracteres no permitidos");

        return errorMessage.append(errors).toString();
    }

    /**
     * Agrega un mensaje de error si la condición se cumple.
     *
     * @param errors StringBuilder que acumula los errores
     * @param condition Condición que indica si hay un error
     * @param message Mensaje de error a agregar
     */
    private void appendErrorIfNeeded(StringBuilder errors, boolean condition, String message) {
        if (condition) {
            if (!errors.isEmpty()) {
                errors.append(", ");
            }
            errors.append(message);
        }
    }

    /**
     * Verifica si la contraseña contiene al menos una minúscula.
     * Usa iteración en lugar de regex para evitar ReDoS.
     */
    private boolean hasLowercase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la contraseña contiene al menos una mayúscula.
     * Usa iteración en lugar de regex para evitar ReDoS.
     */
    private boolean hasUppercase(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la contraseña contiene al menos un dígito.
     * Usa iteración en lugar de regex para evitar ReDoS.
     */
    private boolean hasDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la contraseña contiene al menos un carácter especial.
     * Usa iteración en lugar de regex para evitar ReDoS.
     */
    private boolean hasSpecialChar(String password) {
        String specialChars = "@$!%*?&.#-_";
        for (int i = 0; i < password.length(); i++) {
            if (specialChars.indexOf(password.charAt(i)) >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la contraseña solo contiene caracteres permitidos.
     * Usa iteración en lugar de regex para evitar ReDoS.
     */
    private boolean hasOnlyAllowedChars(String password) {
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            boolean isValid = Character.isLetterOrDigit(c) || "@$!%*?&.#-_".indexOf(c) >= 0;
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
}