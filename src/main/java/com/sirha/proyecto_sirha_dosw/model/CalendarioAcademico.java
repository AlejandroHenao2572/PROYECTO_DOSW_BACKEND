package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

public enum CalendarioAcademico {
    INSTANCIA; // singleton

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Constructor del singleton
    CalendarioAcademico() {
        // Se inicializan los valores por defecto, calendario 2025-2
        fechaInicio = LocalDate.of(2025, 7, 11);
        fechaFin = LocalDate.of(2025, 12, 17);
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

