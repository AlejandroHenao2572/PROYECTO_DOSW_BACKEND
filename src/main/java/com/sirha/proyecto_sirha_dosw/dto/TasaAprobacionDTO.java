/**
 * DTO para representar las tasas de aprobación y rechazo de solicitudes.
 * Contiene métricas estadísticas sobre el procesamiento de solicitudes en el sistema.
 */
package com.sirha.proyecto_sirha_dosw.dto;

import java.time.LocalDateTime;

public class TasaAprobacionDTO {
    
    private long totalSolicitudes;
    private long solicitudesAprobadas;
    private long solicitudesRechazadas;
    private long solicitudesPendientes;
    private long solicitudesEnRevision;
    
    private double tasaAprobacion;
    private double tasaRechazo;
    private double tasaPendientes;
    private double tasaEnRevision;
    
    private String facultad;
    private String tipoSolicitud;
    private LocalDateTime fechaConsulta;
    
    /**
     * Constructor por defecto.
     */
    public TasaAprobacionDTO() {
        this.fechaConsulta = LocalDateTime.now();
    }
    
    /**
     * Constructor con parámetros básicos para cálculo automático de tasas.
     * @param totalSolicitudes Total de solicitudes
     * @param solicitudesAprobadas Cantidad de solicitudes aprobadas
     * @param solicitudesRechazadas Cantidad de solicitudes rechazadas
     * @param solicitudesPendientes Cantidad de solicitudes pendientes
     * @param solicitudesEnRevision Cantidad de solicitudes en revisión
     */
    public TasaAprobacionDTO(long totalSolicitudes, long solicitudesAprobadas, 
                           long solicitudesRechazadas, long solicitudesPendientes, 
                           long solicitudesEnRevision) {
        this();
        this.totalSolicitudes = totalSolicitudes;
        this.solicitudesAprobadas = solicitudesAprobadas;
        this.solicitudesRechazadas = solicitudesRechazadas;
        this.solicitudesPendientes = solicitudesPendientes;
        this.solicitudesEnRevision = solicitudesEnRevision;
        
        calcularTasas();
    }
    
    /**
     * Calcula las tasas como porcentajes del total.
     */
    private void calcularTasas() {
        if (totalSolicitudes > 0) {
            this.tasaAprobacion = (double) solicitudesAprobadas / totalSolicitudes * 100;
            this.tasaRechazo = (double) solicitudesRechazadas / totalSolicitudes * 100;
            this.tasaPendientes = (double) solicitudesPendientes / totalSolicitudes * 100;
            this.tasaEnRevision = (double) solicitudesEnRevision / totalSolicitudes * 100;
        } else {
            this.tasaAprobacion = 0.0;
            this.tasaRechazo = 0.0;
            this.tasaPendientes = 0.0;
            this.tasaEnRevision = 0.0;
        }
    }
    
    /**
     * Recalcula las tasas cuando se actualizan los valores.
     */
    public void actualizarTasas() {
        calcularTasas();
    }
    
    // Getters y setters
    public long getTotalSolicitudes() { return totalSolicitudes; }
    public void setTotalSolicitudes(long totalSolicitudes) { 
        this.totalSolicitudes = totalSolicitudes;
        calcularTasas();
    }
    
    public long getSolicitudesAprobadas() { return solicitudesAprobadas; }
    public void setSolicitudesAprobadas(long solicitudesAprobadas) { 
        this.solicitudesAprobadas = solicitudesAprobadas;
        calcularTasas();
    }
    
    public long getSolicitudesRechazadas() { return solicitudesRechazadas; }
    public void setSolicitudesRechazadas(long solicitudesRechazadas) { 
        this.solicitudesRechazadas = solicitudesRechazadas;
        calcularTasas();
    }
    
    public long getSolicitudesPendientes() { return solicitudesPendientes; }
    public void setSolicitudesPendientes(long solicitudesPendientes) { 
        this.solicitudesPendientes = solicitudesPendientes;
        calcularTasas();
    }
    
    public long getSolicitudesEnRevision() { return solicitudesEnRevision; }
    public void setSolicitudesEnRevision(long solicitudesEnRevision) { 
        this.solicitudesEnRevision = solicitudesEnRevision;
        calcularTasas();
    }
    
    public double getTasaAprobacion() { return tasaAprobacion; }
    public void setTasaAprobacion(double tasaAprobacion) { this.tasaAprobacion = tasaAprobacion; }
    
    public double getTasaRechazo() { return tasaRechazo; }
    public void setTasaRechazo(double tasaRechazo) { this.tasaRechazo = tasaRechazo; }
    
    public double getTasaPendientes() { return tasaPendientes; }
    public void setTasaPendientes(double tasaPendientes) { this.tasaPendientes = tasaPendientes; }
    
    public double getTasaEnRevision() { return tasaEnRevision; }
    public void setTasaEnRevision(double tasaEnRevision) { this.tasaEnRevision = tasaEnRevision; }
    
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
    
    public String getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(String tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }
    
    public LocalDateTime getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
}