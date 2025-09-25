package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Constructor con inyección de dependencias.
     * @param solicitudRepository repositorio de {@link Solicitud}
     * @param usuarioRepository repositorio de {@link Usuario}
     * @param grupoRepository repositorio de {@link Grupo}
     * @param materiaRepository repositorio de {@link Materia}
     */
    @Autowired
    public EstudianteService(SolicitudRepository solicitudRepository, UsuarioRepository usuarioRepository,
                             GrupoRepository grupoRepository, MateriaRepository materiaRepository) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
    }

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * @param idEstudiante ID del estudiante.
     * @param semestre número de semestre a consultar.
     * @return lista de {@link RegistroMaterias} correspondientes al semestre.
     * @throws IllegalArgumentException si esl estudiante no existe o no tiene registros.
     */
    public List<RegistroMaterias> consultarHorarioBySemester(String idEstudiante, int semestre) {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no existe");
        }
        try {
            Estudiante estudiante = estudianteOpt.get();
            List<RegistroMaterias> registroMaterias = estudiante.getRegistrosBySemestre(semestre);
            if (registroMaterias.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron registros para el semestre: " + semestre);
            }
            return registroMaterias;
        }catch (Exception e) {
            throw new IllegalArgumentException("Semestre no válido " + semestre);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * El semáforo refleja el estado de las materias inscritas por el estudiante.
     * @param idEstudiante ID del estudiante.
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el {@link Semaforo}
     * @throws IllegalArgumentException si el estudiante no existe.
     */
    public Map<String, Semaforo> consultarSemaforoAcademico(String idEstudiante) {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no existe");
        }
        Estudiante estudiante = estudianteOpt.get();
        return estudiante.getSemaforo();
    }

    /**
     * Crea una nueva solicitud académica (ej. cambio de grupo) y realiza unas validaciones.
     * @param solicitudDTO objeto con los datos de la solicitud.
     * @return la {@link Solicitud} creada y almacenada.
     * @throws IllegalArgumentException si alguna validación falla.
     */
    public Solicitud crearSolicitud(SolicitudDTO solicitudDTO) {
        // 1. Verificar que el estudiante existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(solicitudDTO.getEstudianteId());
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new IllegalArgumentException("El estudiante con ID " + solicitudDTO.getEstudianteId() + " no existe o no es un estudiante");
        }
        Estudiante estudiante = (Estudiante) usuarioOpt.get();

        // 2. Verificar grupo y materia de origen
        Optional<Grupo> grupoProblemaOpt = grupoRepository.findById(solicitudDTO.getGrupoProblemaId());
        if (grupoProblemaOpt.isEmpty()) {
            throw new IllegalArgumentException("El grupo con ID " + solicitudDTO.getGrupoProblemaId() + " no existe");
        }
        Grupo grupoProblema = grupoProblemaOpt.get();

        Optional<Materia> materiaProblemaOpt = materiaRepository.findByAcronimo(solicitudDTO.getMateriaProblemaAcronimo());
        if (materiaProblemaOpt.isEmpty()) {
            throw new IllegalArgumentException("La materia con acrónimo " + solicitudDTO.getMateriaProblemaAcronimo() + " no existe");
        }
        Materia materiaProblema = materiaProblemaOpt.get();

        // 3. Verificar que la materia corresponde al grupo
        if (!grupoProblema.getMateria().getId().equals(materiaProblema.getId())) {
            throw new IllegalArgumentException("La materia indicada no corresponde al grupo con problemas");
        }

        // 4. Verificar que el estudiante está inscrito en el grupo
        if (!grupoProblema.getEstudiantesId().contains(solicitudDTO.getEstudianteId())) {
            throw new IllegalArgumentException("El estudiante no está inscrito en el grupo indicado");
        }

        // 5. Si es CAMBIO_GRUPO, verificar grupo y materia destino
        Grupo grupoDestino = null;
        Materia materiaDestino = null;

        if (solicitudDTO.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO) {
            if (solicitudDTO.getGrupoDestinoId() == null || solicitudDTO.getMateriaDestinoAcronimo() == null) {
                throw new IllegalArgumentException("Para cambio de grupo se requiere especificar el grupo y materia destino");
            }

            Optional<Grupo> grupoDestinoOpt = grupoRepository.findById(solicitudDTO.getGrupoDestinoId());
            if (grupoDestinoOpt.isEmpty()) {
                throw new IllegalArgumentException("El grupo destino con ID " + solicitudDTO.getGrupoDestinoId() + " no existe");
            }
            grupoDestino = grupoDestinoOpt.get();

            Optional<Materia> materiaDestinoOpt = materiaRepository.findByAcronimo(solicitudDTO.getMateriaDestinoAcronimo());
            if (materiaDestinoOpt.isEmpty()) {
                throw new IllegalArgumentException("La materia destino con acrónimo " + solicitudDTO.getMateriaDestinoAcronimo() + " no existe");
            }
            materiaDestino = materiaDestinoOpt.get();

            // Verificar que la materia destino corresponde al grupo destino
            if (!grupoDestino.getMateria().getId().equals(materiaDestino.getId())) {
                throw new IllegalArgumentException("La materia destino no corresponde al grupo destino");
            }

            // Verificar que el grupo destino no esté lleno
            if (grupoDestino.isEstaCompleto()) {
                throw new IllegalArgumentException("El grupo destino está completo");
            }
        }

        // 6. Crear la solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setEstudianteId(estudiante.getId());
        solicitud.setTipoSolicitud(solicitudDTO.getTipoSolicitud());
        solicitud.setGrupoProblema(grupoProblema);
        solicitud.setMateriaProblema(materiaProblema);

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
    public List<Solicitud> consultarSolicitudes(String idEstudiante) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idEstudiante);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no existe o no es un estudiante");
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
    public Solicitud consultarSolicitudesById(String idEstudiante, String solicitudId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idEstudiante);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Estudiante)) {
            throw new IllegalArgumentException("El estudiante con ID " + idEstudiante + " no existe o no es un estudiante");
        }
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe");
        }
        Solicitud solicitud = solicitudOpt.get();
        if (!solicitud.getEstudianteId().equals(idEstudiante)) {
            throw new IllegalArgumentException("La solicitud no pertenece al estudiante con ID " + idEstudiante);
        }
        return solicitud;
    }
}
