package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;
import com.sirha.proyecto_sirha_dosw.util.HorarioResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las funcionalidades relacionadas con los estudiantes.
 * Expone endpoints para consultar horarios, semáforos académicos y solicitudes.
 */
@RestController
@RequestMapping("/api/Estudiantes")
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
        this.estudianteService = java.util.Objects.requireNonNull(estudianteService);
    }

    /**
     * Consulta el horario de un estudiante por semestre.
     * @param idEstudiante ID único del estudiante.
     * @param semestre número del semestre.
     * @return Map con el nombre de la materia y la lista de horarios asociados.
     */
    @GetMapping("/horario/{idEstudiante}/{semestre}")
    public ResponseEntity<Map<String, List<Horario>>> consultarHorarioPorSemestre(@PathVariable String idEstudiante, @PathVariable int semestre) {
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
    @GetMapping("/semaforo/{idEstudiante}")
    public ResponseEntity<Map<String, Semaforo>> consultarSemaforoAcademico(@PathVariable String idEstudiante) {
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
    @PostMapping("/solicitudes")
    public ResponseEntity<Object> crearSolicitud(@Valid @RequestBody SolicitudDTO solicitudDTO) {
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
    @GetMapping("/solicitudes/{idEstudiante}")
    public ResponseEntity<List<Solicitud>> consultarSolicitudes(@PathVariable String idEstudiante) {
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
    @GetMapping("/solicitudes/{idEstudiante}/{solicitudId}")
    public ResponseEntity<Solicitud> consultarSolicitudesPorId(@PathVariable String idEstudiante, @PathVariable String solicitudId) {
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
    @PutMapping("/materias/{idEstudiante}/{acronimoMateria}/cancelar")
    public ResponseEntity<String> cancelarMateria(@PathVariable String idEstudiante, @PathVariable String acronimoMateria) {
        try {
            String resultado = estudianteService.cancelarMateria(idEstudiante, acronimoMateria);
            return ResponseEntity.ok(resultado);
        } catch (SirhaException e) {
            Log.logException(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Consulta todas las solicitudes que están en un estado específico.
     * Estados disponibles: PENDIENTE, EN_REVISION, APROBADA, RECHAZADA
     * @param estado Estado de las solicitudes a consultar.
     * @return Lista de solicitudes en el estado especificado.
     */
    @GetMapping("/solicitudes/estado/{estado}")
    public ResponseEntity<Map<String, Object>> consultarSolicitudesPorEstado(@PathVariable String estado) {
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
    @GetMapping("/solicitudes/{idEstudiante}/estado/{estado}")
    public ResponseEntity<Map<String, Object>> consultarSolicitudesEstudiantePorEstado(
        @PathVariable String idEstudiante, @PathVariable String estado) {
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
