package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GrupoTest {
    private Grupo grupo;
    private Materia materia;
    private Horario horario;

    @BeforeEach
    void setUp() {
        materia = new Materia("Matemáticas", "MATH01", 3);
        horario = new Horario();
        horario.setDia(Dia.LUNES);
        horario.setHoraInicio("08:00");
        horario.setHoraFin("10:00");

        grupo = new Grupo(materia, 30, Arrays.asList(horario));
    }

    @Test
    void testAddEstudiante() {
        grupo.addEstudiante("est123");
        assertEquals(1, grupo.getCantidadInscritos());
        assertFalse(grupo.isEstaCompleto());
    }

    @Test
    void testAddEstudianteDuplicado() {
        grupo.addEstudiante("est123");
        grupo.addEstudiante("est123"); // Debería ignorar duplicado

        assertEquals(1, grupo.getCantidadInscritos());
    }

    @Test
    void testGrupoCompleto() {
        for (int i = 1; i <= 30; i++) {
            grupo.addEstudiante("est" + i);
        }

        assertTrue(grupo.isEstaCompleto());
        assertEquals(30, grupo.getCantidadInscritos());
    }

    @Test
    void testRemoveEstudiante() {
        grupo.addEstudiante("est123");
        grupo.removeEstudiante("est123");

        assertEquals(0, grupo.getCantidadInscritos());
        assertFalse(grupo.isEstaCompleto());
    }
}
