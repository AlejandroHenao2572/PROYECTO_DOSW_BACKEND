package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class EstrategiaTresCortesTest {

    @Test
    void testCalcularNotaConPesosDiferentes() {
        EstrategiaCalculo estrategia = new EstrategiaTresCortes(0.4, 0.3, 0.3);
        List<Double> notas = Arrays.asList(4.0, 3.5, 4.2);

        double resultado = estrategia.calcularNota(notas);
        double esperado = (4.0 * 0.4) + (3.5 * 0.3) + (4.2 * 0.3);

        assertEquals(esperado, resultado, 0.01);
    }

    @Test
    void testCalcularNotaConPesosIguales() {
        EstrategiaCalculo estrategia = new EstrategiaTresCortes(0.333, 0.333, 0.334);
        List<Double> notas = Arrays.asList(3.0, 3.0, 3.0);

        double resultado = estrategia.calcularNota(notas);
        assertEquals(3.0, resultado, 0.01);
    }

    @Test
    void testGetters() {
        EstrategiaTresCortes estrategia = new EstrategiaTresCortes(0.3, 0.3, 0.4);

        assertEquals(0.3, estrategia.getP1(), 0.01);
        assertEquals(0.3, estrategia.getP2(), 0.01);
        assertEquals(0.4, estrategia.getP3(), 0.01);
    }
}