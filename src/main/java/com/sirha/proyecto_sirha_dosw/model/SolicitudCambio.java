package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "solicitudes")
public class SolicitudCambio {

    @Id
    private String id;

    private String idEstudiante;
    private String materiaConProblema;
    private String sugerenciaCambio;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private EstadoSolicitud estado;
    private int prioridad;

    // Getters
    public String getId() {
        return id;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public String getMateriaConProblema() {
        return materiaConProblema;
    }

    public String getSugerenciaCambio() {
        return sugerenciaCambio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public void setMateriaConProblema(String materiaConProblema) {
        this.materiaConProblema = materiaConProblema;
    }

    public void setSugerenciaCambio(String sugerenciaCambio) {
        this.sugerenciaCambio = sugerenciaCambio;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
}
