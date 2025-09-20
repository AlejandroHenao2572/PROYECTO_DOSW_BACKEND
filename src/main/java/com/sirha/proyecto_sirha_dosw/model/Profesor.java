package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Field;

public class Profesor extends Usuario {

    @Field("departamento")
    @NotBlank(message = "La departamento no puede estar vacío")
    private String departamento;

    public Profesor(String nombre ,String apellido,String email, String password,Rol rol) {
        super(nombre,apellido,email,password,rol);
    }

    public String getDepartamento() { return departamento; }


}
