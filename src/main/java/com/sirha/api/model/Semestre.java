package com.sirha.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Semestre {

    private int numero;

	private List<RegistroMaterias> registros = new ArrayList<>();

    public Semestre() {
    }

    public List<RegistroMaterias> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroMaterias> registros) {
        this.registros = registros;
    }

    public void addRegistro(RegistroMaterias registro) {
        this.registros.add(registro);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
