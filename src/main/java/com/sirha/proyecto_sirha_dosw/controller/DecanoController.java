package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.service.DecanoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Decanos")
public class DecanoController {

    private final DecanoService decanoService;

    public DecanoController(DecanoService decanoService) {
        this.decanoService = decanoService;
    }

    @GetMapping("/{facutal}/")
    public ResponseEntity<List<Usuario>> listarUsuarios(@PathVariable String facutal) {
        List<Usuario> estudiantes = decanoService.findEstudiantesByFacultad(facutal);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{facutal}/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String facutal, @PathVariable String id) {
        Usuario estudiante = decanoService.findEstudianteByIdAndFacultad(id, facutal);
        if (estudiante != null) {
            return new ResponseEntity<>(estudiante, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{facutal}/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String facutal, @PathVariable String email) {
        Usuario estudiante = decanoService.findEstudianteByEmailAndFacultad(email, facutal);
        if (estudiante != null) {
            return new ResponseEntity<>(estudiante, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
}
