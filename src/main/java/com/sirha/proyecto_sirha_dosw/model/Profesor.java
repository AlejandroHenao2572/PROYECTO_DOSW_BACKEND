/**
 * Clase que representa a un profesor en el sistema.
 * Extiende de Usuario y no añade campos específicos adicionales.
 */
package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@TypeAlias("profesor")
public class Profesor extends Usuario {

    /**
     * Constructor por defecto.
     */
    public Profesor() {
        super();
    }

    /**
     * Constructor con parámetros básicos.
     * @param nombre Nombre del profesor
     * @param apellido Apellido del profesor
     * @param email Email del profesor
     * @param password Contraseña del profesor
     */
    public Profesor(String nombre, String apellido, String email, String password) {
        super(nombre, apellido, email, password);
    }

    /**
     * Constructor completo con todos los parámetros.
     * @param nombre Nombre del profesor
     * @param apellido Apellido del profesor
     * @param email Email del profesor
     * @param password Contraseña del profesor
     * @param rol Rol del profesor
     */
    public Profesor(String nombre, String apellido, String email, String password, Rol rol) {
        super(nombre, apellido, email, password, rol);
    }
}