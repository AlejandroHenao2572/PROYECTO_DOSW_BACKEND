package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    // registrar un nuevo usuario
    public Usuario registrar(Usuario usuario) {
        if (usuarios.containsKey(usuario.getEmail())) {
            throw new IllegalArgumentException("El usuario con este correo ya existe.");
        }
        usuarios.put(usuario.getEmail(), usuario);
        return usuario;
    }

    // autenticar un usuario (login)
    public boolean autenticar(String email, String password) {
        Usuario usuario = usuarios.get(email);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    // obtener un usuario por correo
    public Usuario buscarPorEmail(String email) {
        return usuarios.get(email);
    }

    // listar todos los usuarios registrados
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    // eliminar un usuario
    public boolean eliminar(String email) {
        return usuarios.remove(email) != null;
    }
}


