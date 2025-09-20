package com.sirha.proyecto_sirha_dosw.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "curso")
public class Materia {
    @Id
    private String id;
    private String codigo;
    private String acronimo;
    private String nombre;
    private int creditos;
    private EstrategiaCalculo estrategiaCalculo;

    public Materia(String codigo,String acronimo, String nombre, int creditos, EstrategiaCalculo estrategiaCalculo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.acronimo = acronimo;
        this.creditos = creditos;
        this.estrategiaCalculo = estrategiaCalculo;
    }

    public Double calcularNota(List<Double> notas){
        return this.estrategiaCalculo.calcularNota(notas);
    };


    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

}
