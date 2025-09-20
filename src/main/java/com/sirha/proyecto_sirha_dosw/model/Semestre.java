package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public class Semestre {
    private int numero;
    private List<RegistroMateria> materias;

    public Semestre(int numero) {
        this.numero = numero;
        this.materias = new ArrayList<>();
    }

    public void agregarRegistro(RegistroMateria r) {
        materias.add(r);
    }

    public double calcularPromedio() {
        return materias.stream()
                .mapToDouble(RegistroMateria::getNotaFinal)
                .average().orElse(0.0);
    }

    public List<RegistroMateria> getMaterias() { return materias; }
    public int getNumero() { return numero; }
}