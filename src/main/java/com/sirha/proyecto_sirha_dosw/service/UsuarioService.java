package com.sirha.proyecto_sirha_dosw.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.model.Usuario;

import java.util.*;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String rawPassword) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent() && usuario.get().getPassword().equals(rawPassword);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    } 
    
    public Optional<Usuario> obtenerPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}


