package com.sirha.proyecto_sirha_dosw.service;


import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecanoService {

    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;

    public DecanoService(UsuarioRepository usuarioRepository, SolicitudRepository solicitudRepository) {
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public List<Usuario> findEstudiantesByFacultad(String facultad) {
        return usuarioRepository.findByFacultadAndRol(facultad, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByIdAndFacultad(String id, String facultad) {
        return usuarioRepository.findByIdAndFacultadAndRol(id, facultad, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByEmailAndFacultad(String email, String facultad) {
        return usuarioRepository.findByEmailAndFacultadAndRol(email, facultad, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreAndFacultad(String nombre, String facultad) {
        return usuarioRepository.findByNombreAndFacultadAndRol(nombre, facultad, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByApellidoAndFacultad(String apellido, String facultad) {
        return usuarioRepository.findByApellidoAndFacultadAndRol(apellido, facultad, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreApellidoAndFacultad(String nombre, String apellido, String facultad) {
        return usuarioRepository.findByNombreAndApellidoAndFacultadAndRol(nombre, apellido, facultad, Rol.ESTUDIANTE);
    }

    /**
     * Consulta todas las solicitudes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @return lista de solicitudes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultad(facultad);
    }

    /**
     * Consulta las solicitudes pendientes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes pendientes
     * @return lista de solicitudes pendientes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPendientesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultadAndEstado(facultad, SolicitudEstado.PENDIENTE);
    }

    /**
     * Consulta las solicitudes por facultad filtradas por estado específico.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @param estado el estado específico de las solicitudes a consultar
     * @return lista de solicitudes filtradas por facultad y estado
     */
    public List<Solicitud> consultarSolicitudesPorFacultadYEstado(Facultad facultad, SolicitudEstado estado) {
        return solicitudRepository.findByFacultadAndEstado(facultad, estado);
    }
}
