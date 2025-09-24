package com.sirha.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Profesor extends Usuario {

    public Profesor() {
        super();
    }

    public Profesor(String nombre, String apellido, String email, String password) {
        super(nombre, apellido, email, password);
    }

    public Profesor(String nombre, String apellido, String email, String password, Rol rol) {
        super(nombre, apellido, email, password, rol);
    }
}