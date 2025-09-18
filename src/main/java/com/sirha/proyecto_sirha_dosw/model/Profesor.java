package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Profesor extends Usuario {

    private String especializacion;

    private Facultad facultad;

    public Profesor(String nombre ,String email, String password) {
        this.setNombre(nombre);
        this.setEmail(email);
        this.setPassword(password);
        this.setRol(RolUsuario.PROFESOR);
    }

    public List<Grupo> obtenerGruposAsignados() {
        return null;
    }

}
