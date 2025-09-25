/**
 * Factory class para crear instancias de usuarios según su rol.
 */
package com.sirha.proyecto_sirha_dosw.model;

public class UsuarioFactory {

    /**
     * Crea una instancia de usuario según el rol especificado.
     * @param rol Rol del usuario a crear
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param facultad Facultad del usuario (solo aplica para ESTUDIANTE y DECANO)
     * @return Instancia de Usuario según el rol especificado
     * @throws IllegalArgumentException Si el rol no está soportado
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