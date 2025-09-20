package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.model.EstadoSolicitud;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.SemaforoAcademico;
import com.sirha.proyecto_sirha_dosw.model.SolicitudCambio;
import com.sirha.proyecto_sirha_dosw.repository.EstudianteRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudCambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private SolicitudCambioRepository solicitudCambioRepository;

    public Optional<Estudiante> getEstudianteById(String id) {
        return estudianteRepository.findById(id);
    }

    // Métodos para solicitudes (existentes mejorados)
    public SolicitudCambio crearSolicitud(SolicitudCambio solicitud) {
        // Lógica de negocio:
        // 1. Validar que las solicitudes están habilitadas
        if (!fechasHabilitadas()) {
            throw new IllegalStateException("El período para crear solicitudes de cambio no está activo.");
        }

        // 2. Validar que el estudiante existe
        if (solicitud.getIdEstudiante() != null && 
            !estudianteRepository.existsById(solicitud.getIdEstudiante())) {
            throw new IllegalArgumentException("El estudiante especificado no existe.");
        }

        // 3. Asignar estado inicial, fecha y prioridad
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setFechaCreacion(LocalDateTime.now());

        // La prioridad por orden de llegada se gestiona ordenando por fechaCreacion.
        solicitud.setPrioridad((int) solicitudCambioRepository.count() + 1);

        return solicitudCambioRepository.save(solicitud);
    }

    public List<SolicitudCambio> getHistorialSolicitudes(String idEstudiante) {
        return solicitudCambioRepository.findByIdEstudianteOrderByFechaCreacionDesc(idEstudiante);
    }

    public Optional<SolicitudCambio> getSolicitudById(String idSolicitud) {
        return solicitudCambioRepository.findById(idSolicitud);
    }

    private boolean fechasHabilitadas() {
        // Lógica para verificar si la fecha actual está en el período permitido.
        // Por ahora, devolvemos 'true' para permitir pruebas.
        return true;
    }

    public Horario getHorarioActual(String estudianteId) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            return estudianteOpt.get().getHorarioActual();
        }
        return null;
    }

    public Horario getHorarioPorSemestre(String estudianteId, int numeroSemestre) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            return estudianteOpt.get().getHorarioPorSemestre(numeroSemestre);
        }
        return null;
    }

    public Map<String, SemaforoAcademico> getSemaforoAcademico(String estudianteId) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            return estudianteOpt.get().calcularSemaforoAcademico();
        }
        return Map.of();
    }

    public SolicitudCambio actualizarSolicitud(String idSolicitud, SolicitudCambio solicitudActualizada) {
        Optional<SolicitudCambio> solicitudExistenteOpt = solicitudCambioRepository.findById(idSolicitud);
        
        if (solicitudExistenteOpt.isPresent()) {
            SolicitudCambio solicitudExistente = solicitudExistenteOpt.get();
            
            // Solo permitir actualización si está en estado PENDIENTE
            if (solicitudExistente.getEstado() != EstadoSolicitud.PENDIENTE) {
                throw new IllegalStateException("Solo se pueden modificar solicitudes en estado PENDIENTE.");
            }
            
            // Actualizar campos permitidos
            if (solicitudActualizada.getMateriaConProblema() != null) {
                solicitudExistente.setMateriaConProblema(solicitudActualizada.getMateriaConProblema());
            }
            if (solicitudActualizada.getSugerenciaCambio() != null) {
                solicitudExistente.setSugerenciaCambio(solicitudActualizada.getSugerenciaCambio());
            }
            if (solicitudActualizada.getObservaciones() != null) {
                solicitudExistente.setObservaciones(solicitudActualizada.getObservaciones());
            }
            
            return solicitudCambioRepository.save(solicitudExistente);
        }
        
        return null;
    }

    public List<String> getMateriasPendientes(String estudianteId) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            return estudianteOpt.get().getMateriasPendientes()
                    .stream()
                    .map(Materia::getNombre)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public Estudiante actualizarHorario(String estudianteId, int semestre, Horario nuevoHorario) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            estudiante.setHorarioPorSemestre(semestre, nuevoHorario);
            return estudianteRepository.save(estudiante);
        }
        return null;
    }

    // Método para inscribir a un estudiante en un grupo
    public boolean inscribirEnGrupo(String estudianteId, String grupoId) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            estudiante.inscribirEnGrupo(grupoId);
            estudianteRepository.save(estudiante);
            return true;
        }
        return false;
    }

}