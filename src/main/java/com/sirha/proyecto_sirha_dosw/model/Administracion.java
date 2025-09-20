package com.sirha.proyecto_sirha_dosw.model;

public class Administracion extends Carrera {
    public Administracion() {
        super("Administración de Empresas");
    }

    @Override
    protected void configurarMaterias() {
        obligatorias.add(new Materia("501", "acronimo","Contabilidad I",3,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        obligatorias.add(new Materia("502", "acronimo","Microeconomía",2,
                new EstrategiaTresCortes(0.4, 0.3, 0.3)));

        electivas.add(new Materia("601", "acronimo","Mercadeo Digital",2,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
        electivas.add(new Materia("602", "acronimo","Comportamiento Organizacional",1,
                new EstrategiaTresCortes(0.3, 0.3, 0.4)));
    }
}
