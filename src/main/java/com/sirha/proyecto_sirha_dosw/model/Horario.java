package com.sirha.proyecto_sirha_dosw.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

public class Horario {

    private Date dia;

    @Field("hora_inicio")
    private Date horaInicio;

    @Field("hora_fin")
    private Date horaFin;

    // Getters y Setters
    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

}
