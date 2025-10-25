package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.model.Facultad;

class MateriaDTOTest {
    @Test
    void testGetFacultad() {
        MateriaDTO dto = new MateriaDTO();
        dto.setFacultad(Facultad.ADMINISTRACION);
        assertEquals(Facultad.ADMINISTRACION, dto.getFacultad());
    }

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
        dto.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        Set<ConstraintViolation<MateriaDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @org.junit.jupiter.params.ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
        "PRO,4,El acronimo debe tener entre 4 y 10 caracteres",
        "PROGRAMACION1,4,El acronimo debe tener entre 4 y 10 caracteres",
        "PROG1,0,Los créditos deben ser mínimo 1",
        "PROG1,5,Los créditos deben ser máximo 4"
    })
    void testMateriaDTOValidaciones(String acronimo, int creditos, String mensajeEsperado) {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Programación I");
        dto.setAcronimo(acronimo);
        dto.setCreditos(creditos);
        dto.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        Set<ConstraintViolation<MateriaDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains(mensajeEsperado));
    }

    @Test
    void testMateriaDTOConCamposVacios() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("");
        dto.setAcronimo("");
        dto.setCreditos(1);
        // No establecer facultad para que genere una violación adicional por @NotNull
        Set<ConstraintViolation<MateriaDTO>> violations = validator.validate(dto);
        assertEquals(4, violations.size()); // nombre vacío, acronimo vacío, acronimo tamaño, facultad nula
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
        dto.setFacultad(Facultad.INGENIERIA_SISTEMAS); // Agregar facultad
        Set<ConstraintViolation<MateriaDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        
        dto.setAcronimo("MATEMATICA");
        dto.setCreditos(4);
        violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}