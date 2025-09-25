/**
 * Clase que representa a un administrador en el sistema.
 * Extiende de Usuario e implementa la interfaz GestorSolicitudes.
 */
package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Administrador extends Usuario implements GestorSolicitudes {

    /**
     * Constructor por defecto.
     */
    public Administrador() {
        super();
    }

    /**
     * Constructor con parámetros básicos.
     * @param nombre Nombre del administrador
     * @param apellido Apellido del administrador
     * @param email Email del administrador
     * @param password Contraseña del administrador
     */
    public Administrador(String nombre, String apellido, String email, String password) {
        super(nombre, apellido, email, password);
    }

    /**
     * Constructor completo con todos los parámetros.
     * @param nombre Nombre del administrador
     * @param apellido Apellido del administrador
     * @param email Email del administrador
     * @param password Contraseña del administrador
     * @param rol Rol del administrador
     */
    public Administrador(String nombre, String apellido, String email, String password, Rol rol) {
        super(nombre, apellido, email, password, rol);
    }

    /**
     * Implementación del método para agregar solicitudes (pendiente de implementación).
     * @param solicitud Solicitud a agregar
     */
    @Override
    public void agregarSolicitud(Solicitud solicitud) {
        // Pendiente de implementación
    }

    /**
     * Implementación del método para gestionar solicitudes (pendiente de implementación).
     * @param solicitud Solicitud a gestionar
     * @param accion Acción a realizar sobre la solicitud
     */
    @Override
    public void gestionarSolicitud(Solicitud solicitud, String accion) {
        // Pendiente de implementación
    }
}