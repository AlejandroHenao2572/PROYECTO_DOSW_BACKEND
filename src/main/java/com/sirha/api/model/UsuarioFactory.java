package com.sirha.api.model;

/**
 * Fábrica de usuarios que crea instancias de {@link Usuario} según el rol dado.
 * Centraliza la lógica de creación para mantener consistencia y evitar duplicación de código.
 */

public class UsuarioFactory {

    /**
     * Crea un nuevo usuario según el rol indicado.
     * @param rol El rol del usuario.
     * @param nombre nombre del usuario.
     * @param apellido apellido del usuario.
     * @param email correo electrónico del usuario.
     * @param password contraseña del usuario.
     * @param facultad Facultad asociada.
     * @return instancia de {@link Usuario} correspondiente al rol.
     * @throws IllegalArgumentException si el rol no está soportado o si faltan parámetros.
     */

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
