package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class CarreraFactory {
    public static Carrera crearCarrera(CarreraTipo tipo) {
        switch (tipo) {
            case INGENIERIA_SISTEMAS:
                return new IngenieriaSistemas();
            case INGENIERIA_CIVIL:
                return new IngenieriaCivil();
            case ADMINISTRACION:
                return new Administracion();
            default:
                throw new IllegalArgumentException("Carrera no reconocida: " + tipo);
        }
    }
}


