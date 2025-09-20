package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarreraFactoryTest {

    @Test
    void testCrearIngenieriaSistemas() {
        Carrera carrera = CarreraFactory.crearCarrera(CarreraTipo.INGENIERIA_SISTEMAS);

        assertNotNull(carrera);
        assertTrue(carrera instanceof IngenieriaSistemas);
        assertEquals("Ingeniería de Sistemas", carrera.getNombre());
        assertFalse(carrera.getObligatorias().isEmpty());
        assertFalse(carrera.getElectivas().isEmpty());
    }

    @Test
    void testCrearIngenieriaCivil() {
        Carrera carrera = CarreraFactory.crearCarrera(CarreraTipo.INGENIERIA_CIVIL);

        assertNotNull(carrera);
        assertTrue(carrera instanceof IngenieriaCivil);
        assertEquals("Ingeniería Civil", carrera.getNombre());
    }

    @Test
    void testCrearAdministracion() {
        Carrera carrera = CarreraFactory.crearCarrera(CarreraTipo.ADMINISTRACION);

        assertNotNull(carrera);
        assertTrue(carrera instanceof Administracion);
        assertEquals("Administración de Empresas", carrera.getNombre());
    }

}