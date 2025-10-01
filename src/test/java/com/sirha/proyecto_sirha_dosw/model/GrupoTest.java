package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GrupoTest {
    private Grupo grupo;
    private Materia materia;
    private Horario horario;

    @BeforeEach
    void setUp() {
        materia = new Materia("Matemáticas", "MATH01", 3, Facultad.INGENIERIA_SISTEMAS);
        horario = new Horario();
        horario.setDia(Dia.LUNES);
        horario.setHoraInicio(LocalTime.parse("08:00"));
        horario.setHoraFin(LocalTime.parse("10:00"));

        grupo = new Grupo(materia, 30, Arrays.asList(horario));
        grupo.setId("grupo1");
    }

    @Test
    void testAddEstudiante() {
        grupo.addEstudiante("est123");
        assertEquals(1, grupo.getCantidadInscritos());
        assertFalse(grupo.isEstaCompleto());
        assertTrue(grupo.getEstudiantesId().contains("est123"));
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
        assertFalse(grupo.getEstudiantesId().contains("est123"));
    }

    @Test
    void testRemoveEstudianteYNoCompleto() {
        // Llenar grupo y luego remover uno
        for (int i = 1; i <= 30; i++) {
            grupo.addEstudiante("est" + i);
        }
        assertTrue(grupo.isEstaCompleto());

        grupo.removeEstudiante("est1");
        assertFalse(grupo.isEstaCompleto());
        assertEquals(29, grupo.getCantidadInscritos());
    }

    @Test
    void testSettersYGetters() {
        grupo.setCapacidad(40);
        assertEquals(40, grupo.getCapacidad());

        grupo.setCantidadInscritos(10);
        assertEquals(10, grupo.getCantidadInscritos());

        grupo.setEstaCompleto(true);
        assertTrue(grupo.isEstaCompleto());

        Materia otraMateria = new Materia("Física", "FIS01", 2, Facultad.INGENIERIA_SISTEMAS);
        grupo.setMateria(otraMateria);
        assertEquals(otraMateria, grupo.getMateria());

        grupo.setHorarios(Collections.singletonList(horario));
        assertEquals(1, grupo.getHorarios().size());

        grupo.setId("nuevoid");
        assertEquals("nuevoid", grupo.getId());

        Profesor prof = new Profesor();
        grupo.setProfesor(prof);
        assertEquals(prof, grupo.getProfesor());

        grupo.setEstudiantesId(Arrays.asList("a", "b"));
        assertEquals(2, grupo.getEstudiantesId().size());
    }

    @Test
    void testTieneCruceDeHorarioTrue() {
        Horario otroHorario = new Horario();
        otroHorario.setDia(Dia.LUNES);
        otroHorario.setHoraInicio(LocalTime.parse("09:00"));
        otroHorario.setHoraFin(LocalTime.parse("11:00"));

        Grupo otroGrupo = new Grupo(materia, 20, Arrays.asList(otroHorario));
        assertTrue(grupo.tieneCruceDeHorario(otroGrupo));
    }

    @Test
    void testTieneCruceDeHorarioFalse() {
        Horario otroHorario = new Horario();
        otroHorario.setDia(Dia.MARTES);
        otroHorario.setHoraInicio(LocalTime.parse("09:00"));
        otroHorario.setHoraFin(LocalTime.parse("11:00"));

        Grupo otroGrupo = new Grupo(materia, 20, Arrays.asList(otroHorario));
        assertFalse(grupo.tieneCruceDeHorario(otroGrupo));
    }

    @Test
    void testTieneCruceDeHorarioConNull() {
        assertFalse(grupo.tieneCruceDeHorario(null));
        Grupo grupoSinHorarios = new Grupo(materia, 10, null);
        assertFalse(grupo.tieneCruceDeHorario(grupoSinHorarios));
        Grupo grupoConHorariosVacios = new Grupo(materia, 10, Collections.emptyList());
        assertFalse(grupo.tieneCruceDeHorario(grupoConHorariosVacios));
    }
}