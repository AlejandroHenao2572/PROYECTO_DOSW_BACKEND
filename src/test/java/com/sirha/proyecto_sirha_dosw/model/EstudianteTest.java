package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class EstudianteTest {

    private Estudiante estudiante;
    private Solicitud solicitud;
    private Semestre semestre1, semestre2;
    private RegistroMaterias registro1, registro2, registro3;
    private Grupo grupo1, grupo2;
    private Materia materia1, materia2;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante(
                "Juan", "Perez", "juan@test.com", "pass123",
                Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS
        );

        // Configurar materias
        materia1 = new Materia("Programación I", "PROG1", 4);
        materia2 = new Materia("Base de Datos", "BD1", 4);

        // Configurar grupos
        grupo1 = new Grupo(materia1, 30, new ArrayList<>());
        grupo2 = new Grupo(materia2, 25, new ArrayList<>());

        // Configurar registros
        registro1 = new RegistroMaterias();
        registro1.setGrupo(grupo1);
        registro1.setEstado(Semaforo.VERDE);

        registro2 = new RegistroMaterias();
        registro2.setGrupo(grupo2);
        registro2.setEstado(Semaforo.ROJO);

        registro3 = new RegistroMaterias();
        registro3.setGrupo(grupo1);
        registro3.setEstado(Semaforo.AZUL);

        // Configurar semestres
        semestre1 = new Semestre();
        semestre1.setNumero(1);
        semestre1.addRegistro(registro1);
        semestre1.addRegistro(registro2);

        semestre2 = new Semestre();
        semestre2.setNumero(2);
        semestre2.addRegistro(registro3);

        // Configurar solicitud
        solicitud = new Solicitud();
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitud.setEstudianteId("est123");
    }

    // Pruebas para métodos con 0% de cobertura

    @Test
    void testGetRegistrosBySemestre() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        // Test semestre 1
        List<RegistroMaterias> registrosSem1 = estudiante.getRegistrosBySemestre(1);
        assertEquals(2, registrosSem1.size());
        assertEquals(registro1, registrosSem1.get(0));
        assertEquals(registro2, registrosSem1.get(1));

        // Test semestre 2
        List<RegistroMaterias> registrosSem2 = estudiante.getRegistrosBySemestre(2);
        assertEquals(1, registrosSem2.size());
        assertEquals(registro3, registrosSem2.get(0));
    }

    @Test
    void testGetRegistrosBySemestreInexistente() {
        estudiante.setSemestres(Arrays.asList(semestre1));

        // Debería lanzar excepción por índice fuera de rango
        assertThrows(IndexOutOfBoundsException.class, () -> {
            estudiante.getRegistrosBySemestre(5);
        });
    }

    @Test
    void testSetSolicitudes() {
        List<Solicitud> solicitudes = Arrays.asList(solicitud);
        estudiante.setSolicitudes(solicitudes);

        assertEquals(1, estudiante.getSolicitudes().size());
        assertEquals(solicitud, estudiante.getSolicitudes().get(0));
    }

    @Test
    void testGetCarrera() {
        assertEquals(Facultad.INGENIERIA_SISTEMAS, estudiante.getCarrera());
    }

    @Test
    void testSetCarrera() {
        estudiante.setCarrera(Facultad.ADMINISTRACION);
        assertEquals(Facultad.ADMINISTRACION, estudiante.getCarrera());
    }

    // Pruebas para getSemaforo() (30% de cobertura - necesita más casos)

    @Test
    void testGetSemaforoConRegistros() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        Map<String, Semaforo> semaforo = estudiante.getSemaforo();

        assertEquals(2, semaforo.size());
        assertEquals(Semaforo.AZUL, semaforo.get("Programación I"));
        assertEquals(Semaforo.ROJO, semaforo.get("Base de Datos"));
        // Nota: registro3 sobrescribe registro1 porque tienen la misma materia
    }

    @Test
    void testGetSemaforoSinSemestres() {
        Map<String, Semaforo> semaforo = estudiante.getSemaforo();
        assertTrue(semaforo.isEmpty());
    }

    @Test
    void testGetSemaforoSemestreVacio() {
        Semestre semestreVacio = new Semestre();
        estudiante.setSemestres(Arrays.asList(semestreVacio));

        Map<String, Semaforo> semaforo = estudiante.getSemaforo();
        assertTrue(semaforo.isEmpty());
    }

    @Test
    void testGetSemaforoMateriasDuplicadas() {
        // Dos registros para la misma materia - debería quedarse con el último
        RegistroMaterias registroVerde = new RegistroMaterias();
        registroVerde.setGrupo(grupo1);
        registroVerde.setEstado(Semaforo.VERDE);

        RegistroMaterias registroRojo = new RegistroMaterias();
        registroRojo.setGrupo(grupo1); // Misma materia que registroVerde
        registroRojo.setEstado(Semaforo.ROJO);

        Semestre semestre = new Semestre();
        semestre.addRegistro(registroVerde);
        semestre.addRegistro(registroRojo);

        estudiante.setSemestres(Arrays.asList(semestre));

        Map<String, Semaforo> semaforo = estudiante.getSemaforo();
        assertEquals(1, semaforo.size());
        assertEquals(Semaforo.ROJO, semaforo.get("Programación I")); // Último estado
    }

    // Pruebas para métodos con 100% de cobertura (para completar)

    @Test
    void testConstructores() {
        // Constructor vacío
        Estudiante est1 = new Estudiante();
        assertNotNull(est1);
        assertNull(est1.getCarrera());

        // Constructor con carrera
        Estudiante est2 = new Estudiante("Maria", "Gomez", "maria@test.com", "pass", Facultad.INGENIERIA_CIVIL);
        assertEquals(Facultad.INGENIERIA_CIVIL, est2.getCarrera());

        // Constructor con rol
        Estudiante est3 = new Estudiante("Carlos", "Lopez", "carlos@test.com", "pass", Rol.ESTUDIANTE, Facultad.ADMINISTRACION);
        assertEquals(Rol.ESTUDIANTE, est3.getRol());
        assertEquals(Facultad.ADMINISTRACION, est3.getCarrera());
    }

    @Test
    void testAddSolicitud() {
        estudiante.addSolicitud(solicitud);
        assertEquals(1, estudiante.getSolicitudes().size());
        assertEquals(solicitud, estudiante.getSolicitudes().get(0));
    }

    @Test
    void testSetSemestres() {
        List<Semestre> semestres = Arrays.asList(semestre1, semestre2);
        estudiante.setSemestres(semestres);

        assertEquals(2, estudiante.getSemestres().size());
        assertEquals(semestre1, estudiante.getSemestres().get(0));
    }

    @Test
    void testGetSemestres() {
        assertNotNull(estudiante.getSemestres());
        assertTrue(estudiante.getSemestres().isEmpty());

        estudiante.setSemestres(Arrays.asList(semestre1));
        assertEquals(1, estudiante.getSemestres().size());
    }

    @Test
    void testGetSolicitudes() {
        assertNotNull(estudiante.getSolicitudes());
        assertTrue(estudiante.getSolicitudes().isEmpty());

        estudiante.addSolicitud(solicitud);
        assertEquals(1, estudiante.getSolicitudes().size());
    }
}