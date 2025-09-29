package com.sirha.proyecto_sirha_dosw.dto;

import java.time.LocalDate;

/**
 * DTO para configurar el calendario académico.
 * Permite a los decanos establecer las fechas de inicio y fin del semestre.
 */
public class CalendarioAcademicoDTO {
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    /**
     * Constructor por defecto.
     */
    public CalendarioAcademicoDTO() {
    }
    
    /**
     * Constructor con parámetros.
     * @param fechaInicio fecha de inicio del calendario académico
     * @param fechaFin fecha de fin del calendario académico
     */
    public CalendarioAcademicoDTO(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    // Getters y setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    /**
     * Valida que las fechas sean consistentes.
     * @return true si las fechas son válidas, false en caso contrario
     */
    public boolean esFechasValidas() {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        return fechaInicio.isBefore(fechaFin) || fechaInicio.isEqual(fechaFin);
    }
    
    @Override
    public String toString() {
        return "CalendarioAcademicoDTO{" +
                "fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}