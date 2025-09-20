package com.sirha.proyecto_sirha_dosw.model;

public class FranjaHoraria {
    private String horaInicio;
    private String horaFin;

    public FranjaHoraria(String horaInicio, String horaFin) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }

    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FranjaHoraria that = (FranjaHoraria) obj;
        return horaInicio.equals(that.horaInicio) && horaFin.equals(that.horaFin);
    }

    @Override
    public int hashCode() {
        return horaInicio.hashCode() * 31 + horaFin.hashCode();
    }
}