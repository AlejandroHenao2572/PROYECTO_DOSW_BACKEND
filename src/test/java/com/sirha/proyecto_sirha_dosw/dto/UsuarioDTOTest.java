package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUsuarioDTOValido() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        // Email ya no se valida porque se genera automáticamente en el servicio
        dto.setPassword("Password123!");
        dto.setRol("ESTUDIANTE");
        dto.setFacultad("INGENIERIA_SISTEMAS");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsuarioDTOConEmailInvalido() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        // Email no se valida en el DTO porque se genera automáticamente
        dto.setEmail("email-invalido"); // Este campo se ignora
        dto.setPassword("Password123!");
        dto.setRol("ESTUDIANTE");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        // No debe haber violaciones porque email no se valida
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsuarioDTOConPasswordCorto() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setPassword("123");
        dto.setRol("ESTUDIANTE");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        // Debe fallar por la validación de @ValidPassword
        assertFalse(violations.isEmpty(), "Debe haber violaciones para una contraseña corta");
        assertTrue(violations.stream().anyMatch(v -> 
            v.getMessage().contains("contraseña") ||
            v.getMessage().contains("mínimo 8 caracteres") ||
            v.getMessage().contains("debe tener") ||
            v.getMessage().contains("8 caracteres")));
    }

    @Test
    void testUsuarioDTOConCamposVacios() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("");
        dto.setApellido("");
        // Email no se valida
        dto.setPassword("");
        dto.setRol("");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        // Debe haber al menos 3 violaciones (nombre, apellido, password, rol)
        assertTrue(violations.size() >= 3);
    }

    @Test
    void testUsuarioDTOConCamposNulos() {
        UsuarioDTO dto = new UsuarioDTO();
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        // Debe haber al menos 3 violaciones (nombre, apellido, password, rol)
        assertTrue(violations.size() >= 3);
    }

    @Test
    void testUsuarioDTOGettersYSetters() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Maria");
        dto.setApellido("Gomez");
        dto.setEmail("maria.gomez@test.com");
        dto.setPassword("SecurePass123!");
        dto.setRol("PROFESOR");
        dto.setFacultad("INGENIERIA_CIVIL");
        assertEquals("Maria", dto.getNombre());
        assertEquals("Gomez", dto.getApellido());
        assertEquals("maria.gomez@test.com", dto.getEmail());
        assertEquals("SecurePass123!", dto.getPassword());
        assertEquals("PROFESOR", dto.getRol());
        assertEquals("INGENIERIA_CIVIL", dto.getFacultad());
    }

    @Test
    void testUsuarioDTOFacultadOpcional() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Lopez");
        // Email no se valida porque se genera automáticamente
        dto.setPassword("AdminPass123!");
        dto.setRol("ADMINISTRADOR");
        Set<ConstraintViolation<UsuarioDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertNull(dto.getFacultad());
    }
}