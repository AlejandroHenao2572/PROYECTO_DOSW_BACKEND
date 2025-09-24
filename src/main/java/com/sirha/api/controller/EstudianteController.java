package com.sirha.api.controller;

import com.sirha.api.dto.SolicitudDTO;
import com.sirha.api.dto.UsuarioDTO;
import com.sirha.api.model.*;
import com.sirha.api.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

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
