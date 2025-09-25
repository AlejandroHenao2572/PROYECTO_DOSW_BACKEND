package com.sirha.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa a un Decano en el sistema.
 * Un decano encargado de gestionar solicitudes.
 */

@Document(collection = "usuarios")
public class Decano extends Profesor implements GestorSolicitudes {

    private Facultad carrera;

    public Decano() {
        super();
    }

    /**
     * Constructor básico para crear un Decano.
     * @param nombre nombre del decano.
     * @param apellido apellido del decano.
     * @param email correo electrónico.
     * @param contraseña contraseña de acceso.
     * @param carrera carrera que gestiona.
     */

    public Decano(String nombre, String apellido, String email, String contraseña, Facultad carrera) {
        super(nombre, apellido, email, contraseña);
        this.carrera = carrera;
    }

    /**
     * Constructor extendiendo para crear un Decano con un rol específico.
     * @param nombre nombre del decano.
     * @param apellido apellido del decano.
     * @param email correo electrónico.
     * @param contraseña contraseña de acceso.
     * @param rol rol asignado al decano.
     * @param carrera carrera que gestiona.
     */

    public Decano(String nombre, String apellido, String email, String contraseña, Rol rol, Facultad carrera) {
        super(nombre, apellido, email, contraseña, rol);
        this.carrera = carrera;
    }

    public Facultad getCarrera() {
        return carrera;
    }

    public void setCarrera(Facultad carrera) {
        this.carrera = carrera;
    }

    /**
     * Agrega una solicitud al sistema.
     * @param solicitud solicitud a agregar.
     */

    @Override
    public void agregarSolicitud(Solicitud solicitud) {

    }

    /**
     * Gestiona una solicitud existente, aplicando la acción correspondiente.
     * @param solicitud solicitud a gestionar.
     * @param accion acción a realizar sobre la solicitud.
     */

    @Override
    public void gestionarSolicitud(Solicitud solicitud, String accion) {

    }
}
