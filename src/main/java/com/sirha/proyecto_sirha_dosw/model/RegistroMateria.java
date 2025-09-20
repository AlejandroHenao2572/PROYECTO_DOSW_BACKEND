package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public class RegistroMateria {
    private Materia materia;
    private List<Double> notas;
    private SemaforoAcademico estado = SemaforoAcademico.AZUL;

    public RegistroMateria(Materia materia) {
        this.materia = materia;
        this.notas = new ArrayList<>();
    }

    public void agregarNota(double nota) {
        notas.add(nota);
    }

    public Materia getMateria() { return materia; }
    public SemaforoAcademico getEstado() { return estado;}

    public void updateEstado() {
        double nota = getNotaFinal();
        if (nota >= 3.0){
            this.estado = SemaforoAcademico.VERDE;
        }else{
            this.estado = SemaforoAcademico.ROJO;
        }
    }

    public void cancelarMateria() {
        this.estado = SemaforoAcademico.BLANCO;
    }
    public double getNotaFinal(){
        return materia.calcularNota(notas);
    }
}

