package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.SolicitudCambio;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}