package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlazoSolicitudesTest {

    @BeforeEach
    void resetSingleton() {
        // Por defecto: fechaInicio = hoy, fechaFin = hoy + 10
        PlazoSolicitudes.INSTANCIA.setFechaInicio(LocalDate.of(2025, 9, 30));
        PlazoSolicitudes.INSTANCIA.setFechaFin(LocalDate.of(2025, 10, 10));
    }

    @Test
    void testValoresPorDefectoSingleton() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertEquals(LocalDate.of(2025, 9, 30), plazo.getFechaInicio());
        assertEquals(LocalDate.of(2025, 10, 10), plazo.getFechaFin());
    }

    @Test
    void testSetFechaInicioYFin() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;

        LocalDate nuevaInicio = LocalDate.of(2026, 1, 1);
        LocalDate nuevaFin = LocalDate.of(2026, 1, 11);

        plazo.setFechaInicio(nuevaInicio);
        plazo.setFechaFin(nuevaFin);

        assertEquals(nuevaInicio, plazo.getFechaInicio());
        assertEquals(nuevaFin, plazo.getFechaFin());
    }

    @Test
    void testEstaEnPlazoConFechaIgualInicio() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertTrue(plazo.estaEnPlazo(LocalDate.of(2025, 9, 30)));
    }

    @Test
    void testEstaEnPlazoConFechaIgualFin() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertTrue(plazo.estaEnPlazo(LocalDate.of(2025, 10, 10)));
    }

    @Test
    void testEstaEnPlazoConFechaIntermedia() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertTrue(plazo.estaEnPlazo(LocalDate.of(2025, 10, 5)));
    }

    @Test
    void testEstaEnPlazoConFechaAntesInicio() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertFalse(plazo.estaEnPlazo(LocalDate.of(2025, 9, 29)));
    }

    @Test
    void testEstaEnPlazoConFechaDespuesFin() {
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        assertFalse(plazo.estaEnPlazo(LocalDate.of(2025, 10, 11)));
    }

    @Test
    void testCambiosSingletonAfectanTodasLasInstancias() {
        PlazoSolicitudes plazo1 = PlazoSolicitudes.INSTANCIA;
        PlazoSolicitudes plazo2 = PlazoSolicitudes.INSTANCIA;

        LocalDate nuevaInicio = LocalDate.of(2030, 1, 1);
        plazo1.setFechaInicio(nuevaInicio);

        assertEquals(nuevaInicio, plazo2.getFechaInicio());
        assertSame(plazo1, plazo2);
    }
}