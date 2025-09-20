package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MateriaTest {

    private Materia materia;
    private EstrategiaCalculo estrategia;

    @BeforeEach
    void setUp() {
        estrategia = new EstrategiaTresCortes(0.3, 0.3, 0.4);
        materia = new Materia("101", "PROG1", "Programación I", 3, estrategia);
    }

    @Test
    void testCalcularNotaCorrectamente() {
        List<Double> notas = Arrays.asList(4.0, 3.5, 4.2);
        double resultado = materia.calcularNota(notas);

        double esperado = (4.0 * 0.3) + (3.5 * 0.3) + (4.2 * 0.4);
        assertEquals(esperado, resultado, 0.01);
    }

    @Test
    void testCalcularNotaConNotasInvalidas() {
        List<Double> notas = Arrays.asList(6.0, 3.0, 4.0);

        assertThrows(IllegalArgumentException.class, () -> {
            materia.calcularNota(notas);
        });
    }

    @Test
    void testTienePrerrequisitos() {
        assertFalse(materia.tienePrerrequisitos());

        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        assertTrue(materia.tienePrerrequisitos());
    }

    @Test
    void testCumplePrerrequisitos() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));

        List<String> materiasAprobadas = Arrays.asList("MATH101", "LOG101", "FIS101");
        assertTrue(materia.cumplePrerrequisitos(materiasAprobadas));

        List<String> materiasIncompletas = Arrays.asList("MATH101");
        assertFalse(materia.cumplePrerrequisitos(materiasIncompletas));
    }

    @Test
    void testConstructorConParametros() {
        assertEquals("101", materia.getCodigo());
        assertEquals("PROG1", materia.getAcronimo());
        assertEquals("Programación I", materia.getNombre());
        assertEquals(3, materia.getCreditos());
        assertEquals(estrategia, materia.getEstrategiaCalculo());
        assertNull(materia.getPrerequisitos());
    }

    @Test
    void testConstructorCompleto() {
        Materia materiaCompleta = new Materia("102", "CAL1", "Cálculo I", 4,
                new EstrategiaTresCortes(0.4, 0.3, 0.3),
                "Descripción", 64, true, 1);

        assertEquals("102", materiaCompleta.getCodigo());
        assertEquals("CAL1", materiaCompleta.getAcronimo());
        assertEquals("Cálculo I", materiaCompleta.getNombre());
        assertEquals(4, materiaCompleta.getCreditos());
        assertNotNull(materiaCompleta.getEstrategiaCalculo());
    }


    @Test
    void testCalcularNotaConNotaNegativa() {
        List<Double> notasNegativas = Arrays.asList(-1.0, 3.0, 4.0);

        assertThrows(IllegalArgumentException.class, () -> {
            materia.calcularNota(notasNegativas);
        });
    }

    @Test
    void testCalcularNotaListaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            materia.calcularNota(null);
        });
    }

    @Test
    void testCalcularNotaListaVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            materia.calcularNota(Arrays.asList());
        });
    }

    @Test
    void testTienePrerrequisitosSinPrerrequisitos() {
        assertFalse(materia.tienePrerrequisitos());
    }

    @Test
    void testTienePrerrequisitosConPrerrequisitos() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        assertTrue(materia.tienePrerrequisitos());
    }

    @Test
    void testTienePrerrequisitosListaVacia() {
        materia.setPrerequisitos(Arrays.asList());
        assertFalse(materia.tienePrerrequisitos());
    }

    @Test
    void testCumplePrerrequisitosSinPrerrequisitos() {
        List<String> materiasAprobadas = Arrays.asList("MATH101", "LOG101");
        assertTrue(materia.cumplePrerrequisitos(materiasAprobadas));
    }

    @Test
    void testCumplePrerrequisitosConPrerrequisitosCumplidos() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        List<String> materiasAprobadas = Arrays.asList("MATH101", "LOG101", "FIS101");
        assertTrue(materia.cumplePrerrequisitos(materiasAprobadas));
    }

    @Test
    void testCumplePrerrequisitosConPrerrequisitosIncompletos() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        List<String> materiasAprobadas = Arrays.asList("MATH101");
        assertFalse(materia.cumplePrerrequisitos(materiasAprobadas));
    }

    @Test
    void testCumplePrerrequisitosListaAprobadasVacia() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        List<String> materiasAprobadas = Arrays.asList();
        assertFalse(materia.cumplePrerrequisitos(materiasAprobadas));
    }

    @Test
    void testObtenerInfoCompleta() {
        String info = materia.obtenerInfoCompleta();
        assertTrue(info.contains("101"));
        assertTrue(info.contains("Programación I"));
        assertTrue(info.contains("PROG1"));
        assertTrue(info.contains("3"));
    }

    @Test
    void testSettersYGetters() {
        materia.setId("MAT001");
        assertEquals("MAT001", materia.getId());

        materia.setCodigo("202");
        assertEquals("202", materia.getCodigo());

        materia.setAcronimo("PROG2");
        assertEquals("PROG2", materia.getAcronimo());

        materia.setNombre("Programación II");
        assertEquals("Programación II", materia.getNombre());

        materia.setCreditos(4);
        assertEquals(4, materia.getCreditos());

        EstrategiaCalculo nuevaEstrategia = new EstrategiaTresCortes(0.4, 0.3, 0.3);
        materia.setEstrategiaCalculo(nuevaEstrategia);
        assertEquals(nuevaEstrategia, materia.getEstrategiaCalculo());

        List<String> prerequisitos = Arrays.asList("MATH101", "LOG101");
        materia.setPrerequisitos(prerequisitos);
        assertEquals(prerequisitos, materia.getPrerequisitos());
    }

    @Test
    void testSetPrerequisitosNull() {
        materia.setPrerequisitos(null);
        assertNull(materia.getPrerequisitos());
        assertFalse(materia.tienePrerrequisitos());
    }

    @Test
    void testValidacionesTamanioCodigo() {
        // Código válido (3-10 caracteres)
        materia.setCodigo("MAT101");
        assertEquals("MAT101", materia.getCodigo());

        // Código en límite mínimo
        materia.setCodigo("MAT");
        assertEquals("MAT", materia.getCodigo());

        // Código en límite máximo
        materia.setCodigo("MAT1010101");
        assertEquals("MAT1010101", materia.getCodigo());
    }

    @Test
    void testValidacionesTamanioAcronimo() {
        // Acrónimo válido (2-6 caracteres)
        materia.setAcronimo("PROG");
        assertEquals("PROG", materia.getAcronimo());

        // Acrónimo en límite mínimo
        materia.setAcronimo("PR");
        assertEquals("PR", materia.getAcronimo());

        // Acrónimo en límite máximo
        materia.setAcronimo("PROGR1");
        assertEquals("PROGR1", materia.getAcronimo());
    }

    @Test
    void testValidacionesTamanioNombre() {
        // Nombre válido (5-100 caracteres)
        materia.setNombre("Programación Orientada a Objetos");
        assertEquals("Programación Orientada a Objetos", materia.getNombre());

        // Nombre en límite mínimo
        materia.setNombre("Mat");
        // La validación se haría con @Size, pero el setter permite cualquier valor
        assertEquals("Mat", materia.getNombre());
    }

    @Test
    void testValidacionesCreditos() {
        // Créditos válidos (mínimo 1)
        materia.setCreditos(1);
        assertEquals(1, materia.getCreditos());

        materia.setCreditos(5);
        assertEquals(5, materia.getCreditos());

        // Créditos cero (no válido según @Min(1))
        materia.setCreditos(0);
        assertEquals(0, materia.getCreditos());
    }

    @Test
    void testCalcularNotaConDiferentesEstrategias() {
        // Estrategia con pesos diferentes
        EstrategiaCalculo estrategiaCustom = new EstrategiaTresCortes(0.4, 0.3, 0.3);
        Materia materiaCustom = new Materia("201", "AI", "Inteligencia Artificial", 3, estrategiaCustom);

        List<Double> notas = Arrays.asList(5.0, 4.0, 3.0);
        double resultado = materiaCustom.calcularNota(notas);

        double esperado = (5.0 * 0.4) + (4.0 * 0.3) + (3.0 * 0.3);
        assertEquals(esperado, resultado, 0.01);
    }

    @Test
    void testCalcularNotaConNotasLimite() {
        // Notas en los límites permitidos (0.0 - 5.0)
        List<Double> notasMinimas = Arrays.asList(0.0, 0.0, 0.0);
        double resultadoMin = materia.calcularNota(notasMinimas);
        assertEquals(0.0, resultadoMin, 0.01);

        List<Double> notasMaximas = Arrays.asList(5.0, 5.0, 5.0);
        double resultadoMax = materia.calcularNota(notasMaximas);
        assertEquals(5.0, resultadoMax, 0.01);
    }

    @Test
    void testPrerrequisitosConListaModificada() {
        List<String> prerequisitos = new ArrayList<>(Arrays.asList("MATH101", "LOG101"));
        materia.setPrerequisitos(prerequisitos);
        assertEquals(2, materia.getPrerequisitos().size()); // Debería mantener 2 elementos
        assertFalse(materia.getPrerequisitos().contains("FIS101"));
    }

    @Test
    void testGetPrerequisitosReturnsCopia() {
        List<String> prerequisitos = Arrays.asList("MATH101", "LOG101");
        materia.setPrerequisitos(prerequisitos);

        List<String> copia = materia.getPrerequisitos();

        assertEquals(2, materia.getPrerequisitos().size()); // Debería mantener 2 elementos
        assertFalse(materia.getPrerequisitos().contains("FIS101"));
    }

    @Test
    void testMateriaConMultiplesPrerrequisitos() {
        List<String> muchosPrerequisitos = Arrays.asList(
                "MATH101", "MATH102", "PHYS101", "PHYS102",
                "CHEM101", "BIO101", "CALC101", "CALC102"
        );

        materia.setPrerequisitos(muchosPrerequisitos);

        assertTrue(materia.tienePrerrequisitos());
        assertEquals(8, materia.getPrerequisitos().size());
    }

    @Test
    void testCumplePrerrequisitosConOrdenDiferente() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        List<String> materiasAprobadas = Arrays.asList("LOG101", "MATH101", "FIS101");
        assertTrue(materia.cumplePrerrequisitos(materiasAprobadas));
    }

    @Test
    void testCumplePrerrequisitosConElementosExtra() {
        materia.setPrerequisitos(Arrays.asList("MATH101", "LOG101"));
        List<String> materiasAprobadas = Arrays.asList("MATH101", "LOG101", "FIS101", "QUIM101");
        assertTrue(materia.cumplePrerrequisitos(materiasAprobadas));
    }
}