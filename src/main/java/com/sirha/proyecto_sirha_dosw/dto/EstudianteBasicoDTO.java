package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Facultad;

/**
 * DTO para mostrar información básica del estudiante.
 * Contiene solo los campos esenciales: código, nombre, carrera y semestre.
 */
public class EstudianteBasicoDTO {
    
    private String codigo;
    private String nombre;
    private String apellido;
    private Facultad carrera;
    private int semestreActual;

    // Constructor por defecto
    public EstudianteBasicoDTO() {
    }

    // Constructor con parámetros
    public EstudianteBasicoDTO(String codigo, String nombre, String apellido, Facultad carrera, int semestreActual) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.semestreActual = semestreActual;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Facultad getCarrera() {
        return carrera;
    }

    public void setCarrera(Facultad carrera) {
        this.carrera = carrera;
    }

    public int getSemestreActual() {
        return semestreActual;
    }

    public void setSemestreActual(int semestreActual) {
        this.semestreActual = semestreActual;
    }

    @Override
    public String toString() {
        return "EstudianteBasicoDTO{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", carrera=" + carrera +
                ", semestreActual=" + semestreActual +
                '}';
    }
}