package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

public class Estudiante extends Usuario {

    @Field("carrera")
    @NotBlank(message = "La carrera no puede estar vacío")
    private String carrera;

    @Field("semestre")
    @NotBlank(message = "La semestre no puede estar vacío")
    private int semestre;

    public Estudiante(String nombre, String apellido,String email, String password,Rol rol) {
        super(nombre,apellido, email,password,rol);
    }

    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }

}

