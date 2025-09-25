/**
 * Clase que representa una carrera académica en el sistema.
 * Contiene información sobre la facultad, duración, créditos y materias que la componen.
 */
package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "carreras")
public class Carrera {
    // Campos
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

    @NotNull
    @NotBlank
    private int creditosTotales;

    private List<Materia> materias = new ArrayList<>();

    /**
     * Constructor por defecto.
     */
    public Carrera() {}

    /**
     * Constructor con parámetros básicos.
     * @param nombre Facultad de la carrera
     * @param codigo Código único de la carrera
     * @param duracionSemestres Duración en semestres de la carrera
     * @param creditosTotales Total de créditos de la carrera
     */
    public Carrera(Facultad nombre, String codigo, int duracionSemestres, int creditosTotales) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionSemestres = duracionSemestres;
        this.creditosTotales = creditosTotales;
    }

    /**
     * Constructor completo con todos los parámetros.
     * @param nombre Facultad de la carrera
     * @param codigo Código único de la carrera
     * @param duracionSemestres Duración en semestres de la carrera
     * @param materias Lista de materias de la carrera
     * @param creditosTotales Total de créditos de la carrera
     */
    public Carrera(Facultad nombre, String codigo,
                   int duracionSemestres, List<Materia> materias, int creditosTotales) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.duracionSemestres = duracionSemestres;
        this.materias = materias;
        this.creditosTotales = creditosTotales;
    }

    // Getters y setters con documentación básica
    public Facultad getNombre() { return nombre; }
    public void setNombre(Facultad nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public int getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(int duracionSemestres) { this.duracionSemestres = duracionSemestres; }
    public List<Materia> getMaterias() { return materias; }
    public void setMaterias(List<Materia> materias) { this.materias = materias; }
    public int getCreditosTotales() { return creditosTotales; }
    public void setCreditosTotales(int creditosTotales) { this.creditosTotales = creditosTotales; }

    /**
     * Obtiene el total de materias en la carrera.
     * @return Número total de materias
     */
    public int getTotalMaterias() {
        return materias != null ? materias.size() : 0;
    }

    /**
     * Agrega una materia a la carrera si no existe ya una con el mismo acrónimo.
     * @param materia Materia a agregar
     * @throws IllegalArgumentException Si ya existe una materia con el mismo acrónimo
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