package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@TypeAlias("Profesor")
public class Profesor extends Usuario {

    private String departamento;
    private String especialidad;

    @DBRef
    private List<Curso> cursosAsignados;

    public Profesor() {}

    public Profesor(String nombre, String email, String password, Rol rol,
                    String departamento, String especialidad, List<Curso> cursosAsignados) {
        super(nombre, email, password, rol);
        this.departamento = departamento;
        this.especialidad = especialidad;
        this.cursosAsignados = cursosAsignados;
    }

    // Getters y setters
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<Curso> getCursosAsignados() {
        return cursosAsignados;
    }

    public void setCursosAsignados(List<Curso> cursosAsignados) {
        this.cursosAsignados = cursosAsignados;
    }
}




