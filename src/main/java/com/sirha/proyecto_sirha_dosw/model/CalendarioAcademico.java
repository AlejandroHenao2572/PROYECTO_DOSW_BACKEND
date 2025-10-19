package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalDate;

import com.sirha.proyecto_sirha_dosw.model.support.DateRangeHolder;
import com.sirha.proyecto_sirha_dosw.model.support.MutableDateRange;

public enum CalendarioAcademico implements DateRangeHolder {
    INSTANCIA(LocalDate.of(2025, 7, 11), LocalDate.of(2025, 12, 17));

    private final MutableDateRange dateRange;

    CalendarioAcademico(LocalDate fechaInicio, LocalDate fechaFin) {
        this.dateRange = new MutableDateRange(fechaInicio, fechaFin);
    }

    @Override
    public MutableDateRange getDateRange() {
        return dateRange;
    }
}

