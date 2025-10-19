package com.sirha.proyecto_sirha_dosw.model.support;

import java.time.LocalDate;

/**
 * Extracted behaviour for singletons/enums that expose date range configuration.
 */
public interface DateRangeHolder {

    MutableDateRange getDateRange();

    default LocalDate getFechaInicio() {
        return getDateRange().getFechaInicio();
    }

    default LocalDate getFechaFin() {
        return getDateRange().getFechaFin();
    }

    default void setFechaInicio(LocalDate fechaInicio) {
        getDateRange().setFechaInicio(fechaInicio);
    }

    default void setFechaFin(LocalDate fechaFin) {
        getDateRange().setFechaFin(fechaFin);
    }

    default boolean estaEnPlazo(LocalDate fecha) {
        return getDateRange().estaEnPlazo(fecha);
    }
}
