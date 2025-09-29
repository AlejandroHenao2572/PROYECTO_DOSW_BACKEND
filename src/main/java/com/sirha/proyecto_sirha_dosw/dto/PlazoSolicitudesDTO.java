package com.sirha.proyecto_sirha_dosw.dto;

import java.time.LocalDate;

/**
 * DTO para configurar el plazo de solicitudes.
 * Permite a los decanos establecer las fechas de inicio y fin para recibir solicitudes.
 */
public class PlazoSolicitudesDTO {
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    /**
     * Constructor por defecto.
     */
    public PlazoSolicitudesDTO() {
    }
    
    /**
     * Constructor con parámetros.
     * @param fechaInicio fecha de inicio del plazo de solicitudes
     * @param fechaFin fecha de fin del plazo de solicitudes
     */
    public PlazoSolicitudesDTO(LocalDate fechaInicio, LocalDate fechaFin) {
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
        return "PlazoSolicitudesDTO{" +
                "fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}