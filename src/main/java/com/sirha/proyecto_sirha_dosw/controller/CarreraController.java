package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.CarreraDTO;
import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.CarreraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las carreras y sus materias.
 * Expone Endpoints para registrar nuevas carreras y asociar materias a ellas.
 */
@RestController
@RequestMapping("/api/carreras")
@Tag(name = "Carreras", description = "API para gestión de carreras académicas y sus materias")
@SecurityRequirement(name = "bearerAuth")
public class CarreraController {

    private final CarreraService carreraService;

    /**
     * Constructor con inyección de dependencias de CarreraService.
     * @param carreraService servicio que maneja la lógica de negocio para carreras.
     */
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    /**
     * Registra una nueva carrera en el sistema.
     * @param dto objeto DTO con la información de la carrera a registrar.
     * @return ResponseEntity con estado 201 (CREATED) si se crea exitosamente,
     *          o 409 (CONFLICT) si ocurre algún error de validación.
     */
    @Operation(
        summary = "Registrar nueva carrera",
        description = "Crea una nueva carrera académica en el sistema. Solo usuarios con rol ADMINISTRADOR o DECANO pueden realizar esta operación.",
        tags = {"Carreras"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Carrera creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class),
                examples = @ExampleObject(
                    name = "Éxito",
                    value = "Carrera registrada exitosamente",
                    description = "Mensaje de confirmación de registro"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = "Error de validación: El nombre de la carrera es obligatorio",
                    description = "Error cuando faltan campos obligatorios"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado - Token JWT requerido",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "No autenticado",
                    value = "{ \"error\": \"Unauthorized\", \"message\": \"Token JWT requerido\" }",
                    description = "Error cuando no se proporciona token de autenticación"
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Permisos insuficientes",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Sin permisos",
                    value = "{ \"error\": \"Forbidden\", \"message\": \"Acceso denegado\" }",
                    description = "Error cuando el usuario no tiene permisos de ADMINISTRADOR o DECANO"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto - Carrera ya existe",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Carrera duplicada",
                    value = "Error al crear carrera: Ya existe una carrera con ese código",
                    description = "Error cuando se intenta crear una carrera que ya existe"
                )
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(
        @Parameter(
            description = "Datos de la carrera a registrar",
            required = true,
            example = "{\"nombre\": \"Ingeniería de Sistemas\", \"codigo\": \"ISIS\", \"duracionSemestres\": 8, \"creditosTotales\": 145 }"
        )
        @Valid @RequestBody CarreraDTO dto
    ) {
        try {
            carreraService.registrar(dto);
            return new ResponseEntity<>("Carrera registrada exitosamente", HttpStatus.CREATED);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_CREACION_CARRERA + e.getMessage());
        }
    }

    /**
     * Asocia una nueva materia a una carrera existente.
     * @param codigoCarrera código único de la carrera a la que se le agregará la materia.
     * @param dto objeto DTO con la información de la materia a agregar.
     * @return ResponseEntity con la materia actualizada o un mensaje de error.
     */

    @Operation(
        summary = "Agregar nueva materia a carrera",
        description = "Crea una nueva materia y la asocia a una carrera específica. Solo usuarios con rol ADMINISTRADOR o DECANO pueden realizar esta operación.",
        tags = {"Carreras", "Materias"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Materia creada y agregada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Materia.class),
                examples = @ExampleObject(
                    name = "Éxito",
                    value = "{ \"nombre\": \"Cálculo Diferencial\", \"codigo\": \"CALD\", \"creditos\": 3, \"facultad\": \"INGENIERIA_SISTEMAS\"}",
                    description = "Materia creada y agregada"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado - Token JWT requerido"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Permisos insuficientes"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrera no encontrada"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto - Error al asociar materia",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error asociación",
                    value = "Error al asociar materia: La materia ya existe en la carrera",
                    description = "Error cuando se intenta agregar una materia duplicada"
                )
            )
        )
    })
    @PostMapping("/materia/{codigoCarrera}")
    public ResponseEntity<Materia> addMateria(
        @Parameter(
            description = "Código único de la carrera",
            required = true,
            example = "ING_SIS"
        )
        @PathVariable String codigoCarrera, 
        
        @Parameter(
            description = "Datos de la materia a crear y agregar",
            required = true,
            example = "{ \"codigo\": \"MAT_001\", \"nombre\": \"Cálculo Diferencial\", \"creditos\": 3, \"semestre\": 1, \"prerequisitos\": [] }"
        )
        @Valid @RequestBody MateriaDTO dto
    ) {
        try {
            Materia actualizado = carreraService.addMateria(dto,codigoCarrera);
            return ResponseEntity.ok(actualizado);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(null);
        }
    }

    /**
     * Asocia una materia existente a una carrera específica.
     * @param codigoCarrera código de la carrera a la cual asociar la materia.
     * @param codigoMateria código de la materia existente a asociar.
     * @return ResponseEntity con la carrera actualizada o un mensaje de error.
     */
    @Operation(
        summary = "Asociar materia existente a carrera",
        description = "Asocia una materia ya existente en el sistema a una carrera específica. Solo usuarios con rol ADMINISTRADOR o DECANO pueden realizar esta operación.",
        tags = {"Carreras", "Materias"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Materia asociada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Carrera.class),
                examples = @ExampleObject(
                    name = "Éxito",
                    value = "{ \"codigo\": \"ING_SIS\", \"nombre\": \"Ingeniería de Sistemas\", \"materias\": [...] }",
                    description = "Carrera con materia asociada"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado - Token JWT requerido"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - Permisos insuficientes"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Carrera o materia no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Recurso no encontrado",
                    value = "Error al asociar materia: Carrera o materia no encontrada",
                    description = "Error cuando el código de carrera o materia no existe"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto - Error al asociar materia",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error asociación",
                    value = "Error al asociar materia: La materia ya está asociada a la carrera",
                    description = "Error cuando la materia ya está asociada"
                )
            )
        )
    })
    @PostMapping("/materia/{codigoCarrera}/{codigoMateria}")
    public ResponseEntity<Carrera> addMateria(
        @Parameter(
            description = "Código único de la carrera",
            required = true,
            example = "ING_SIS"
        )
        @PathVariable String codigoCarrera, 
        
        @Parameter(
            description = "Código único de la materia existente",
            required = true,
            example = "MAT_001"
        )
        @PathVariable String codigoMateria
    ) {
        try {
            Carrera actualizado = carreraService.addMateriaById(codigoCarrera,codigoMateria);
            return ResponseEntity.ok(actualizado);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(null);
        }
    }
}
