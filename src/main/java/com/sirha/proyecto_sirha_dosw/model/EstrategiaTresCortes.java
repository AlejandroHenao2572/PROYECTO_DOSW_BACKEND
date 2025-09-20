package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class EstrategiaTresCortes implements EstrategiaCalculo {
    private double p1, p2, p3;

    public EstrategiaTresCortes(double p1, double p2, double p3) {
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
    }
    // Getters para Jackson
    public double getP1() { return p1; }
    public double getP2() { return p2; }
    public double getP3() { return p3; }

    @Override
    public double calcularNota(List<Double> notas) {
        return notas.get(0) * p1 + notas.get(1) * p2 + notas.get(2) * p3;
    }
}
