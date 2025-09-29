/**
 * Servicio para la generación de reportes y estadísticas del sistema.
 * Proporciona métodos para obtener estadísticas de grupos más solicitados,
 * reportes de solicitudes y otras métricas del sistema.
 */
package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportesService {
    
    private final SolicitudRepository solicitudRepository;
    private final GrupoRepository grupoRepository;
    
    /**
     * Constructor para inyección de dependencias.
     * @param solicitudRepository Repositorio de solicitudes
     * @param grupoRepository Repositorio de grupos
     */
    public ReportesService(SolicitudRepository solicitudRepository, GrupoRepository grupoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.grupoRepository = grupoRepository;
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados para cambio.
     * @return Lista de EstadisticasGrupoDTO ordenada por cantidad de solicitudes (descendente)
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitados() {
        return obtenerGruposMasSolicitados(null, 10);
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados para cambio con filtros.
     * @param facultad Facultad específica (null para todas)
     * @param limite Número máximo de grupos a retornar
     * @return Lista de EstadisticasGrupoDTO ordenada por cantidad de solicitudes (descendente)
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitados(Facultad facultad, int limite) {
        List<Grupo> grupos;
        
        // Obtener grupos según la facultad
        if (facultad != null) {
            grupos = grupoRepository.findByMateria_Facultad(facultad);
        } else {
            grupos = grupoRepository.findAll();
        }
        
        List<EstadisticasGrupoDTO> estadisticas = new ArrayList<>();
        
        // Para cada grupo, calcular las estadísticas
        for (Grupo grupo : grupos) {
            if (grupo.getMateria() != null) {
                // Contar solicitudes de cambio hacia este grupo
                long cantidadSolicitudes = solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud(
                    grupo.getId(), TipoSolicitud.CAMBIO_GRUPO);
                
                // Solo incluir grupos que tienen al menos una solicitud
                if (cantidadSolicitudes > 0) {
                    EstadisticasGrupoDTO estadistica = new EstadisticasGrupoDTO(
                        grupo.getId(),
                        grupo.getMateria().getId(),
                        grupo.getMateria().getNombre(),
                        grupo.getMateria().getAcronimo(),
                        grupo.getMateria().getFacultad().name()
                    );
                    
                    estadistica.setCapacidad(grupo.getCapacidad());
                    estadistica.setCantidadInscritos(grupo.getCantidadInscritos());
                    estadistica.setCantidadSolicitudes(cantidadSolicitudes);
                    
                    estadisticas.add(estadistica);
                }
            }
        }
        
        // Ordenar por cantidad de solicitudes (descendente) y aplicar límite
        return estadisticas.stream()
                .sorted(Comparator.comparing(EstadisticasGrupoDTO::getCantidadSolicitudes).reversed())
                .limit(limite)
                .toList();
    }
    
    /**
     * Obtiene estadísticas detalladas de un grupo específico.
     * @param grupoId ID del grupo
     * @return EstadisticasGrupoDTO con información del grupo o null si no existe
     */
    public EstadisticasGrupoDTO obtenerEstadisticasGrupo(String grupoId) {
        return grupoRepository.findById(grupoId)
                .map(grupo -> {
                    if (grupo.getMateria() != null) {
                        long cantidadSolicitudes = solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud(
                            grupo.getId(), TipoSolicitud.CAMBIO_GRUPO);
                        
                        EstadisticasGrupoDTO estadistica = new EstadisticasGrupoDTO(
                            grupo.getId(),
                            grupo.getMateria().getId(),
                            grupo.getMateria().getNombre(),
                            grupo.getMateria().getAcronimo(),
                            grupo.getMateria().getFacultad().name()
                        );
                        
                        estadistica.setCapacidad(grupo.getCapacidad());
                        estadistica.setCantidadInscritos(grupo.getCantidadInscritos());
                        estadistica.setCantidadSolicitudes(cantidadSolicitudes);
                        
                        return estadistica;
                    }
                    return null;
                })
                .orElse(null);
    }
    
    /**
     * Obtiene el total de solicitudes de cambio de grupo en el sistema.
     * @return Número total de solicitudes de cambio
     */
    public long obtenerTotalSolicitudesCambio() {
        return solicitudRepository.findByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO).size();
    }
    
    /**
     * Obtiene estadísticas de grupos más solicitados por facultad.
     * @param facultad Facultad específica
     * @return Lista de EstadisticasGrupoDTO para la facultad especificada
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitadosPorFacultad(Facultad facultad) {
        return obtenerGruposMasSolicitados(facultad, 5); // Top 5 por facultad
    }
}