package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.CalendarioAcademicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.DisponibilidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.MonitoreoGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.PlazoSolicitudesDTO;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.DecanoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/decano")
@Tag(name = "Decano Controller", description = "API para la gestión de funcionalidades específicas del Decano. " +
        "Incluye gestión de estudiantes, solicitudes de cambio de grupo, configuración de calendario académico, " +
        "monitoreo de grupos y administración de plazos de solicitudes por facultad.")
public class DecanoController {

    private static final String ERROR_INTERNO = "Error interno del servidor";
    private static final String FACULTDAD = "Facultad";
    private static final String MENSAJE = "mensaje";
    private static final String FECHA_INICIO = "fechaInicio";
    private static final String FECHA_FIN = "fechaFin";
    private static final String CANTIDAD = "cantidad";
    private static final String PORCENTAJE = "porcentaje";
    private final DecanoService decanoService;

    public DecanoController(DecanoService decanoService) {
        this.decanoService = decanoService;
    }

    @Operation(
        summary = "Listar estudiantes por facultad",
        description = "Obtiene una lista de todos los estudiantes que pertenecen a una facultad específica.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de estudiantes obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class),
            examples = @ExampleObject(
                name = "Lista de estudiantes",
                description = "Ejemplo de respuesta exitosa con lista de estudiantes",
                value = """
                [
                    {
                        "id": "20221005001",
                        "nombre": "Juan",
                        "apellido": "Pérez",
                        "email": "juan.perez@estudiantes.edu.co",
                        "tipoUsuario": "ESTUDIANTE"
                    }
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error facultad inválida",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error interno",
                value = "\"Error interno del servidor\""
            )
        )
    )
    @GetMapping("/{facultad}/")
    public ResponseEntity<Object> listarUsuarios(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad.",
                required = true
            )
            @PathVariable String facultad) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            List<Usuario> estudiantes = decanoService.findEstudiantesByFacultad(facultad);
            return new ResponseEntity<>(estudiantes, HttpStatus.OK);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        summary = "Obtener estudiante por ID y facultad",
        description = "Obtiene un estudiante específico por su ID, validando que pertenezca a la facultad indicada.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estudiante encontrado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class),
            examples = @ExampleObject(
                name = "Estudiante encontrado",
                value = """
                {
                    "id": "20221005001",
                    "nombre": "Juan",
                    "apellido": "Pérez",
                    "email": "juan.perez@estudiantes.edu.co",
                    "tipoUsuario": "ESTUDIANTE"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida o estudiante no pertenece a la facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"El estudiante no pertenece a la facultad especificada\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/{id}")
    public ResponseEntity<Object> obtenerPorId(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "id",
                description = "Código único del estudiante",
                required = true,
                example = "20221005001"
            )
            @PathVariable String id) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            // Validar que el estudiante existe y pertenece a la facultad
            decanoService.validarEstudianteFacultad(id, facultad);
            
            Usuario estudiante = decanoService.findEstudianteByIdAndFacultad(id, facultad);
            return new ResponseEntity<>(estudiante, HttpStatus.OK);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        summary = "Buscar estudiante por email y facultad",
        description = "Busca un estudiante específico por su dirección de email, validando que pertenezca a la facultad indicada.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estudiante encontrado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Email vacío o facultad inválida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Email vacío",
                value = "\"Email no puede estar vacío\""
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Estudiante no encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Estudiante no encontrado",
                value = "\"Estudiante no encontrado con email: ejemplo@email.com\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/email/{email}")
    public ResponseEntity<Object> obtenerPorEmail(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "email",
                description = "Dirección de correo electrónico del estudiante",
                required = true,
                example = "juan.perez@estudiantes.edu.co"
            )
            @PathVariable String email) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            if (email == null || email.trim().isEmpty()) {
                return new ResponseEntity<>("Email no puede estar vacío", HttpStatus.BAD_REQUEST);
            }
            
            Usuario estudiante = decanoService.findEstudianteByEmailAndFacultad(email, facultad);
            if (estudiante != null) {
                return new ResponseEntity<>(estudiante, HttpStatus.OK);
            }
            return new ResponseEntity<>("Estudiante no encontrado con email: " + email, HttpStatus.NOT_FOUND);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
        summary = "Buscar estudiantes por nombre y facultad",
        description = "Busca estudiantes que coincidan con el nombre especificado dentro de una facultad específica.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de estudiantes encontrados",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class)
        )
    )
    @GetMapping("/{facultad}/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> obtenerPorNombre(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "nombre",
                description = "Nombre del estudiante a buscar",
                required = true,
                example = "Juan"
            )
            @PathVariable String nombre) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByNombreAndFacultad(nombre, facultad);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @Operation(
        summary = "Buscar estudiantes por apellido y facultad",
        description = "Busca estudiantes que coincidan con el apellido especificado dentro de una facultad específica.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de estudiantes encontrados",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class)
        )
    )
    @GetMapping("/{facultad}/apellido/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorApellido(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "apellido",
                description = "Apellido del estudiante a buscar",
                required = true,
                example = "Pérez"
            )
            @PathVariable String apellido) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByApellidoAndFacultad(apellido, facultad);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @Operation(
        summary = "Buscar estudiantes por nombre, apellido y facultad",
        description = "Busca estudiantes que coincidan con el nombre y apellido especificados dentro de una facultad específica.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de estudiantes encontrados",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Usuario.class)
        )
    )
    @GetMapping("/{facultad}/nombre/{nombre}/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorNombreYApellido(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "nombre",
                description = "Nombre del estudiante a buscar",
                required = true,
                example = "Juan"
            )
            @PathVariable String nombre,
            @Parameter(
                name = "apellido",
                description = "Apellido del estudiante a buscar",
                required = true,
                example = "Pérez"
            )
            @PathVariable String apellido) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByNombreApellidoAndFacultad(nombre, apellido, facultad);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    /**
     * Consulta todas las solicitudes recibidas en el área de una facultad específica.
     * @param facultad nombre de la facultad 
     * @return lista de solicitudes asociadas a la facultad
     */
    @Operation(
        summary = "Consultar todas las solicitudes por facultad",
        description = "Obtiene una lista de todas las solicitudes de cambio de grupo recibidas en una facultad específica, independientemente de su estado.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de solicitudes obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class),
            examples = @ExampleObject(
                name = "Lista de solicitudes",
                value = """
                [
                    {
                        "id": "SOL-001",
                        "estudianteId": "20221005001",
                        "grupoActualId": "GRP-001",
                        "grupoDeseadoId": "GRP-002",
                        "motivo": "Conflicto de horario laboral",
                        "estado": "PENDIENTE",
                        "fechaSolicitud": "2024-03-15T10:30:00"
                    }
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error facultad",
                value = "400"
            )
        )
    )
    @GetMapping("/{facultad}/solicitudes")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPorFacultad(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad.",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            List<Solicitud> solicitudes = decanoService.consultarSolicitudesPorFacultad(facultadEnum);
            return new ResponseEntity<>(solicitudes, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Consulta las solicitudes pendientes recibidas en el área de una facultad específica.
     * @param facultad nombre de la facultad 
     * @return lista de solicitudes pendientes asociadas a la facultad
     */
    @Operation(
        summary = "Consultar solicitudes pendientes por facultad",
        description = "Obtiene una lista de las solicitudes de cambio de grupo que están pendientes de revisión en una facultad específica.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de solicitudes pendientes obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida"
    )
    @GetMapping("/{facultad}/solicitudes/pendientes")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPendientesPorFacultad(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            List<Solicitud> solicitudes = decanoService.consultarSolicitudesPendientesPorFacultad(facultadEnum);
            return new ResponseEntity<>(solicitudes, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Consulta las solicitudes por facultad filtradas por estado específico.
     * @param facultad nombre de la facultad 
     * @param estado estado de la solicitud 
     * @return lista de solicitudes filtradas por facultad y estado
     */
    @Operation(
        summary = "Consultar solicitudes por facultad y estado",
        description = "Obtiene una lista de solicitudes de cambio de grupo filtradas por facultad y estado específico (PENDIENTE, APROBADA, RECHAZADA).",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de solicitudes filtradas obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad o estado inválido"
    )
    @GetMapping("/{facultad}/solicitudes/estado/{estado}")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPorFacultadYEstado(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "estado",
                description = "Estado de la solicitud. Valores válidos: PENDIENTE, APROBADA, RECHAZADA",
                required = true,
                example = "PENDIENTE"
            )
            @PathVariable String estado) {
        try {
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            SolicitudEstado estadoEnum = SolicitudEstado.valueOf(estado.toUpperCase());
            List<Solicitud> solicitudes = decanoService.consultarSolicitudesPorFacultadYEstado(facultadEnum, estadoEnum);
            return new ResponseEntity<>(solicitudes, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * Permite al decano visualizar el horario del estudiante que solicita cambio.
     * @param facultad nombre de la facultad del decano
     * @param idEstudiante ID del estudiante
     * @param semestre número de semestre a consultar
     * @return Map con el nombre de la materia y la lista de horarios asociados
     */
    @Operation(
        summary = "Consultar horario académico de un estudiante",
        description = "Obtiene el horario académico completo de un estudiante para un semestre específico. " +
                     "Retorna un mapa donde la clave es el nombre de la materia y el valor es la lista de horarios asociados.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Horario obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Horario de estudiante",
                value = """
                {
                    "Cálculo I": [
                        {
                            "dia": "LUNES",
                            "horaInicio": "08:00",
                            "horaFin": "10:00",
                            "aula": "A-101"
                        }
                    ],
                    "Programación I": [
                        {
                            "dia": "MARTES",
                            "horaInicio": "10:00",
                            "horaFin": "12:00",
                            "aula": "B-201"
                        }
                    ]
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad, estudiante o semestre",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"El estudiante no pertenece a la facultad especificada\""
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "No se encontró horario para el semestre especificado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Sin horario",
                value = "\"No se encontró horario para el semestre especificado\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/horario/{semestre}")
    public ResponseEntity<Object> consultarHorarioEstudiante(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "20221005001"
            )
            @PathVariable String idEstudiante,
            @Parameter(
                name = "semestre",
                description = "Número del semestre a consultar",
                required = true,
                example = "1"
            )
            @PathVariable int semestre) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            // Validar semestre
            decanoService.validarSemestre(semestre);
            
            // Validar que el estudiante existe y pertenece a la facultad
            decanoService.validarEstudianteFacultad(idEstudiante, facultad);
            
            List<RegistroMaterias> registroMaterias = decanoService.consultarHorarioEstudiante(idEstudiante, semestre);
            if (registroMaterias.isEmpty()) {
                return new ResponseEntity<>(SirhaException.NO_HORARIO_ENCONTRADO, HttpStatus.NOT_FOUND);
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
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * Permite al decano consultar el rendimiento académico del estudiante.
     * @param facultad nombre de la facultad del decano
     * @param idEstudiante ID del estudiante
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el Semaforo
     */
    @Operation(
        summary = "Consultar semáforo académico de un estudiante",
        description = "Obtiene el semáforo académico de un estudiante específico, que indica el rendimiento " +
                     "académico por materia. Retorna un mapa donde la clave es el acrónimo de la materia " +
                     "y el valor es el estado del semáforo (VERDE, AMARILLO, ROJO).",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Semáforo académico obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Semáforo académico",
                value = """
                {
                    "CALC1": {
                        "estado": "VERDE",
                        "promedio": 4.2,
                        "descripcion": "Rendimiento excelente"
                    },
                    "PROG1": {
                        "estado": "AMARILLO",
                        "promedio": 3.1,
                        "descripcion": "Rendimiento regular, requiere atención"
                    },
                    "MATE1": {
                        "estado": "ROJO",
                        "promedio": 2.5,
                        "descripcion": "Rendimiento bajo, necesita apoyo urgente"
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad o estudiante",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"El estudiante no pertenece a la facultad especificada\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/semaforo")
    public ResponseEntity<Object> consultarSemaforoAcademicoEstudiante(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "20221005001"
            )
            @PathVariable String idEstudiante) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            // Validar que el estudiante existe y pertenece a la facultad
            decanoService.validarEstudianteFacultad(idEstudiante, facultad);
            
            Map<String, Semaforo> semaforo = decanoService.consultarSemaforoAcademicoEstudiante(idEstudiante);
            return new ResponseEntity<>(semaforo, HttpStatus.OK);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta la información básica de un estudiante (código, nombre, carrera, semestre).
     * Permite al decano ver información esencial del estudiante de su facultad.
     * @param facultad nombre de la facultad del decano
     * @param idEstudiante ID del estudiante
     * @return EstudianteBasicoDTO con código, nombre, apellido, carrera y semestre actual
     */
    @Operation(
        summary = "Consultar información básica de un estudiante",
        description = "Obtiene la información básica de un estudiante específico, incluyendo código, " +
                     "nombre, apellido, carrera y semestre actual. Información esencial para la gestión académica.",
        tags = {"Consulta de Estudiantes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Información básica obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = EstudianteBasicoDTO.class),
            examples = @ExampleObject(
                name = "Información básica",
                value = """
                {
                    "codigo": "20221005001",
                    "nombre": "Juan",
                    "apellido": "Pérez",
                    "carrera": "Ingeniería de Sistemas",
                    "semestreActual": 5,
                    "facultad": "INGENIERIA",
                    "estado": "ACTIVO"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad o estudiante",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"El estudiante no pertenece a la facultad especificada\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/informacion-basica")
    public ResponseEntity<Object> consultarInformacionBasicaEstudiante(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "idEstudiante",
                description = "Código único del estudiante",
                required = true,
                example = "20221005001"
            )
            @PathVariable String idEstudiante) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            EstudianteBasicoDTO informacionBasica = decanoService.obtenerInformacionBasicaEstudiante(idEstudiante, facultad);
            return new ResponseEntity<>(informacionBasica, HttpStatus.OK);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta la disponibilidad de grupos para una materia específica.
     * Muestra capacidad actual, cupo máximo y lista de espera.
     * @param facultad nombre de la facultad del decano
     * @param materiaAcronimo Acrónimo de la materia a consultar
     * @return lista de grupos con información de disponibilidad
     */
    @Operation(
        summary = "Consultar disponibilidad de grupos por materia",
        description = "Obtiene información detallada de disponibilidad de todos los grupos de una materia específica, " +
                     "incluyendo capacidad actual, cupo máximo, lista de espera y estado de disponibilidad.",
        tags = {"Gestión de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Disponibilidad de grupos obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = DisponibilidadGrupoDTO.class),
            examples = @ExampleObject(
                name = "Disponibilidad de grupos",
                value = """
                [
                    {
                        "grupoId": "GRP-001",
                        "materia": "Cálculo I",
                        "profesor": "Dr. García",
                        "horario": "Lunes 8:00-10:00",
                        "capacidadActual": 28,
                        "capacidadMaxima": 30,
                        "listaEspera": 5,
                        "disponible": true,
                        "porcentajeOcupacion": 93.33
                    },
                    {
                        "grupoId": "GRP-002",
                        "materia": "Cálculo I",
                        "profesor": "Dra. López",
                        "horario": "Martes 10:00-12:00",
                        "capacidadActual": 30,
                        "capacidadMaxima": 30,
                        "listaEspera": 8,
                        "disponible": false,
                        "porcentajeOcupacion": 100.0
                    }
                ]
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad o materia",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Materia no encontrada o facultad inválida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/materia/{materiaAcronimo}/disponibilidad")
    public ResponseEntity<Object> consultarDisponibilidadGrupos(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "materiaAcronimo",
                description = "Acrónimo de la materia a consultar",
                required = true,
                example = "CALC1"
            )
            @PathVariable String materiaAcronimo) {
        try {
            List<DisponibilidadGrupoDTO> disponibilidades = decanoService.consultarDisponibilidadGrupos(materiaAcronimo, facultad);
            return ResponseEntity.ok(disponibilidades);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responde a una solicitud de cambio de grupo (aprobar, rechazar, en_revision).
     * @param facultad nombre de la facultad del decano
     * @param solicitudId ID de la solicitud a responder
     * @param respuesta datos de la respuesta (estado y observaciones)
     * @return confirmación de la operación
     */
    @Operation(
        summary = "Responder a una solicitud de cambio de grupo",
        description = "Permite al decano aprobar, rechazar o marcar en revisión una solicitud de cambio de grupo. " +
                     "Se requiere proporcionar el nuevo estado y opcionalmente observaciones.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Solicitud respondida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Respuesta exitosa",
                value = """
                {
                    "mensaje": "Solicitud respondida",
                    "solicitudId": "SOL-001",
                    "estado": "APROBADA"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en la validación de datos o solicitud no válida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"La solicitud no pertenece a la facultad especificada\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @PostMapping("/{facultad}/solicitud/{solicitudId}/responder")
    public ResponseEntity<Object> responderSolicitud(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "solicitudId",
                description = "ID único de la solicitud a responder",
                required = true,
                example = "SOL-001"
            )
            @PathVariable String solicitudId,
            @Parameter(
                description = "Datos de la respuesta del decano",
                required = true
            )
            @RequestBody RespuestaSolicitudDTO respuesta) {
        try {

            respuesta.setSolicitudId(solicitudId);
            
            decanoService.responderSolicitud(respuesta, facultad);
            return ResponseEntity.ok(Map.of(
                MENSAJE, "Solicitud respondida",
                "solicitudId", solicitudId,
                "estado", respuesta.getNuevoEstado().name()
            ));
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene una solicitud específica con todos sus detalles.
     * Permite al decano revisar la información completa antes de responder.
     * @param facultad nombre de la facultad del decano
     * @param solicitudId ID de la solicitud a consultar
     * @return detalles completos de la solicitud
     */
    @Operation(
        summary = "Consultar detalle de una solicitud específica",
        description = "Obtiene información completa de una solicitud incluyendo datos del estudiante, " +
                     "materia, grupos de origen y destino, motivo y estado actual.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Detalle de solicitud obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Solicitud.class),
            examples = @ExampleObject(
                name = "Detalle de solicitud",
                value = """
                {
                    "id": "SOL-001",
                    "estudiante": {
                        "codigo": "2022001",
                        "nombre": "Juan Pérez",
                        "correo": "juan.perez@universidad.edu"
                    },
                    "materia": {
                        "acronimo": "CALC1",
                        "nombre": "Cálculo I",
                        "creditos": 4
                    },
                    "grupoOrigen": {
                        "id": "GRP-001",
                        "profesor": "Dr. García",
                        "horario": "Lunes 8:00-10:00"
                    },
                    "grupoDestino": {
                        "id": "GRP-002",
                        "profesor": "Dra. López",
                        "horario": "Martes 10:00-12:00"
                    },
                    "motivo": "Conflicto de horario laboral",
                    "estado": "PENDIENTE",
                    "fechaSolicitud": "2024-02-15T10:30:00",
                    "observaciones": null
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Solicitud no encontrada en la facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Solicitud no encontrada",
                value = "\"Solicitud no encontrada en la facultad: INGENIERIA\""
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/solicitud/{solicitudId}/detalle")
    public ResponseEntity<Object> consultarDetalleSolicitud(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                name = "solicitudId",
                description = "ID único de la solicitud a consultar",
                required = true,
                example = "SOL-001"
            )
            @PathVariable String solicitudId) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            // Obtener la solicitud y validar que pertenece a la facultad
            List<Solicitud> solicitudes = decanoService.consultarSolicitudesPorFacultad(Facultad.valueOf(facultad.toUpperCase()));
            Solicitud solicitud = solicitudes.stream()
                    .filter(s -> s.getId().equals(solicitudId))
                    .findFirst()
                    .orElse(null);
            
            if (solicitud == null) {
                return new ResponseEntity<>("Solicitud no encontrada en la facultad: " + facultad, HttpStatus.NOT_FOUND);
            }
            
            return ResponseEntity.ok(solicitud);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Consulta resumen estadístico de solicitudes por facultad.
     * Proporciona un resumen con contadores por estado para análisis rápido del decano.
     * @param facultad nombre de la facultad
     * @return resumen estadístico de solicitudes con contadores por estado
     */
    @Operation(
        summary = "Consultar resumen estadístico de solicitudes por facultad",
        description = "Obtiene un resumen estadístico de todas las solicitudes de cambio de grupo en una facultad, " +
                     "mostrando contadores por estado (pendientes, aprobadas, rechazadas) y total.",
        tags = {"Gestión de Solicitudes"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Resumen estadístico obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Resumen estadístico",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "totalSolicitudes": 45,
                    "resumen": {
                        "pendientes": 12,
                        "aprobadas": 28,
                        "rechazadas": 5
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/solicitudes/resumen")
    public ResponseEntity<Object> consultarResumenSolicitudesPorFacultad(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            // Validar facultad
            decanoService.validarFacultad(facultad);
            
            Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
            
            // Obtener todas las solicitudes
            List<Solicitud> todasLasSolicitudes = decanoService.consultarSolicitudesPorFacultad(facultadEnum);
            
            // Contar por estado
            long pendientes = todasLasSolicitudes.stream()
                    .filter(s -> s.getEstado() == SolicitudEstado.PENDIENTE)
                    .count();
            long aprobadas = todasLasSolicitudes.stream()
                    .filter(s -> s.getEstado() == SolicitudEstado.APROBADA)
                    .count();
            long rechazadas = todasLasSolicitudes.stream()
                    .filter(s -> s.getEstado() == SolicitudEstado.RECHAZADA)
                    .count();
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("totalSolicitudes", todasLasSolicitudes.size());
            response.put("resumen", Map.of(
                    "pendientes", pendientes,
                    "aprobadas", aprobadas,
                    "rechazadas", rechazadas
            ));
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Configura el calendario académico estableciendo las fechas de inicio y fin del semestre.
     * Solo los decanos pueden realizar esta configuración.
     * @param facultad nombre de la facultad del decano
     * @param calendarioDTO DTO con las nuevas fechas del calendario académico
     * @return confirmación de la configuración realizada
     */
    @Operation(
        summary = "Configurar calendario académico",
        description = "Establece las fechas de inicio y fin del semestre académico para una facultad específica. " +
                     "Esta operación solo puede ser realizada por decanos.",
        tags = {"Configuración Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Calendario académico configurado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Configuración exitosa",
                value = """
                {
                    "mensaje": "Calendario académico configurado exitosamente",
                    "facultad": "INGENIERIA",
                    "fechaInicio": "2024-02-01",
                    "fechaFin": "2024-06-30"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de datos o fechas inválidas",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"La fecha de inicio debe ser anterior a la fecha de fin\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @PostMapping(value = "/{facultad}/configuracion/calendario-academico")
    public ResponseEntity<Object> configurarCalendarioAcademico(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                description = "Datos del calendario académico con fechas de inicio y fin del semestre",
                required = true
            )
            @RequestBody CalendarioAcademicoDTO calendarioDTO) {
        try {
     
            decanoService.configurarCalendarioAcademico(calendarioDTO, facultad);
            Map<String, Object> response = new HashMap<>();
            response.put(MENSAJE, "Calendario académico configurado exitosamente");
            response.put(FACULTDAD, facultad);
            response.put(FECHA_INICIO, calendarioDTO.getFechaInicio());
            response.put(FECHA_FIN, calendarioDTO.getFechaFin());

            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Configura el plazo de solicitudes estableciendo las fechas de inicio y fin para recibir solicitudes.
     * Solo los decanos pueden realizar esta configuración.
     * @param facultad nombre de la facultad del decano
     * @param plazoDTO DTO con las nuevas fechas del plazo de solicitudes
     * @return confirmación de la configuración realizada
     */
    @Operation(
        summary = "Configurar plazo de solicitudes",
        description = "Establece las fechas de inicio y fin para el período de recepción de solicitudes de cambio de grupo. " +
                     "Durante este período los estudiantes pueden enviar solicitudes.",
        tags = {"Configuración Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Plazo de solicitudes configurado exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Configuración exitosa",
                value = """
                {
                    "mensaje": "Plazo de solicitudes configurado exitosamente",
                    "facultad": "INGENIERIA",
                    "fechaInicio": "2024-01-15",
                    "fechaFin": "2024-01-31"
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de datos o fechas inválidas",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"La fecha de inicio debe ser anterior a la fecha de fin\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @PostMapping(value = "/{facultad}/configuracion/plazo-solicitudes")
    public ResponseEntity<Object> configurarPlazoSolicitudes(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad,
            @Parameter(
                description = "Datos del plazo de solicitudes con fechas de inicio y fin",
                required = true
            )
            @RequestBody PlazoSolicitudesDTO plazoDTO) {
        try {
            decanoService.configurarPlazoSolicitudes(plazoDTO, facultad);
            Map<String, Object> response = new HashMap<>();
            response.put(MENSAJE, "Plazo de solicitudes configurado exitosamente");
            response.put(FACULTDAD, facultad);
            response.put(FECHA_INICIO, plazoDTO.getFechaInicio());
            response.put(FECHA_FIN, plazoDTO.getFechaFin());

            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la configuración actual del calendario académico.
     * @param facultad nombre de la facultad del decano
     * @return configuración actual del calendario académico
     */
    @Operation(
        summary = "Obtener configuración del calendario académico",
        description = "Consulta la configuración actual del calendario académico de una facultad, " +
                     "incluyendo fechas de inicio y fin del semestre.",
        tags = {"Configuración Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Configuración del calendario obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Calendario académico",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "calendarioAcademico": {
                        "fechaInicio": "2024-02-01",
                        "fechaFin": "2024-06-30",
                        "semestre": "2024-1",
                        "estado": "ACTIVO"
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Calendario académico no configurado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "No configurado",
                value = "\"Calendario académico no configurado para la facultad\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/configuracion/calendario-academico")
    public ResponseEntity<Object> obtenerCalendarioAcademico(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            CalendarioAcademicoDTO calendario = decanoService.obtenerCalendarioAcademico(facultad);
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("calendarioAcademico", calendario);
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la configuración actual del plazo de solicitudes.
     * @param facultad nombre de la facultad del decano
     * @return configuración actual del plazo de solicitudes
     */
    @Operation(
        summary = "Obtener configuración del plazo de solicitudes",
        description = "Consulta la configuración actual del plazo para recibir solicitudes de cambio de grupo " +
                     "en una facultad específica, incluyendo fechas de inicio y fin.",
        tags = {"Configuración Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Configuración del plazo obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Plazo de solicitudes",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "plazoSolicitudes": {
                        "fechaInicio": "2024-01-15",
                        "fechaFin": "2024-01-31",
                        "estado": "ACTIVO",
                        "descripcion": "Período de solicitudes para cambio de grupo semestre 2024-1"
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plazo de solicitudes no configurado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "No configurado",
                value = "\"Plazo de solicitudes no configurado para la facultad\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/configuracion/plazo-solicitudes")
    public ResponseEntity<Object> obtenerPlazoSolicitudes(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            PlazoSolicitudesDTO plazo = decanoService.obtenerPlazoSolicitudes(facultad);
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("plazoSolicitudes", plazo);
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Obtiene un resumen completo de todas las configuraciones actuales.
     * @param facultad nombre de la facultad del decano
     * @return resumen de configuraciones del calendario académico y plazo de solicitudes
     */
    @Operation(
        summary = "Obtener resumen de todas las configuraciones",
        description = "Consulta un resumen completo de todas las configuraciones de una facultad, " +
                     "incluyendo calendario académico, plazo de solicitudes y estados actuales.",
        tags = {"Configuración Académica"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Resumen de configuraciones obtenido exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Resumen configuraciones",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "calendarioAcademico": {
                        "fechaInicio": "2024-02-01",
                        "fechaFin": "2024-06-30"
                    },
                    "plazoSolicitudes": {
                        "fechaInicio": "2024-01-15",
                        "fechaFin": "2024-01-31",
                        "activo": false
                    }
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/configuracion/resumen")
    public ResponseEntity<Object> obtenerResumenConfiguraciones(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            CalendarioAcademicoDTO calendario = decanoService.obtenerCalendarioAcademico(facultad);
            PlazoSolicitudesDTO plazo = decanoService.obtenerPlazoSolicitudes(facultad);
            boolean plazoActivo = decanoService.esPlazoSolicitudesActivo(facultad);
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("calendarioAcademico", Map.of(
                FECHA_INICIO, calendario.getFechaInicio(),
                FECHA_FIN, calendario.getFechaFin()
            ));
            response.put("plazoSolicitudes", Map.of(
                FECHA_INICIO, plazo.getFechaInicio(),
                FECHA_FIN, plazo.getFechaFin(),
                "activo", plazoActivo
            ));
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene el estado de todos los grupos de una facultad para monitoreo de cargas.
     * @param facultad nombre de la facultad
     * @return lista con información de monitoreo de todos los grupos
     */
    @Operation(
        summary = "Monitorear grupos por facultad",
        description = "Obtiene información completa de monitoreo de todos los grupos de una facultad, " +
                     "incluyendo capacidad actual, cupo máximo, porcentaje de ocupación y nivel de alerta.",
        tags = {"Monitoreo de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Información de monitoreo obtenida exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Monitoreo de grupos",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "totalGrupos": 25,
                    "grupos": [
                        {
                            "grupoId": "GRP-001",
                            "materia": "Cálculo I",
                            "capacidadActual": 28,
                            "capacidadMaxima": 30,
                            "porcentajeOcupacion": 93.33,
                            "nivelAlerta": "CRITICO"
                        }
                    ]
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Facultad inválida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error facultad",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/grupos/monitoreo")
    public ResponseEntity<Object> monitorearGrupos(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad a monitorear",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            List<MonitoreoGrupoDTO> grupos = decanoService.monitorearGruposPorFacultad(facultad);
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("totalGrupos", grupos.size());
            response.put("grupos", grupos);
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene solo los grupos que tienen alerta de capacidad (90% o más ocupados).
     * @param facultad nombre de la facultad
     * @return lista con grupos que superan el 90% de ocupación
     */
    @Operation(
        summary = "Obtener grupos con alerta de capacidad",
        description = "Consulta únicamente los grupos que han alcanzado o superado el 90% de su capacidad máxima, " +
                     "requiriendo atención inmediata del decano para gestión de cupos.",
        tags = {"Monitoreo de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Grupos con alerta obtenidos exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Grupos con alerta",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "totalGruposConAlerta": 3,
                    "gruposConAlerta": [
                        {
                            "grupoId": "GRP-001",
                            "materia": "Cálculo I",
                            "capacidadActual": 28,
                            "capacidadMaxima": 30,
                            "porcentajeOcupacion": 93.33,
                            "nivelAlerta": "CRITICO"
                        },
                        {
                            "grupoId": "GRP-005",
                            "materia": "Física I",
                            "capacidadActual": 27,
                            "capacidadMaxima": 30,
                            "porcentajeOcupacion": 90.0,
                            "nivelAlerta": "ALTO"
                        }
                    ],
                    "mensaje": "Se encontraron 3 grupos con alerta de capacidad."
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/grupos/alertas")
    public ResponseEntity<Object> obtenerGruposConAlerta(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            List<MonitoreoGrupoDTO> gruposConAlerta = decanoService.obtenerGruposConAlerta(facultad);
            Map<String, Object> response = new HashMap<>();
            response.put("facultad", facultad);
            response.put("totalGruposConAlerta", gruposConAlerta.size());
            response.put("gruposConAlerta", gruposConAlerta);
            response.put(MENSAJE, gruposConAlerta.isEmpty() ? 
                "No hay grupos con alerta de capacidad en esta facultad." :
                "Se encontraron " + gruposConAlerta.size() + " grupos con alerta de capacidad."
            );
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene estadísticas de ocupación de grupos por nivel de alerta.
     * @param facultad nombre de la facultad
     * @return estadísticas agrupadas por nivel de alerta (NORMAL, ADVERTENCIA, CRITICO)
     */
    @Operation(
        summary = "Obtener estadísticas de grupos por nivel de alerta",
        description = "Proporciona un resumen estadístico de todos los grupos de una facultad agrupados por nivel de alerta " +
                     "(NORMAL, ADVERTENCIA, CRITICO) con cantidades y porcentajes.",
        tags = {"Monitoreo de Grupos"}
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estadísticas obtenidas exitosamente",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Estadísticas por alerta",
                value = """
                {
                    "facultad": "INGENIERIA",
                    "totalGrupos": 25,
                    "estadisticasPorNivel": {
                        "NORMAL": {
                            "cantidad": 18,
                            "porcentaje": 72
                        },
                        "ADVERTENCIA": {
                            "cantidad": 5,
                            "porcentaje": 20
                        },
                        "CRITICO": {
                            "cantidad": 2,
                            "porcentaje": 8
                        }
                    },
                    "alertaGeneral": "Hay 7 grupos que requieren atención."
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error en validación de facultad",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name = "Error validación",
                value = "\"Facultad no válida\""
            )
        )
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
    @GetMapping("/{facultad}/grupos/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticasGrupos(
            @Parameter(
                name = "facultad",
                description = "Nombre de la facultad del decano",
                required = true,
                example = "INGENIERIA"
            )
            @PathVariable String facultad) {
        try {
            Map<String, Long> estadisticas = decanoService.obtenerEstadisticasAlertas(facultad);
            List<MonitoreoGrupoDTO> todosGrupos = decanoService.monitorearGruposPorFacultad(facultad);
            
            // Calcular totales y porcentajes
            long totalGrupos = todosGrupos.size();
            long gruposNormales = estadisticas.getOrDefault("NORMAL", 0L);
            long gruposAdvertencia = estadisticas.getOrDefault("ADVERTENCIA", 0L);
            long gruposCriticos = estadisticas.getOrDefault("CRITICO", 0L);
            
            Map<String, Object> response = new HashMap<>();
            response.put(FACULTDAD, facultad);
            response.put("totalGrupos", totalGrupos);
            response.put("estadisticasPorNivel", Map.of(
                "NORMAL", Map.of(
                    CANTIDAD, gruposNormales,
                    PORCENTAJE, totalGrupos > 0 ? Math.round((gruposNormales * 100.0) / totalGrupos) : 0
                ),
                "ADVERTENCIA", Map.of(
                    CANTIDAD, gruposAdvertencia,
                    PORCENTAJE, totalGrupos > 0 ? Math.round((gruposAdvertencia * 100.0) / totalGrupos) : 0
                ),
                "CRITICO", Map.of(
                    CANTIDAD, gruposCriticos,
                    PORCENTAJE, totalGrupos > 0 ? Math.round((gruposCriticos * 100.0) / totalGrupos) : 0
                )
            ));
            response.put("alertaGeneral", (gruposAdvertencia + gruposCriticos) > 0 ? 
                "Hay " + (gruposAdvertencia + gruposCriticos) + " grupos que requieren atención." :
                "Todos los grupos tienen capacidad normal."
            );
            
            return ResponseEntity.ok(response);
        } catch (SirhaException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}