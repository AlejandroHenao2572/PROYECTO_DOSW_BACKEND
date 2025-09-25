package com.sirha.proyecto_sirha_dosw.model.builder;

import java.util.List;

import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Materia;

public interface CarreraBuilder {
    CarreraBuilder nombre(Facultad nombre);
    CarreraBuilder codigo(String codigo);
    CarreraBuilder duracionSemestres(int duracion);
    CarreraBuilder materias(List<Materia> materias);
    CarreraBuilder creditosTotales(int creditos);
    Carrera build();
}