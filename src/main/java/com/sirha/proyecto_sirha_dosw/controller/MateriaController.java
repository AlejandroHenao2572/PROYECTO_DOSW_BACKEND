package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar materias académicas.
 * Permite crear y eliminar materias en el sistema SIRHA.
 */
@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "API para la gestión de materias académicas")
public class MateriaController {

    private final MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    /**
     * Crea una nueva materia en el sistema.
     * 
     * @param materiaDTO objeto con los datos de la materia a crear
     * @return materia creada con código HTTP 201 si es exitoso
     */
    @Operation(
        summary = "Crear una nueva materia",
        description = "Registra una nueva materia académica en el sistema con la información proporcionada. " +
                     "La materia debe tener un código único y cumplir con las validaciones establecidas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Materia creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Materia.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos o incompletos",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(
                    type = "string",
                    example = "Los datos proporcionados no cumplen con las validaciones requeridas"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto al crear la materia (ej: código ya existe)",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(
                    type = "string",
                    example = "Error en la creación de materia: El código de materia ya existe"
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<?> createMateria(
        @Parameter(
            description = "Datos de la materia a crear. Debe incluir código único, nombre, créditos y otros campos requeridos.",
            required = true,
            schema = @Schema(implementation = MateriaDTO.class)
        )
        @Valid @RequestBody MateriaDTO materiaDTO
    ) {
        try {
            Materia created = materiaService.createMateria(materiaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_CREACION_MATERIA+e.getMessage());
        }
    }

    /**
     * Elimina una materia específica del sistema.
     * 
     * @param id identificador único de la materia a eliminar
     * @return código HTTP 204 si fue eliminada exitosamente
     */
    @Operation(
        summary = "Eliminar materia",
        description = "Elimina permanentemente una materia del sistema usando su identificador único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Materia eliminada exitosamente",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia no encontrada",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(
                    type = "string",
                    example = "La materia con ID especificado no existe"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto al eliminar la materia (ej: tiene grupos asociados)",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(
                    type = "string",
                    example = "Error en la eliminación de materia: No se puede eliminar porque tiene grupos asociados"
                )
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMateria(
        @Parameter(
            description = "ID único de la materia a eliminar",
            required = true,
            example = "MAT001",
            schema = @Schema(type = "string", pattern = "^[A-Z]{3}[0-9]{3}$")
        )
        @PathVariable String id
    ) {
        try {
            materiaService.deleteMateria(id);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_ELIMINACION_MATERIA+e.getMessage());
        }
    }
}