package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SemestreTest {

    private Semestre semestre;
    private Materia materia1;
    private Materia materia2;
    private Materia materia3;
    private Materia materia4;

    @BeforeEach
    void setUp() {
        semestre = new Semestre(1);
        EstrategiaCalculo estrategia = new EstrategiaTresCortes(0.3, 0.3, 0.4);

        materia1 = new Materia("101", "PROG1", "Programación I", 3, estrategia);
        materia2 = new Materia("102", "CAL1", "Cálculo I", 4, estrategia);
        materia3 = new Materia("103", "FIS1", "Física I", 3, estrategia);
        materia4 = new Materia("104", "QUI1", "Química I", 3, estrategia);
    }

    @Test
    void testAgregarRegistro() {
        RegistroMateria registro = new RegistroMateria(materia1);
        semestre.agregarRegistro(registro);

        assertEquals(1, semestre.getMaterias().size());
        assertEquals(materia1, semestre.getMaterias().get(0).getMateria());
    }

    @Test
    void testAgregarRegistroDuplicado() {
        RegistroMateria registro1 = new RegistroMateria(materia1);
        RegistroMateria registro2 = new RegistroMateria(materia1); // Misma materia

        semestre.agregarRegistro(registro1);

        assertThrows(IllegalArgumentException.class, () -> {
            semestre.agregarRegistro(registro2);
        });
    }

    @Test
    void testRemoverRegistro() {
        RegistroMateria registro = new RegistroMateria(materia1);
        semestre.agregarRegistro(registro);

        assertTrue(semestre.removerRegistro("101"));
        assertEquals(0, semestre.getMaterias().size());
    }

    @Test
    void testBuscarRegistroPorCodigo() {
        RegistroMateria registro = new RegistroMateria(materia1);
        semestre.agregarRegistro(registro);

        RegistroMateria encontrado = semestre.buscarRegistroPorCodigo("101");
        assertNotNull(encontrado);
        assertEquals(materia1, encontrado.getMateria());

        RegistroMateria noEncontrado = semestre.buscarRegistroPorCodigo("999");
        assertNull(noEncontrado);
    }

    @Test
    void testCalcularPromedio() {
        // Materia 1: Aprobada
        RegistroMateria registro1 = new RegistroMateria(materia1);
        registro1.agregarNota(4.0);
        registro1.agregarNota(4.0);
        registro1.agregarNota(4.0);
        registro1.updateEstado();

        // Materia 2: Reprobada
        RegistroMateria registro2 = new RegistroMateria(materia2);
        registro2.agregarNota(2.0);
        registro2.agregarNota(2.0);
        registro2.agregarNota(2.0);
        registro2.updateEstado();

        semestre.agregarRegistro(registro1);
        semestre.agregarRegistro(registro2);

        double promedio = semestre.calcularPromedio();
        double esperado = ((4.0 * 3) + (2.0 * 4)) / 7; // Ponderado por créditos

        assertEquals(esperado, promedio, 0.01);
    }

    @Test
    void testCalcularPromedioSinMaterias() {
        assertEquals(0.0, semestre.calcularPromedio(), 0.01);
    }

    @Test
    void testCreditosAprobados() {
        RegistroMateria registro = new RegistroMateria(materia1);
        registro.agregarNota(4.0);
        registro.agregarNota(4.0);
        registro.agregarNota(4.0);
        registro.updateEstado();

        semestre.agregarRegistro(registro);

        assertEquals(3, semestre.getCreditosAprobados());
        assertEquals(0, semestre.getCreditosPerdidos());
        assertEquals(0, semestre.getCreditosEnProgreso());
    }

    @Test
    void testEstaCompletado() {
        RegistroMateria registro = new RegistroMateria(materia1);
        registro.agregarNota(4.0);
        registro.agregarNota(4.0);
        registro.agregarNota(4.0);
        registro.updateEstado();

        semestre.agregarRegistro(registro);

        assertTrue(semestre.estaCompletado());
    }

    @Test
    void testGetMateriasAprobadasSinMaterias() {
        assertEquals(0, semestre.getMateriasAprobadas());
    }


    @Test
    void testGetMateriasPerdidasSinMaterias() {
        assertEquals(0, semestre.getMateriasPerdidas());
    }


    @Test
    void testGetMateriasEnProgresoSinMaterias() {
        assertEquals(0, semestre.getMateriasEnProgreso());
    }

    @Test
    void testMateriasConEstadoLimite() {
        // Materia en el límite de aprobación (3.0)
        RegistroMateria registroLimite = new RegistroMateria(materia1);
        registroLimite.agregarNota(3.0);
        registroLimite.agregarNota(3.0);
        registroLimite.agregarNota(3.0);
        registroLimite.updateEstado();

        semestre.agregarRegistro(registroLimite);

        // Debería contar como aprobada (VERDE)
        assertEquals(SemaforoAcademico.VERDE, registroLimite.getEstado());
        assertEquals(1, semestre.getMateriasAprobadas());
        assertEquals(0, semestre.getMateriasPerdidas());
    }

    @Test
    void testMateriasConEstadoJustoDebajo() {
        // Materia justo debajo del límite (2.99)
        RegistroMateria registroDebajo = new RegistroMateria(materia1);
        registroDebajo.agregarNota(3.0);
        registroDebajo.agregarNota(3.0);
        registroDebajo.agregarNota(2.97); // Promedio: 2.99
        registroDebajo.updateEstado();

        semestre.agregarRegistro(registroDebajo);

        // Debería contar como reprobada (ROJO)
        assertEquals(SemaforoAcademico.ROJO, registroDebajo.getEstado());
        assertEquals(0, semestre.getMateriasAprobadas());
        assertEquals(1, semestre.getMateriasPerdidas());
    }

}