package com.sirha.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa una carrera universitaria dentro del sistema.
 */

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

    /**
     * Constructor básico.
     * @param nombre facultad asociada.
     * @param codigo código único de la carrera.
     * @param duracionSemestres duración en semestres.
     * @param creditosTotales créditos totales de la carrera.
     */

    public Carrera(Facultad nombre, String codigo, int duracionSemestres, int creditosTotales) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionSemestres = duracionSemestres;
        this.creditosTotales = creditosTotales;
    }

    /**
     * Constructor extendido.
     * @param nombre facultad asociada.
     * @param codigo código único de la carrera.
     * @param duracionSemestres duración en semestres.
     * @param materias materias que pertenecen a la carrera.
     * @param creditosTotales créditos totales de la carrera.
     */

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

    /**
     * Calcula el total de materias registradas en la carrera.
     * @return número total de materias.
     */

    public int getTotalMaterias() {
        return materias != null ? materias.size() : 0;
    }

    /**
     * Devuelve una representación en formato String de la carrera.
     * @return descripción con nombre, código, duración, materias y créditos.
     */

    @Override
    public String toString() {
        return String.format("Carrera{nombre='%s', codigo='%s', duracion=%d semestres, materias=%d, creditos=%d}",
                nombre, codigo, duracionSemestres, getTotalMaterias(), creditosTotales);
    }

    /**
     * Agrega una nueva materia a la carrera validando que no existe previamente,
     *          una con el mismo acrónimo.
     * @param materia materia a agregar.
     * @throws IllegalArgumentException si la materia ya existe en la carrera.
     */

    public void addMateria(Materia materia) {
        for(Materia materia1 : materias) {
            if(materia1.getAcronimo().equals(materia.getAcronimo())) {
                throw new IllegalArgumentException("La materia con acronimo " + materia.getAcronimo() + " ya existe en la carrera " + this.codigo);
            }
        }
        this.materias.add(materia);
    }
}