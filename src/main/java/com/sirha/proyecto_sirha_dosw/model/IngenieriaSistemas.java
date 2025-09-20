package com.sirha.proyecto_sirha_dosw.model;

public class IngenieriaSistemas extends Carrera {

    public IngenieriaSistemas() {
        super("Ingeniería de Sistemas");
    }

    @Override
    protected void configurarMaterias() {
        obligatorias.add(new Materia("101", "acronimo","Programación I", 3,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        obligatorias.add(new Materia("102", "acronimo","Cálculo I",3,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        obligatorias.add(new Materia("103","acronimo", "Lógica de Programación", 4,
                new EstrategiaTresCortes(0.4, 0.3, 0.3)));

        electivas.add(new Materia("201", "acronimo","Inteligencia Artificial",3,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        electivas.add(new Materia("202", "acronimo","Fundamentos ",2,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
    }
}
