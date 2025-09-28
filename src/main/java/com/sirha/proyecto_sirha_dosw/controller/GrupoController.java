package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestioanar grupos.
 * Permite crear, actualizar, eliminar y consultar grupos,
 *          así como asignar o remover estudiantes.
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    @Autowired
    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    /**
     * Obtiene todos los grupos registrados.
     * @return lista de grupos existentes.
     */
    @GetMapping
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        return ResponseEntity.ok(grupoService.getAllGrupos());
    }

    /**
     * Consulta un grupo específico por su ID.
     * @param id identificador único del grupo.
     * @return grupo encontrado o 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable String id) {
        Optional<Grupo> grupo = grupoService.getGrupoById(id);
        return grupo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo grupo a partir de un DTO.
     * @param grupoDTO objeto con los datos del grupo.
     * @return grupo creado con código HTTP 201.
     */
    @PostMapping
    public ResponseEntity<?> createGrupo(@Valid @RequestBody GrupoDTO grupoDTO) {
        try {
            // No need to validate materia/profesor objects since we're using IDs now
            Grupo createdGrupo = grupoService.createGrupo(grupoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGrupo);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_CREACION_GRUPO+e.getMessage());
        }
    }

    /**
     * Actualiza un grupo existente.
     * @param id identificador del grupo.
     * @param grupoDTO datos actualizados del grupo.
     * @return grupo actualizado o 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrupo(@PathVariable String id, @Valid @RequestBody GrupoDTO grupoDTO) {
        try {
            Grupo updatedGrupo = grupoService.updateGrupo(id, grupoDTO);
            return ResponseEntity.ok(updatedGrupo);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_ACTUALIZACION_GRUPO+e.getMessage());
        }
    }

    /**
     * Elimina un grupo por su ID.
     * @param id identificador del grupo.
     * @return código HTTP 204 si fue eliminado, 404 si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrupo(@PathVariable String id) {
        try {
            grupoService.deleteGrupo(id);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_ELIMINACION_GRUPO+e.getMessage());
        }
    }

    /**
     * Obtiene todos los grupos asociados a una materia.
     * @param materiaId identificador de la materia.
     * @return lista de grupos relacionados con la materia.
     */
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<Grupo>> getGruposByMateria(@PathVariable String materiaId) {
        List<Grupo> grupos = grupoService.getGruposByMateria(materiaId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene todos los grupos asociados a un profesor.
     * @param profesorId identificador del profesor.
     * @return lista de grupos asignados al profesor.
     */
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Grupo>> getGruposByProfesor(@PathVariable String profesorId) {
        List<Grupo> grupos = grupoService.getGruposByProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene los grupos que todavía tienen cupos disponibles.
     * @return lista de grupos disponibles.
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Grupo>> getGruposDisponibles() {
        List<Grupo> grupos = grupoService.getGruposDisponibles();
        return ResponseEntity.ok(grupos);
    }

    /**
     * Agrega un estudiante a un grupo.
     * @param grupoId identificador del grupo.
     * @param estudianteId identificador del estudiante.
     * @return grupo actualizado con el estudiante agregado.
     */
    @PostMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<?> addEstudianteToGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo updatedGrupo = grupoService.addEstudianteToGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        }catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_INSCRIPCION_ESTUDIANTE+e.getMessage());
        }
    }

    /**
     * Elimina un estudiante de un grupo.
     * @param grupoId identificador del grupo.
     * @param estudianteId identificador del estudiante.
     * @return grupo actualizado sin el estudiante.
     */
    @DeleteMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<?> removeEstudianteFromGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo updatedGrupo = grupoService.removeEstudianteFromGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_DESINSCRIPCION_ESTUDIANTE+e.getMessage());
        }
    }
}