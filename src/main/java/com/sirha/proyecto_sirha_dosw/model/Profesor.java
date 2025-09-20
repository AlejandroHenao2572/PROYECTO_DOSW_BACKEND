package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Field;

public class Profesor extends Usuario {

    @Field("departamento")
    @NotBlank(message = "La departamento no puede estar vacío")
    private CarreraTipo departamento;

    public Profesor(String nombre ,String apellido,String email, String password,Rol rol,CarreraTipo departamento ) {
        super(nombre,apellido,email,password,rol);
        this.departamento = departamento;
    }

    public CarreraTipo getDepartamento() { return departamento; }


}
