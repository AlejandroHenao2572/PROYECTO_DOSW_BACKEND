package com.sirha.proyecto_sirha_dosw.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "materias")
public class Materia {
     @Id
    private String id;

    private String nombre;
    private int creditos;
    private String facultad;

    public Materia(String nombre, int creditos, String facultad) {
        this.nombre = nombre;
        this.creditos = creditos;
        this.facultad = facultad;

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getCreditos() {
        return creditos;
    }
    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    public String getFacultad() {
        return facultad;
    }
    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

}
