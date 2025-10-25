package com.sirha.proyecto_sirha_dosw.dto.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para la clase PasswordValidator.
 * 
 * Cubre todos los escenarios de validación de contraseñas:
 * - Contraseñas válidas
 * - Contraseñas inválidas (longitud, caracteres, requisitos)
 * - Casos edge (null, vacías)
 * - Mensajes de error personalizados
 */
@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    private PasswordValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
        validator.initialize(null); // El método initialize no hace nada especial
        
        // Configurar el mock del contexto para construir mensajes de error (lenient para evitar unnecessary stubbing)
        lenient().when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        lenient().when(violationBuilder.addConstraintViolation()).thenReturn(context);
    }

    // ==================== TESTS DE CONTRASEÑAS VÁLIDAS ====================

    @ParameterizedTest
    @ValueSource(strings = {
        "Abc123!@",                                      // Con todos los requisitos básicos
        "Pass123@$!%*?&.#-_",                           // Todos los caracteres especiales
        "Abcd123!",                                     // Mínima longitud (8 caracteres)
        "VerySecurePassword123!@#$",                    // Contraseña larga
        "Password.123",                                 // Con punto
        "Pass-Word123!",                                // Con guion
        "Pass_Word123!",                                // Con guion bajo
        "Password#123",                                 // Con numeral
        "aA1!abcd",                                     // Solo mínimos de cada requisito
        "Pass@123",                                     // Con arroba
        "Pass$123",                                     // Con dólar
        "Pass!123",                                     // Con exclamación
        "Pass%123",                                     // Con porcentaje
        "Pass*123",                                     // Con asterisco
        "Pass?123",                                     // Con interrogación
        "Pass&123",                                     // Con ampersand
        "Abc1@$!%*?&.#-_",                             // Todos los caracteres especiales permitidos
        "Pass1234567890!",                              // Varios números
        "ABCDabcd123!",                                 // Varias minúsculas y mayúsculas
        "VeryLongPasswordWithNumbers123AndSpecialCharacters!@#$%" // Muy larga
    })
    void testPasswordsValidas(String password) {
        assertTrue(validator.isValid(password, context));
        verify(context, never()).disableDefaultConstraintViolation();
    }

    // ==================== TESTS DE CONTRASEÑAS INVÁLIDAS ====================

    @Test
    void testPasswordMuyCorta() {
        String password = "Abc12!"; // Solo 6 caracteres
        assertFalse(validator.isValid(password, context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(contains("debe tener mínimo 8 caracteres"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "ABC12345!",  // Sin minúscula
        "abc12345!",  // Sin mayúscula
        "Abcdefgh!",  // Sin número
        "Abc12345",   // Sin carácter especial
        "Abc123!ñ",   // Con ñ (no permitida)
        "Abc 123!",   // Con espacios
        "Ábcd123!",   // Con acentos
        "Abc123!€",   // Con euro (unicode)
        "Abc123!(",   // Con paréntesis (no permitido)
        "Abc123!,",   // Con coma (no permitida)
        "Abc123!;"    // Con punto y coma (no permitido)
    })
    void testPasswordsInvalidas(String password) {
        assertFalse(validator.isValid(password, context));
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate(anyString());
    }

    // ==================== TESTS DE MÚLTIPLES ERRORES ====================

    @Test
    void testPasswordConMultiplesErrores() {
        String password = "abc"; // Corta, sin mayúscula, sin número, sin especial
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(argThat(msg -> 
            msg.contains("debe tener mínimo 8 caracteres") &&
            msg.contains("debe contener al menos una mayúscula") &&
            msg.contains("debe contener al menos un número") &&
            msg.contains("debe contener al menos un carácter especial")
        ));
    }

    @Test
    void testPasswordSoloMinusculas() {
        String password = "abcdefgh"; // Sin mayúscula, número ni especial
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(argThat(msg -> 
            msg.contains("debe contener al menos una mayúscula") &&
            msg.contains("debe contener al menos un número") &&
            msg.contains("debe contener al menos un carácter especial")
        ));
    }

    @Test
    void testPasswordSoloMayusculas() {
        String password = "ABCDEFGH";
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(argThat(msg -> 
            msg.contains("debe contener al menos una minúscula") &&
            msg.contains("debe contener al menos un número") &&
            msg.contains("debe contener al menos un carácter especial")
        ));
    }

    @Test
    void testPasswordSoloNumeros() {
        String password = "12345678";
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(argThat(msg -> 
            msg.contains("debe contener al menos una minúscula") &&
            msg.contains("debe contener al menos una mayúscula") &&
            msg.contains("debe contener al menos un carácter especial")
        ));
    }

    // ==================== TESTS DE CASOS EDGE ====================

    @Test
    void testPasswordNula() {
        // Null debe retornar true porque se delega a @NotNull
        assertTrue(validator.isValid(null, context));
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void testPasswordVacia() {
        // Vacía debe retornar true porque se delega a @NotBlank
        assertTrue(validator.isValid("", context));
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    void testPasswordExactamente7Caracteres() {
        String password = "Abc123!"; // 7 caracteres - debe fallar
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(contains("debe tener mínimo 8 caracteres"));
    }

    @Test
    void testPasswordCasiValida() {
        // Le falta solo un carácter especial
        String password = "Abcd1234";
        assertFalse(validator.isValid(password, context));
        verify(context).buildConstraintViolationWithTemplate(argThat(msg -> 
            msg.contains("debe contener al menos un carácter especial") &&
            !msg.contains("debe tener mínimo 8 caracteres") &&
            !msg.contains("debe contener al menos una minúscula") &&
            !msg.contains("debe contener al menos una mayúscula") &&
            !msg.contains("debe contener al menos un número")
        ));
    }

    // ==================== HELPER METHOD ====================

    /**
     * Método auxiliar para verificar que un string contiene un substring.
     * Se usa con ArgumentMatchers de Mockito.
     */
    private String contains(String substring) {
        return argThat(msg -> msg != null && msg.contains(substring));
    }
}
