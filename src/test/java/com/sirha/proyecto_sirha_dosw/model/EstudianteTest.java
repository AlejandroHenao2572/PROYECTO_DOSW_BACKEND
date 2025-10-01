package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstudianteTest {

    private Estudiante estudiante;
    private Solicitud solicitud;
    private Semestre semestre1, semestre2;
    private RegistroMaterias registro1, registro2, registro3;
    private Grupo grupo1, grupo2, grupo3;
    private Materia materia1, materia2, materia3;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante(
                "Juan", "Perez", "juan@test.com", "pass123",
                Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS
        );

        // Configurar materias
        materia1 = new Materia("Programación I", "PROG1", 4, Facultad.INGENIERIA_SISTEMAS);
        materia2 = new Materia("Base de Datos", "BD1", 4, Facultad.INGENIERIA_SISTEMAS);
        materia3 = new Materia("Matemáticas", "MAT1", 3, Facultad.INGENIERIA_SISTEMAS);

        // Configurar grupos con IDs
        grupo1 = new Grupo(materia1, 30, new ArrayList<>());
        grupo1.setId("grupo1");
        grupo2 = new Grupo(materia2, 25, new ArrayList<>());
        grupo2.setId("grupo2");
        grupo3 = new Grupo(materia3, 20, new ArrayList<>());
        grupo3.setId("grupo3");

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

    // NUEVAS PRUEBAS PARA LOS MÉTODOS FALTANTES

    @Test
    void testTieneMateriaCandeladaEnSemestreActualConMateriaCancelada() {
        // Crear registro con materia cancelada
        RegistroMaterias registroCancelado = new RegistroMaterias();
        registroCancelado.setGrupo(grupo1);
        registroCancelado.setEstado(Semaforo.CANCELADO);

        Semestre semestreActual = new Semestre();
        semestreActual.addRegistro(registroCancelado);

        estudiante.setSemestres(Arrays.asList(semestreActual));

        assertTrue(estudiante.tieneMateriaCandeladaEnSemestreActual("PROG1"));
    }

    @Test
    void testTieneMateriaCandeladaEnSemestreActualSinMateriaCancelada() {
        estudiante.setSemestres(Arrays.asList(semestre1));

        assertFalse(estudiante.tieneMateriaCandeladaEnSemestreActual("PROG1"));
    }

    @Test
    void testTieneMateriaCandeladaEnSemestreActualSinSemestres() {
        assertFalse(estudiante.tieneMateriaCandeladaEnSemestreActual("PROG1"));
    }

    @Test
    void testTieneMateriaCandeladaEnSemestreActualMateriaNoExiste() {
        estudiante.setSemestres(Arrays.asList(semestre1));

        assertFalse(estudiante.tieneMateriaCandeladaEnSemestreActual("MATERIA_INEXISTENTE"));
    }

    @Test
    void testGetSemestreActualConSemestres() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        Semestre actual = estudiante.getSemestreActual();
        assertEquals(semestre2, actual);
        assertEquals(2, actual.getNumero());
    }

    @Test
    void testGetSemestreActualSinSemestres() {
        Semestre actual = estudiante.getSemestreActual();
        assertNull(actual);
    }

    @Test
    void testGetSemestreActualConUnSemestre() {
        estudiante.setSemestres(Arrays.asList(semestre1));

        Semestre actual = estudiante.getSemestreActual();
        assertEquals(semestre1, actual);
    }

    @Test
    void testAddGrupo() {
        estudiante.setSemestres(Arrays.asList(semestre1));
        int registrosIniciales = semestre1.getRegistros().size();

        estudiante.addGrupo(grupo3);

        assertEquals(registrosIniciales + 1, semestre1.getRegistros().size());
        
        // Verificar que el nuevo grupo fue agregado
        boolean grupoEncontrado = false;
        for (RegistroMaterias registro : semestre1.getRegistros()) {
            if (registro.getGrupo().equals(grupo3)) {
                grupoEncontrado = true;
                break;
            }
        }
        assertTrue(grupoEncontrado);
    }

    @Test
    void testAddGrupoConSemestreVacio() {
        Semestre semestreVacio = new Semestre();
        estudiante.setSemestres(Arrays.asList(semestreVacio));

        estudiante.addGrupo(grupo1);

        assertEquals(1, semestreVacio.getRegistros().size());
        assertEquals(grupo1, semestreVacio.getRegistros().get(0).getGrupo());
    }

    @Test
    void testRemoveGrupoExistente() {
        estudiante.setSemestres(Arrays.asList(semestre1));
        int registrosIniciales = semestre1.getRegistros().size();

        estudiante.removeGrupo(grupo1);

        assertEquals(registrosIniciales - 1, semestre1.getRegistros().size());
        
        // Verificar que el grupo fue removido
        for (RegistroMaterias registro : semestre1.getRegistros()) {
            assertNotEquals(grupo1, registro.getGrupo());
        }
    }

    @Test
    void testRemoveGrupoNoExistente() {
        estudiante.setSemestres(Arrays.asList(semestre1));
        int registrosIniciales = semestre1.getRegistros().size();

        estudiante.removeGrupo(grupo3); // grupo3 no está en semestre1

        assertEquals(registrosIniciales, semestre1.getRegistros().size());
    }

    @Test
    void testRemoveGrupoSemestreVacio() {
        Semestre semestreVacio = new Semestre();
        estudiante.setSemestres(Arrays.asList(semestreVacio));

        // No debería lanzar excepción
        assertDoesNotThrow(() -> estudiante.removeGrupo(grupo1));
        assertEquals(0, semestreVacio.getRegistros().size());
    }

    @Test
    void testGetGruposConSemestres() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGrupos();

        assertEquals(1, grupos.size()); // semestre2 (último) tiene 1 registro
        assertEquals(grupo1, grupos.get(0)); // registro3 tiene grupo1
    }

    @Test
    void testGetGruposSinSemestres() {
        List<Grupo> grupos = estudiante.getGrupos();

        assertTrue(grupos.isEmpty());
    }

    @Test
    void testGetGruposSemestreActualVacio() {
        Semestre semestreVacio = new Semestre();
        estudiante.setSemestres(Arrays.asList(semestre1, semestreVacio));

        List<Grupo> grupos = estudiante.getGrupos();

        assertTrue(grupos.isEmpty());
    }

    @Test
    void testGetGruposConMultiplesGrupos() {
        // Agregar más registros al semestre actual
        RegistroMaterias registro4 = new RegistroMaterias();
        registro4.setGrupo(grupo2);
        registro4.setEstado(Semaforo.VERDE);

        RegistroMaterias registro5 = new RegistroMaterias();
        registro5.setGrupo(grupo3);
        registro5.setEstado(Semaforo.AZUL);

        semestre2.addRegistro(registro4);
        semestre2.addRegistro(registro5);

        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGrupos();

        assertEquals(3, grupos.size());
        assertTrue(grupos.contains(grupo1));
        assertTrue(grupos.contains(grupo2));
        assertTrue(grupos.contains(grupo3));
    }

    @Test
    void testGetGruposExcluyendoConGrupoAExcluir() {
        // Agregar más registros al semestre actual
        RegistroMaterias registro4 = new RegistroMaterias();
        registro4.setGrupo(grupo2);
        registro4.setEstado(Semaforo.VERDE);

        semestre2.addRegistro(registro4);
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGruposExcluyendo(grupo1);

        assertEquals(1, grupos.size());
        assertEquals(grupo2, grupos.get(0));
        assertFalse(grupos.contains(grupo1));
    }

    @Test
    void testGetGruposExcluyendoConGrupoNulo() {
        RegistroMaterias registro4 = new RegistroMaterias();
        registro4.setGrupo(grupo2);
        registro4.setEstado(Semaforo.VERDE);

        semestre2.addRegistro(registro4);
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGruposExcluyendo(null);

        assertEquals(2, grupos.size());
        assertTrue(grupos.contains(grupo1));
        assertTrue(grupos.contains(grupo2));
    }

    @Test
    void testGetGruposExcluyendoSinSemestres() {
        List<Grupo> grupos = estudiante.getGruposExcluyendo(grupo1);

        assertTrue(grupos.isEmpty());
    }

    @Test
    void testGetGruposExcluyendoGrupoNoExistente() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGruposExcluyendo(grupo3); // grupo3 no está en semestre2

        assertEquals(1, grupos.size());
        assertEquals(grupo1, grupos.get(0));
    }

    @Test
    void testGetGruposExcluyendoTodosLosGrupos() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));

        List<Grupo> grupos = estudiante.getGruposExcluyendo(grupo1);

        assertTrue(grupos.isEmpty()); // semestre2 solo tiene grupo1
    }

    @Test
    void testGetGruposExcluyendoSemestreActualVacio() {
        Semestre semestreVacio = new Semestre();
        estudiante.setSemestres(Arrays.asList(semestre1, semestreVacio));

        List<Grupo> grupos = estudiante.getGruposExcluyendo(grupo1);

        assertTrue(grupos.isEmpty());
    }

    // Pruebas adicionales para casos edge
    @Test
    void testAddGrupoConMultiplesSemestres() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));
        int registrosIniciales = semestre2.getRegistros().size(); // Debe agregar al último semestre

        estudiante.addGrupo(grupo3);

        assertEquals(registrosIniciales + 1, semestre2.getRegistros().size());
        assertEquals(semestre1.getRegistros().size(), 2); // semestre1 no debe cambiar
    }

    @Test
    void testRemoveGrupoConMultiplesSemestres() {
        estudiante.setSemestres(Arrays.asList(semestre1, semestre2));
        int registrosIniciales = semestre2.getRegistros().size();

        estudiante.removeGrupo(grupo1); // Debe remover del último semestre

        assertEquals(registrosIniciales - 1, semestre2.getRegistros().size());
        assertEquals(semestre1.getRegistros().size(), 2); // semestre1 no debe cambiar
    }
}