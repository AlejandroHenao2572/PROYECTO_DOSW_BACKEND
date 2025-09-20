package com.sirha.proyecto_sirha_dosw.model;

public class IngenieriaCivil extends Carrera {
    public IngenieriaCivil() {
        super("Ingeniería Civil");
    }

    @Override
    protected void configurarMaterias() {
        obligatorias.add(new Materia("301", "acronimo","Estática",3,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        obligatorias.add(new Materia("302","acronimo", "Cálculo Diferencial",4,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));

        electivas.add(new Materia("401","acronimo", "Geotecnia",3,
                new EstrategiaTresCortes(0.4, 0.3, 0.3)));
        electivas.add(new Materia("402","acronimo", "Diseño Estructural",2,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
    }
}

