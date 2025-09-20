package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

class EstudianteTest {

    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante("Juan", "Perez", "juan@email.com",
                "password", Rol.ESTUDIANTE, CarreraTipo.INGENIERIA_SISTEMAS);
    }

    @Test
    void testCreacionEstudiante() {
        assertNotNull(estudiante.getCarrera());
        assertTrue(estudiante.getCarrera() instanceof IngenieriaSistemas);
        assertEquals(1, estudiante.getSemestres().size());
        assertNotNull(estudiante.getSemestreActual());
    }

    @Test
    void testAgregarSemestre() {
        int semestresIniciales = estudiante.getSemestres().size();
        estudiante.agregarSemestre(new Semestre(2));

        assertEquals(semestresIniciales + 1, estudiante.getSemestres().size());
        assertEquals(2, estudiante.getSemestreActual().getNumero());
    }

    @Test
    void testAgregarMateriaActual() {
        Materia materia = estudiante.getCarrera().getObligatorias().get(0);
        estudiante.agregarMateriaActual(materia);

        assertEquals(1, estudiante.getSemestreActual().getMaterias().size());
        assertEquals(materia, estudiante.getSemestreActual().getMaterias().get(0).getMateria());
    }

    @Test
    void testGetMateriasPendientes() {
        List<Materia> pendientes = estudiante.getMateriasPendientes();
        assertNotNull(pendientes);
        // Todas las obligatorias deberían estar pendientes inicialmente
        assertEquals(estudiante.getCarrera().getObligatorias().size(), pendientes.size());
    }
    @Test
    void testHorariosPorSemestre() {
        Horario horario = new Horario("1");
        estudiante.setHorarioPorSemestre(1, horario);

        assertNotNull(estudiante.getHorarioActual());
        assertNotNull(estudiante.getHorarioPorSemestre(1));
    }

    @Test
    void testGruposInscritos() {
        estudiante.inscribirEnGrupo("GRP001");
        estudiante.inscribirEnGrupo("GRP002");

        List<String> grupos = estudiante.getGruposInscritos();
        assertEquals(2, grupos.size());
        assertTrue(grupos.contains("GRP001"));
        assertTrue(grupos.contains("GRP002"));
    }

    @Test
    void testDesinscribirDeGrupo() {
        estudiante.inscribirEnGrupo("GRP001");
        estudiante.inscribirEnGrupo("GRP002");

        estudiante.desinscribirDeGrupo("GRP001");

        List<String> grupos = estudiante.getGruposInscritos();
        assertEquals(1, grupos.size());
        assertFalse(grupos.contains("GRP001"));
        assertTrue(grupos.contains("GRP002"));
    }

    @Test
    void testSemaforoAcademico() {
        // Agregar materia al semestre actual
        Materia materia = estudiante.getCarrera().getObligatorias().get(0);
        estudiante.agregarMateriaActual(materia);

        Map<String, SemaforoAcademico> semaforo = estudiante.calcularSemaforoAcademico();

        assertNotNull(semaforo);
        assertTrue(semaforo.containsKey("101"));
        assertEquals(SemaforoAcademico.AZUL, semaforo.get("101")); // Estado inicial
    }

    @Test
    void testGetHistorial() {
        // Semestre inicial
        assertEquals(0, estudiante.getHistorial().size());

        // Agregar semestre
        estudiante.agregarSemestre(new Semestre(2));

        assertEquals(1, estudiante.getHistorial().size());
        assertEquals(1, estudiante.getHistorial().get(0).getNumero());
    }

    @Test
    void testPromedioAcumulado() {
        estudiante.setPromedioAcumulado(4.2);
        assertEquals(4.2, estudiante.getPromedioAcumulado(), 0.01);
    }

    @Test
    void testCarreraSetter() {
        Carrera nuevaCarrera = new IngenieriaCivil();
        estudiante.setCarrera(nuevaCarrera);

        assertEquals("Ingeniería Civil", estudiante.getCarrera().getNombre());
        assertTrue(estudiante.getCarrera() instanceof IngenieriaCivil);
    }
}