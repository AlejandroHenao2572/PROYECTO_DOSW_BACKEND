package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.SolicitudCambio;
import com.sirha.proyecto_sirha_dosw.repository.EstudianteRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudCambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private SolicitudCambioRepository solicitudCambioRepository;

    public Optional<Estudiante> getEstudianteById(String id) {
        return estudianteRepository.findById(id);
    }

    public SolicitudCambio crearSolicitud(SolicitudCambio solicitud) {
        // Lógica de negocio:
        // 1. Validar que las solicitudes están habilitadas (simplificado aquí)
        if (!fechasHabilitadas()) {
            throw new IllegalStateException("El período para crear solicitudes de cambio no está activo.");
        }

        // 2. Asignar estado inicial, fecha y prioridad (orden de llegada)
        solicitud.setEstado(com.sirha.proyecto_sirha_dosw.model.EstadoSolicitud.PENDIENTE);
        solicitud.setFechaCreacion(LocalDateTime.now());

        // La prioridad por orden de llegada se gestiona ordenando por fechaCreacion.
        // Contamos cuántas solicitudes hay para asignar un número simple.
        solicitud.setPrioridad((int) solicitudCambioRepository.count() + 1);

        return solicitudCambioRepository.save(solicitud);
    }

    public List<SolicitudCambio> getHistorialSolicitudes(String idEstudiante) {
        return solicitudCambioRepository.findByIdEstudianteOrderByFechaCreacionDesc(idEstudiante);
    }

    private boolean fechasHabilitadas() {
        // Lógica para verificar si la fecha actual está en el período permitido.
        // Por ahora, devolvemos 'true' para permitir pruebas.
        return true;
    }
}