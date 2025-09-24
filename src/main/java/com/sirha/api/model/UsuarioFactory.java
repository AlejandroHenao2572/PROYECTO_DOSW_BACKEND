package com.sirha.api.model;

public class UsuarioFactory {

    public static Usuario crearUsuario(Rol rol, String nombre, String apellido, String email, String password,
            Facultad facultad) {
        switch (rol) {
            case ESTUDIANTE:
                return new Estudiante(nombre, apellido, email, password, rol, facultad);
            case PROFESOR:
                return new Profesor(nombre, apellido, email, password, rol);
            case DECANO:
                return new Decano(nombre, apellido, email, password, rol, facultad);
            case ADMINISTRADOR:
                return new Administrador(nombre, apellido, email, password, rol);
            default:
                throw new IllegalArgumentException("Rol no soportado: " + rol);
        }
    }
}
