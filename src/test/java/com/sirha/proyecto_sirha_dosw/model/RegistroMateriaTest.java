package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RegistroMateriaTest {

    private RegistroMateria registro;
    private Materia materia;

    @BeforeEach
    void setUp() {
        EstrategiaCalculo estrategia = new EstrategiaTresCortes(0.3, 0.3, 0.4);
        materia = new Materia("101", "PROG1", "Programación I", 3, estrategia);
        registro = new RegistroMateria(materia);
    }

    @Test
    void testAgregarNotaYCalcularFinal() {
        registro.agregarNota(4.0);
        registro.agregarNota(3.5);
        registro.agregarNota(4.2);

        double resultado = registro.getNotaFinal();
        double esperado = (4.0 * 0.3) + (3.5 * 0.3) + (4.2 * 0.4);

        assertEquals(esperado, resultado, 0.01);
    }

    @Test
    void testUpdateEstadoAprobado() {
        registro.agregarNota(3.5);
        registro.agregarNota(4.0);
        registro.agregarNota(4.2);
        registro.updateEstado();

        assertEquals(SemaforoAcademico.VERDE, registro.getEstado());
    }

    @Test
    void testUpdateEstadoReprobado() {
        registro.agregarNota(2.0);
        registro.agregarNota(2.5);
        registro.agregarNota(2.8);
        registro.updateEstado();

        assertEquals(SemaforoAcademico.ROJO, registro.getEstado());
    }

    @Test
    void testCancelarMateria() {
        registro.cancelarMateria();
        assertEquals(SemaforoAcademico.BLANCO, registro.getEstado());
    }
}