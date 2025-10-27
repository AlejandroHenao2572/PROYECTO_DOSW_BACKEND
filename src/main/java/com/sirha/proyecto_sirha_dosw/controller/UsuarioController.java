package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones administrativas de usuarios.
 * 
 * <p>Este controlador maneja la administración de usuarios (CRUD) y consultas.
 * Para registro y autenticación, usar {@link AuthController}.</p>
 * 
 * <p>Endpoints disponibles bajo la ruta base {@code /api/usuarios}:</p>
 * <ul>
 *   <li>GET / - Listar todos los usuarios</li>
 *   <li>GET /{id} - Buscar usuario por ID</li>
 *   <li>GET /email/{email} - Buscar usuario por email</li>
 *   <li>GET /rol/{rol} - Buscar usuarios por rol</li>
 *   <li>GET /nombre/{nombre} - Buscar usuarios por nombre</li>
 *   <li>GET /apellido/{apellido} - Buscar usuarios por apellido</li>
 *   <li>GET /nombre/{nombre}/{apellido} - Buscar por nombre y apellido</li>
 *   <li>PUT /{usuarioId} - Actualizar usuario</li>
 *   <li>DELETE /{usuarioId} - Eliminar usuario</li>
 * </ul>
 * 
 * <p><b>Nota:</b> Para registro y login, usar {@code /api/auth/register} y {@code /api/auth/login}</p>
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Gestión de Usuarios", description = "API para la administración de usuarios del sistema SIRHA. " +
        "Incluye operaciones CRUD y consultas. Para autenticación y registro, usar el controlador de Autenticación (/api/auth).")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param usuarioId identificador único del usuario.
     * @param dto datos actualizados.
     * @return usuario actualizado o 404 si no existe.
     */
    @Operation(
        summary = "Actualizar usuario existente",
        description = "Actualiza la información de un usuario existente identificado por su ID. " +
                     "Se pueden modificar nombre, apellido, email y otros datos del perfil.",
        tags = {"Gestión de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario actualizado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario actualizado",
                    value = """
                    {
                        "id": "20221005001",
                        "nombre": "Juan Carlos",
                        "apellido": "Pérez García",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Error de validación",
                    value = "\"Los datos proporcionados no son válidos\""
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Usuario no existe",
                    value = "\"Error en la actualización del usuario: Usuario no encontrado\""
                )
            )
        )
    })
    @PutMapping("/{usuarioId}")
    public ResponseEntity<Usuario> updateUsuario(
        @Parameter(
            description = "ID único del usuario a actualizar",
            required = true,
            example = "20221005001"
        )
        @PathVariable String usuarioId,
        @Parameter(
            description = "Datos actualizados del usuario",
            required = true,
            schema = @Schema(implementation = UsuarioDTO.class)
        )
        @RequestBody UsuarioDTO dto
    ) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(usuarioId, dto);
            return ResponseEntity.ok(actualizado);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un usuario del sistema.
     * @param usuarioId identificador único del usuario.
     * @return 204 No Content si se eliminó, 404 si no existe.
     */
    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina permanentemente un usuario del sistema. Esta operación no se puede deshacer " +
                     "y puede fallar si el usuario tiene registros académicos asociados.",
        tags = {"Gestión de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuario eliminado exitosamente",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Usuario no existe",
                    value = "\"Error en la eliminación del usuario: Usuario no encontrado\""
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto - Usuario tiene registros asociados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Error de dependencias",
                    value = "\"No se puede eliminar el usuario porque tiene registros académicos asociados\""
                )
            )
        )
    })
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<String> deleteUsuario(
        @Parameter(
            description = "ID único del usuario a eliminar",
            required = true,
            example = "20221005001"
        )
        @PathVariable String usuarioId
    ) {
        try {
            usuarioService.eliminarUsuario(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(404).body(SirhaException.ERROR_ELIMINACION_USUARIO+e.getMessage());
        }
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return lista de {@link Usuario}
     */
    @Operation(
        summary = "Listar todos los usuarios",
        description = "Obtiene una lista completa de todos los usuarios registrados en el sistema SIRHA, " +
                     "incluyendo estudiantes, profesores, decanos y administradores.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Lista de usuarios",
                    value = """
                    [
                        {
                            "id": "20221005001",
                            "nombre": "Juan",
                            "apellido": "Pérez",
                            "email": "juan.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        },
                        {
                            "id": "PROF001",
                            "nombre": "María",
                            "apellido": "García",
                            "email": "maria.garcia@profesores.edu.co",
                            "rol": "PROFESOR"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No hay usuarios registrados en el sistema",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Sin usuarios",
                    value = "\"No se encontraron usuarios en el sistema\""
                )
            )
        )
    })
    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (!usuarios.isEmpty()) {
                return ResponseEntity.ok(usuarioService.listarUsuarios());
            } else {
                return new ResponseEntity<>(List.of(), HttpStatus.NO_CONTENT);
            }
    }

    /**
     * Busca un usuario por su identificador único.
     * @param id identificador del usuario
     * @return {@link Usuario} si existe, 404 si no
     */
    @Operation(
        summary = "Buscar usuario por ID",
        description = "Busca y retorna un usuario específico usando su identificador único del sistema.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario encontrado",
                    value = """
                    {
                        "id": "20221005001",
                        "nombre": "Juan",
                        "apellido": "Pérez",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE",
                        "facultad": "INGENIERIA"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(
        @Parameter(
            description = "ID único del usuario en el sistema",
            required = true,
            example = "20221005001"
        )
        @PathVariable String id
    ) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @param email correo electrónico
     * @return {@link Usuario} si existe, 404 si no
     */
    @Operation(
        summary = "Buscar usuario por email",
        description = "Busca y retorna un usuario específico usando su dirección de correo electrónico.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario por email",
                    value = """
                    {
                        "id": "20221005001",
                        "nombre": "Juan",
                        "apellido": "Pérez",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado con el email especificado",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(
        @Parameter(
            description = "Dirección de correo electrónico del usuario",
            required = true,
            example = "juan.perez@estudiantes.edu.co"
        )
        @PathVariable String email
    ) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene el usuario autenticado actualmente (self).
     * Este endpoint permite a cualquier usuario autenticado (ESTUDIANTE, DECANO, ADMINISTRADOR)
     * obtener su propia información sin necesidad de conocer su email.
     * 
     * @return {@link Usuario} del usuario autenticado
     */
    @Operation(
        summary = "Obtener información del usuario autenticado",
        description = "Obtiene la información del usuario que está actualmente autenticado. " +
                     "No requiere parámetros ya que usa el token JWT para identificar al usuario.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Información del usuario autenticado obtenida exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario autenticado",
                    value = """
                    {
                        "id": "20221005001",
                        "nombre": "Juan",
                        "apellido": "Pérez",
                        "email": "juan.perez@estudiantes.edu.co",
                        "rol": "ESTUDIANTE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado - Token JWT inválido o faltante",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario autenticado no encontrado en la base de datos",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/email/self")
    public ResponseEntity<Usuario> obtenerUsuarioAutenticado() {
        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // El email está almacenado en el principal (username)
        String email = authentication.getName();
        
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca todos los usuarios que tengan el rol indicado.
     * @param rol nombre del rol (ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR)
     * @return lista de usuarios, 204 si no hay ninguno, 400 si el rol es inválido
     */
    @Operation(
        summary = "Buscar usuarios por rol",
        description = "Obtiene una lista de todos los usuarios que tienen un rol específico en el sistema. " +
                     "Los roles válidos son: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios con el rol especificado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuarios por rol",
                    value = """
                    [
                        {
                            "id": "20221005001",
                            "nombre": "Juan",
                            "apellido": "Pérez",
                            "email": "juan.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        },
                        {
                            "id": "20221005002",
                            "nombre": "Ana",
                            "apellido": "González",
                            "email": "ana.gonzalez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No se encontraron usuarios con el rol especificado",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Rol inválido",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    name = "Rol inválido",
                    value = "\"El rol especificado no es válido. Roles válidos: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR\""
                )
            )
        )
    })
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(
        @Parameter(
            description = "Nombre del rol a buscar. Valores válidos: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR",
            required = true,
            example = "ESTUDIANTE"
        )
        @PathVariable String rol
    ) {
        try {
            Rol rolEnum = Rol.valueOf(rol.toUpperCase());
            List<Usuario> usuarios = usuarioService.obtenerPorRol(rolEnum);

            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);

        } catch (IllegalArgumentException e) {
            Log.logException(e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Busca usuarios por su nombre.
     * @param nombre nombre del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Busca todos los usuarios que coincidan con el nombre especificado. " +
                     "La búsqueda puede retornar múltiples usuarios si comparten el mismo nombre.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios encontrados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuarios por nombre",
                    value = """
                    [
                        {
                            "id": "20221005001",
                            "nombre": "Juan",
                            "apellido": "Pérez",
                            "email": "juan.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        },
                        {
                            "id": "PROF001",
                            "nombre": "Juan",
                            "apellido": "García",
                            "email": "juan.garcia@profesores.edu.co",
                            "rol": "PROFESOR"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron usuarios con el nombre especificado",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> obtenerPorNombre(
        @Parameter(
            description = "Nombre del usuario a buscar",
            required = true,
            example = "Juan"
        )
        @PathVariable String nombre
    ) {
        List<Usuario> usuarios = usuarioService.obtenerPorNombre(nombre);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca usuarios por su apellido.
     * @param apellido apellido del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @Operation(
        summary = "Buscar usuarios por apellido",
        description = "Busca todos los usuarios que coincidan con el apellido especificado. " +
                     "La búsqueda puede retornar múltiples usuarios si comparten el mismo apellido.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios encontrados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuarios por apellido",
                    value = """
                    [
                        {
                            "id": "20221005001",
                            "nombre": "Juan",
                            "apellido": "Pérez",
                            "email": "juan.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        },
                        {
                            "id": "20221005003",
                            "nombre": "María",
                            "apellido": "Pérez",
                            "email": "maria.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron usuarios con el apellido especificado",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorApellido(
        @Parameter(
            description = "Apellido del usuario a buscar",
            required = true,
            example = "Pérez"
        )
        @PathVariable String apellido
    ) {
        List<Usuario> usuarios = usuarioService.obtenerPorApellido(apellido);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca usuarios por su nombre y apellido.
     * @param nombre   nombre del usuario
     * @param apellido apellido del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @Operation(
        summary = "Buscar usuarios por nombre y apellido",
        description = "Busca todos los usuarios que coincidan exactamente con el nombre y apellido especificados. " +
                     "Esta búsqueda es más específica que buscar solo por nombre o apellido por separado.",
        tags = {"Consulta de Usuarios"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios encontrados",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuarios por nombre y apellido",
                    value = """
                    [
                        {
                            "id": "20221005001",
                            "nombre": "Juan",
                            "apellido": "Pérez",
                            "email": "juan.perez@estudiantes.edu.co",
                            "rol": "ESTUDIANTE"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No se encontraron usuarios con el nombre y apellido especificados",
            content = @Content(
                schema = @Schema(hidden = true)
            )
        )
    })
    @GetMapping("/nombre/{nombre}/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorNombreYApellido(
        @Parameter(
            description = "Nombre del usuario a buscar",
            required = true,
            example = "Juan"
        )
        @PathVariable String nombre,
        @Parameter(
            description = "Apellido del usuario a buscar",
            required = true,
            example = "Pérez"
        )
        @PathVariable String apellido
    ) {
        List<Usuario> usuarios = usuarioService.obtenerPorNombreYApellido(nombre, apellido);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }
}