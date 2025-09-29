package com.sirha.proyecto_sirha_dosw.dto;

import java.util.List;

/**
 * DTO para el monitoreo de carga de grupos.
 * Contiene información sobre la ocupación de un grupo y alertas de capacidad.
 */
public class MonitoreoGrupoDTO {
    
    private static final String ESTUDIANTES_SUFFIX = " estudiantes)";
    
    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private int capacidad;
    private int cantidadInscritos;
    private double porcentajeOcupacion;
    private boolean alertaCapacidad;
    private String nivelAlerta; // "NORMAL", "ADVERTENCIA", "CRITICO"
    private String mensaje;
    private List<String> estudiantesId;
    private String profesorId;
    
    // Constructor por defecto
    public MonitoreoGrupoDTO() {
    }
    
    // Constructor con parámetros principales
    public MonitoreoGrupoDTO(String grupoId, String materiaId, String materiaNombre, 
                            int capacidad, int cantidadInscritos) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.capacidad = capacidad;
        this.cantidadInscritos = cantidadInscritos;
        this.porcentajeOcupacion = calcularPorcentajeOcupacion();
        this.alertaCapacidad = porcentajeOcupacion >= 90.0;
        this.nivelAlerta = determinarNivelAlerta();
        this.mensaje = generarMensaje();
    }
    
    /**
     * Calcula el porcentaje de ocupación del grupo.
     * @return porcentaje de ocupación (0-100)
     */
    private double calcularPorcentajeOcupacion() {
        if (capacidad == 0) return 0.0;
        return ((double) cantidadInscritos / capacidad) * 100.0;
    }
    
    /**
     * Determina el nivel de alerta basado en el porcentaje de ocupación.
     * @return nivel de alerta como String
     */
    private String determinarNivelAlerta() {
        if (porcentajeOcupacion >= 100.0) {
            return "CRITICO";
        } else if (porcentajeOcupacion >= 90.0) {
            return "ADVERTENCIA";
        } else {
            return "NORMAL";
        }
    }
    
    /**
     * Genera un mensaje descriptivo basado en el nivel de alerta.
     * @return mensaje descriptivo
     */
    private String generarMensaje() {
        switch (nivelAlerta) {
            case "CRITICO":
                return "El grupo ha alcanzado su capacidad máxima (" + capacidad + ESTUDIANTES_SUFFIX;
            case "ADVERTENCIA":
                return "ALERTA: El grupo ha superado el 90% de su capacidad (" + cantidadInscritos + "/" + capacidad + ESTUDIANTES_SUFFIX;
            case "NORMAL":
                return "El grupo tiene capacidad disponible (" + cantidadInscritos + "/" + capacidad + ESTUDIANTES_SUFFIX;
            default:
                return "Estado del grupo: " + cantidadInscritos + "/" + capacidad + ESTUDIANTES_SUFFIX;
        }
    }
    
    // Getters y Setters
    public String getGrupoId() {
        return grupoId;
    }
    
    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }
    
    public String getMateriaId() {
        return materiaId;
    }
    
    public void setMateriaId(String materiaId) {
        this.materiaId = materiaId;
    }
    
    public String getMateriaNombre() {
        return materiaNombre;
    }
    
    public void setMateriaNombre(String materiaNombre) {
        this.materiaNombre = materiaNombre;
    }
    
    public int getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
        this.porcentajeOcupacion = calcularPorcentajeOcupacion();
        this.alertaCapacidad = porcentajeOcupacion >= 90.0;
        this.nivelAlerta = determinarNivelAlerta();
        this.mensaje = generarMensaje();
    }
    
    public int getCantidadInscritos() {
        return cantidadInscritos;
    }
    
    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
        this.porcentajeOcupacion = calcularPorcentajeOcupacion();
        this.alertaCapacidad = porcentajeOcupacion >= 90.0;
        this.nivelAlerta = determinarNivelAlerta();
        this.mensaje = generarMensaje();
    }
    
    public double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }
    
    public void setPorcentajeOcupacion(double porcentajeOcupacion) {
        this.porcentajeOcupacion = porcentajeOcupacion;
    }
    
    public boolean isAlertaCapacidad() {
        return alertaCapacidad;
    }
    
    public void setAlertaCapacidad(boolean alertaCapacidad) {
        this.alertaCapacidad = alertaCapacidad;
    }
    
    public String getNivelAlerta() {
        return nivelAlerta;
    }
    
    public void setNivelAlerta(String nivelAlerta) {
        this.nivelAlerta = nivelAlerta;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public List<String> getEstudiantesId() {
        return estudiantesId;
    }
    
    public void setEstudiantesId(List<String> estudiantesId) {
        this.estudiantesId = estudiantesId;
    }
    
    public String getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(String profesorId) {
        this.profesorId = profesorId;
    }
}