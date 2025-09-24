package com.sirha.api.model.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sirha.api.model.Carrera;
import com.sirha.api.model.Facultad;
import com.sirha.api.model.Materia;

public class ImpCarreraBuilder implements CarreraBuilder {
    private Facultad nombre;
    private String codigo;
    private int duracionSemestres;
    private List<Materia> materias = new ArrayList<>();
    private int creditosTotales;

    @Override
    public CarreraBuilder nombre(Facultad nombre) {
        this.nombre = nombre;
        return this;
    }

    @Override
    public CarreraBuilder codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    @Override
    public CarreraBuilder duracionSemestres(int duracion) {
        this.duracionSemestres = duracion;
        return this;
    }

    @Override
    public CarreraBuilder materias(List<Materia> materias) {
        this.materias = materias;
        return this;
    }


    @Override
    public CarreraBuilder creditosTotales(int creditos) {
        this.creditosTotales = creditos;
        return this;
    }

    @Override
    public Carrera build() {
        List<Materia> materiasList = materias != null ? new ArrayList<>(materias) : null;
        return new Carrera(nombre, codigo, duracionSemestres, materiasList, creditosTotales);
}
}
