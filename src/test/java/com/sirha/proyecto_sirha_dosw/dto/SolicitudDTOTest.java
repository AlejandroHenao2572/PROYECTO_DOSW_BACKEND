package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSolicitudDTOValida() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("est123");
        dto.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        dto.setGrupoProblemaId("grupo123");
        dto.setMateriaProblemaAcronimo("PROG1");
        dto.setGrupoDestinoId("grupo456");
        dto.setMateriaDestinoAcronimo("PROG1");
        dto.setObservaciones("Necesito cambiar de grupo por horario");
        Set violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSolicitudDTOConEstudianteIdVacio() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("");
        dto.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        dto.setGrupoProblemaId("grupo123");
        dto.setMateriaProblemaAcronimo("PROG1");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("El ID del estudiante no puede estar vacío"));
    }

    @Test
    void testSolicitudDTOConTipoSolicitudNulo() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("est123");
        dto.setTipoSolicitud(null);
        dto.setGrupoProblemaId("grupo123");
        dto.setMateriaProblemaAcronimo("PROG1");
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("El tipo de solicitud no puede ser nulo"));
    }

    @Test
    void testSolicitudDTOConObservacionesDemasiadoLargas() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("est123");
        dto.setTipoSolicitud(TipoSolicitud.CANCELACION_GRUPO);
        dto.setGrupoProblemaId("grupo123");
        dto.setMateriaProblemaAcronimo("PROG1");
        String observacionesLargas = "a".repeat(501);
        dto.setObservaciones(observacionesLargas);
        Set violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().toString().contains("Las observaciones no pueden exceder los 500 caracteres"));
    }

    @Test
    void testSolicitudDTOGettersYSetters() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("est456");
        dto.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        dto.setGrupoProblemaId("grupo789");
        dto.setMateriaProblemaAcronimo("BD1");
        dto.setGrupoDestinoId("grupo999");
        dto.setMateriaDestinoAcronimo("BD1");
        dto.setObservaciones("Observación de prueba");
        assertEquals("est456", dto.getEstudianteId());
        assertEquals(TipoSolicitud.INSCRIPCION_GRUPO, dto.getTipoSolicitud());
        assertEquals("grupo789", dto.getGrupoProblemaId());
        assertEquals("BD1", dto.getMateriaProblemaAcronimo());
        assertEquals("grupo999", dto.getGrupoDestinoId());
        assertEquals("BD1", dto.getMateriaDestinoAcronimo());
        assertEquals("Observación de prueba", dto.getObservaciones());
    }

    @Test
    void testSolicitudDTOParaInscripcion() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("est123");
        dto.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        dto.setGrupoProblemaId("");
        dto.setMateriaProblemaAcronimo("PROG1");
        dto.setGrupoDestinoId("grupo456");
        Set violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}