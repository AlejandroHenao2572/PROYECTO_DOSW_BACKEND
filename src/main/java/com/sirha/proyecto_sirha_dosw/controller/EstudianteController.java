package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;
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
    @GetMapping("/horario/{idEstudiante}/{semestre}")
    public ResponseEntity<?> consultarHorarioPorSemestre(@PathVariable String idEstudiante, @PathVariable int semestre) {
        try {
            List<RegistroMaterias> registroMaterias = estudianteService.consultarHorarioBySemester(idEstudiante, semestre);
            if (registroMaterias.isEmpty()) {
                return new ResponseEntity<>("No se encontro el horario", HttpStatus.NO_CONTENT);
            }

            Map<String, List<Horario>> horariosPorMateria = new HashMap<>();
            for (RegistroMaterias registro : registroMaterias) {
                Grupo grupo = registro.getGrupo();
                if (grupo != null && grupo.getHorarios() != null && !grupo.getHorarios().isEmpty()) {
                    String nombreMateria = grupo.getMateria().getNombre();
                    List<Horario> horarios = grupo.getHorarios();
                    horariosPorMateria.put(nombreMateria, horarios);
                }
            }
            return ResponseEntity.ok(horariosPorMateria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * @param idEstudiante ID único del estudiante.
     * @return Mapa con el estado del semáforo académico (ej. verde, azul, rojo).
     */
    @GetMapping("/semaforo/{idEstudiante}")
    public ResponseEntity<?> consultarSemaforoAcademico(@PathVariable String idEstudiante) {
        try {
            Map<String, Semaforo> semaforo = estudianteService.consultarSemaforoAcademico(idEstudiante);
            if (!semaforo.isEmpty()) {
                return ResponseEntity.ok(estudianteService.consultarSemaforoAcademico(idEstudiante));
            } else {
                return new ResponseEntity<>("No se encontro el horario", HttpStatus.NO_CONTENT);
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Crea una nueva solicitud de cambio para el estudiante.
     * @param solicitudDTO Datos de la solicitud enviados en el cuerpo de la petición.
     * @return La solicitud creada si el proceso es exitoso.
     */
    @PostMapping("/solicitudes")
    public ResponseEntity<?> crearSolicitud(@Valid @RequestBody SolicitudDTO solicitudDTO) {
        try {
            Solicitud solicitudCreada = estudianteService.crearSolicitud(solicitudDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    /**
     * Consulta todas las solicitudes hechas por un estudiante.
     * @param idEstudiante ID único del estudiante.
     * @return Lista de solicitudes asociadas al estudiante.
     */
    @GetMapping("/solicitudes/{idEstudiante}")
    public ResponseEntity<?> consultarSolicitudes(@PathVariable String idEstudiante) {
        try {
            List<Solicitud> solicitudes = estudianteService.consultarSolicitudes(idEstudiante);
            if (!solicitudes.isEmpty()) {
                return ResponseEntity.ok(solicitudes);
            } else {
                return new ResponseEntity<>("No se encontraron Solicitudes", HttpStatus.NO_CONTENT);
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Consulta una solicitud específica de un estudiante por su ID.
     * @param idEstudiante ID del estudiante.
     * @param solicitudId ID de la solicitud.
     * @return La solicitud encontrada.
     */
    @GetMapping("/solicitudes/{idEstudiante}/{solicitudId}")
    public ResponseEntity<?> consultarSolicitudesPorId(@PathVariable String idEstudiante, @PathVariable String solicitudId) {
        try {
            Solicitud solicitud = estudianteService.consultarSolicitudesById(idEstudiante, solicitudId);
            return ResponseEntity.ok(solicitud);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}
