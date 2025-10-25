package com.sirha.proyecto_sirha_dosw.dto;

import java.time.LocalDate;

import com.sirha.proyecto_sirha_dosw.dto.base.AbstractDateRangeDTO;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO para configurar el plazo de solicitudes.
 * Permite a los decanos establecer las fechas de inicio y fin para recibir solicitudes.
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlazoSolicitudesDTO extends AbstractDateRangeDTO {

    @Builder
    public PlazoSolicitudesDTO(LocalDate fechaInicio, LocalDate fechaFin) {
        super(fechaInicio, fechaFin);
    }
}