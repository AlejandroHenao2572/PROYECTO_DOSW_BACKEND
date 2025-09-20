package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.SemaforoAcademico;
import com.sirha.proyecto_sirha_dosw.model.SolicitudCambio;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes") 
@Tag(name = "Gestión de Estudiantes", description = "Operaciones relacionadas con los estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService){this.estudianteService = estudianteService;}

    @GetMapping("/{id}")
    @Operation(summary = "Consultar datos de un estudiante", description = "Obtiene los detalles de un estudiante")
    public ResponseEntity<Estudiante> getEstudiante(@PathVariable String id) {
        return estudianteService.getEstudianteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/solicitudes")
    @Operation(summary = "Crear una nueva solicitud de cambio", description = "Registra una nueva solicitud de cambio para el estudiante.")
    public ResponseEntity<SolicitudCambio> crearSolicitud(@RequestBody SolicitudCambio solicitud) {
        try {
            SolicitudCambio nuevaSolicitud = estudianteService.crearSolicitud(solicitud);
            return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{idEstudiante}/solicitudes")
    @Operation(summary = "Consultar historial de solicitudes", description = "Devuelve todas las solicitudes hechas por un estudiante")
    public ResponseEntity<List<SolicitudCambio>> getHistorialSolicitudes(@PathVariable String idEstudiante) {
        List<SolicitudCambio> historial = estudianteService.getHistorialSolicitudes(idEstudiante);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/{id}/horario")
    @Operation(summary = "Consultar horario actual", description = "Obtiene el horario del semestre actual del estudiante")
    public ResponseEntity<Horario> getHorarioActual(@PathVariable String id) {
        try {
            Horario horario = estudianteService.getHorarioActual(id);
            return horario != null ? ResponseEntity.ok(horario) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/horario/{semestre}")
    @Operation(summary = "Consultar horario de semestre específico", description = "Obtiene el horario de un semestre específico del estudiante")
    public ResponseEntity<Horario> getHorarioPorSemestre(@PathVariable String id, @PathVariable int semestre) {
        try {
            Horario horario = estudianteService.getHorarioPorSemestre(id, semestre);
            return horario != null ? ResponseEntity.ok(horario) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/semaforo-academico")
    @Operation(summary = "Consultar semáforo académico", description = "Obtiene el estado del semáforo académico del estudiante (avance en plan de estudios)")
    public ResponseEntity<Map<String, SemaforoAcademico>> getSemaforoAcademico(@PathVariable String id) {
        try {
            Map<String, SemaforoAcademico> semaforo = estudianteService.getSemaforoAcademico(id);
            return ResponseEntity.ok(semaforo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/solicitudes/{idSolicitud}")
    @Operation(summary = "Consultar estado de solicitud específica", description = "Obtiene los detalles y estado de una solicitud específica")
    public ResponseEntity<SolicitudCambio> getEstadoSolicitud(@PathVariable String idSolicitud) {
        try {
            return estudianteService.getSolicitudById(idSolicitud)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/solicitudes/{idSolicitud}")
    @Operation(summary = "Actualizar solicitud de cambio", description = "Actualiza una solicitud de cambio existente (solo si está en estado PENDIENTE)")
    public ResponseEntity<SolicitudCambio> actualizarSolicitud(
            @PathVariable String idSolicitud, 
            @RequestBody SolicitudCambio solicitudActualizada) {
        try {
            SolicitudCambio solicitudActualizadaResponse = estudianteService.actualizarSolicitud(idSolicitud, solicitudActualizada);
            return solicitudActualizadaResponse != null ? 
                    ResponseEntity.ok(solicitudActualizadaResponse) : 
                    ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint adicional para consultar materias pendientes
    @GetMapping("/{id}/materias-pendientes")
    @Operation(summary = "Consultar materias pendientes", description = "Obtiene las materias que el estudiante aún no ha cursado")
    public ResponseEntity<List<String>> getMateriasPendientes(@PathVariable String id) {
        try {
            List<String> materiasPendientes = estudianteService.getMateriasPendientes(id);
            return ResponseEntity.ok(materiasPendientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
}