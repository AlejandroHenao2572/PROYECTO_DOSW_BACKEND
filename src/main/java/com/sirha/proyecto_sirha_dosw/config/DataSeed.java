
package com.sirha.proyecto_sirha_dosw.config;

import com.sirha.proyecto_sirha_dosw.model.*;
// removed unused imports: UsuarioDTO, AuthService (we'll create admin directly)
import com.sirha.proyecto_sirha_dosw.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(DataSeed.class);
    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final CarreraRepository carreraRepository;
    private final SolicitudRepository solicitudRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private static final String HORA_DIEZ = "10:00";
    private static final String HORA_CATORCE = "14:00";
    private static final String HORA_DIECISIES = "16:00";

    public DataSeed(MateriaRepository materiaRepository,
                    UsuarioRepository usuarioRepository,
                    GrupoRepository grupoRepository,
                    CarreraRepository carreraRepository,
                    SolicitudRepository solicitudRepository,
                    org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.carreraRepository = carreraRepository;
        this.solicitudRepository = solicitudRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Limpiar datos existentes para pruebas limpias
        limpiarDatos();
        
        // Crear datos de prueba
        crearDatosDePrueba();
        
        logger.info("DataSeed ejecutado exitosamente - Datos de prueba creados");
    }

    private void limpiarDatos() {
        solicitudRepository.deleteAll();
        grupoRepository.deleteAll();
        materiaRepository.deleteAll();
        usuarioRepository.deleteAll();
        carreraRepository.deleteAll();
        logger.info("Datos existentes eliminados");
    }

    private void crearDatosDePrueba() {
        // 1. Crear carreras para todas las facultades disponibles
        Carrera ingenieriaSistemas = crearCarreraConParametros(Facultad.INGENIERIA_SISTEMAS, "ISIS", 8, 145);
        Carrera ingenieriaCivil = crearCarreraConParametros(Facultad.INGENIERIA_CIVIL, "ICIV", 10, 150);
        Carrera administracion = crearCarreraConParametros(Facultad.ADMINISTRACION, "ADMI", 8, 120);
        
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
            crearHorarios(Dia.LUNES, "08:00", HORA_DIEZ, Dia.MIERCOLES, "08:00", HORA_DIEZ));
        grupoDOSW1.setId("01");

        Grupo grupoDOSW2 = crearGrupo(desarrolloSoftware, 30, profesorPablo,
            crearHorarios(Dia.MARTES, HORA_CATORCE, HORA_DIECISIES, Dia.JUEVES, HORA_CATORCE, HORA_DIECISIES));
        grupoDOSW2.setId("02");

        Grupo grupoODSC1 = crearGrupo(sistemasOperativos, 28, profesorGerardo,
            crearHorarios(Dia.LUNES, HORA_DIEZ, "12:00", Dia.MIERCOLES, HORA_DIEZ, "12:00"));
        grupoODSC1.setId("03");

        Grupo grupoODSC2 = crearGrupo(sistemasOperativos, 25, profesorGerardo,
            crearHorarios(Dia.MARTES, HORA_CATORCE, HORA_DIECISIES, Dia.JUEVES, HORA_CATORCE, HORA_DIECISIES));
        grupoODSC2.setId("04");

        // 4. Crear estudiantes
        Estudiante estudiante1 = crearEstudiante("David", "Patacon", "david.patacon@mail.escuelaing.edu.co", "student123");
        estudiante1.setId("1000100406");
        Estudiante estudiante2 = crearEstudiante("Jared", "Farfan", "jared.farfan@mail.escuelaing.edu.co", "student123");
        estudiante2.setId("1000100631");

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

        // 7.b Crear administrador con correo fijo y contraseña conocida (dev only)
        Administrador admin = crearAdministrador("Admin", "Root", "admin@escuelaing.edu.co", "Admin123!");
        admin.setId("ADMIN001");

        // 8. Guardar todos los cambios
        materiaRepository.saveAll(Arrays.asList(desarrolloSoftware, sistemasOperativos));

        // Encriptar contraseñas antes de guardar usuarios (para que autenticar funcione)
        List<Usuario> usuariosARegistrar = Arrays.asList(profesorPablo, profesorGerardo, estudiante1, estudiante2, decano, admin);
        for (Usuario u : usuariosARegistrar) {
            if (u.getPassword() != null) {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
            }
        }
        usuarioRepository.saveAll(usuariosARegistrar);
        
        grupoRepository.saveAll(Arrays.asList(grupoDOSW1, grupoDOSW2, grupoODSC1, grupoODSC2));


    }

    private Carrera crearCarreraConParametros(Facultad facultad, String acronimo, int duracionSemestres, int creditos) {
        Carrera carrera = new Carrera(
            facultad, 
            acronimo, 
            duracionSemestres, 
            creditos
        );
        return carreraRepository.save(carrera);
    }

    private Materia crearMateria(String nombre, String acronimo, int creditos) {
        return new Materia(nombre, acronimo, creditos, Facultad.INGENIERIA_SISTEMAS);
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

    private Administrador crearAdministrador(String nombre, String apellido, String email, String password) {
        return new Administrador(nombre, apellido, email, password, Rol.ADMINISTRADOR);
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

}

