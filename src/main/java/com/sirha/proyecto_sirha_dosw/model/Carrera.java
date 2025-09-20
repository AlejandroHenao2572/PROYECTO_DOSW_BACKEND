package com.sirha.proyecto_sirha_dosw.model;

import com.sirha.proyecto_sirha_dosw.model.Materia;

import java.util.ArrayList;
import java.util.List;

public abstract class Carrera {
    protected String nombre;
    protected List<Materia> obligatorias;
    protected List<Materia> electivas;

    public Carrera(String nombre) {
        this.nombre = nombre;
        this.obligatorias = new ArrayList<>();
        this.electivas = new ArrayList<>();
        configurarMaterias(); // Hook: cada subclase define sus materias
    }

    protected abstract void configurarMaterias();

    public String getNombre() { return nombre; }
    public List<Materia> getObligatorias() { return obligatorias; }
    public List<Materia> getElectivas() { return electivas; }

}
