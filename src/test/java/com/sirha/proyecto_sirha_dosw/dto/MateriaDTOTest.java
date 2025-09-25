package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class MateriaDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testMateriaDTOValida() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo("PROG1");
        dto.setCreditos(4);
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMateriaDTOConAcronimoCorto() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo("PRO");
        dto.setCreditos(4);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("El acronimo debe tener entre 4 y 10 caracteres"));
    }

    @Test
    void testMateriaDTOConAcronimoLargo() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo("PROGRAMACION1");
        dto.setCreditos(4);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("El acronimo debe tener entre 4 y 10 caracteres"));
    }

    @Test
    void testMateriaDTOConCreditosInvalidos() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo("PROG1");
        dto.setCreditos(0);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Los créditos deben ser mínimo 1"));
    }

    @Test
    void testMateriaDTOConCreditosExcedidos() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo("PROG1");
        dto.setCreditos(5);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Los créditos deben ser máximo 4"));
    }

    @Test
    void testMateriaDTOConCamposVacios() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("");
        dto.setAcronimo("");
        dto.setCreditos(1);
        Set violations = validator.validate(dto);
        assertEquals(3, violations.size());
    }

    @Test
    void testMateriaDTOGettersYSetters() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Base de Datos");
        dto.setAcronimo("BD1");
        dto.setCreditos(3);
        assertEquals("Base de Datos", dto.getNombre());
        assertEquals("BD1", dto.getAcronimo());
        assertEquals(3, dto.getCreditos());
    }

    @Test
    void testMateriaDTOValoresLimite() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Matemáticas");
        dto.setAcronimo("MATH");
        dto.setCreditos(1);
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        dto.setAcronimo("MATEMATICA");
        dto.setCreditos(4);
        violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}