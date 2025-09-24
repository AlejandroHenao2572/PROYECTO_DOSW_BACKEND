package com.sirha.api.dto;

import com.sirha.api.model.Horario;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GrupoDTO {

    private String id;

    @NotNull(message = "La capacidad no puede ser nula")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Max(value = 40, message = "La capacidad maxima es 40")
    private int capacidad;

    private int cantidadInscritos;

    private boolean estaCompleto;

    @NotNull(message = "Los horarios no pueden ser nulos")
    private List<Horario> horarios = new ArrayList<>();

    @NotNull(message = "El ID de la materia no puede ser nulo")
    private String materiaId;

    private List<String> estudiantesId = new ArrayList<>();

    private String profesorId;

    // Default constructor
    public GrupoDTO() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCantidadInscritos() {
        return cantidadInscritos;
    }

    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
    }

    public boolean isEstaCompleto() {
        return estaCompleto;
    }

    public void setEstaCompleto(boolean estaCompleto) {
        this.estaCompleto = estaCompleto;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public String getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(String materiaId) {
        this.materiaId = materiaId;
    }

    public List<String> getEstudiantesId() {
        return estudiantesId;
    }

    public void setEstudiantesId(List<String> estudiantesId) {
        this.estudiantesId = estudiantesId;
    }

    public String getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(String profesorId) {
        this.profesorId = profesorId;
    }
}