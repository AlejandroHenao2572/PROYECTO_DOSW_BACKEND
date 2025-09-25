package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    private Solicitud solicitud;
    private Materia materiaProblema, materiaDestino;
    private Grupo grupoProblema, grupoDestino;

    @BeforeEach
    void setUp() {
        solicitud = new Solicitud();

        materiaProblema = new Materia("Programación I", "PROG1", 4);
        materiaDestino = new Materia("Programación II", "PROG2", 4);

        grupoProblema = new Grupo(materiaProblema, 30, new ArrayList<>());
        grupoDestino = new Grupo(materiaDestino, 25, new ArrayList<>());
    }

    // Pruebas para setters con 0% de cobertura

    @Test
    void testSettersAndGettersBasicos() {
        solicitud.setId("sol123");
        assertEquals("sol123", solicitud.getId());

        solicitud.setEstudianteId("est456");
        assertEquals("est456", solicitud.getEstudianteId());

        solicitud.setObservaciones("Necesito cambiar de grupo por horario");
        assertEquals("Necesito cambiar de grupo por horario", solicitud.getObservaciones());

        solicitud.setComentariosAdmin("Solicitud aprobada por disponibilidad");
        assertEquals("Solicitud aprobada por disponibilidad", solicitud.getComentariosAdmin());
    }

    @Test
    void testSettersAndGettersGruposYMaterias() {
        solicitud.setGrupoProblema(grupoProblema);
        assertEquals(grupoProblema, solicitud.getGrupoProblema());

        solicitud.setMateriaProblema(materiaProblema);
        assertEquals(materiaProblema, solicitud.getMateriaProblema());

        solicitud.setGrupoDestino(grupoDestino);
        assertEquals(grupoDestino, solicitud.getGrupoDestino());

        solicitud.setMateriaDestino(materiaDestino);
        assertEquals(materiaDestino, solicitud.getMateriaDestino());
    }

    @Test
    void testSettersAndGettersFechas() {
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaResolucion = LocalDateTime.now().plusDays(2);

        solicitud.setFechaCreacion(fechaCreacion);
        assertEquals(fechaCreacion, solicitud.getFechaCreacion());

        solicitud.setFechaResolucion(fechaResolucion);
        assertEquals(fechaResolucion, solicitud.getFechaResolucion());
    }

    @Test
    void testSetterAndGetterEstado() {
        solicitud.setEstado(SolicitudEstado.ACEPTADA);
        assertEquals(SolicitudEstado.ACEPTADA, solicitud.getEstado());

        solicitud.setEstado(SolicitudEstado.RECHAZADA);
        assertEquals(SolicitudEstado.RECHAZADA, solicitud.getEstado());

        solicitud.setEstado(SolicitudEstado.PENDIENTE);
        assertEquals(SolicitudEstado.PENDIENTE, solicitud.getEstado());
    }

    @Test
    void testSetterAndGetterTipoSolicitud() {
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        assertEquals(TipoSolicitud.CAMBIO_GRUPO, solicitud.getTipoSolicitud());

        solicitud.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        assertEquals(TipoSolicitud.INSCRIPCION_GRUPO, solicitud.getTipoSolicitud());

        solicitud.setTipoSolicitud(TipoSolicitud.CANCELACION_GRUPO);
        assertEquals(TipoSolicitud.CANCELACION_GRUPO, solicitud.getTipoSolicitud());
    }

    // Pruebas para constructor y estado inicial

    @Test
    void testConstructor() {
        Solicitud nuevaSolicitud = new Solicitud();
        assertEquals(SolicitudEstado.PENDIENTE, nuevaSolicitud.getEstado());
        assertNull(nuevaSolicitud.getFechaCreacion());
        assertNull(nuevaSolicitud.getFechaResolucion());
    }

    @Test
    void testEstadoInicial() {
        assertEquals(SolicitudEstado.PENDIENTE, solicitud.getEstado());
        assertNull(solicitud.getFechaCreacion());
        assertNull(solicitud.getFechaResolucion());
        assertNull(solicitud.getComentariosAdmin());
    }

    // Pruebas para escenarios específicos de solicitudes

    @Test
    void testSolicitudInscripcionGrupo() {
        solicitud.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        solicitud.setMateriaDestino(materiaDestino);
        solicitud.setGrupoDestino(grupoDestino);
        solicitud.setObservaciones("Quiero inscribirme en este grupo");

        assertEquals(TipoSolicitud.INSCRIPCION_GRUPO, solicitud.getTipoSolicitud());
        assertEquals(materiaDestino, solicitud.getMateriaDestino());
        assertEquals(grupoDestino, solicitud.getGrupoDestino());
        assertNull(solicitud.getMateriaProblema()); // No debería tener materia problema
        assertNull(solicitud.getGrupoProblema()); // No debería tener grupo problema
    }

    @Test
    void testSolicitudCambioGrupo() {
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitud.setMateriaProblema(materiaProblema);
        solicitud.setGrupoProblema(grupoProblema);
        solicitud.setMateriaDestino(materiaProblema); // Misma materia, diferente grupo
        solicitud.setGrupoDestino(grupoDestino);
        solicitud.setObservaciones("Necesito cambiar por conflicto de horario");

        assertEquals(TipoSolicitud.CAMBIO_GRUPO, solicitud.getTipoSolicitud());
        assertEquals(materiaProblema, solicitud.getMateriaProblema());
        assertEquals(materiaProblema, solicitud.getMateriaDestino());
    }

    @Test
    void testSolicitudCancelacionGrupo() {
        solicitud.setTipoSolicitud(TipoSolicitud.CANCELACION_GRUPO);
        solicitud.setMateriaProblema(materiaProblema);
        solicitud.setGrupoProblema(grupoProblema);
        solicitud.setObservaciones("Necesito cancelar por problemas personales");

        assertEquals(TipoSolicitud.CANCELACION_GRUPO, solicitud.getTipoSolicitud());
        assertNull(solicitud.getMateriaDestino()); // No debería tener materia destino
        assertNull(solicitud.getGrupoDestino()); // No debería tener grupo destino
    }

    @Test
    void testTransicionEstados() {
        LocalDateTime fechaCreacion = LocalDateTime.now();
        solicitud.setFechaCreacion(fechaCreacion);

        // Aprobar solicitud
        LocalDateTime fechaResolucion = fechaCreacion.plusDays(1);
        solicitud.setEstado(SolicitudEstado.ACEPTADA);
        solicitud.setFechaResolucion(fechaResolucion);
        solicitud.setComentariosAdmin("Aprobado por disponibilidad");

        assertEquals(SolicitudEstado.ACEPTADA, solicitud.getEstado());
        assertEquals(fechaResolucion, solicitud.getFechaResolucion());
        assertEquals("Aprobado por disponibilidad", solicitud.getComentariosAdmin());
    }

    @Test
    void testValoresNulos() {
        solicitud.setGrupoProblema(null);
        assertNull(solicitud.getGrupoProblema());

        solicitud.setMateriaProblema(null);
        assertNull(solicitud.getMateriaProblema());

        solicitud.setObservaciones(null);
        assertNull(solicitud.getObservaciones());

        solicitud.setComentariosAdmin(null);
        assertNull(solicitud.getComentariosAdmin());
    }
}