package com.sirha.proyecto_sirha_dosw.dto.base;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base class for DTOs that expose a pair of start and end dates.
 * Provides common behaviour to validate the range and to reuse getters/setters via Lombok.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDateRangeDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /**
     * Validates the stored dates, ensuring both are non-null and the start is not after the end.
     *
     * @return {@code true} when the range is valid, {@code false} otherwise.
     */
    public boolean esFechasValidas() {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return !fechaInicio.isAfter(fechaFin);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
