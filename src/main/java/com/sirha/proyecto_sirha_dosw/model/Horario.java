package com.sirha.proyecto_sirha_dosw.model;


import java.time.LocalDate;
import java.time.LocalTime;

public class Horario {

    private LocalDate dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Getters y Setters
    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

}
