package com.sirha.proyecto_sirha_dosw.model.support;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Simple mutable holder for a start/end {@link LocalDate} pair with helper methods.
 */
public final class MutableDateRange {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public MutableDateRange(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "fechaInicio");
        this.fechaFin = Objects.requireNonNull(fechaFin, "fechaFin");
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "fechaInicio");
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = Objects.requireNonNull(fechaFin, "fechaFin");
    }

    public boolean estaEnPlazo(LocalDate fecha) {
        Objects.requireNonNull(fecha, "fecha");
        return (fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
               (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin));
    }
}
