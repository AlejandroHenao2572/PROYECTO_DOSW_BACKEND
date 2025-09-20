package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public class RegistroMateria {
    private Materia materia;
    private List<Double> notas;
    private SemaforoAcademico estado;

    public RegistroMateria(Materia materia) {
        this.materia = materia;
        this.notas = new ArrayList<>();
    }

    public void agregarNota(double nota) {
        notas.add(nota);
    }


    public Materia getMateria() { return materia; }
    public SemaforoAcademico getEstado() { return estado; }
}

