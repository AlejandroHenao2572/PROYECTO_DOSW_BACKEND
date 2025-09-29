package com.sirha.proyecto_sirha_dosw.service;


import com.sirha.proyecto_sirha_dosw.dto.CalendarioAcademicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.DisponibilidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.MonitoreoGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.PlazoSolicitudesDTO;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DecanoService {

    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;
    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;

    public DecanoService(UsuarioRepository usuarioRepository, SolicitudRepository solicitudRepository, 
                        GrupoRepository grupoRepository, MateriaRepository materiaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
    }

    public List<Usuario> findEstudiantesByFacultad(String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByFacultadAndRol(facultadEnum, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByIdAndFacultad(String id, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByIdAndFacultadAndRol(id, facultadEnum, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByEmailAndFacultad(String email, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByEmailAndFacultadAndRol(email, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreAndFacultad(String nombre, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByNombreAndFacultadAndRol(nombre, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByApellidoAndFacultad(String apellido, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByApellidoAndFacultadAndRol(apellido, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreApellidoAndFacultad(String nombre, String apellido, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByNombreAndApellidoAndFacultadAndRol(nombre, apellido, facultadEnum, Rol.ESTUDIANTE);
    }

    /**
     * Consulta todas las solicitudes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @return lista de solicitudes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultad(facultad);
    }

    /**
     * Consulta las solicitudes pendientes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes pendientes
     * @return lista de solicitudes pendientes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPendientesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultadAndEstado(facultad, SolicitudEstado.PENDIENTE);
    }

    /**
     * Consulta las solicitudes por facultad filtradas por estado específico.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @param estado el estado específico de las solicitudes a consultar
     * @return lista de solicitudes filtradas por facultad y estado
     */
    public List<Solicitud> consultarSolicitudesPorFacultadYEstado(Facultad facultad, SolicitudEstado estado) {
        return solicitudRepository.findByFacultadAndEstado(facultad, estado);
    }

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * Permite al decano visualizar el horario del estudiante que solicita cambio.
     * @param idEstudiante ID del estudiante
     * @param semestre número de semestre a consultar
     * @return lista de RegistroMaterias correspondientes al semestre
     * @throws SirhaException si el estudiante no existe o no tiene registros
     */
    public List<RegistroMaterias> consultarHorarioEstudiante(String idEstudiante, int semestre) throws SirhaException {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        
        System.out.println(estudianteOpt.isEmpty());
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
        } catch (Exception e) {
            throw new SirhaException(SirhaException.SEMESTRE_INVALIDO);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * Permite al decano consultar el rendimiento académico del estudiante.
     * @param idEstudiante ID del estudiante
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el Semaforo
     * @throws SirhaException si el estudiante no existe
     */
    public Map<String, Semaforo> consultarSemaforoAcademicoEstudiante(String idEstudiante) throws SirhaException {
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
     * Valida que la facultad proporcionada sea válida.
     * @param facultad nombre de la facultad a validar
     * @throws SirhaException si la facultad no es válida
     */
    public void validarFacultad(String facultad) throws SirhaException {
        if (facultad == null || facultad.trim().isEmpty()) {
            throw new SirhaException(SirhaException.FACULTAD_INVALIDA);
        }
        
        // Validar que la facultad exista en el enum Facultad
        try {
            Facultad.valueOf(facultad.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SirhaException(SirhaException.FACULTAD_INVALIDA + facultad);
        }
    }

    /**
     * Valida que el estudiante exista y pertenezca a la facultad especificada.
     * @param idEstudiante ID del estudiante
     * @param facultad facultad a la que debe pertenecer el estudiante
     * @throws SirhaException si el estudiante no existe o no pertenece a la facultad
     */
    public void validarEstudianteFacultad(String idEstudiante, String facultad) throws SirhaException {
        if (idEstudiante == null || idEstudiante.trim().isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + "ID vacío");
        }
        
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante);
        }
        Estudiante estudiante = estudianteOpt.get();
        Facultad facultadEnum = estudiante.getCarrera();
        if (!facultadEnum.name().equalsIgnoreCase(facultad)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante + " en la facultad " + facultad);
        }
    }

    /**
     * Valida que el semestre esté en el rango válido (1-10).
     * @param semestre número de semestre
     * @throws SirhaException si el semestre está fuera del rango válido
     */
    public void validarSemestre(int semestre) throws SirhaException {
        if (semestre < 1 || semestre > 10) {
            throw new SirhaException(SirhaException.SEMESTRE_FUERA_RANGO + " Recibido: " + semestre);
        }
    }

    /**
     * Obtiene la información básica de un estudiante (código, nombre, carrera, semestre).
     * @param idEstudiante ID del estudiante
     * @param facultad facultad del decano
     * @return EstudianteBasicoDTO con la información básica del estudiante
     * @throws SirhaException si ocurre algún error
     */
    public EstudianteBasicoDTO obtenerInformacionBasicaEstudiante(String idEstudiante, String facultad) throws SirhaException {
        // Validar que el estudiante existe y pertenece a la facultad
        validarEstudianteFacultad(idEstudiante, facultad);
        
        // Obtener el estudiante
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante);
        }
        
        Estudiante estudiante = estudianteOpt.get();
        
        // Calcular el semestre actual (número de semestres cursados)
        int semestreActual = estudiante.getSemestres().size();
        
        // Crear y retornar el DTO con la información básica
        return new EstudianteBasicoDTO(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getCarrera(),
                semestreActual
        );
    }

    /**
     * Consulta la disponibilidad de grupos para una materia específica.
     * Muestra capacidad, cupos disponibles y lista de espera.
     * @param materiaId ID de la materia
     * @param facultad facultad del decano
     * @return lista de DisponibilidadGrupoDTO con información de los grupos
     * @throws SirhaException si la materia no existe o no pertenece a la facultad
     */
    public List<DisponibilidadGrupoDTO> consultarDisponibilidadGrupos(String materiaAcronimo, String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        // Buscar todos los grupos de la materia
        Optional<Materia> materiaOpt = materiaRepository.findByAcronimo(materiaAcronimo);
        if (materiaOpt.isEmpty()) {
            throw new SirhaException("Materia no encontrada: " + materiaAcronimo);
        }
        List<Grupo> grupos = grupoRepository.findByMateria_Id(materiaOpt.get().getId());

        if (grupos.isEmpty()) {
            throw new SirhaException("No se encontraron grupos para la materia: " + materiaAcronimo);
        }
        
        List<DisponibilidadGrupoDTO> disponibilidades = new ArrayList<>();
        
        for (Grupo grupo : grupos) {
            // Validar que la materia pertenece a la facultad del decano
            validarMateriaPerteneceFacultad(grupo.getMateria(), facultad);
            
            DisponibilidadGrupoDTO disponibilidad = new DisponibilidadGrupoDTO();
            disponibilidad.setGrupoId(grupo.getId());
            disponibilidad.setNombreMateria(grupo.getMateria().getNombre());
            disponibilidad.setAcronimoMateria(grupo.getMateria().getAcronimo());
            disponibilidad.setCapacidadMaxima(grupo.getCapacidad());
            disponibilidad.setCantidadInscritos(grupo.getCantidadInscritos());
            disponibilidad.setEstaCompleto(grupo.isEstaCompleto());
            disponibilidad.setHorarios(grupo.getHorarios());
            disponibilidad.setListaEspera(new ArrayList<>()); // Por ahora lista vacía
            disponibilidades.add(disponibilidad);
        }
        
        return disponibilidades;
    }

    /**
     * Responde a una solicitud de cambio de grupo.
     * @param respuesta DTO con la respuesta (aprobar, rechazar, en_revision)
     * @param facultad facultad del decano
     * @throws SirhaException si ocurre algún error en la validación o procesamiento
     */
    public void responderSolicitud(RespuestaSolicitudDTO respuesta, String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        // Buscar la solicitud
        Optional<Solicitud> solicitudOpt = solicitudRepository.findById(respuesta.getSolicitudId());
        if (solicitudOpt.isEmpty()) {
            throw new SirhaException("Solicitud no encontrada: " + respuesta.getSolicitudId());
        }
        
        Solicitud solicitud = solicitudOpt.get();
        validarMateriaPerteneceFacultad(solicitud.getGrupoProblema().getMateria(), facultad);
        
        // Validar que la solicitud pertenece a la facultad del decano
        if (!solicitud.getFacultad().name().equals(facultad.toUpperCase())) {
            throw new SirhaException("La solicitud no pertenece a la facultad: " + facultad);
        }
        
        // Validar que la solicitud está pendiente
        if (solicitud.getEstado() != SolicitudEstado.PENDIENTE) {
            throw new SirhaException("Solo se pueden responder solicitudes pendientes");
        }
        
        // Validar restricciones según el tipo de respuesta
        if (respuesta.getNuevoEstado() == SolicitudEstado.APROBADA) {
            validarAprobacionSolicitud(solicitud);
        }

        else if (respuesta.getNuevoEstado() == SolicitudEstado.RECHAZADA) {
            RechazoSolicitud(solicitud);
        }

        else if (respuesta.getNuevoEstado() == SolicitudEstado.EN_REVISION) {
            SolicitudEnRevision(solicitud);
        }
        else {
            throw new SirhaException("Estado de respuesta inválido: " + respuesta.getNuevoEstado());
        }
        
        
        // Actualizar la solicitud
        solicitud.setEstado(respuesta.getNuevoEstado());
        solicitud.setRespuesta(respuesta.getObservacionesRespuesta());
        solicitud.setFechaResolucion(LocalDateTime.now());
        
        // Guardar los cambios
        solicitudRepository.save(solicitud);
        
        // Si se aprueba, actualizar los grupos
        if (respuesta.getNuevoEstado() == SolicitudEstado.APROBADA) {
            procesarAprobacionSolicitud(solicitud);
        }
    }

    /**
     * Valida si una solicitud puede ser aprobada según las restricciones de negocio.
     * @param solicitud solicitud a validar
     * @throws SirhaException si la solicitud no puede ser aprobada
     */
    private void validarAprobacionSolicitud(Solicitud solicitud) throws SirhaException {
        // Validar que estamos dentro del calendario académico
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        LocalDate fechaActual = LocalDate.now();
        
        if (fechaActual.isBefore(calendario.getFechaInicio()) || fechaActual.isAfter(calendario.getFechaFin())) {
            throw new SirhaException("No se pueden aprobar solicitudes fuera del calendario académico");
        }
        
        // Validar que el grupo destino tiene cupos disponibles
        if (solicitud.getGrupoDestino() != null) {
            Grupo grupoDestino = solicitud.getGrupoDestino();
            if (grupoDestino.isEstaCompleto()) {
                throw new SirhaException("El grupo destino ya está lleno");
            }
        }
        
        // Validar plazo de respuesta (5 dias habiles)
        LocalDateTime fechaLimite = solicitud.getFechaCreacion().plusDays(5);
        if (LocalDateTime.now().isAfter(fechaLimite)) {
            throw new SirhaException("El plazo para responder la solicitud ha vencido");
        }

        // Validar que no haya cruce de horarios con el grupo destino
        Grupo grupoDestino = solicitud.getGrupoDestino();
        if (grupoDestino != null) {
            Estudiante estudiante = usuarioRepository.findById(solicitud.getEstudianteId())
                        .filter(Estudiante.class::isInstance)
                        .map(Estudiante.class::cast)
                        .orElseThrow(() -> new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + solicitud.getEstudianteId()));
            
            // Obtener los grupos del estudiante excluyendo el grupo problema
            List<Grupo> gruposActuales = estudiante.getGruposExcluyendo(solicitud.getGrupoProblema());
            
            // Verificar cruces de horario con cada grupo actual
            for (Grupo grupoActual : gruposActuales) {
                if (grupoDestino.tieneCruceDeHorario(grupoActual)) {
                    throw new SirhaException("El grupo destino tiene cruce de horarios con el grupo de la materia: " 
                        + grupoActual.getMateria().getNombre());
                }
            }
        }
    }

    /**
     * Procesa la aprobación de una solicitud actualizando los grupos correspondientes.
     * @param solicitud solicitud aprobada
     */
    private void procesarAprobacionSolicitud(Solicitud solicitud) {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(solicitud.getEstudianteId())
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        Estudiante estudiante = estudianteOpt.get();

        // Remover estudiante del grupo problema si existe
        if (solicitud.getGrupoProblema() != null) {
            Grupo grupoProblema = solicitud.getGrupoProblema();
            grupoProblema.removeEstudiante(solicitud.getEstudianteId());
            grupoRepository.save(grupoProblema);    
            estudiante.removeGrupo(grupoProblema);
        }
        
        // Agregar estudiante al grupo destino si existe
        if (solicitud.getGrupoDestino() != null) {
            Grupo grupoDestino = solicitud.getGrupoDestino();
            grupoDestino.addEstudiante(solicitud.getEstudianteId());
            grupoRepository.save(grupoDestino);
            estudiante.addGrupo(grupoDestino);
        }


        solicitud.setEstado(SolicitudEstado.APROBADA);
        solicitud.setRespuesta("Solicitud aprobada y procesada con éxito.");
        solicitudRepository.save(solicitud);
        usuarioRepository.save(estudiante);
    }

    /**
     * Procesa el rechazo de una solicitud.
     * @param solicitud solicitud rechazada
     */ 
    private void RechazoSolicitud(Solicitud solicitud) {
        solicitud.setEstado(SolicitudEstado.RECHAZADA);
        solicitud.setRespuesta("Solicitud rechazada.");
        solicitudRepository.save(solicitud);
    }

    /**
     * Procesa la puesta en revisión de una solicitud.
     * @param solicitud solicitud en revisión
     */
    private void SolicitudEnRevision(Solicitud solicitud) {
        solicitud.setEstado(SolicitudEstado.EN_REVISION);
        solicitud.setRespuesta("Solicitud en revisión, se necesita información adicional.");
        solicitudRepository.save(solicitud);
    }

    /**
     * Valida que una materia pertenece a la facultad especificada.
     * @param materia materia a validar
     * @param facultad facultad esperada
     * @throws SirhaException si la materia no pertenece a la facultad
     */
    private void validarMateriaPerteneceFacultad(Materia materia, String facultad) throws SirhaException {
        if (materia.getFacultad().equals(facultad)) {
            throw new SirhaException("La materia " + materia.getAcronimo() + " no pertenece a la facultad: " + facultad);
        }
    }


    /**
     * Configura el calendario académico estableciendo las fechas de inicio y fin del semestre.
     * Solo los decanos pueden realizar esta configuración.
     * @param calendarioDTO DTO con las nuevas fechas del calendario académico
     * @param facultad facultad del decano que realiza la configuración
     * @throws SirhaException si las fechas no son válidas
     */
    public void configurarCalendarioAcademico(CalendarioAcademicoDTO calendarioDTO, String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        // Validar que las fechas sean consistentes
        if (!calendarioDTO.esFechasValidas()) {
            throw new SirhaException("Las fechas del calendario académico no son válidas. La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        // Configurar el calendario académico
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        calendario.setFechaInicio(calendarioDTO.getFechaInicio());
        calendario.setFechaFin(calendarioDTO.getFechaFin());
    }

    /**
     * Configura el plazo de solicitudes estableciendo las fechas de inicio y fin para recibir solicitudes.
     * Solo los decanos pueden realizar esta configuración.
     * @param plazoDTO DTO con las nuevas fechas del plazo de solicitudes
     * @param facultad facultad del decano que realiza la configuración
     * @throws SirhaException si las fechas no son válidas
     */
    public void configurarPlazoSolicitudes(PlazoSolicitudesDTO plazoDTO, String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        // Validar que las fechas sean consistentes
        if (!plazoDTO.esFechasValidas()) {
            throw new SirhaException("Las fechas del plazo de solicitudes no son válidas. La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
        
        // Validar que el plazo esté dentro del calendario académico
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        if (plazoDTO.getFechaInicio().isBefore(calendario.getFechaInicio()) || 
            plazoDTO.getFechaFin().isAfter(calendario.getFechaFin())) {
            throw new SirhaException("El plazo de solicitudes debe estar dentro del calendario académico (" + 
                                   calendario.getFechaInicio() + " - " + calendario.getFechaFin() + ").");
        }
        
        // Configurar el plazo de solicitudes
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        plazo.setFechaInicio(plazoDTO.getFechaInicio());
        plazo.setFechaFin(plazoDTO.getFechaFin());
    }

    /**
     * Obtiene la configuración actual del calendario académico.
     * @param facultad facultad del decano que consulta
     * @return DTO con las fechas actuales del calendario académico
     * @throws SirhaException si la facultad no es válida
     */
    public CalendarioAcademicoDTO obtenerCalendarioAcademico(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
        return new CalendarioAcademicoDTO(calendario.getFechaInicio(), calendario.getFechaFin());
    }

    /**
     * Obtiene la configuración actual del plazo de solicitudes.
     * @param facultad facultad del decano que consulta
     * @return DTO con las fechas actuales del plazo de solicitudes
     * @throws SirhaException si la facultad no es válida
     */
    public PlazoSolicitudesDTO obtenerPlazoSolicitudes(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        return new PlazoSolicitudesDTO(plazo.getFechaInicio(), plazo.getFechaFin());
    }

    /**
     * Verifica si actualmente se pueden recibir solicitudes.
     * @param facultad facultad del decano que consulta
     * @return true si el plazo de solicitudes está activo, false en caso contrario
     * @throws SirhaException si la facultad no es válida
     */
    public boolean esPlazoSolicitudesActivo(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
        LocalDate fechaActual = LocalDate.now();
        return plazo.estaEnPlazo(fechaActual);
    }

    /**
     * Calcula el porcentaje de ocupación de un grupo.
     * @param grupo el grupo del cual calcular la ocupación
     * @return porcentaje de ocupación (0-100)
     */
    public double calcularPorcentajeOcupacion(Grupo grupo) {
        if (grupo.getCapacidad() == 0) return 0.0;
        return ((double) grupo.getCantidadInscritos() / grupo.getCapacidad()) * 100.0;
    }

    /**
     * Verifica si un grupo tiene alerta de capacidad (90% o más ocupado).
     * @param grupo el grupo a verificar
     * @return true si el grupo supera el 90% de ocupación
     */
    public boolean tieneAlertaCapacidad(Grupo grupo) {
        return calcularPorcentajeOcupacion(grupo) >= 90.0;
    }

    /**
     * Convierte un grupo a DTO de monitoreo con información de alertas.
     * @param grupo el grupo a convertir
     * @return DTO con información de monitoreo del grupo
     */
    public MonitoreoGrupoDTO convertirGrupoAMonitoreoDTO(Grupo grupo) {
        MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO(
            grupo.getId(),
            grupo.getMateria().getId(),
            grupo.getMateria().getNombre(),
            grupo.getCapacidad(),
            grupo.getCantidadInscritos()
        );
        
        dto.setEstudiantesId(grupo.getEstudiantesId());
        if (grupo.getProfesor() != null) {
            dto.setProfesorId(grupo.getProfesor().getId());
        }
        
        return dto;
    }

    /**
     * Obtiene todos los grupos de una facultad para monitoreo de cargas.
     * @param facultad facultad de la cual obtener los grupos
     * @return lista de DTOs con información de monitoreo de todos los grupos
     * @throws SirhaException si la facultad no es válida
     */
    public List<MonitoreoGrupoDTO> monitorearGruposPorFacultad(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        List<Grupo> grupos = grupoRepository.findByMateria_Facultad(facultadEnum);
        
        return grupos.stream()
                .map(this::convertirGrupoAMonitoreoDTO)
                .toList();
    }

    /**
     * Obtiene solo los grupos con alerta de capacidad (90% o más ocupados) de una facultad.
     * @param facultad facultad de la cual obtener los grupos con alerta
     * @return lista de DTOs con información de grupos que tienen alerta de capacidad
     * @throws SirhaException si la facultad no es válida
     */
    public List<MonitoreoGrupoDTO> obtenerGruposConAlerta(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        List<Grupo> grupos = grupoRepository.findByMateria_Facultad(facultadEnum);
        
        return grupos.stream()
                .filter(this::tieneAlertaCapacidad)
                .map(this::convertirGrupoAMonitoreoDTO)
                .toList();
    }

    /**
     * Obtiene el conteo de grupos por nivel de alerta para una facultad.
     * @param facultad facultad de la cual obtener estadísticas
     * @return mapa con conteos por nivel de alerta (NORMAL, ADVERTENCIA, CRITICO)
     * @throws SirhaException si la facultad no es válida
     */
    public Map<String, Long> obtenerEstadisticasAlertas(String facultad) throws SirhaException {
        // Validar facultad
        validarFacultad(facultad);
        
        List<MonitoreoGrupoDTO> grupos = monitorearGruposPorFacultad(facultad);
        
        return grupos.stream()
                .collect(Collectors.groupingBy(
                    MonitoreoGrupoDTO::getNivelAlerta,
                    Collectors.counting()
                ));
    }
}

