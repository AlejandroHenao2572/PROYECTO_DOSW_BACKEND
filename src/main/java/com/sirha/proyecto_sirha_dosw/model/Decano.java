package com.sirha.proyecto_sirha_dosw.model;

public class Decano extends Usuario {
    private String facultad;

    public Decano(String nombre ,String apellido,String email, String password,Rol rol) {
        super(nombre,apellido,email,password,rol);
    }

    public String getFacultad() { return facultad; }


}

