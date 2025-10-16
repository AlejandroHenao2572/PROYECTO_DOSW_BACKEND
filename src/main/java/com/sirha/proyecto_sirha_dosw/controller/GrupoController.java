package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.CapacidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.service.GrupoService;
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

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar grupos.
 * Permite crear, actualizar, eliminar y consultar grupos,
 * así como asignar o remover estudiantes.
 */
@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "API para la gestión de grupos académicos")
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
    @Operation(
        summary = "Obtener todos los grupos",
        description = "Retorna una lista completa de todos los grupos registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        return ResponseEntity.ok(grupoService.getAllGrupos());
    }

    /**
     * Consulta un grupo específico por su ID.
     * @param id identificador único del grupo.
     * @return grupo encontrado o 404 si no existe.
     */
    @Operation(
        summary = "Obtener grupo por ID",
        description = "Busca y retorna un grupo específico usando su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Grupo encontrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupoById(
        @Parameter(description = "ID único del grupo", required = true, example = "GRP001")
        @PathVariable String id
    ) {
        Optional<Grupo> grupo = grupoService.getGrupoById(id);
        return grupo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo grupo a partir de un DTO.
     * @param grupoDTO objeto con los datos del grupo.
     * @return grupo creado con código HTTP 201.
     */
    @Operation(
        summary = "Crear un nuevo grupo",
        description = "Crea un nuevo grupo académico con la información proporcionada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Grupo creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error en la creación del grupo",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @PostMapping
    public ResponseEntity<Grupo> createGrupo(
        @Parameter(description = "Datos del grupo a crear", required = true)
        @Valid @RequestBody GrupoDTO grupoDTO
    ) {
        try {
            Grupo createdGrupo = grupoService.createGrupo(grupoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGrupo);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Actualiza un grupo existente.
     * @param id identificador del grupo.
     * @param grupoDTO datos actualizados del grupo.
     * @return grupo actualizado o 404 si no existe.
     */
    @Operation(
        summary = "Actualizar grupo existente",
        description = "Actualiza la información de un grupo existente identificado por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Grupo actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error en la actualización del grupo",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Grupo> updateGrupo(
        @Parameter(description = "ID único del grupo a actualizar", required = true, example = "GRP001")
        @PathVariable String id,
        @Parameter(description = "Datos actualizados del grupo", required = true)
        @Valid @RequestBody GrupoDTO grupoDTO
    ) {
        try {
            Grupo updatedGrupo = grupoService.updateGrupo(id, grupoDTO);
            return ResponseEntity.ok(updatedGrupo);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Elimina un grupo por su ID.
     * @param id identificador del grupo.
     * @return código HTTP 204 si fue eliminado, 404 si no existe.
     */
    @Operation(
        summary = "Eliminar grupo",
        description = "Elimina permanentemente un grupo del sistema usando su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Grupo eliminado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error en la eliminación del grupo",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrupo(
        @Parameter(description = "ID único del grupo a eliminar", required = true, example = "GRP001")
        @PathVariable String id
    ) {
        try {
            grupoService.deleteGrupo(id);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_ELIMINACION_GRUPO+e.getMessage());
        }
    }

    /**
     * Obtiene todos los grupos asociados a una materia.
     * @param materiaId identificador de la materia.
     * @return lista de grupos relacionados con la materia.
     */
    @Operation(
        summary = "Obtener grupos por materia",
        description = "Retorna todos los grupos asociados a una materia específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        )
    })
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<Grupo>> getGruposByMateria(
        @Parameter(description = "ID de la materia", required = true, example = "MAT001")
        @PathVariable String materiaId
    ) {
        List<Grupo> grupos = grupoService.getGruposByMateria(materiaId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene todos los grupos asociados a un profesor.
     * @param profesorId identificador del profesor.
     * @return lista de grupos asignados al profesor.
     */
    @Operation(
        summary = "Obtener grupos por profesor",
        description = "Retorna todos los grupos asignados a un profesor específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        )
    })
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<Grupo>> getGruposByProfesor(
        @Parameter(description = "ID del profesor", required = true, example = "PROF001")
        @PathVariable String profesorId
    ) {
        List<Grupo> grupos = grupoService.getGruposByProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene los grupos que todavía tienen cupos disponibles.
     * @return lista de grupos disponibles.
     */
    @Operation(
        summary = "Obtener grupos disponibles",
        description = "Retorna todos los grupos que aún tienen cupos disponibles para estudiantes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos disponibles obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        )
    })
    @GetMapping("/disponibles")
    public ResponseEntity<List<Grupo>> getGruposDisponibles() {
        List<Grupo> grupos = grupoService.getGruposDisponibles();
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene los grupos disponibles (con cupos) de una materia específica.
     * @param materiaId identificador de la materia.
     * @return lista de grupos disponibles de la materia especificada.
     */
    @Operation(
        summary = "Obtener grupos disponibles por materia",
        description = "Retorna los grupos con cupos disponibles de una materia específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos disponibles obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        )
    })
    @GetMapping("/materia/{materiaId}/disponibles")
    public ResponseEntity<List<Grupo>> getGruposDisponiblesPorMateria(
        @Parameter(description = "ID de la materia", required = true, example = "MAT001")
        @PathVariable String materiaId
    ) {
        List<Grupo> grupos = grupoService.getGruposDisponiblesPorMateria(materiaId);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Agrega un estudiante a un grupo.
     * @param grupoId identificador del grupo.
     * @param estudianteId identificador del estudiante.
     * @return grupo actualizado con el estudiante agregado.
     */
    @Operation(
        summary = "Agregar estudiante a grupo",
        description = "Inscribe un estudiante en un grupo específico si hay cupos disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante agregado exitosamente al grupo",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo o estudiante no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error en la inscripción del estudiante",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @PostMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<Grupo> addEstudianteToGrupo(
        @Parameter(description = "ID del grupo", required = true, example = "GRP001")
        @PathVariable String grupoId,
        @Parameter(description = "ID del estudiante", required = true, example = "EST001")
        @PathVariable String estudianteId
    ) {
        try {
            Grupo updatedGrupo = grupoService.addEstudianteToGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        }catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Elimina un estudiante de un grupo.
     * @param grupoId identificador del grupo.
     * @param estudianteId identificador del estudiante.
     * @return grupo actualizado sin el estudiante.
     */
    @Operation(
        summary = "Remover estudiante de grupo",
        description = "Desinscribe un estudiante de un grupo específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante removido exitosamente del grupo",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo o estudiante no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Error en la desinscripción del estudiante",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @DeleteMapping("/{grupoId}/estudiantes/{estudianteId}")
    public ResponseEntity<Grupo> removeEstudianteFromGrupo(
        @Parameter(description = "ID del grupo", required = true, example = "GRP001")
        @PathVariable String grupoId,
        @Parameter(description = "ID del estudiante", required = true, example = "EST001")
        @PathVariable String estudianteId
    ) {
        try {
            Grupo updatedGrupo = grupoService.removeEstudianteFromGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(updatedGrupo);
        } catch (SirhaException e) {
            Log.logException(e);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
    }

    /**
     * Consulta la capacidad de un grupo específico.
     * @param id identificador del grupo.
     * @return información de capacidad del grupo.
     */
    @Operation(
        summary = "Consultar capacidad de grupo",
        description = "Obtiene información detallada sobre la capacidad y ocupación de un grupo específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Información de capacidad obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapacidadGrupoDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @GetMapping("/{id}/capacidad")
    public ResponseEntity<CapacidadGrupoDTO> consultarCapacidadGrupo(
        @Parameter(description = "ID único del grupo", required = true, example = "GRP001")
        @PathVariable String id
    ) {
        try {
            CapacidadGrupoDTO capacidad = grupoService.obtenerCapacidadGrupo(id);
            return ResponseEntity.ok(capacidad);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Consulta la capacidad de todos los grupos.
     * @return lista con información de capacidad de todos los grupos.
     */
    @Operation(
        summary = "Consultar capacidad de todos los grupos",
        description = "Obtiene información de capacidad y ocupación de todos los grupos del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de capacidades obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapacidadGrupoDTO.class)
            )
        )
    })
    @GetMapping("/capacidad")
    public ResponseEntity<List<CapacidadGrupoDTO>> consultarCapacidadTodosLosGrupos() {
        List<CapacidadGrupoDTO> capacidades = grupoService.obtenerCapacidadTodosLosGrupos();
        return ResponseEntity.ok(capacidades);
    }

    /**
     * Consulta la capacidad de grupos por materia.
     * @param materiaId identificador de la materia.
     * @return lista con información de capacidad de grupos de la materia.
     */
    @Operation(
        summary = "Consultar capacidad de grupos por materia",
        description = "Obtiene información de capacidad y ocupación de todos los grupos de una materia específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de capacidades por materia obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapacidadGrupoDTO.class)
            )
        )
    })
    @GetMapping("/materia/{materiaId}/capacidad")
    public ResponseEntity<List<CapacidadGrupoDTO>> consultarCapacidadGruposPorMateria(
        @Parameter(description = "ID de la materia", required = true, example = "MAT001")
        @PathVariable String materiaId
    ) {
        List<CapacidadGrupoDTO> capacidades = grupoService.obtenerCapacidadGruposPorMateria(materiaId);
        return ResponseEntity.ok(capacidades);
    }

    /**
     * Asigna un profesor a un grupo.
     * @param grupoId identificador del grupo.
     * @param profesorId identificador del profesor.
     * @return grupo actualizado con profesor asignado.
     */
    @Operation(
        summary = "Asignar profesor a grupo",
        description = "Asigna un profesor específico como responsable de un grupo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profesor asignado exitosamente al grupo",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error al asignar profesor",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo o profesor no encontrado",
            content = @Content
        )
    })
    @PutMapping("/{grupoId}/profesor/{profesorId}")
    public ResponseEntity<Grupo> asignarProfesorAGrupo(
        @Parameter(description = "ID del grupo", required = true, example = "GRP001")
        @PathVariable String grupoId,
        @Parameter(description = "ID del profesor", required = true, example = "PROF001")
        @PathVariable String profesorId
    ) {
        try {
            Grupo grupoActualizado = grupoService.asignarProfesorAGrupo(grupoId, profesorId);
            return ResponseEntity.ok(grupoActualizado);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remueve la asignación de profesor de un grupo.
     * @param grupoId identificador del grupo.
     * @return grupo actualizado sin profesor asignado.
     */
    @Operation(
        summary = "Remover profesor de grupo",
        description = "Remueve la asignación del profesor de un grupo específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profesor removido exitosamente del grupo",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Grupo.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string")
            )
        )
    })
    @DeleteMapping("/{grupoId}/profesor")
    public ResponseEntity<Grupo> removerProfesorDeGrupo(
        @Parameter(description = "ID del grupo", required = true, example = "GRP001")
        @PathVariable String grupoId
    ) {
        try {
            Grupo grupoActualizado = grupoService.removerProfesorDeGrupo(grupoId);
            return ResponseEntity.ok(grupoActualizado);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Consulta grupos asignados a un profesor con información de capacidad.
     * @param profesorId identificador del profesor.
     * @return lista de grupos con información de capacidad del profesor.
     */
    @Operation(
        summary = "Consultar grupos con capacidad por profesor",
        description = "Obtiene información detallada de capacidad de todos los grupos asignados a un profesor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de grupos con capacidad obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CapacidadGrupoDTO.class)
            )
        )
    })
    @GetMapping("/profesor/{profesorId}/capacidad")
    public ResponseEntity<List<CapacidadGrupoDTO>> consultarGruposConCapacidadPorProfesor(
        @Parameter(description = "ID del profesor", required = true, example = "PROF001")
        @PathVariable String profesorId
    ) {
        List<CapacidadGrupoDTO> grupos = grupoService.obtenerGruposConCapacidadPorProfesor(profesorId);
        return ResponseEntity.ok(grupos);
    }
}