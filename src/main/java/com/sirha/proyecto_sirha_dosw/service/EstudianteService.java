package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.util.EstudianteValidationUtil;
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
    
    private static final String MATERIA_CANCELADA_MSG = "El estudiante no tiene semestres registrados";
    private static final String MATERIA_NO_INSCRITA_MSG = "El estudiante no está inscrito en la materia %s en el semestre actual";
    private static final String MATERIA_YA_CANCELADA_MSG = "La materia %s ya está cancelada";
    private static final String MATERIA_APROBADA_MSG = "No se puede cancelar la materia %s porque ya está aprobada";
    private static final String MATERIA_CANCELADA_EXITOSAMENTE_MSG = "La materia %s ha sido cancelada exitosamente";
    private static final String ESTADO_NULO_MSG = "El estado de la solicitud no puede ser nulo";
    
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

    // ========== MÉTODOS PRIVADOS DE VALIDACIÓN ==========

    /**
     * Valida que un grupo existe en el sistema.
     * @param grupoId ID del grupo a validar
     * @return el Grupo encontrado
     * @throws SirhaException si el grupo no existe
     */
    private Grupo validarGrupo(String grupoId) throws SirhaException {
        return grupoRepository.findById(grupoId)
                .orElseThrow(() -> new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO));
    }

    /**
     * Valida que una materia existe en el sistema.
     * @param materiaAcronimo acrónimo de la materia a validar
     * @return la Materia encontrada
     * @throws SirhaException si la materia no existe
     */
    private Materia validarMateria(String materiaAcronimo) throws SirhaException {
        return materiaRepository.findByAcronimo(materiaAcronimo)
                .orElseThrow(() -> new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA));
    }

    /**
     * Valida que un grupo corresponde a una materia específica.
     * @param grupo grupo a validar
     * @param materia materia esperada
     * @param errorMsg mensaje de error personalizado
     * @throws SirhaException si la materia del grupo no coincide
     */
    private void validarGrupoMateriaCorrespondencia(Grupo grupo, Materia materia, String errorMsg) throws SirhaException {
        if (!grupo.getMateria().getId().equals(materia.getId())) {
            throw new SirhaException(errorMsg);
        }
    }

    /**
     * Valida que un estudiante está inscrito en un grupo.
     * @param grupo grupo a validar
     * @param estudianteId ID del estudiante
     * @param errorMsg mensaje de error personalizado
     * @throws SirhaException si el estudiante no está en el grupo
     */
    private void validarEstudianteEnGrupo(Grupo grupo, String estudianteId, String errorMsg) throws SirhaException {
        if (!grupo.getEstudiantesId().contains(estudianteId)) {
            throw new SirhaException(errorMsg);
        }
    }

    /**
     * Valida que el grupo destino es válido para un cambio de grupo.
     * @param grupoDestino grupo destino a validar
     * @param materiaDestino materia destino esperada
     * @throws SirhaException si el grupo no es válido o está completo
     */
    private void validarGrupoDestino(Grupo grupoDestino, Materia materiaDestino) throws SirhaException {
        validarGrupoMateriaCorrespondencia(grupoDestino, materiaDestino, 
            SirhaException.ERROR_CREACION_SOLICITUD + "La materia destino no corresponde al grupo destino");
        if (grupoDestino.isEstaCompleto()) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "El grupo destino está completo");
        }
    }

    /**
     * Busca un registro de materia en el semestre actual por su acrónimo.
     * @param semestreActual semestre donde buscar
     * @param acronimoMateria acrónimo de la materia
     * @return el registro encontrado o null
     */
    private RegistroMaterias buscarRegistroMateria(Semestre semestreActual, String acronimoMateria) {
        return semestreActual.getRegistros().stream()
                .filter(registro -> registro.getGrupo().getMateria().getAcronimo().equals(acronimoMateria))
                .findFirst()
                .orElse(null);
    }

    /**
     * Valida que un registro de materia puede ser cancelado.
     * @param registro registro a validar
     * @param acronimoMateria acrónimo de la materia
     * @throws SirhaException si la materia no puede ser cancelada
     */
    private void validarCancelacionMateria(RegistroMaterias registro, String acronimoMateria) throws SirhaException {
        if (registro.getEstado() == Semaforo.CANCELADO) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                String.format(MATERIA_YA_CANCELADA_MSG, acronimoMateria));
        }

        if (registro.getEstado() == Semaforo.VERDE) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                String.format(MATERIA_APROBADA_MSG, acronimoMateria));
        }
    }

    // ========== MÉTODOS PÚBLICOS ==========

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * @param idEstudiante ID del estudiante.
     * @param semestre número de semestre a consultar.
     * @return lista de {@link RegistroMaterias} correspondientes al semestre.
     * @throws IllegalArgumentException si esl estudiante no existe o no tiene registros.
     */
    public List<RegistroMaterias> consultarHorarioBySemester(String idEstudiante, int semestre) throws SirhaException {
        Estudiante estudiante = EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);
        return EstudianteValidationUtil.obtenerRegistrosPorSemestre(estudiante, semestre);
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * El semáforo refleja el estado de las materias inscritas por el estudiante.
     * @param idEstudiante ID del estudiante.
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el {@link Semaforo}
     * @throws IllegalArgumentException si el estudiante no existe.
     */
    public Map<String, Semaforo> consultarSemaforoAcademico(String idEstudiante) throws SirhaException {
        Estudiante estudiante = EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);
        return EstudianteValidationUtil.obtenerSemaforo(estudiante);
    }

    /**
     * Crea una nueva solicitud académica (ej. cambio de grupo) y realiza validaciones.
     * @param solicitudDTO objeto con los datos de la solicitud.
     * @return la {@link Solicitud} creada y almacenada.
     * @throws IllegalArgumentException si alguna validación falla.
     */
    public Solicitud crearSolicitud(SolicitudDTO solicitudDTO) throws SirhaException {
        Estudiante estudiante = EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, solicitudDTO.getEstudianteId());
        if (estudiante.tieneMateriaCandeladaEnSemestreActual(solicitudDTO.getMateriaProblemaAcronimo())) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD +
                    "No se puede crear solicitud para la materia " + solicitudDTO.getMateriaProblemaAcronimo() +
                    " porque ya fue cancelada en el semestre actual");
        }

        Grupo grupoProblema = validarGrupo(solicitudDTO.getGrupoProblemaId());
        Materia materiaProblema = validarMateria(solicitudDTO.getMateriaProblemaAcronimo());
        validarGrupoMateriaCorrespondencia(grupoProblema, materiaProblema, SirhaException.ERROR_CREACION_SOLICITUD + "La materia problema no corresponde al grupo problema");
        validarEstudianteEnGrupo(grupoProblema, solicitudDTO.getEstudianteId(), SirhaException.ERROR_CREACION_SOLICITUD + "El estudiante no está inscrito en el grupo problema");

        Grupo grupoDestino = null;
        Materia materiaDestino = null;
        if (solicitudDTO.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO) {
            if (solicitudDTO.getGrupoDestinoId() == null || solicitudDTO.getMateriaDestinoAcronimo() == null) {
                throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + "Faltan datos para el grupo o materia destino");
            }
            grupoDestino = validarGrupo(solicitudDTO.getGrupoDestinoId());
            materiaDestino = validarMateria(solicitudDTO.getMateriaDestinoAcronimo());
            validarGrupoDestino(grupoDestino, materiaDestino);
        }

        Solicitud solicitud = crearSolicitudBase(estudiante, solicitudDTO, grupoProblema, materiaProblema);
        if (grupoDestino != null) {
            solicitud.setGrupoDestino(grupoDestino);
            solicitud.setMateriaDestino(materiaDestino);
        }
        solicitud.setObservaciones(solicitudDTO.getObservaciones());
        return solicitudRepository.save(solicitud);
    }

    private Solicitud crearSolicitudBase(Estudiante estudiante, SolicitudDTO solicitudDTO, Grupo grupoProblema, Materia materiaProblema) {
        Solicitud solicitud = new Solicitud();
        solicitud.setEstudianteId(estudiante.getId());
        solicitud.setTipoSolicitud(solicitudDTO.getTipoSolicitud());
        solicitud.setGrupoProblema(grupoProblema);
        solicitud.setMateriaProblema(materiaProblema);
        solicitud.setFacultad(materiaProblema.getFacultad());
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setEstado(SolicitudEstado.PENDIENTE);
        solicitud.setNumeroRadicado(solicitudUtil.generarNumeroRadicado());
        solicitud.setPrioridad(solicitudUtil.generarNumeroPrioridad());
        return solicitud;
    }

    /**
     * Consulta todas las solicitudes hechas por un estudiante.
     * @param idEstudiante ID del estudiante.
     * @return lista de {@link Solicitud} asociadas al estudiante.
     * @throws IllegalArgumentException si el estudiante no existe o no es un estudiante.
     */
    public List<Solicitud> consultarSolicitudes(String idEstudiante) throws SirhaException {
        EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);
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
        EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);
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
        Estudiante estudiante = EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);

        if (estudiante.getSemestres().isEmpty()) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + MATERIA_CANCELADA_MSG);
        }

        Semestre semestreActual = estudiante.getSemestres().get(estudiante.getSemestres().size() - 1);
        RegistroMaterias registroEncontrado = buscarRegistroMateria(semestreActual, acronimoMateria);

        if (registroEncontrado == null) {
            throw new SirhaException(SirhaException.ERROR_CREACION_SOLICITUD + 
                String.format(MATERIA_NO_INSCRITA_MSG, acronimoMateria));
        }

        validarCancelacionMateria(registroEncontrado, acronimoMateria);

        registroEncontrado.setEstado(Semaforo.CANCELADO);
        usuarioRepository.save(estudiante);

        return String.format(MATERIA_CANCELADA_EXITOSAMENTE_MSG, acronimoMateria);
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
            throw new SirhaException(ESTADO_NULO_MSG);
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
        EstudianteValidationUtil.obtenerEstudiante(usuarioRepository, idEstudiante);

        if (estado == null) {
            throw new SirhaException(ESTADO_NULO_MSG);
        }

        return solicitudRepository.findByEstudianteId(idEstudiante)
                .stream()
                .filter(solicitud -> solicitud.getEstado() == estado)
                .toList();
    }
}
