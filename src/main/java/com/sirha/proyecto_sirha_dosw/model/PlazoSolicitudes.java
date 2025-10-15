package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

import com.sirha.proyecto_sirha_dosw.model.support.DateRangeHolder;
import com.sirha.proyecto_sirha_dosw.model.support.MutableDateRange;

public enum PlazoSolicitudes implements DateRangeHolder {
    INSTANCIA(LocalDate.now(), LocalDate.now().plusDays(10));

    private final MutableDateRange dateRange;

    PlazoSolicitudes(LocalDate fechaInicio, LocalDate fechaFin) {
        this.dateRange = new MutableDateRange(fechaInicio, fechaFin);
    }

    @Override
    public MutableDateRange getDateRange() {
        return dateRange;
    }
}

