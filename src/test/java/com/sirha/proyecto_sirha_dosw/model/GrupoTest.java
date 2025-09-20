package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class GrupoTest {

    private Grupo grupo;

    @BeforeEach
    void setUp() {
        grupo = new Grupo("G01", "MAT101", "PROF001", 30, "2024-1");
    }

    @Test
    void testConstructorConParametros() {
        assertEquals("G01", grupo.getNumero());
        assertEquals("MAT101", grupo.getMateriaId());
        assertEquals("PROF001", grupo.getProfesorId());
        assertEquals(30, grupo.getCapacidadMaxima());
        assertEquals("2024-1", grupo.getSemestre());
        assertTrue(grupo.getEstudiantesInscritos().isEmpty());
        assertNull(grupo.getHorario());
        assertNull(grupo.getSalon());
    }

    @Test
    void testConstructorVacio() {
        Grupo grupoVacio = new Grupo();
        assertNull(grupoVacio.getNumero());
        assertNull(grupoVacio.getMateriaId());
        assertNull(grupoVacio.getProfesorId());
        assertNull(grupoVacio.getCapacidadMaxima());
        assertNull(grupoVacio.getSemestre());
        assertNotNull(grupoVacio.getEstudiantesInscritos());
        assertTrue(grupoVacio.getEstudiantesInscritos().isEmpty());
    }

    @Test
    void testEstaCompletoGrupoVacio() {
        assertFalse(grupo.estaCompleto());
    }

    @Test
    void testEstaCompletoGrupoLleno() {
        // Llenar el grupo
        for (int i = 1; i <= 30; i++) {
            grupo.inscribirEstudiante("EST" + i);
        }

        assertTrue(grupo.estaCompleto());
    }

    @Test
    void testEstaCompletoGrupoParcialmenteLleno() {
        grupo.inscribirEstudiante("EST001");
        grupo.inscribirEstudiante("EST002");

        assertFalse(grupo.estaCompleto());
    }

    @Test
    void testGetCuposDisponiblesGrupoVacio() {
        assertEquals(30, grupo.getCuposDisponibles());
    }

    @Test
    void testGetCuposDisponiblesGrupoParcial() {
        grupo.inscribirEstudiante("EST001");
        grupo.inscribirEstudiante("EST002");

        assertEquals(28, grupo.getCuposDisponibles());
    }

    @Test
    void testGetCuposDisponiblesGrupoLleno() {
        for (int i = 1; i <= 30; i++) {
            grupo.inscribirEstudiante("EST" + i);
        }

        assertEquals(0, grupo.getCuposDisponibles());
    }

    @Test
    void testGetCantidadInscritosGrupoVacio() {
        assertEquals(0, grupo.getCantidadInscritos());
    }

    @Test
    void testGetCantidadInscritosGrupoConEstudiantes() {
        grupo.inscribirEstudiante("EST001");
        grupo.inscribirEstudiante("EST002");
        grupo.inscribirEstudiante("EST003");

        assertEquals(3, grupo.getCantidadInscritos());
    }

    @Test
    void testInscribirEstudianteExitoso() {
        assertTrue(grupo.inscribirEstudiante("EST001"));
        assertEquals(1, grupo.getCantidadInscritos());
        assertTrue(grupo.getEstudiantesInscritos().contains("EST001"));
    }

    @Test
    void testInscribirEstudianteDuplicado() {
        grupo.inscribirEstudiante("EST001");
        assertFalse(grupo.inscribirEstudiante("EST001")); // No debería permitir duplicados
        assertEquals(1, grupo.getCantidadInscritos());
    }

    @Test
    void testInscribirEstudianteGrupoLleno() {
        // Llenar el grupo
        for (int i = 1; i <= 30; i++) {
            grupo.inscribirEstudiante("EST" + i);
        }

        assertFalse(grupo.inscribirEstudiante("EST031")); // No debería permitir inscripción
        assertEquals(30, grupo.getCantidadInscritos());
    }

    @Test
    void testDesinscribirEstudianteExistente() {
        grupo.inscribirEstudiante("EST001");
        assertTrue(grupo.desinscribirEstudiante("EST001"));
        assertEquals(0, grupo.getCantidadInscritos());
        assertFalse(grupo.getEstudiantesInscritos().contains("EST001"));
    }

    @Test
    void testDesinscribirEstudianteInexistente() {
        assertFalse(grupo.desinscribirEstudiante("EST999")); // No existe
        assertEquals(0, grupo.getCantidadInscritos());
    }

    @Test
    void testDesinscribirEstudianteDeGrupoVacio() {
        assertFalse(grupo.desinscribirEstudiante("EST001"));
        assertEquals(0, grupo.getCantidadInscritos());
    }

    @Test
    void testSettersYGetters() {
        // Test de todos los setters y getters
        grupo.setId("GRP001");
        assertEquals("GRP001", grupo.getId());

        grupo.setNumero("G02");
        assertEquals("G02", grupo.getNumero());

        grupo.setMateriaId("MAT102");
        assertEquals("MAT102", grupo.getMateriaId());

        grupo.setProfesorId("PROF002");
        assertEquals("PROF002", grupo.getProfesorId());

        grupo.setCapacidadMaxima(40);
        assertEquals(40, grupo.getCapacidadMaxima());

        grupo.setSalon("A101");
        assertEquals("A101", grupo.getSalon());

        grupo.setSemestre("2024-2");
        assertEquals("2024-2", grupo.getSemestre());

        // Test de horario
        Horario horario = new Horario("2024-1");
        grupo.setHorario(horario);
        assertEquals(horario, grupo.getHorario());

        // Test de estudiantes inscritos
        List<String> estudiantes = List.of("EST001", "EST002");
        grupo.setEstudiantesInscritos(estudiantes);
        assertEquals(2, grupo.getEstudiantesInscritos().size());
        assertTrue(grupo.getEstudiantesInscritos().contains("EST001"));
        assertTrue(grupo.getEstudiantesInscritos().contains("EST002"));
    }

    @Test
    void testSetEstudiantesInscritosReemplazaLista() {
        grupo.inscribirEstudiante("EST001");
        grupo.inscribirEstudiante("EST002");

        List<String> nuevosEstudiantes = List.of("EST003", "EST004");
        grupo.setEstudiantesInscritos(nuevosEstudiantes);

        assertEquals(2, grupo.getEstudiantesInscritos().size());
        assertTrue(grupo.getEstudiantesInscritos().contains("EST003"));
        assertTrue(grupo.getEstudiantesInscritos().contains("EST004"));
        assertFalse(grupo.getEstudiantesInscritos().contains("EST001"));
    }

    @Test
    void testCapacidadMaximaCero() {
        grupo.setCapacidadMaxima(0);
        assertTrue(grupo.estaCompleto());
        assertEquals(0, grupo.getCuposDisponibles());

        assertFalse(grupo.inscribirEstudiante("EST001")); // No debería permitir
    }

    @Test
    void testCapacidadMaximaNegativa() {
        grupo.setCapacidadMaxima(5);
        assertFalse(grupo.estaCompleto());
        assertEquals(5, grupo.getCuposDisponibles());

        assertTrue(grupo.inscribirEstudiante("EST001")); // No debería permitir
    }

    @Test
    void testInscribirMultiplesEstudiantes() {
        for (int i = 1; i <= 10; i++) {
            assertTrue(grupo.inscribirEstudiante("EST" + i));
        }

        assertEquals(10, grupo.getCantidadInscritos());
        assertEquals(20, grupo.getCuposDisponibles());
        assertFalse(grupo.estaCompleto());
    }

    @Test
    void testDesinscribirYReinscribir() {
        grupo.inscribirEstudiante("EST001");
        assertEquals(1, grupo.getCantidadInscritos());

        grupo.desinscribirEstudiante("EST001");
        assertEquals(0, grupo.getCantidadInscritos());

        assertTrue(grupo.inscribirEstudiante("EST001")); // Debería permitir reinscripción
        assertEquals(1, grupo.getCantidadInscritos());
    }

    @Test
    void testDesinscribirEstudianteNull() {
        assertFalse(grupo.desinscribirEstudiante(null));
    }

    @Test
    void testDesinscribirEstudianteVacio() {
        assertFalse(grupo.desinscribirEstudiante(""));
    }

    @Test
    void testCapacidadMaximaCambioDinamico() {
        grupo.inscribirEstudiante("EST001");
        grupo.inscribirEstudiante("EST002");

        assertEquals(2, grupo.getCantidadInscritos());
        assertEquals(28, grupo.getCuposDisponibles());

        // Reducir capacidad máxima
        grupo.setCapacidadMaxima(2);
        assertTrue(grupo.estaCompleto());
        assertEquals(0, grupo.getCuposDisponibles());

        // Aumentar capacidad máxima
        grupo.setCapacidadMaxima(5);
        assertFalse(grupo.estaCompleto());
        assertEquals(3, grupo.getCuposDisponibles());
    }

    @Test
    void testHorarioFuncionalidad() {
        Horario horario = new Horario("2024-1");
        Materia materia = new Materia("MAT101", "MAT", "Matemáticas", 3, new EstrategiaTresCortes(0.3, 0.3, 0.4));
        FranjaHoraria franja = new FranjaHoraria("08:00", "10:00");

        horario.agregarClase(Dia.LUNES, franja, materia, "A101");
        grupo.setHorario(horario);

        assertNotNull(grupo.getHorario());
        assertEquals(1, grupo.getHorario().getClasesPorDia(Dia.LUNES).size());
    }

    @Test
    void testSemestreGetterSetter() {
        grupo.setSemestre("2024-2");
        assertEquals("2024-2", grupo.getSemestre());

        grupo.setSemestre(null);
        assertNull(grupo.getSemestre());
    }

    @Test
    void testSalonGetterSetter() {
        grupo.setSalon("B205");
        assertEquals("B205", grupo.getSalon());

        grupo.setSalon(null);
        assertNull(grupo.getSalon());
    }

}