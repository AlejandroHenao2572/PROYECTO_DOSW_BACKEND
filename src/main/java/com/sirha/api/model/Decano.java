package com.sirha.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Decano extends Profesor implements GestorSolicitudes {

    private Facultad carrera;

    public Decano() {
        super();
    }

    public Decano(String nombre, String apellido, String email, String contraseña, Facultad carrera) {
        super(nombre, apellido, email, contraseña);
        this.carrera = carrera;
    }

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


    @Override
    public void agregarSolicitud(Solicitud solicitud) {

    }

    @Override
    public void gestionarSolicitud(Solicitud solicitud, String accion) {

    }
}
