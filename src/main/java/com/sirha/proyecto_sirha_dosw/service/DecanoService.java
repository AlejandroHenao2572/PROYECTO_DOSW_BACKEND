package com.sirha.proyecto_sirha_dosw.service;


import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DecanoService {

    private final UsuarioRepository usuarioRepository;

    public DecanoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}
