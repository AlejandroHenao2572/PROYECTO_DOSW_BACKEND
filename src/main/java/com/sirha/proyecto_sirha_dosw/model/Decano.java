package com.sirha.proyecto_sirha_dosw.model;

public class Decano extends Usuario {
    private CarreraTipo facultad;

    public Decano(String nombre ,String apellido,String email, String password,Rol rol, CarreraTipo facultad) {
        super(nombre,apellido,email,password,rol);
        this.facultad = facultad;
    }

    public CarreraTipo getFacultad() { return facultad; }


}

