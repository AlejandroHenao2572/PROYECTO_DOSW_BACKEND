package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class CarreraTest {

    private Materia materia1;
    private Materia materia2;
    private Materia materia3;

    @BeforeEach
    void setUp() {
        materia1 = new Materia("Programación I", "PROG1", 4);
        materia2 = new Materia("Base de Datos", "BD1", 4);
        materia3 = new Materia("Matemáticas", "MATH1", 3);

        materia1.setId("m1");
        materia2.setId("m2");
        materia3.setId("m3");
    }

    @Test
    void testConstructorVacio() {
        Carrera carreraVacia = new Carrera();
        assertNotNull(carreraVacia);
        assertNull(carreraVacia.getNombre());
        assertNull(carreraVacia.getCodigo());
        assertEquals(0, carreraVacia.getDuracionSemestres());
        assertEquals(0, carreraVacia.getCreditosTotales());
        assertNotNull(carreraVacia.getMaterias());
        assertTrue(carreraVacia.getMaterias().isEmpty());
    }

    @Test
    void testConstructorConParametrosBasicos() {
        Carrera carrera = new Carrera(
                Facultad.INGENIERIA_SISTEMAS,
                "IS001",
                10,
                150
        );

        assertEquals(Facultad.INGENIERIA_SISTEMAS, carrera.getNombre());
        assertEquals("IS001", carrera.getCodigo());
        assertEquals(10, carrera.getDuracionSemestres());
        assertEquals(150, carrera.getCreditosTotales());
        assertNotNull(carrera.getMaterias());
        assertTrue(carrera.getMaterias().isEmpty());
    }

    @Test
    void testConstructorCompleto() {
        List<Materia> materias = Arrays.asList(materia1, materia2);

        Carrera carrera = new Carrera(
                Facultad.INGENIERIA_CIVIL,
                "IC001",
                12,
                materias,
                180
        );

        assertEquals(Facultad.INGENIERIA_CIVIL, carrera.getNombre());
        assertEquals("IC001", carrera.getCodigo());
        assertEquals(12, carrera.getDuracionSemestres());
        assertEquals(180, carrera.getCreditosTotales());
        assertEquals(2, carrera.getMaterias().size());
        assertEquals(materia1, carrera.getMaterias().get(0));
    }

    @Test
    void testSetterCodigo() {
        Carrera carrera = new Carrera();
        carrera.setCodigo("TEST001");
        assertEquals("TEST001", carrera.getCodigo());
    }

    @Test
    void testSetterDuracionSemestres() {
        Carrera carrera = new Carrera();
        carrera.setDuracionSemestres(8);
        assertEquals(8, carrera.getDuracionSemestres());
    }

    @Test
    void testSetterCreditosTotales() {
        Carrera carrera = new Carrera();
        carrera.setCreditosTotales(200);
        assertEquals(200, carrera.getCreditosTotales());
    }

    @Test
    void testGetterNombre() {
        Carrera carrera = new Carrera(Facultad.ADMINISTRACION, "ADM001", 9, 160);
        assertEquals(Facultad.ADMINISTRACION, carrera.getNombre());
    }

    @Test
    void testGetterCodigo() {
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS002", 10, 150);
        assertEquals("IS002", carrera.getCodigo());
    }

    @Test
    void testGetterDuracionSemestres() {
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS003", 11, 170);
        assertEquals(11, carrera.getDuracionSemestres());
    }

    @Test
    void testGetterMaterias() {
        List<Materia> materias = Arrays.asList(materia1, materia2, materia3);
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS004", 10, materias, 150);

        List<Materia> materiasObtenidas = carrera.getMaterias();
        assertNotNull(materiasObtenidas);
        assertEquals(3, materiasObtenidas.size());
        assertEquals("PROG1", materiasObtenidas.get(0).getAcronimo());
    }

    @Test
    void testGetterCreditosTotales() {
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS005", 10, 155);
        assertEquals(155, carrera.getCreditosTotales());
    }

    @Test
    void testGetTotalMateriasConListaVacia() {
        Carrera carrera = new Carrera();
        assertEquals(0, carrera.getTotalMaterias());
    }

    @Test
    void testGetTotalMateriasConListaNula() {
        Carrera carrera = new Carrera();
        carrera.setMaterias(null);
        assertEquals(0, carrera.getTotalMaterias());
    }

    @Test
    void testGetTotalMateriasConMaterias() {
        List<Materia> materias = Arrays.asList(materia1, materia2);
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS006", 10, materias, 150);

        assertEquals(2, carrera.getTotalMaterias());
    }

    @Test
    void testAddMateriaExitosa() {
        Carrera carrera = new Carrera();
        carrera.addMateria(materia1);

        assertEquals(1, carrera.getTotalMaterias());
        assertEquals(materia1, carrera.getMaterias().get(0));
    }

    @Test
    void testAddMateriaDuplicada() {
        Carrera carrera = new Carrera();
        carrera.addMateria(materia1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carrera.addMateria(materia1);
        });

        assertEquals("La materia con acronimo PROG1 ya existe en la carrera null", exception.getMessage());
        assertEquals(1, carrera.getTotalMaterias()); // Solo debe tener una materia
    }

    @Test
    void testAddMateriasDiferentes() {
        Carrera carrera = new Carrera();
        carrera.addMateria(materia1);
        carrera.addMateria(materia2);
        carrera.addMateria(materia3);

        assertEquals(3, carrera.getTotalMaterias());
    }

    @Test
    void testAddMateriaConCodigoDeCarrera() {
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS007", 10, 150);
        carrera.addMateria(materia1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carrera.addMateria(materia1);
        });

        assertEquals("La materia con acronimo PROG1 ya existe en la carrera IS007", exception.getMessage());
    }

    @Test
    void testSetterMaterias() {
        Carrera carrera = new Carrera();
        List<Materia> nuevasMaterias = Arrays.asList(materia1, materia2);

        carrera.setMaterias(nuevasMaterias);

        assertEquals(2, carrera.getMaterias().size());
        assertEquals(nuevasMaterias, carrera.getMaterias());
    }

    @Test
    void testSetterMateriasReemplazaListaAnterior() {
        List<Materia> materiasIniciales = Arrays.asList(materia1);
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS008", 10, materiasIniciales, 150);

        List<Materia> nuevasMaterias = Arrays.asList(materia2, materia3);
        carrera.setMaterias(nuevasMaterias);

        assertEquals(2, carrera.getMaterias().size());
        assertTrue(carrera.getMaterias().contains(materia2));
        assertFalse(carrera.getMaterias().contains(materia1));
    }

    // Pruebas edge cases
    @Test
    void testCarreraConDuracionCero() {
        Carrera carrera = new Carrera(Facultad.INGENIERIA_SISTEMAS, "IS010", 0, 0);
        assertEquals(0, carrera.getDuracionSemestres());
        assertEquals(0, carrera.getCreditosTotales());
    }

    @Test
    void testCarreraConCreditosNegativos() {
        Carrera carrera = new Carrera();
        carrera.setCreditosTotales(-100);
        assertEquals(-100, carrera.getCreditosTotales());
    }

    @Test
    void testManejoDeListaMateriasNula() {
        Carrera carrera = new Carrera();
        carrera.setMaterias(null);

        assertNull(carrera.getMaterias());
        assertEquals(0, carrera.getTotalMaterias());

        carrera.setMaterias(Arrays.asList(materia1));
        assertEquals(1, carrera.getTotalMaterias());
    }
}