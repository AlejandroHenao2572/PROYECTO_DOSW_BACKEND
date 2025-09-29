package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.service.DecanoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
