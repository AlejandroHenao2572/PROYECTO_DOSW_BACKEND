package com.sirha.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carreras")
public class Carrera {

    @NotNull
    @NotBlank
    private Facultad nombre;

    @Id
    @NotNull
    @NotBlank
    private String codigo;

    @NotNull
    @NotBlank
    private int duracionSemestres;

    @NotBlank
    @NotBlank
    private int creditosTotales;

    private List<Materia> materias = new ArrayList<>();
    

    public Carrera() {}

    public Carrera(Facultad nombre, String codigo, int duracionSemestres, int creditosTotales) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionSemestres = duracionSemestres;
        this.creditosTotales = creditosTotales;
    }

    public Carrera(Facultad nombre, String codigo,
                   int duracionSemestres, List<Materia> materias, int creditosTotales) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionSemestres = duracionSemestres;
        this.materias = materias;
        this.creditosTotales = creditosTotales;
    }

    // Getters y setters

    public Facultad getNombre() {
        return nombre;
    }

    public void setNombre(Facultad nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDuracionSemestres() {
        return duracionSemestres;
    }

    public void setDuracionSemestres(int duracionSemestres) {
        this.duracionSemestres = duracionSemestres;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public int getCreditosTotales() {
        return creditosTotales;
    }

    public void setCreditosTotales(int creditosTotales) {
        this.creditosTotales = creditosTotales;
    }

    public int getTotalMaterias() {
        return materias != null ? materias.size() : 0;
    }

    @Override
    public String toString() {
        return String.format("Carrera{nombre='%s', codigo='%s', duracion=%d semestres, materias=%d, creditos=%d}",
                nombre, codigo, duracionSemestres, getTotalMaterias(), creditosTotales);
    }

    public void addMateria(Materia materia) {
        for(Materia materia1 : materias) {
            if(materia1.getAcronimo().equals(materia.getAcronimo())) {
                throw new IllegalArgumentException("La materia con acronimo " + materia.getAcronimo() + " ya existe en la carrera " + this.codigo);
            }
        }
        this.materias.add(materia);
    }
}