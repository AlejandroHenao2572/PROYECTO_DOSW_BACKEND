package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class Destino {

    @Field("id_grupo")
    private String idGrupo;

    private String materia;

    private List<Horario> horario;

    // Getters y Setters
    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public List<Horario> getHorario() {
        return horario;
    }

    public void setHorario(List<Horario> horario) {
        this.horario = horario;
    }
}
