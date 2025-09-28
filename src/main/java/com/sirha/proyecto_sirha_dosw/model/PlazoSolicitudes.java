package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

public enum PlazoSolicitudes {
    INSTANCIA; // singleton

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Constructor del singleton
    PlazoSolicitudes() {
        // Se inicializan los valores por defecto
        fechaInicio = LocalDate.now();
        fechaFin = fechaInicio.plusDays(10); // 10 dias de plazo por defecto
    }

    // Getters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    // Setters (si quieres modificar el plazo)
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    // Método utilitario: verificar si una fecha está en plazo
    public boolean estaEnPlazo(LocalDate fecha) {
        return (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) 
                && (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin));
    }
}

