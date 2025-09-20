package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Grupo")
public class Grupo {
  
    @Id
    private String idGrupo;   // id_grupo

    @Field("id_materia")
    private String idMateria;

    private Profesor profesor;

    @Field("cupos_maximos")
    private int cuposMaximos;

    @Field("cupos_asignados")
    private int cuposAsignados;

    private List<Horario> horario;

    // Getters y Setters
    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public void setCuposMaximos(int cuposMaximos) {
        this.cuposMaximos = cuposMaximos;
    }

    public int getCuposAsignados() {
        return cuposAsignados;
    }

    public void setCuposAsignados(int cuposAsignados) {
        this.cuposAsignados = cuposAsignados;
    }

    public List<Horario> getHorario() {
        return horario;
    }

    public void setHorario(List<Horario> horario) {
        this.horario = horario;
    }
}


