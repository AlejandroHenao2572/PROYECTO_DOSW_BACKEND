package com.sirha.proyecto_sirha_dosw.model;


import java.util.List;

public class Horario {
    private String semestre; // "2024-2"
    private List<String> materiasInscritas; // ["Materia 1 - Grupo A", "Materia 2 - Grupo B"]

    // Getters
    public String getSemestre() {
        return semestre;
    }

    public List<String> getMateriasInscritas() {
        return materiasInscritas;
    }

    // Setters
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setMateriasInscritas(List<String> materiasInscritas) {
        this.materiasInscritas = materiasInscritas;
    }
}
