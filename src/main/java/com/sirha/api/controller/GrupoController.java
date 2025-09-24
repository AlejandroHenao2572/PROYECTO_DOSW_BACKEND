package com.sirha.api.controller;

import com.sirha.api.dto.GrupoDTO;
import com.sirha.api.model.Grupo;
import com.sirha.api.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    @Autowired
    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    @GetMapping
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        return ResponseEntity.ok(grupoService.getAllGrupos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable String id) {
        Optional<Grupo> grupo = grupoService.getGrupoById(id);
        return grupo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Grupo> createGrupo(@Valid @RequestBody GrupoDTO grupoDTO) {
        try {
            // No need to validate materia/profesor objects since we're using IDs now
            Grupo createdGrupo = grupoService.createGrupo(grupoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGrupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> updateGrupo(@PathVariable String id, @Valid @RequestBody GrupoDTO grupoDTO) {
        try {
            Grupo updatedGrupo = grupoService.updateGrupo(id, grupoDTO);
            return ResponseEntity.ok(updatedGrupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupo(@PathVariable String id) {
        try {
            grupoService.deleteGrupo(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<Grupo>> getGruposByMateria(@PathVariable String materiaId) {
        List<Grupo> grupos = grupoService.getGruposByMateria(materiaId);
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Grupo>> getGruposByProfesor(@PathVariable String profesorId) {
        List<Grupo> grupos = grupoService.getGruposByProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Grupo>> getGruposDisponibles() {
        List<Grupo> grupos = grupoService.getGruposDisponibles();
        return ResponseEntity.ok(grupos);
    }

    @PostMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<Grupo> addEstudianteToGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo updatedGrupo = grupoService.addEstudianteToGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @DeleteMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<Grupo> removeEstudianteFromGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo updatedGrupo = grupoService.removeEstudianteFromGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}