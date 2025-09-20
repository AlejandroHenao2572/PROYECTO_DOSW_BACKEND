package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class SolicitudCambioTest {

    private SolicitudCambio solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new SolicitudCambio();
        solicitud.setId("SOL001");
        solicitud.setIdEstudiante("EST001");
        solicitud.setMateriaConProblema("MAT101");
        solicitud.setSugerenciaCambio("Cambiar horario");
        solicitud.setObservaciones("Conflicto con otra materia");
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setPrioridad(1);
    }

    @Test
    void testGettersYSetters() {
        assertEquals("SOL001", solicitud.getId());
        assertEquals("EST001", solicitud.getIdEstudiante());
        assertEquals("MAT101", solicitud.getMateriaConProblema());
        assertEquals("Cambiar horario", solicitud.getSugerenciaCambio());
        assertEquals("Conflicto con otra materia", solicitud.getObservaciones());
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertEquals(1, solicitud.getPrioridad());
        assertNotNull(solicitud.getFechaCreacion());
    }

    @Test
    void testCambiarEstado() {
        solicitud.setEstado(EstadoSolicitud.APROBADA);
        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());

        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstado());
    }

    @Test
    void testPrioridad() {
        solicitud.setPrioridad(3);
        assertEquals(3, solicitud.getPrioridad());

        solicitud.setPrioridad(1);
        assertEquals(1, solicitud.getPrioridad());
    }
}