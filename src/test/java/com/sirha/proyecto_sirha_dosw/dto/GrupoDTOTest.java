package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Dia;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GrupoDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGrupoDTOValido() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(30);
        dto.setMateriaId("mat123");
        Horario horario = new Horario();
        horario.setDia(Dia.LUNES);
        horario.setHoraInicio("08:00");
        horario.setHoraFin("10:00");
        dto.setHorarios(Arrays.asList(horario));
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testGrupoDTOConCapacidadInvalida() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(0);
        dto.setMateriaId("mat123");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("La capacidad debe ser al menos 1"));
    }

    @Test
    void testGrupoDTOConCapacidadExcedida() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(41);
        dto.setMateriaId("mat123");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("La capacidad maxima es 40"));
    }

    @Test
    void testGrupoDTOConHorariosNulos() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(30);
        dto.setMateriaId("mat123");
        dto.setHorarios(null);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Los horarios no pueden ser nulos"));
    }

    @Test
    void testGrupoDTOConMateriaIdNulo() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(30);
        dto.setMateriaId(null);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("El ID de la materia no puede ser nulo"));
    }

    @Test
    void testGrupoDTOGettersYSetters() {
        GrupoDTO dto = new GrupoDTO();
        dto.setId("grupo123");
        dto.setCapacidad(25);
        dto.setCantidadInscritos(10);
        dto.setEstaCompleto(false);
        dto.setMateriaId("mat456");
        dto.setProfesorId("prof789");
        List<String> estudiantes = Arrays.asList("est1", "est2", "est3");
        dto.setEstudiantesId(estudiantes);
        Horario horario = new Horario();
        horario.setDia(Dia.MARTES);
        horario.setHoraInicio("14:00");
        horario.setHoraFin("16:00");
        dto.setHorarios(Arrays.asList(horario));
        assertEquals("grupo123", dto.getId());
        assertEquals(25, dto.getCapacidad());
        assertEquals(10, dto.getCantidadInscritos());
        assertFalse(dto.isEstaCompleto());
        assertEquals("mat456", dto.getMateriaId());
        assertEquals("prof789", dto.getProfesorId());
        assertEquals(3, dto.getEstudiantesId().size());
        assertEquals(1, dto.getHorarios().size());
        assertEquals(Dia.MARTES, dto.getHorarios().get(0).getDia());
    }

    @Test
    void testGrupoDTOValoresLimite() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(1);
        dto.setMateriaId("mat123");
        dto.setHorarios(Arrays.asList(new Horario()));
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        dto.setCapacidad(40);
        violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}