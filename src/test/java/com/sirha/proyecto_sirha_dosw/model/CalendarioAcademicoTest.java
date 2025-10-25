package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalendarioAcademicoTest {

    @BeforeEach
    void resetSingleton() {
        CalendarioAcademico.INSTANCIA.setFechaInicio(LocalDate.of(2025, 7, 11));
        CalendarioAcademico.INSTANCIA.setFechaFin(LocalDate.of(2025, 12, 17));
    }

    @Test
    void testValoresPorDefectoSingleton() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertEquals(LocalDate.of(2025, 7, 11), calendario.getFechaInicio());
        assertEquals(LocalDate.of(2025, 12, 17), calendario.getFechaFin());
    }

    @Test
    void testSetFechaInicioYFin() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        LocalDate nuevaInicio = LocalDate.of(2026, 1, 10);
        LocalDate nuevaFin = LocalDate.of(2026, 6, 30);

        calendario.setFechaInicio(nuevaInicio);
        calendario.setFechaFin(nuevaFin);

        assertEquals(nuevaInicio, calendario.getFechaInicio());
        assertEquals(nuevaFin, calendario.getFechaFin());
    }

    @Test
    void testEstaEnPlazoConFechaIgualInicio() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertTrue(calendario.estaEnPlazo(LocalDate.of(2025, 7, 11)));
    }

    @Test
    void testEstaEnPlazoConFechaIgualFin() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertTrue(calendario.estaEnPlazo(LocalDate.of(2025, 12, 17)));
    }

    @Test
    void testEstaEnPlazoConFechaIntermedia() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertTrue(calendario.estaEnPlazo(LocalDate.of(2025, 10, 1)));
    }

    @Test
    void testEstaEnPlazoConFechaAntesInicio() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertFalse(calendario.estaEnPlazo(LocalDate.of(2025, 7, 10)));
    }

    @Test
    void testEstaEnPlazoConFechaDespuesFin() {
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        assertFalse(calendario.estaEnPlazo(LocalDate.of(2025, 12, 18)));
    }

    @Test
    void testCambiosSingletonAfectanTodasLasInstancias() {
        CalendarioAcademico calendario1 = CalendarioAcademico.INSTANCIA;
        CalendarioAcademico calendario2 = CalendarioAcademico.INSTANCIA;

        LocalDate nuevaInicio = LocalDate.of(2030, 1, 1);
        calendario1.setFechaInicio(nuevaInicio);

        assertEquals(nuevaInicio, calendario2.getFechaInicio());
        assertSame(calendario1, calendario2);
    }
}