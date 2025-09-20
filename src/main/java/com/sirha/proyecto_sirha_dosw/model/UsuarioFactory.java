package com.sirha.proyecto_sirha_dosw.model;

/**
 * Clase de fábrica para la creación de instancias de {@link Usuario}.
 *
 * <p>Implementa el patrón de diseño Factory Method, centralizando
 * la lógica de creación de usuarios según su {@link Rol}.</p>
 *
 * <p>De esta manera, el código cliente no necesita instanciar directamente
 * a las clases concretas como {@link Estudiante}, {@link Profesor},
 * {@link Decano} o {@link Administrador}, sino que delega esa
 * responsabilidad a la fábrica.</p>
 */

public class UsuarioFactory {

    /**
     * Crea un nuevo usuario en función del rol especificado.
     *
     * @param rol       El rol del usuario (ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR)
     * @param nombre    Nombre del usuario
     * @param apellido  Apellido del usuario
     * @param email     Correo electrónico del usuario
     * @param password  Contraseña del usuario
     * @return Una instancia de la subclase de {@link Usuario} correspondiente al rol
     * @throws IllegalArgumentException Si el rol no está soportado
     */

    public static Usuario crearUsuario(Rol rol,String nombre,String apellido, String email, String password, CarreraTipo facultad) {
        switch (rol) {
            case ESTUDIANTE:
                return (facultad != null)
                        ? new Estudiante(nombre,apellido, email, password, rol,facultad)
                        : new Estudiante(nombre,apellido,email,password,rol);
            case PROFESOR:
                return  new Profesor(nombre, apellido, email, password, rol, facultad);
            case DECANO:
                return new Decano(nombre,apellido, email, password, rol, facultad);
            case ADMINISTRADOR:
                return new Administrador(nombre,apellido, email, password, rol);
            default:
                throw new IllegalArgumentException("Rol no soportado: " + rol);
        }
    }
}

