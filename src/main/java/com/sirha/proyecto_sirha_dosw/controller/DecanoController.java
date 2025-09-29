package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.DisponibilidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.DecanoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/Decanos")
public class DecanoController {

    private static final String ERROR_INTERNO = "Error interno del servidor";
    private final DecanoService decanoService;

    public DecanoController(DecanoService decanoService) {
        this.decanoService = decanoService;
    }

    @GetMapping("/{facultad}/")
    public ResponseEntity<Object> listarUsuarios(@PathVariable String facultad) {
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

    @GetMapping("/{facultad}/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable String facultad, @PathVariable String id) {
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

    @GetMapping("/{facultad}/email/{email}")
    public ResponseEntity<Object> obtenerPorEmail(@PathVariable String facultad, @PathVariable String email) {
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

    @GetMapping("/{facutal}/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> obtenerPorNombre(@PathVariable String facutal, @PathVariable String nombre) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByNombreAndFacultad(nombre, facutal);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{facutal}/apellido/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorApellido(@PathVariable String facutal, @PathVariable String apellido) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByApellidoAndFacultad(apellido, facutal);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{facutal}/nombre/{nombre}/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorNombreYApellido(@PathVariable String facutal, @PathVariable String nombre, @PathVariable String apellido) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByNombreApellidoAndFacultad(nombre, apellido, facutal);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    /**
     * Consulta todas las solicitudes recibidas en el área de una facultad específica.
     * @param facultad nombre de la facultad 
     * @return lista de solicitudes asociadas a la facultad
     */
    @GetMapping("/{facultad}/solicitudes")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPorFacultad(@PathVariable String facultad) {
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
    @GetMapping("/{facultad}/solicitudes/pendientes")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPendientesPorFacultad(@PathVariable String facultad) {
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
    @GetMapping("/{facultad}/solicitudes/estado/{estado}")
    public ResponseEntity<List<Solicitud>> consultarSolicitudesPorFacultadYEstado(@PathVariable String facultad, @PathVariable String estado) {
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
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/horario/{semestre}")
    public ResponseEntity<Object> consultarHorarioEstudiante(
            @PathVariable String facultad, 
            @PathVariable String idEstudiante, 
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
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/semaforo")
    public ResponseEntity<Object> consultarSemaforoAcademicoEstudiante(
            @PathVariable String facultad, 
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
    @GetMapping("/{facultad}/estudiante/{idEstudiante}/informacion-basica")
    public ResponseEntity<Object> consultarInformacionBasicaEstudiante(
            @PathVariable String facultad, 
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
     * @param materiaId ID de la materia a consultar
     * @return lista de grupos con información de disponibilidad
     */
    @GetMapping("/{facultad}/materia/{materiaAcronimo}/disponibilidad")
    public ResponseEntity<Object> consultarDisponibilidadGrupos(
            @PathVariable String facultad, 
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
    @PostMapping("/{facultad}/solicitud/{solicitudId}/responder")
    public ResponseEntity<Object> responderSolicitud(
            @PathVariable String facultad,
            @PathVariable String solicitudId,
            @RequestBody RespuestaSolicitudDTO respuesta) {
        try {

            respuesta.setSolicitudId(solicitudId);
            
            decanoService.responderSolicitud(respuesta, facultad);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Solicitud respondida",
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
    @GetMapping("/{facultad}/solicitud/{solicitudId}/detalle")
    public ResponseEntity<Object> consultarDetalleSolicitud(
            @PathVariable String facultad,
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
    @GetMapping("/{facultad}/solicitudes/resumen")
    public ResponseEntity<Object> consultarResumenSolicitudesPorFacultad(@PathVariable String facultad) {
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
            response.put("facultad", facultad);
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
}
