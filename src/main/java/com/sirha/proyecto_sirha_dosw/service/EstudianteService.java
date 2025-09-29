package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.util.SolicitudUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Servicio que gestiona las operaciones académicas relacionadas con los estudiantes.
 * Permite consultar horarios, semáforos académicos, crear solicitudes de cambio,
 *          y recuperar solicitudes existentes.
 */
@Service
public class EstudianteService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;
    private final SolicitudUtil solicitudUtil;

    /**
     * Constructor con inyección de dependencias.
     * @param solicitudRepository repositorio de {@link Solicitud}
     * @param usuarioRepository repositorio de {@link Usuario}
     * @param grupoRepository repositorio de {@link Grupo}
     * @param materiaRepository repositorio de {@link Materia}
     * @param solicitudUtil utilidad para generar radicados y prioridades
     */
    public EstudianteService(SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository,
                             GrupoRepository grupoRepository, MateriaRepository materiaRepository,
                             SolicitudUtil solicitudUtil) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
        this.solicitudUtil = solicitudUtil;
    }

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * @param idEstudiante ID del estudiante.
     * @param semestre número de semestre a consultar.
     * @return lista de {@link RegistroMaterias} correspondientes al semestre.
     * @throws IllegalArgumentException si esl estudiante no existe o no tiene registros.
     */
    public List<RegistroMaterias> consultarHorarioBySemester(String idEstudiante, int semestre) throws SirhaException {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        try {
            Estudiante estudiante = estudianteOpt.get();
            List<RegistroMaterias> registroMaterias = estudiante.getRegistrosBySemestre(semestre);
            if (registroMaterias.isEmpty()) {
                throw new SirhaException(SirhaException.NO_HORARIO_ENCONTRADO);
            }
            return registroMaterias;
        }catch (Exception e) {
            throw new SirhaException(SirhaException.SEMESTRE_INVALIDO);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * El semáforo refleja el estado de las materias inscritas por el estudiante.
     * @param idEstudiante ID del estudiante.
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el {@link Semaforo}
     * @throws IllegalArgumentException si el estudiante no existe.
     */
    public Map<String, Semaforo> consultarSemaforoAcademico(String idEstudiante) throws SirhaException {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        Estudiante estudiante = estudianteOpt.get();
        return estudiante.getSemaforo();
    }

    /**
     * Crea una nueva solicitud académica (ej. cambio de grupo) y realiza validaciones.
     * @param solicitudDTO objeto con los datos de la solicitud.
     * @return la {@link Solicitud} creada y almacenada.
     * @throws IllegalArgumentException si alguna validación falla.
     */
    public Solicitud crearSolicitud(SolicitudDTO solicitudDTO) throws SirhaException {
        // 1. Verificar que el estudiante existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(solicitudDTO.getEstudianteId());
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        Estudiante estudiante = (Estudiante) usuarioOpt.get();
        
        // 2. Verificar que la materia no este cancelada en el semestre actual
        if (estudiante.tieneMateriaCandeladaEnSemestreActual(solicitudDTO.getMateriaProblemaAcronimo())) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                "No se puede crear solicitud para la materia " + solicitudDTO.getMateriaProblemaAcronimo() + 
                " porque ya fue cancelada en el semestre actual");
        }
        
        // 3. Verificar grupo y materia de origen
        Optional<Grupo> grupoProblemaOpt = grupoRepository.findById(solicitudDTO.getGrupoProblemaId());
        if (grupoProblemaOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }
        Grupo grupoProblema = grupoProblemaOpt.get();
        Optional<Materia> materiaProblemaOpt = materiaRepository.findByAcronimo(solicitudDTO.getMateriaProblemaAcronimo());
        if (materiaProblemaOpt.isEmpty()) {
            throw new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA);
        }
        Materia materiaProblema = materiaProblemaOpt.get();
        // 4. Verificar que la materia corresponde al grupo
        if (!grupoProblema.getMateria().getId().equals(materiaProblema.getId())) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "La materia problema no corresponde al grupo problema");
        }
        // 5. Verificar que el estudiante está inscrito en el grupo
        if (!grupoProblema.getEstudiantesId().contains(solicitudDTO.getEstudianteId())) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "El estudiante no está inscrito en el grupo problema");
        }
        // 6. Si es CAMBIO_GRUPO, verificar grupo y materia destino
        Grupo grupoDestino = null;
        Materia materiaDestino = null;

        if (solicitudDTO.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO) {
            if (solicitudDTO.getGrupoDestinoId() == null || solicitudDTO.getMateriaDestinoAcronimo() == null) {
                throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "Faltan datos para el grupo o materia destino");
            }
            Optional<Grupo> grupoDestinoOpt = grupoRepository.findById(solicitudDTO.getGrupoDestinoId());
            if (grupoDestinoOpt.isEmpty()) {
                throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
            }
            grupoDestino = grupoDestinoOpt.get();
            Optional<Materia> materiaDestinoOpt = materiaRepository.findByAcronimo(solicitudDTO.getMateriaDestinoAcronimo());
            if (materiaDestinoOpt.isEmpty()) {
                throw new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA);
            }
            materiaDestino = materiaDestinoOpt.get();

            // Verificar que la materia destino corresponde al grupo destino
            if (!grupoDestino.getMateria().getId().equals(materiaDestino.getId())) {
                throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "La materia destino no corresponde al grupo destino");
            }

            // Verificar que el grupo destino no esté lleno
            if (grupoDestino.isEstaCompleto()) {
                throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "El grupo destino está completo");
            }
        }

        // 7. Crear la solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setEstudianteId(estudiante.getId());
        solicitud.setTipoSolicitud(solicitudDTO.getTipoSolicitud());
        solicitud.setGrupoProblema(grupoProblema);
        solicitud.setMateriaProblema(materiaProblema);
        solicitud.setFacultad(estudiante.getCarrera());
        solicitud.setFechaCreacion(LocalDateTime.now());

        // 8. Generar número de radicado automático
        solicitud.setNumeroRadicado(solicitudUtil.generarNumeroRadicado());
        
        // 9. Asignar prioridad secuencial automática (orden de llegada)
        solicitud.setPrioridad(solicitudUtil.generarNumeroPrioridad());

        if (grupoDestino != null && materiaDestino != null) {
            solicitud.setGrupoDestino(grupoDestino);
            solicitud.setMateriaDestino(materiaDestino);
        }

        solicitud.setObservaciones(solicitudDTO.getObservaciones());

        return solicitudRepository.save(solicitud);
    }

    /**
     * Consulta todas las solicitudes hechas por un estudiante.
     * @param idEstudiante ID del estudiante.
     * @return lista de {@link Solicitud} asociadas al estudiante.
     * @throws IllegalArgumentException si el estudiante no existe o no es un estudiante.
     */
    public List<Solicitud> consultarSolicitudes(String idEstudiante) throws SirhaException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idEstudiante);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        return solicitudRepository.findByEstudianteId(idEstudiante);
    }

    /**
     * Consulta una solicitud específica de un estudiante.
     * @param idEstudiante ID del estudiante.
     * @param solicitudId ID de la solicitud.
     * @return la {@link Solicitud} encontrada.
     * @throws IllegalArgumentException si el estudiante no existe, si la solicitud no existe.
     */
    public Solicitud consultarSolicitudesById(String idEstudiante, String solicitudId) throws SirhaException{
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idEstudiante);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isEmpty()) {
            throw new SirhaException(SirhaException.SOLICITUD_NO_ENCONTRADA);
        }
        Solicitud solicitud = solicitudOpt.get();
        if (!solicitud.getEstudianteId().equals(idEstudiante)) {
            throw new SirhaException(SirhaException.SOLICITUD_NO_ENCONTRADA);
        }
        return solicitud;
    }

    /**
     * Cancela una materia específica del estudiante en su semestre actual.
     * @param idEstudiante ID del estudiante.
     * @param acronimoMateria Acrónimo de la materia a cancelar.
     * @return mensaje de confirmación de la cancelación.
     * @throws SirhaException si el estudiante no existe, si la materia no está inscrita o ya está cancelada.
     */
    public String cancelarMateria(String idEstudiante, String acronimoMateria) throws SirhaException {
        // 1. Verificar que el estudiante existe
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        Estudiante estudiante = estudianteOpt.get();

        // 2. Verificar que el estudiante tiene semestres
        if (estudiante.getSemestres().isEmpty()) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "El estudiante no tiene semestres registrados");
        }

        // 3. Obtener el semestre actual (último semestre)
        Semestre semestreActual = estudiante.getSemestres().get(estudiante.getSemestres().size() - 1);

        // 4. Buscar la materia en el semestre actual
        RegistroMaterias registroEncontrado = null;
        for (RegistroMaterias registro : semestreActual.getRegistros()) {
            if (registro.getGrupo().getMateria().getAcronimo().equals(acronimoMateria)) {
                registroEncontrado = registro;
                break;
            }
        }

        // 5. Verificar que se encontró la materia
        if (registroEncontrado == null) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                "El estudiante no está inscrito en la materia " + acronimoMateria + " en el semestre actual");
        }

        // 6. Verificar que la materia no esté ya cancelada
        if (registroEncontrado.getEstado() == Semaforo.CANCELADO) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                "La materia " + acronimoMateria + " ya está cancelada");
        }

        // 7. Verificar que la materia no esté ya aprobada
        if (registroEncontrado.getEstado() == Semaforo.VERDE) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                "No se puede cancelar la materia " + acronimoMateria + " porque ya está aprobada");
        }

        // 8. Cancelar la materia
        registroEncontrado.setEstado(Semaforo.CANCELADO);

        // 9. Guardar los cambios
        usuarioRepository.save(estudiante);

        return "La materia " + acronimoMateria + " ha sido cancelada exitosamente";
    }

    /**
     * Consulta todas las solicitudes que se encuentran en un estado específico.
     * Este método es útil para que los estudiantes puedan filtrar sus consultas
     * o para que los administradores gestionen las solicitudes por estado.
     * 
     * @param estado el estado de las solicitudes a consultar (PENDIENTE, EN_REVISION, APROBADA, RECHAZADA)
     * @return lista de {@link Solicitud} en el estado indicado
     * @throws SirhaException si el estado no es válido
     */
    public List<Solicitud> consultarSolicitudesPorEstado(SolicitudEstado estado) throws SirhaException {
        if (estado == null) {
            throw new SirhaException("El estado de la solicitud no puede ser nulo");
        }
        return solicitudRepository.findByEstado(estado);
    }

    /**
     * Consulta todas las solicitudes existentes en el sistema.
     * Este método es útil para obtener un panorama general de todas las solicitudes.
     * 
     * @return lista completa de {@link Solicitud}
     */
    public List<Solicitud> consultarTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }

    /**
     * Consulta las solicitudes de un estudiante específico filtradas por estado.
     * 
     * @param idEstudiante ID del estudiante
     * @param estado estado de las solicitudes a consultar
     * @return lista de {@link Solicitud} del estudiante en el estado indicado
     * @throws SirhaException si el estudiante no existe o el estado es inválido
     */
    public List<Solicitud> consultarSolicitudesEstudiantePorEstado(String idEstudiante, SolicitudEstado estado) throws SirhaException {
        // Verificar que el estudiante existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idEstudiante);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        
        if (estado == null) {
            throw new SirhaException("El estado de la solicitud no puede ser nulo");
        }

        // Obtener todas las solicitudes del estudiante y filtrar por estado
        return solicitudRepository.findByEstudianteId(idEstudiante)
                .stream()
                .filter(solicitud -> solicitud.getEstado() == estado)
                .toList();
    }
}
