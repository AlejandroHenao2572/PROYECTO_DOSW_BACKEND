package com.sirha.proyecto_sirha_dosw.dto;

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
        dto.setEmail("juan.perez@test.com");
        dto.setPassword("password123");
        dto.setRol("ESTUDIANTE");
        dto.setFacultad("INGENIERIA_SISTEMAS");
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsuarioDTOConEmailInvalido() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("email-invalido");
        dto.setPassword("password123");
        dto.setRol("ESTUDIANTE");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Debe ser un correo válido"));
    }

    @Test
    void testUsuarioDTOConPasswordCorto() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@test.com");
        dto.setPassword("123");
        dto.setRol("ESTUDIANTE");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("La contraseña debe tener al menos 6 caracteres"));
    }

    @Test
    void testUsuarioDTOConCamposVacios() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("");
        dto.setApellido("");
        dto.setEmail("");
        dto.setPassword("");
        dto.setRol("");
        Set violations = validator.validate(dto);
        assertTrue(violations.size() >= 4);
    }

    @Test
    void testUsuarioDTOConCamposNulos() {
        UsuarioDTO dto = new UsuarioDTO();
        Set violations = validator.validate(dto);
        assertTrue(violations.size() >= 4);
    }

    @Test
    void testUsuarioDTOGettersYSetters() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Maria");
        dto.setApellido("Gomez");
        dto.setEmail("maria.gomez@test.com");
        dto.setPassword("securepass");
        dto.setRol("PROFESOR");
        dto.setFacultad("INGENIERIA_CIVIL");
        assertEquals("Maria", dto.getNombre());
        assertEquals("Gomez", dto.getApellido());
        assertEquals("maria.gomez@test.com", dto.getEmail());
        assertEquals("securepass", dto.getPassword());
        assertEquals("PROFESOR", dto.getRol());
        assertEquals("INGENIERIA_CIVIL", dto.getFacultad());
    }

    @Test
    void testUsuarioDTOFacultadOpcional() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Lopez");
        dto.setEmail("carlos@test.com");
        dto.setPassword("password");
        dto.setRol("ADMINISTRADOR");
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertNull(dto.getFacultad());
    }
}