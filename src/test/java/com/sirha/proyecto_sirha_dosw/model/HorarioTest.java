package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class HorarioTest {

    private Horario horario;
    private Materia materia;
    private FranjaHoraria franja;

    @BeforeEach
    void setUp() {
        horario = new Horario("2024-1");
        EstrategiaCalculo estrategia = new EstrategiaTresCortes(0.3, 0.3, 0.4);
        materia = new Materia("101", "PROG1", "Programación I", 3, estrategia);
        franja = new FranjaHoraria("08:00", "10:00");
    }

    @Test
    void testAgregarClase() {
        horario.agregarClase(Dia.LUNES, franja, materia, "A101");

        assertEquals(1, horario.getClasesPorDia(Dia.LUNES).size());
        assertEquals(0, horario.getClasesPorDia(Dia.MARTES).size());
    }

    @Test
    void testTieneConflicto() {
        horario.agregarClase(Dia.LUNES, franja, materia, "A101");

        assertTrue(horario.tieneConflicto(Dia.LUNES, franja));
        assertFalse(horario.tieneConflicto(Dia.MARTES, franja));
    }

    @Test
    void testGetClasesPorDia() {
        horario.agregarClase(Dia.LUNES, franja, materia, "A101");
        horario.agregarClase(Dia.LUNES,
                new FranjaHoraria("10:00", "12:00"), materia, "A102");

        assertEquals(2, horario.getClasesPorDia(Dia.LUNES).size());
    }
}