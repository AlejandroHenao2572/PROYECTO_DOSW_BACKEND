package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;
import com.sirha.proyecto_sirha_dosw.util.HorarioResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las funcionalidades relacionadas con los estudiantes.
 * Expone endpoints para consultar horarios, semáforos académicos y solicitudes.
 */
@Tag(
    name = "Estudiantes", 
    description = "API para gestión de funcionalidades estudiantiles incluyendo consulta de horarios, " +
                 "semáforos académicos, creación y gestión de solicitudes de cambio de grupo, " +
                 "y cancelación de materias"
)
@RestController
@RequestMapping("/api/estudiante/")
public class EstudianteController {
    private static final String SOLICITUDES_KEY = "solicitudes";
    private static final String TOTAL_KEY = "total";
    private static final String ESTADO_KEY = "estado";
    private static final String MENSAJE_KEY = "mensaje";
    private static final String ERROR_KEY = "error";
    private final EstudianteService estudianteService;

    /**
     * Constructor con inyección de dependencias de CarreraService.
     * @param estudianteService servicio que maneja la lógica de negocio para estudiante.
     */
    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    /**
     * Consulta el horario de un estudiante por semestre.
     * @param idEstudiante ID único del estudiante.
     * @param semestre número del semestre.
     * @return Map con el nombre de la materia y la lista de horarios asociados.
     */
    @Operation(
        summary = "Consultar horario de estudiante por semestre",
        description = "Obtiene el horario completo de un estudiante para un semestre específico, " +
                     "mostrando todas las materias matriculadas con sus respectivos horarios de clase.",
        tags = {"Consultas Académicas"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Horario obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Horario del estudiante",
                value = """
                {
                    "Cálculo I": [
                        {
                            "dia": "LUNES",
                            "horaInicio": "08:00",
                            "horaFin": "10:00",
                            "salon": "A101"
                        },
                        {
                            "dia": "MIERCOLES",
                            "horaInicio": "08:00",
                            "horaFin": "10:00",
                            "salon": "A101"
                        }
                    ],
                    "Física I": [
                        {
                            "dia": "MARTES",
                            "horaInicio": "10:00",
                            "horaFin": "12:00",
                            "salon": "B205"
                        }
                    ]
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "No se encontró horario para el estudiante en el semestre especificado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Sin horario",
                value = "\"No se encontró horario para el estudiante en este semestre\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/horario/{idEstudiante}/{semestre}")
    public ResponseEntity<Map<String, List<Horario>>>   consultarHorarioPorSemestre(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante, 
            @Parameter(
                name = "semestre",
                description = "Número del semestre a consultar",
                required = true,
                example = "5"
            )
            @PathVariable int semestre) {
        try {
            List<RegistroMaterias> registroMaterias = estudianteService.consultarHorarioBySemester(idEstudiante, semestre);
            if (registroMaterias.isEmpty()) {
                Map<String, List<Horario>> errorMap = new HashMap<>();
                errorMap.put(ERROR_KEY, List.of());
                return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
            }

            Map<String, List<Horario>> horariosPorMateria = HorarioResponseUtil.mapearHorariosPorMateria(registroMaterias);
            return ResponseEntity.ok(horariosPorMateria);
        } catch (SirhaException e) {
            Log.logException(e);
            Map<String, List<Horario>> errorMap = new HashMap<>();
            errorMap.put(ERROR_KEY, List.of());
            return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * @param idEstudiante ID único del estudiante.
     * @return Mapa con el estado del semáforo académico (ej. verde, azul, rojo).
     */
    @Operation(
        summary = "Consultar semáforo académico del estudiante",
        description = "Obtiene el estado del semáforo académico que indica el rendimiento del estudiante. " +
                     "Verde = buen rendimiento, Azul = rendimiento medio, Rojo = bajo rendimiento o problemas académicos.",
        tags = {"Consultas Académicas"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Semáforo académico obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Semáforo académico",
                value = """
                {
                    "2024-1": {
                        "estado": "VERDE",
                        "promedio": 4.2,
                        "creditosAprobados": 18,
                        "creditosMatriculados": 20,
                        "porcentajeAvance": 85.5,
                        "observaciones": "Buen rendimiento académico"
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "204",
        description = "No se encontró información del semáforo académico",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Sin información",
                value = "\"No se encontró horario para el estudiante\""
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
                value = "\"Estudiante no encontrado con el ID especificado\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/semaforo/{idEstudiante}")
    public ResponseEntity<Map<String, Semaforo>> consultarSemaforoAcademico(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante) {
        try {
            Map<String, Semaforo> semaforo = estudianteService.consultarSemaforoAcademico(idEstudiante);
            if (!semaforo.isEmpty()) {
                return ResponseEntity.ok(semaforo);
            } else {
                Map<String, Semaforo> errorMap = new HashMap<>();
                return new ResponseEntity<>(errorMap, HttpStatus.NO_CONTENT);
            }
        }catch (SirhaException e) {
            Log.logException(e);
            Map<String, Semaforo> errorMap = new HashMap<>();
            return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva solicitud de cambio para el estudiante.
     * Solo se pueden crear solicitudes dentro de las fechas habilitadas por la institucion.
     * Si se proporciona fechaSolicitud en el JSON, se validará esa fecha; caso contrario se usa la fecha actual.
     * @param solicitudDTO Datos de la solicitud enviados en el cuerpo de la peticion.
     * @return La solicitud creada si el proceso es exitoso.
     */
    @Operation(
        summary = "Crear nueva solicitud de cambio de grupo",
        description = "Permite al estudiante crear una solicitud de cambio de grupo. " +
                     "La solicitud solo puede ser creada durante el período habilitado por la institución. " +
                     "Se valida la disponibilidad del grupo destino y los requisitos del estudiante.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "201",
        description = "Solicitud creada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class),
            examples = @ExampleObject(
                name = "Solicitud creada",
                value = """
                {
                    "id": "SOL-001",
                    "estudiante": {
                        "codigo": "2022001",
                        "nombre": "Juan Pérez"
                    },
                    "materia": {
                        "acronimo": "CALC1",
                        "nombre": "Cálculo I"
                    },
                    "grupoOrigen": {
                        "id": "GRP-001",
                        "profesor": "Dr. García"
                    },
                    "grupoDestino": {
                        "id": "GRP-002",
                        "profesor": "Dra. López"
                    },
                    "motivo": "Conflicto de horario laboral",
                    "estado": "PENDIENTE",
                    "fechaSolicitud": "2024-01-20T14:30:00"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación o fuera del plazo de solicitudes",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Fuera de plazo",
                value = "\"No se pueden crear solicitudes fuera del plazo establecido. Plazo valido: 2024-01-15 al 2024-01-31. Fecha de solicitud: 2024-02-01\""
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
                value = "\"Error al procesar la solicitud\""
            )
        )
    )
    @PostMapping("/solicitudes")
    public ResponseEntity<Object> crearSolicitud(
            @Parameter(
                description = "Datos de la solicitud de cambio de grupo",
                required = true
            )
            @Valid @RequestBody SolicitudDTO solicitudDTO) {
        try {
            // Determinar que fecha usar para la validación
            java.time.LocalDate fechaValidacion = solicitudDTO.getFechaSolicitud() != null 
                ? solicitudDTO.getFechaSolicitud() 
                : java.time.LocalDate.now();
            
            // Validar que la fecha este dentro del plazo para crear solicitudes
            if (!PlazoSolicitudes.INSTANCIA.estaEnPlazo(fechaValidacion)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pueden crear solicitudes fuera del plazo establecido. " +
                          "Plazo valido: " + PlazoSolicitudes.INSTANCIA.getFechaInicio() + 
                          " al " + PlazoSolicitudes.INSTANCIA.getFechaFin() +
                          ". Fecha de solicitud: " + fechaValidacion);
            }
            
            Solicitud solicitudCreada = estudianteService.crearSolicitud(solicitudDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada);
        }catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    /**
     * Consulta todas las solicitudes hechas por un estudiante.
     * @param idEstudiante ID único del estudiante.
     * @return Lista de solicitudes asociadas al estudiante.
     */
    @Operation(
        summary = "Consultar todas las solicitudes de un estudiante",
        description = "Obtiene la lista completa de solicitudes de cambio de grupo realizadas por un estudiante específico, " +
                     "incluyendo solicitudes en todos los estados (pendientes, aprobadas, rechazadas).",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitudes obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Lista de solicitudes",
                value = """
                [
                    {
                        "id": "SOL-001",
                        "materia": "Cálculo I",
                        "grupoOrigen": "GRP-001",
                        "grupoDestino": "GRP-002",
                        "estado": "PENDIENTE",
                        "fechaSolicitud": "2024-01-20T14:30:00",
                        "motivo": "Conflicto de horario"
                    },
                    {
                        "id": "SOL-002",
                        "materia": "Física I",
                        "grupoOrigen": "GRP-003",
                        "grupoDestino": "GRP-004",
                        "estado": "APROBADA",
                        "fechaSolicitud": "2024-01-18T09:15:00",
                        "motivo": "Mejor horario"
                    }
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "204",
        description = "El estudiante no tiene solicitudes",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Sin solicitudes",
                value = "\"Solicitud no encontrada\""
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
                value = "\"Estudiante no encontrado\""
            )
        )
    )
    @GetMapping("/solicitudes/{idEstudiante}")
    public ResponseEntity<List<Solicitud>> consultarSolicitudes(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante) {
        try {
            List<Solicitud> solicitudes = estudianteService.consultarSolicitudes(idEstudiante);
            if (!solicitudes.isEmpty()) {
                return ResponseEntity.ok(solicitudes);
            } else {
                    return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
            }
        }catch (SirhaException e) {
            Log.logException(e);
            return new ResponseEntity<>(List.of(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Consulta una solicitud específica de un estudiante por su ID.
     * @param idEstudiante ID del estudiante.
     * @param solicitudId ID de la solicitud.
     * @return La solicitud encontrada.
     */
    @Operation(
        summary = "Consultar solicitud específica por ID",
        description = "Obtiene los detalles completos de una solicitud específica de un estudiante utilizando " +
                     "el ID del estudiante y el ID de la solicitud.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitud encontrada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class),
            examples = @ExampleObject(
                name = "Solicitud específica",
                value = """
                {
                    "id": "SOL-001",
                    "estudiante": {
                        "codigo": "2022001",
                        "nombre": "Juan Pérez",
                        "correo": "juan.perez@universidad.edu"
                    },
                    "materia": {
                        "acronimo": "CALC1",
                        "nombre": "Cálculo I",
                        "creditos": 4
                    },
                    "grupoOrigen": {
                        "id": "GRP-001",
                        "profesor": "Dr. García",
                        "horario": "Lunes 8:00-10:00"
                    },
                    "grupoDestino": {
                        "id": "GRP-002",
                        "profesor": "Dra. López",
                        "horario": "Martes 10:00-12:00"
                    },
                    "motivo": "Conflicto de horario laboral",
                    "estado": "PENDIENTE",
                    "fechaSolicitud": "2024-01-20T14:30:00",
                    "observaciones": null
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Solicitud no encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Solicitud no encontrada",
                value = "\"Solicitud no encontrada para el estudiante especificado\""
            )
        )
    )
    @GetMapping("/solicitudes/{idEstudiante}/{solicitudId}")
    public  ResponseEntity<Solicitud>  consultarSolicitudesPorId(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante, 
            @Parameter(
                name = "solicitudId",
                description = "ID único de la solicitud",
                required = true,
                example = "SOL-001"
            )
            @PathVariable String solicitudId) {
        try {
            Solicitud solicitud = estudianteService.consultarSolicitudesById(idEstudiante, solicitudId);
            return ResponseEntity.ok(solicitud);
        }catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cancela una materia específica del estudiante en su semestre actual.
     * @param idEstudiante ID del estudiante.
     * @param acronimoMateria Acrónimo de la materia a cancelar.
     * @return Mensaje de confirmación de la cancelación.
     */
    @Operation(
        summary = "Cancelar materia del estudiante",
        description = "Permite al estudiante cancelar una materia específica de su registro académico en el semestre actual. " +
                     "Esta acción afecta su carga académica y puede tener implicaciones en su avance curricular.",
        tags = {"Gestión Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Materia cancelada exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Cancelación exitosa",
                value = "\"Materia CALC1 cancelada exitosamente para el estudiante 2022001\""
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en la cancelación - materia no encontrada o no cancelable",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error cancelación",
                value = "\"No se puede cancelar la materia: el estudiante no está matriculado en esta materia\""
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Estudiante o materia no encontrados",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "No encontrado",
                value = "\"Estudiante no encontrado\""
            )
        )
    )
    @PutMapping("/materias/{idEstudiante}/{acronimoMateria}/cancelar")
    public ResponseEntity<String> cancelarMateria(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante, 
            @Parameter(
                name = "acronimoMateria",
                description = "Acrónimo de la materia a cancelar",
                required = true,
                example = "CALC1"
            )
            @PathVariable String acronimoMateria) {
        try {
            String resultado = estudianteService.cancelarMateria(idEstudiante, acronimoMateria);
            return ResponseEntity.ok(resultado);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Consulta todas las solicitudes que están en un estado específico.
     * Estados disponibles: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA
     * @param estado Estado de las solicitudes a consultar.
     * @return Lista de solicitudes en el estado especificado.
     */
    @Operation(
        summary = "Consultar solicitudes por estado",
        description = "Obtiene todas las solicitudes del sistema que se encuentran en un estado específico. " +
                     "Estados válidos: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA.",
        tags = {"Consultas Administrativas"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitudes obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Solicitudes por estado",
                value = """
                {
                    "solicitudes": [
                        {
                            "id": "SOL-001",
                            "estudiante": "Juan Pérez",
                            "materia": "Cálculo I",
                            "estado": "PENDIENTE",
                            "fechaSolicitud": "2024-01-20T14:30:00"
                        },
                        {
                            "id": "SOL-003",
                            "estudiante": "María García",
                            "materia": "Física I",
                            "estado": "PENDIENTE",
                            "fechaSolicitud": "2024-01-21T09:15:00"
                        }
                    ],
                    "estado": "PENDIENTE",
                    "total": 2
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Estado inválido",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Estado inválido",
                value = "\"Estado inválido. Estados válidos: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA\""
            )
        )
    )
    @GetMapping("/solicitudes/estado/{estado}")
    public ResponseEntity<Map<String, Object>> consultarSolicitudesPorEstado(
            @Parameter(
                name = "estado",
                description = "Estado de las solicitudes a consultar",
                required = true,
                example = "PENDIENTE"
            )
            @PathVariable String estado) {
        try {
            SolicitudEstado solicitudEstado = parseSolicitudEstado(estado);
            if (solicitudEstado == null) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put(ERROR_KEY, "Estado inválido. Estados válidos: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA");
                return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
            }

            List<Solicitud> solicitudes = estudianteService.consultarSolicitudesPorEstado(solicitudEstado);

            if (solicitudes.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
            response.put(MENSAJE_KEY, "No se encontraron solicitudes en estado: " + estado);
            response.put(ESTADO_KEY, estado);
            response.put(TOTAL_KEY, 0);
                return ResponseEntity.ok(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put(SOLICITUDES_KEY, solicitudes);
            response.put(ESTADO_KEY, estado);
            response.put(TOTAL_KEY, solicitudes.size());
            return ResponseEntity.ok(response);

        } catch (SirhaException e) {
            Log.logException(e);
            Map<String, Object> errorMap = new HashMap<>();
        errorMap.put(ERROR_KEY, e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
    }

    // Método privado para convertir el estado a SolicitudEstado
    private SolicitudEstado parseSolicitudEstado(String estado) {
        try {
            return SolicitudEstado.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Consulta todas las solicitudes existentes en el sistema.
     * @return Lista completa de todas las solicitudes.
     */
    @Operation(
        summary = "Consultar todas las solicitudes del sistema",
        description = "Obtiene la lista completa de todas las solicitudes de cambio de grupo existentes en el sistema, " +
                     "independientemente del estudiante o estado. Útil para administradores y reportes generales.",
        tags = {"Consultas Administrativas"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitudes obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Todas las solicitudes",
                value = """
                {
                    "solicitudes": [
                        {
                            "id": "SOL-001",
                            "estudiante": "Juan Pérez",
                            "materia": "Cálculo I",
                            "estado": "PENDIENTE",
                            "fechaSolicitud": "2024-01-20T14:30:00"
                        },
                        {
                            "id": "SOL-002",
                            "estudiante": "María García",
                            "materia": "Física I",
                            "estado": "APROBADA",
                            "fechaSolicitud": "2024-01-18T09:15:00"
                        }
                    ],
                    "total": 2
                }
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
                value = "\"Error interno del servidor: descripción del error\""
            )
        )
    )
    @GetMapping("/solicitudes/todas")
    public ResponseEntity<Map<String, Object>> consultarTodasLasSolicitudes() {
        try {
            List<Solicitud> solicitudes = estudianteService.consultarTodasLasSolicitudes();
            
            if (solicitudes.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put(MENSAJE_KEY, "No se encontraron solicitudes en el sistema");
                response.put(TOTAL_KEY, 0);
                return ResponseEntity.ok(response);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put(SOLICITUDES_KEY, solicitudes);
            response.put(TOTAL_KEY, solicitudes.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Log.logException(e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put(ERROR_KEY, "Error interno del servidor: " + e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta las solicitudes de un estudiante específico filtradas por estado.
     * @param idEstudiante ID del estudiante.
     * @param estado Estado de las solicitudes a consultar.
     * @return Lista de solicitudes del estudiante en el estado especificado.
     */
    @Operation(
        summary = "Consultar solicitudes de estudiante por estado",
        description = "Obtiene las solicitudes de un estudiante específico filtradas por estado. " +
                     "Combina la consulta por estudiante y estado para resultados más precisos.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitudes obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Solicitudes del estudiante por estado",
                value = """
                {
                    "solicitudes": [
                        {
                            "id": "SOL-001",
                            "materia": "Cálculo I",
                            "grupoOrigen": "GRP-001",
                            "grupoDestino": "GRP-002",
                            "estado": "PENDIENTE",
                            "fechaSolicitud": "2024-01-20T14:30:00",
                            "motivo": "Conflicto de horario"
                        }
                    ],
                    "idEstudiante": "2022001",
                    "estado": "PENDIENTE",
                    "total": 1
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Estado inválido",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Estado inválido",
                value = "\"Estado inválido. Estados válidos: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA\""
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
                value = "\"Estudiante no encontrado\""
            )
        )
    )
    @GetMapping("/solicitudes/{idEstudiante}/estado/{estado}")
    public ResponseEntity<Map<String, Object>> consultarSolicitudesEstudiantePorEstado(
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "2022001"
            )
            @PathVariable String idEstudiante, 
            @Parameter(
                name = "estado",
                description = "Estado de las solicitudes a consultar",
                required = true,
                example = "PENDIENTE"
            )
            @PathVariable String estado) {
        try {
            SolicitudEstado solicitudEstado = parseSolicitudEstado(estado);
            if (solicitudEstado == null) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put(ERROR_KEY, "Estado inválido. Estados válidos: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA");
                return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
            }

            List<Solicitud> solicitudes = estudianteService.consultarSolicitudesEstudiantePorEstado(idEstudiante, solicitudEstado);

            if (solicitudes.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put(MENSAJE_KEY, "No se encontraron solicitudes del estudiante en estado: " + estado);
                response.put("idEstudiante", idEstudiante);
                // ...existing code...
                response.put(TOTAL_KEY, 0);
                return ResponseEntity.ok(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put(SOLICITUDES_KEY, solicitudes);
                response.put("idEstudiante", idEstudiante);
                response.put(ESTADO_KEY, estado);
            response.put(TOTAL_KEY, solicitudes.size());
            return ResponseEntity.ok(response);
            
        } catch (SirhaException e) {
            Log.logException(e);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put(ERROR_KEY, e.getMessage());
            return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
        }
    }
}