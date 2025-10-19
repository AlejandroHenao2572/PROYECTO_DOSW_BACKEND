package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CarreraDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCarreraDTOValida() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("INGENIERIA_SISTEMAS");
        dto.setCodigo("IS001");
        dto.setDuracionSemestres(10);
        dto.setCreditosTotales(150);
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCarreraDTOConDuracionInvalida() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("INGENIERIA_SISTEMAS");
        dto.setCodigo("IS001");
        dto.setDuracionSemestres(0);
        dto.setCreditosTotales(150);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("La duración debe ser al menos 1 semestre"));
    }

    @Test
    void testCarreraDTOConCreditosInvalidos() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("INGENIERIA_SISTEMAS");
        dto.setCodigo("IS001");
        dto.setDuracionSemestres(10);
        dto.setCreditosTotales(0);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Los créditos totales deben ser al menos 1"));
    }

    @Test
    void testCarreraDTOConCamposVacios() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("");
        dto.setCodigo("");
        dto.setDuracionSemestres(10);
        dto.setCreditosTotales(150);
        Set violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    void testCarreraDTOGettersYSetters() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("ADMINISTRACION");
        dto.setCodigo("ADM001");
        dto.setDuracionSemestres(9);
        dto.setCreditosTotales(160);
        assertEquals("ADMINISTRACION", dto.getNombre());
        assertEquals("ADM001", dto.getCodigo());
        assertEquals(9, dto.getDuracionSemestres());
        assertEquals(160, dto.getCreditosTotales());
    }

    @Test
    void testCarreraDTOValoresLimite() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("INGENIERIA_CIVIL");
        dto.setCodigo("IC001");
        dto.setDuracionSemestres(1);
        dto.setCreditosTotales(1);
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}