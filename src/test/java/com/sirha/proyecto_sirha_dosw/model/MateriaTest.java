package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MateriaTest {

    @Test
    void testCreacionMateria() {
        Materia materia = new Materia("Física", "FIS01", 4);

        assertEquals("Física", materia.getNombre());
        assertEquals("FIS01", materia.getAcronimo());
        assertEquals(4, materia.getCreditos());
    }

    @Test
    void testSettersMateria() {
        Materia materia = new Materia();
        materia.setNombre("Química");
        materia.setAcronimo("QUIM01");
        materia.setCreditos(3);

        assertEquals("Química", materia.getNombre());
        assertEquals("QUIM01", materia.getAcronimo());
        assertEquals(3, materia.getCreditos());
    }
}