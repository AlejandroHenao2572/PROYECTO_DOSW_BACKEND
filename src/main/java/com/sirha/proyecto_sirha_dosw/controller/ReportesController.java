/**
 * Controlador REST para la gestión de reportes y estadísticas del sistema.
 * Proporciona endpoints para obtener estadísticas de grupos más solicitados
 * y otros reportes del sistema SIRHA.
 */
package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.dto.TasaAprobacionDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.service.ReportesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReportesController {
    
    private final ReportesService reportesService;
    
    /**
     * Constructor para inyección de dependencias.
     * @param reportesService Servicio de reportes
     */
    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados para cambio.
     * @param limite Número máximo de grupos a retornar (opcional, por defecto 10)
     * @return Lista de EstadisticasGrupoDTO ordenada por cantidad de solicitudes
     */
    @GetMapping("/grupos-mas-solicitados")
    public ResponseEntity<List<EstadisticasGrupoDTO>> obtenerGruposMasSolicitados(
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<EstadisticasGrupoDTO> estadisticas = reportesService.obtenerGruposMasSolicitados(null, limite);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados por facultad.
     * @param facultad Nombre de la facultad
     * @param limite Número máximo de grupos a retornar (opcional, por defecto 5)
     * @return Lista de EstadisticasGrupoDTO para la facultad especificada
     */
    @GetMapping("/grupos-mas-solicitados/facultad/{facultad}")
    public ResponseEntity<List<EstadisticasGrupoDTO>> obtenerGruposMasSolicitadosPorFacultad(
            @PathVariable String facultad,
            @RequestParam(defaultValue = "5") int limite) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            List<EstadisticasGrupoDTO> estadisticas = reportesService.obtenerGruposMasSolicitados(facultadEnum, limite);
            return ResponseEntity.ok(estadisticas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene estadísticas detalladas de un grupo específico.
     * @param grupoId ID del grupo
     * @return EstadisticasGrupoDTO con información del grupo
     */
    @GetMapping("/grupo/{grupoId}/estadisticas")
    public ResponseEntity<EstadisticasGrupoDTO> obtenerEstadisticasGrupo(@PathVariable String grupoId) {
        try {
            EstadisticasGrupoDTO estadisticas = reportesService.obtenerEstadisticasGrupo(grupoId);
            if (estadisticas != null) {
                return ResponseEntity.ok(estadisticas);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene el total de solicitudes de cambio de grupo en el sistema.
     * @return Número total de solicitudes de cambio
     */
    @GetMapping("/total-solicitudes-cambio")
    public ResponseEntity<Long> obtenerTotalSolicitudesCambio() {
        try {
            long total = reportesService.obtenerTotalSolicitudesCambio();
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo globales del sistema.
     * @return TasaAprobacionDTO con métricas globales
     */
    @GetMapping("/tasas-aprobacion")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionGlobal() {
        try {
            TasaAprobacionDTO tasas = reportesService.obtenerTasasAprobacionGlobal();
            return ResponseEntity.ok(tasas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por facultad.
     * @param facultad Nombre de la facultad
     * @return TasaAprobacionDTO con métricas de la facultad
     */
    @GetMapping("/tasas-aprobacion/facultad/{facultad}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorFacultad(@PathVariable String facultad) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            TasaAprobacionDTO tasas = reportesService.obtenerTasasAprobacionPorFacultad(facultadEnum);
            return ResponseEntity.ok(tasas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por tipo de solicitud.
     * @param tipo Tipo de solicitud (INSCRIPCION_GRUPO, CAMBIO_GRUPO, CANCELACION_GRUPO)
     * @return TasaAprobacionDTO con métricas del tipo de solicitud
     */
    @GetMapping("/tasas-aprobacion/tipo/{tipo}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorTipo(@PathVariable String tipo) {
        try {
            TipoSolicitud tipoEnum = TipoSolicitud.valueOf(tipo.toUpperCase());
            TasaAprobacionDTO tasas = reportesService.obtenerTasasAprobacionPorTipo(tipoEnum);
            return ResponseEntity.ok(tasas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por facultad y tipo de solicitud.
     * @param facultad Nombre de la facultad
     * @param tipo Tipo de solicitud
     * @return TasaAprobacionDTO con métricas combinadas
     */
    @GetMapping("/tasas-aprobacion/facultad/{facultad}/tipo/{tipo}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorFacultadYTipo(
            @PathVariable String facultad, @PathVariable String tipo) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            TipoSolicitud tipoEnum = TipoSolicitud.valueOf(tipo.toUpperCase());
            TasaAprobacionDTO tasas = reportesService.obtenerTasasAprobacionPorFacultadYTipo(facultadEnum, tipoEnum);
            return ResponseEntity.ok(tasas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene todos los tipos de solicitud disponibles.
     * @return Lista de tipos de solicitud
     */
    @GetMapping("/tipos-solicitud")
    public ResponseEntity<List<String>> obtenerTiposSolicitud() {
        try {
            List<String> tipos = List.of(
                TipoSolicitud.INSCRIPCION_GRUPO.name(),
                TipoSolicitud.CAMBIO_GRUPO.name(),
                TipoSolicitud.CANCELACION_GRUPO.name()
            );
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =================== INDICADORES DE AVANCE ACADÉMICO ===================

    /**
     * Obtiene los indicadores de avance académico para un estudiante específico.
     * @param estudianteId ID del estudiante
     * @return IndicadoresAvanceDTO con métricas del estudiante
     */
    @GetMapping("/indicadores-avance/estudiante/{estudianteId}")
    public ResponseEntity<IndicadoresAvanceDTO> obtenerIndicadoresAvanceEstudiante(
            @PathVariable String estudianteId) {
        try {
            IndicadoresAvanceDTO indicadores = reportesService.calcularIndicadoresAvanceEstudiante(estudianteId);
            if (indicadores == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(indicadores);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtiene estadísticas globales de indicadores de avance académico.
     * @param facultad Facultad específica (opcional)
     * @return IndicadoresAvanceDTO con estadísticas globales
     */
    @GetMapping("/indicadores-avance/global")
    public ResponseEntity<IndicadoresAvanceDTO> obtenerIndicadoresAvanceGlobales(
            @RequestParam(required = false) String facultad) {
        try {

            Facultad facultadEnum = parseFacultad(facultad);
            // Solo retorna 400 si el parámetro no es vacío y no coincide con ningún enum
            if (facultad != null && !facultad.trim().isEmpty() && facultadEnum == null) {
                return ResponseEntity.badRequest().build();
            }

            IndicadoresAvanceDTO estadisticas = reportesService.calcularIndicadoresAvanceGlobales(facultadEnum);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Método privado para convertir el string facultad en enum Facultad
    private Facultad parseFacultad(String facultad) {
        if (facultad == null || facultad.trim().isEmpty()) {
            return null;
        }
        try {
            return Facultad.valueOf(facultad.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
