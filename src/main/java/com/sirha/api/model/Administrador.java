package com.sirha.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa a un administrador dentro del sistema.
 * Un administrador es un {@link Usuario} con permisos especiales para gestionar {@link Solicitud} solicitudes.
 */

@Document(collection = "usuarios")
public class Administrador extends Usuario implements GestorSolicitudes {

    /**
     * Constructor vacío necesario para Spring Data.
     */

    public Administrador() {
        super();
    }

    /**
     * Constructor básico de Administrador.
     * @param nombre nombre del administrador.
     * @param apellido apellido del administrador.
     * @param email correo electrónico del administrador.
     * @param password contraseña.
     */

    public Administrador(String nombre, String apellido, String email, String password) {
        super(nombre, apellido, email, password);
    }

    /**
     * Constructor que permite definir el rol explícitamente.
     * @param nombre nombre del administrador.
     * @param apellido apellido del administrador.
     * @param email correo electrónico.
     * @param password contraseña.
     * @param rol rol del usuario (debería ser {@link Rol#ADMINISTRADOR})
     */

    public Administrador(String nombre, String apellido, String email, String password, Rol rol) {
        super(nombre, apellido, email, password, rol);
    }

    /**
     * Agrega una nueva solicitud al sistema.
     * @param solicitud solicitud a agregar.
     */

    @Override
    public void agregarSolicitud(Solicitud solicitud) {

    }

    /**
     * Gestiona una solicitud existente aplicando una acción.
     * @param solicitud solicitud a gestionar.
     * @param accion acción a realizar sobre la solicitud.
     */

    @Override
    public void gestionarSolicitud(Solicitud solicitud, String accion) {

    }
}
