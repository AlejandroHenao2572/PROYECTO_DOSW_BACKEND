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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
@Tag(name = "Reportes Controller", description = "API para la gestión de reportes y estadísticas del sistema SIRHA. " +
        "Proporciona endpoints para obtener estadísticas de grupos más solicitados, tasas de aprobación, " +
        "indicadores de avance académico y otros reportes analíticos del sistema.")
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
    @Operation(
        summary = "Obtener grupos más solicitados",
        description = "Obtiene una lista de los grupos más solicitados para cambio en todo el sistema, " +
                     "ordenados por cantidad de solicitudes de mayor a menor. Permite especificar un límite " +
                     "para controlar la cantidad de resultados retornados.",
        tags = {"Estadísticas de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de grupos más solicitados obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EstadisticasGrupoDTO.class),
            examples = @ExampleObject(
                name = "Grupos más solicitados",
                value = """
                [
                    {
                        "grupoId": "GRP-001",
                        "materia": "Cálculo I",
                        "profesor": "Dr. García",
                        "facultad": "INGENIERIA",
                        "totalSolicitudes": 25,
                        "solicitudesAprobadas": 15,
                        "solicitudesRechazadas": 10,
                        "tasaAprobacion": 60.0
                    },
                    {
                        "grupoId": "GRP-002",
                        "materia": "Programación I",
                        "profesor": "Dra. López",
                        "facultad": "INGENIERIA",
                        "totalSolicitudes": 18,
                        "solicitudesAprobadas": 12,
                        "solicitudesRechazadas": 6,
                        "tasaAprobacion": 66.67
                    }
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error interno",
                value = "500"
            )
        )
    )
    @GetMapping("/grupos-mas-solicitados")
    public ResponseEntity<List<EstadisticasGrupoDTO>> obtenerGruposMasSolicitados(
            @Parameter(
                name = "limite",
                description = "Número máximo de grupos a retornar en la respuesta",
                example = "10"
            )
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
    @Operation(
        summary = "Obtener grupos más solicitados por facultad",
        description = "Obtiene una lista de los grupos más solicitados para cambio dentro de una facultad específica, " +
                     "ordenados por cantidad de solicitudes de mayor a menor.",
        tags = {"Estadísticas de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de grupos más solicitados por facultad obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EstadisticasGrupoDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error facultad",
                value = "400"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/grupos-mas-solicitados/facultad/{facultad}")
    public ResponseEntity<List<EstadisticasGrupoDTO>> obtenerGruposMasSolicitadosPorFacultad(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad. Valores válidos: INGENIERIA, MEDICINA, DERECHO, etc.",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "limite",
                description = "Número máximo de grupos a retornar",
                example = "5"
            )
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
    @Operation(
        summary = "Obtener estadísticas de un grupo específico",
        description = "Obtiene estadísticas detalladas de un grupo específico, incluyendo total de solicitudes, " +
                     "solicitudes aprobadas, rechazadas y tasa de aprobación.",
        tags = {"Estadísticas de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estadísticas del grupo obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EstadisticasGrupoDTO.class),
            examples = @ExampleObject(
                name = "Estadísticas de grupo",
                value = """
                {
                    "grupoId": "GRP-001",
                    "materia": "Cálculo I",
                    "profesor": "Dr. García",
                    "facultad": "INGENIERIA",
                    "totalSolicitudes": 25,
                    "solicitudesAprobadas": 15,
                    "solicitudesRechazadas": 10,
                    "tasaAprobacion": 60.0,
                    "capacidadActual": 28,
                    "capacidadMaxima": 30
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Grupo no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Grupo no encontrado",
                value = "404"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/grupo/{grupoId}/estadisticas")
    public ResponseEntity<EstadisticasGrupoDTO> obtenerEstadisticasGrupo(
            @Parameter(
                name = "grupoId",
                description = "ID único del grupo",
                required = true,
                example = "GRP-001"
            )
            @PathVariable String grupoId) {
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
    @Operation(
        summary = "Obtener total de solicitudes de cambio",
        description = "Obtiene el número total de solicitudes de cambio de grupo registradas en todo el sistema.",
        tags = {"Estadísticas Generales"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Total de solicitudes obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Long.class),
            examples = @ExampleObject(
                name = "Total solicitudes",
                value = "150"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
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
    @Operation(
        summary = "Obtener tasas de aprobación globales",
        description = "Obtiene las tasas de aprobación y rechazo de solicitudes a nivel global del sistema, " +
                     "proporcionando métricas estadísticas generales.",
        tags = {"Tasas de Aprobación"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Tasas de aprobación globales obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TasaAprobacionDTO.class),
            examples = @ExampleObject(
                name = "Tasas globales",
                value = """
                {
                    "totalSolicitudes": 500,
                    "solicitudesAprobadas": 300,
                    "solicitudesRechazadas": 150,
                    "solicitudesPendientes": 50,
                    "tasaAprobacion": 60.0,
                    "tasaRechazo": 30.0,
                    "tasaPendiente": 10.0,
                    "periodo": "2024-1"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
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
    @Operation(
        summary = "Obtener tasas de aprobación por facultad",
        description = "Obtiene las tasas de aprobación y rechazo de solicitudes para una facultad específica.",
        tags = {"Tasas de Aprobación"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Tasas de aprobación por facultad obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TasaAprobacionDTO.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/tasas-aprobacion/facultad/{facultad}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorFacultad(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
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
    @Operation(
        summary = "Obtener tasas de aprobación por tipo de solicitud",
        description = "Obtiene las tasas de aprobación y rechazo de solicitudes filtradas por tipo específico " +
                     "(INSCRIPCION_GRUPO, CAMBIO_GRUPO, CANCELACION_GRUPO).",
        tags = {"Tasas de Aprobación"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Tasas de aprobación por tipo obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TasaAprobacionDTO.class),
            examples = @ExampleObject(
                name = "Tasas por tipo",
                value = """
                {
                    "totalSolicitudes": 200,
                    "solicitudesAprobadas": 140,
                    "solicitudesRechazadas": 50,
                    "solicitudesPendientes": 10,
                    "tasaAprobacion": 70.0,
                    "tasaRechazo": 25.0,
                    "tasaPendiente": 5.0,
                    "tipoSolicitud": "CAMBIO_GRUPO"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Tipo de solicitud inválido",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error tipo inválido",
                value = "400"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/tasas-aprobacion/tipo/{tipo}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorTipo(
            @Parameter(
                name = "tipo",
                description = "Tipo de solicitud. Valores válidos: INSCRIPCION_GRUPO, CAMBIO_GRUPO, CANCELACION_GRUPO",
                required = true,
                example = "CAMBIO_GRUPO"
            )
            @PathVariable String tipo) {
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
    @Operation(
        summary = "Obtener tasas de aprobación por facultad y tipo de solicitud",
        description = "Obtiene las tasas de aprobación y rechazo de solicitudes filtradas por facultad específica " +
                     "y tipo de solicitud. Proporciona análisis combinado de ambos criterios.",
        tags = {"Tasas de Aprobación"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Tasas de aprobación combinadas obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = TasaAprobacionDTO.class),
            examples = @ExampleObject(
                name = "Tasas combinadas",
                value = """
                {
                    "totalSolicitudes": 75,
                    "solicitudesAprobadas": 50,
                    "solicitudesRechazadas": 20,
                    "solicitudesPendientes": 5,
                    "tasaAprobacion": 66.67,
                    "tasaRechazo": 26.67,
                    "tasaPendiente": 6.67,
                    "facultad": "INGENIERIA",
                    "tipoSolicitud": "CAMBIO_GRUPO"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad o tipo de solicitud inválidos",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error parámetros inválidos",
                value = "400"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/tasas-aprobacion/facultad/{facultad}/tipo/{tipo}")
    public ResponseEntity<TasaAprobacionDTO> obtenerTasasAprobacionPorFacultadYTipo(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "tipo",
                description = "Tipo de solicitud. Valores válidos: INSCRIPCION_GRUPO, CAMBIO_GRUPO, CANCELACION_GRUPO",
                required = true,
                example = "CAMBIO_GRUPO"
            )
            @PathVariable String tipo) {
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
    @Operation(
        summary = "Obtener tipos de solicitud disponibles",
        description = "Obtiene una lista de todos los tipos de solicitud disponibles en el sistema.",
        tags = {"Configuración"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de tipos de solicitud obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(
                name = "Tipos de solicitud",
                value = """
                [
                    "INSCRIPCION_GRUPO",
                    "CAMBIO_GRUPO",
                    "CANCELACION_GRUPO"
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
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

    /**
     * Obtiene los indicadores de avance académico para un estudiante específico.
     * @param estudianteId ID del estudiante
     * @return IndicadoresAvanceDTO con métricas del estudiante
     */
    @Operation(
        summary = "Obtener indicadores de avance de un estudiante",
        description = "Obtiene los indicadores de avance académico para un estudiante específico, " +
                     "incluyendo progreso curricular, promedio académico, materias aprobadas y otros indicadores de rendimiento.",
        tags = {"Indicadores Académicos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Indicadores de avance obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = IndicadoresAvanceDTO.class),
            examples = @ExampleObject(
                name = "Indicadores de estudiante",
                value = """
                {
                    "estudianteId": "20221005001",
                    "nombreEstudiante": "Juan Pérez",
                    "semestreActual": 5,
                    "creditosAprobados": 90,
                    "creditosTotales": 180,
                    "porcentajeAvance": 50.0,
                    "promedioGeneral": 3.8,
                    "materiasAprobadas": 18,
                    "materiasReprobadas": 2,
                    "semaforoAcademico": "VERDE"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Estudiante no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Estudiante no encontrado",
                value = "404"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/indicadores-avance/estudiante/{estudianteId}")
    public ResponseEntity<IndicadoresAvanceDTO> obtenerIndicadoresAvanceEstudiante(
            @Parameter(
                name = "estudianteId",
                description = "ID único del estudiante",
                required = true,
                example = "20221005001"
            )
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
    @Operation(
        summary = "Obtener indicadores de avance globales",
        description = "Obtiene estadísticas globales de indicadores de avance académico del sistema, " +
                     "opcionalmente filtradas por facultad. Proporciona métricas agregadas de rendimiento académico.",
        tags = {"Indicadores Académicos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Indicadores globales obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = IndicadoresAvanceDTO.class),
            examples = @ExampleObject(
                name = "Indicadores globales",
                value = """
                {
                    "totalEstudiantes": 1250,
                    "promedioGeneralSistema": 3.5,
                    "porcentajeAvancePromedio": 45.0,
                    "estudiantesEnRiesgo": 125,
                    "estudiantesDestacados": 200,
                    "tasaRetencion": 85.0,
                    "semaforoGeneral": "AMARILLO"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida (si se proporciona)",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error facultad",
                value = "400"
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/indicadores-avance/global")
    public ResponseEntity<IndicadoresAvanceDTO> obtenerIndicadoresAvanceGlobales(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad para filtrar los resultados (opcional)",
                required = false,
                example = "INGENIERIA"
            )
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
