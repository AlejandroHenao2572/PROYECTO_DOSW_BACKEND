package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;

/**
 * DTO para la respuesta a una solicitud de cambio de grupo.
 * Incluye el nuevo estado y observaciones de la respuesta.
 */
public class RespuestaSolicitudDTO {
    
    private String solicitudId;
    private SolicitudEstado nuevoEstado;
    private String observacionesRespuesta;
    private String decanoId; // ID del decano que responde
    
    // Constructor por defecto
    public RespuestaSolicitudDTO() {}
    
    // Constructor con parámetros
    public RespuestaSolicitudDTO(String solicitudId, SolicitudEstado nuevoEstado, 
                                String observacionesRespuesta, String decanoId) {
        this.solicitudId = solicitudId;
        this.nuevoEstado = nuevoEstado;
        this.observacionesRespuesta = observacionesRespuesta;
        this.decanoId = decanoId;
    }
    
    // Getters y Setters
    public String getSolicitudId() {
        return solicitudId;
    }
    
    public void setSolicitudId(String solicitudId) {
        this.solicitudId = solicitudId;
    }
    
    public SolicitudEstado getNuevoEstado() {
        return nuevoEstado;
    }
    
    public void setNuevoEstado(SolicitudEstado nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
    
    public String getObservacionesRespuesta() {
        return observacionesRespuesta;
    }
    
    public void setObservacionesRespuesta(String observacionesRespuesta) {
        this.observacionesRespuesta = observacionesRespuesta;
    }
    
    public String getDecanoId() {
        return decanoId;
    }
    
    public void setDecanoId(String decanoId) {
        this.decanoId = decanoId;
    }
}