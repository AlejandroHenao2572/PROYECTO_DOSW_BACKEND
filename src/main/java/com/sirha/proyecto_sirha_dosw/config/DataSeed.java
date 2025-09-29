package com.sirha.proyecto_sirha_dosw.config;

import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que inicializa datos de prueba en la base de datos.
 * Solo se ejecuta en el perfil 'dev' para evitar ejecutarse en producción.
 */
@Component
@Profile("dev")
public class DataSeed implements CommandLineRunner {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpiar datos existentes para pruebas limpias
        limpiarDatos();
        
        // Crear datos de prueba
        crearDatosDePrueba();
        
        System.out.println("DataSeed ejecutado exitosamente - Datos de prueba creados");
    }

    private void limpiarDatos() {
        solicitudRepository.deleteAll();
        grupoRepository.deleteAll();
        materiaRepository.deleteAll();
        usuarioRepository.deleteAll();
        carreraRepository.deleteAll();
        System.out.println("🧹 Datos existentes eliminados");
    }

    private void crearDatosDePrueba() {
        // 1. Crear carrera
        Carrera ingenieriaSistemas = crearCarrera();
        
        // 2. Crear materias
        Materia desarrolloSoftware = crearMateria("Desarrollo de Software", "DOSW", 4);
        Materia sistemasOperativos = crearMateria("Sistemas Operativos", "ODSC", 3);

        // 3. Agregar materias a la carrera
        ingenieriaSistemas.getMaterias().add(desarrolloSoftware);
        ingenieriaSistemas.getMaterias().add(sistemasOperativos);
        
        // Guardar carrera actualizada con materias
        carreraRepository.save(ingenieriaSistemas);

        // 2. Crear profesores
        Profesor profesorPablo = crearProfesor("Pablo", "Melo", "palo.melo@escuelaing.edu.co", "password123");
        Profesor profesorGerardo = crearProfesor("Gerardo", "Ospina", "gerardo.ospina@escuelaing.edu.co", "password123");

        // 3. Crear grupos con horarios diferentes
        Grupo grupoDOSW1 = crearGrupo(desarrolloSoftware, 25, profesorPablo, 
            crearHorarios(Dia.LUNES, "08:00", "10:00", Dia.MIERCOLES, "08:00", "10:00"));
        grupoDOSW1.setId("01");

        Grupo grupoDOSW2 = crearGrupo(desarrolloSoftware, 30, profesorPablo,
            crearHorarios(Dia.MARTES, "14:00", "16:00", Dia.JUEVES, "14:00", "16:00"));
        grupoDOSW2.setId("02");

        Grupo grupoODSC1 = crearGrupo(sistemasOperativos, 28, profesorGerardo,
            crearHorarios(Dia.LUNES, "10:00", "12:00", Dia.MIERCOLES, "10:00", "12:00"));
        grupoODSC1.setId("03");

        Grupo grupoODSC2 = crearGrupo(sistemasOperativos, 25, profesorGerardo,
            crearHorarios(Dia.MARTES, "16:00", "18:00", Dia.VIERNES, "16:00", "18:00"));
        grupoODSC2.setId("04");

        // 4. Crear estudiantes
        Estudiante estudiante1 = crearEstudiante("David", "Patacon", "david.patacon@mail.escuelaing.edu.co", "student123");
        estudiante1.setId("1000100406");
        Estudiante estudiante2 = crearEstudiante("Jared", "Farfan", "jared.farfan@mail.escuelaing.edu.co", "student123");
        estudiante2.setId("1000100405");

        // 5. Crear semestres y registros de materias para los estudiantes
        crearSemestreParaEstudiante(estudiante1, Arrays.asList(grupoDOSW1, grupoODSC1));
        crearSemestreParaEstudiante(estudiante2, Arrays.asList(grupoDOSW2, grupoODSC2));

        // 6. Inscribir estudiantes en los grupos
        inscribirEstudianteEnGrupo(grupoDOSW1, estudiante1.getId());
        inscribirEstudianteEnGrupo(grupoODSC1, estudiante1.getId());
        inscribirEstudianteEnGrupo(grupoDOSW2, estudiante2.getId());
        inscribirEstudianteEnGrupo(grupoODSC2, estudiante2.getId());

        // 7. Crear decano
        Decano decano = crearDecano("Claudia", "Cely", "roberto.fernandez@universidad.edu", "decano123");
        decano.setId("DEC001");

        // 8. Guardar todos los cambios
        materiaRepository.saveAll(Arrays.asList(desarrolloSoftware, sistemasOperativos));
        usuarioRepository.saveAll(Arrays.asList(profesorPablo, profesorGerardo, estudiante1, estudiante2, decano));
        grupoRepository.saveAll(Arrays.asList(grupoDOSW1, grupoDOSW2, grupoODSC1, grupoODSC2));

        // 9. Crear solicitudes de prueba
        crearSolicitudesDePrueba(estudiante1, estudiante2, grupoDOSW1, grupoDOSW2, 
                               grupoODSC1, grupoODSC2, desarrolloSoftware, sistemasOperativos);

    }

    private Carrera crearCarrera() {
        Carrera ingenieriaSistemas = new Carrera(
            Facultad.INGENIERIA_SISTEMAS, 
            "ISIS", 
            8, 
            145
        );
        return carreraRepository.save(ingenieriaSistemas);
    }

    private Materia crearMateria(String nombre, String acronimo, int creditos) {
        return new Materia(nombre, acronimo, creditos, Facultad.INGENIERIA_CIVIL);
    }

    private Profesor crearProfesor(String nombre, String apellido, String email, String password) {
        return new Profesor(nombre, apellido, email, password, Rol.PROFESOR);
    }

    private Estudiante crearEstudiante(String nombre, String apellido, String email, String password) {
        return new Estudiante(nombre, apellido, email, password, Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS);
    }

    private Decano crearDecano(String nombre, String apellido, String email, String password) {
        return new Decano(nombre, apellido, email, password, Rol.DECANO, Facultad.INGENIERIA_SISTEMAS);
    }

    private List<Horario> crearHorarios(Dia dia1, String inicio1, String fin1, Dia dia2, String inicio2, String fin2) {
        List<Horario> horarios = new ArrayList<>();
        
        Horario horario1 = new Horario();
        horario1.setDia(dia1);
        horario1.setHoraInicio(LocalTime.parse(inicio1));
        horario1.setHoraFin(LocalTime.parse(fin1));
        
        Horario horario2 = new Horario();
        horario2.setDia(dia2);
        horario2.setHoraInicio(LocalTime.parse(inicio2));
        horario2.setHoraFin(LocalTime.parse(fin2));
        
        horarios.add(horario1);
        horarios.add(horario2);
        
        return horarios;
    }

    private Grupo crearGrupo(Materia materia, int capacidad, Profesor profesor, List<Horario> horarios) {
        Grupo grupo = new Grupo(materia, capacidad, horarios);
        grupo.setProfesor(profesor);
        return grupo;
    }

    private void crearSemestreParaEstudiante(Estudiante estudiante, List<Grupo> grupos) {
        Semestre semestre = new Semestre();
        
        for (Grupo grupo : grupos) {
            RegistroMaterias registro = new RegistroMaterias();
            registro.setGrupo(grupo);
            registro.setEstado(Semaforo.AZUL); 
            semestre.getRegistros().add(registro);
        }
        
        estudiante.getSemestres().add(semestre);
    }

    private void inscribirEstudianteEnGrupo(Grupo grupo, String estudianteId) {
        grupo.addEstudiante(estudianteId);
    }

    /**
     * Crea solicitudes de prueba para validar las funcionalidades del decano.
     */
    private void crearSolicitudesDePrueba(Estudiante estudiante1, Estudiante estudiante2, 
                                         Grupo grupoDOSW1, Grupo grupoDOSW2, 
                                         Grupo grupoODSC1, Grupo grupoODSC2,
                                         Materia desarrolloSoftware, Materia sistemasOperativos) {
        
        // Solicitud 1: Estudiante1 quiere cambiar de grupoDOSW1 a grupoDOSW2 (Desarrollo de Software)
        Solicitud solicitud1 = new Solicitud();
        solicitud1.setId("SOL001");
        solicitud1.setEstudianteId(estudiante1.getId());
        solicitud1.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitud1.setGrupoProblema(grupoDOSW1);
        solicitud1.setMateriaProblema(desarrolloSoftware);
        solicitud1.setGrupoDestino(grupoDOSW2);
        solicitud1.setMateriaDestino(desarrolloSoftware);
        solicitud1.setObservaciones("Solicito cambio de grupo debido a conflicto de horarios con trabajo de medio tiempo");
        solicitud1.setEstado(SolicitudEstado.PENDIENTE);
        solicitud1.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        solicitud1.setNumeroRadicado("RAD-2025-001");
        solicitud1.setPrioridad(1);
        solicitud1.setFechaCreacion(LocalDateTime.now());
        
        // Solicitud 2: Estudiante2 quiere cambiar de grupoODSC2 a grupoODSC1 (Sistemas Operativos)
        Solicitud solicitud2 = new Solicitud();
        solicitud2.setId("SOL002");
        solicitud2.setEstudianteId(estudiante2.getId());
        solicitud2.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitud2.setGrupoProblema(grupoODSC2);
        solicitud2.setMateriaProblema(sistemasOperativos);
        solicitud2.setGrupoDestino(grupoODSC1);
        solicitud2.setMateriaDestino(sistemasOperativos);
        solicitud2.setObservaciones("Solicito cambio de grupo para mejorar mi rendimiento académico en horario matutino");
        solicitud2.setEstado(SolicitudEstado.PENDIENTE);
        solicitud2.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        solicitud2.setNumeroRadicado("RAD-2025-002");
        solicitud2.setPrioridad(2);
        solicitud2.setFechaCreacion(LocalDateTime.now());
        
        // Guardar las solicitudes
        solicitudRepository.saveAll(Arrays.asList(solicitud1, solicitud2));
    }
}