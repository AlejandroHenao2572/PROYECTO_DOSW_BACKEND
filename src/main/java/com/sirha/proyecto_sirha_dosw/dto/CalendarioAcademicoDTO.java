package com.sirha.proyecto_sirha_dosw.dto;

import java.time.LocalDate;

import com.sirha.proyecto_sirha_dosw.dto.base.AbstractDateRangeDTO;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO para configurar el calendario académico.
 * Permite a los decanos establecer las fechas de inicio y fin del semestre.
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CalendarioAcademicoDTO extends AbstractDateRangeDTO {

    @Builder
    public CalendarioAcademicoDTO(LocalDate fechaInicio, LocalDate fechaFin) {
        super(fechaInicio, fechaFin);
    }
}