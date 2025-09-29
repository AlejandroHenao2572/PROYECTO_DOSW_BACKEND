/**
 * DTO para representar las estadísticas de grupos más solicitados para cambio.
 * Contiene información del grupo y la cantidad de solicitudes realizadas.
 */
package com.sirha.proyecto_sirha_dosw.dto;

public class EstadisticasGrupoDTO {
    
    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private String materiaAcronimo;
    private String facultad;
    private int capacidad;
    private int cantidadInscritos;
    private long cantidadSolicitudes;
    private double porcentajeOcupacion;
    
    /**
     * Constructor por defecto.
     */
    public EstadisticasGrupoDTO() {
    }
    
    /**
     * Constructor con parámetros principales de identificación.
     * @param grupoId ID del grupo
     * @param materiaId ID de la materia
     * @param materiaNombre Nombre de la materia
     * @param materiaAcronimo Acrónimo de la materia
     * @param facultad Facultad a la que pertenece
     */
    public EstadisticasGrupoDTO(String grupoId, String materiaId, String materiaNombre, 
                               String materiaAcronimo, String facultad) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.materiaAcronimo = materiaAcronimo;
        this.facultad = facultad;
    }
    
    // Getters y setters
    public String getGrupoId() { return grupoId; }
    public void setGrupoId(String grupoId) { this.grupoId = grupoId; }
    
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    
    public String getMateriaNombre() { return materiaNombre; }
    public void setMateriaNombre(String materiaNombre) { this.materiaNombre = materiaNombre; }
    
    public String getMateriaAcronimo() { return materiaAcronimo; }
    public void setMateriaAcronimo(String materiaAcronimo) { this.materiaAcronimo = materiaAcronimo; }
    
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
    
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { 
        this.capacidad = capacidad;
        this.porcentajeOcupacion = capacidad > 0 ? (double) cantidadInscritos / capacidad * 100 : 0;
    }
    
    public int getCantidadInscritos() { return cantidadInscritos; }
    public void setCantidadInscritos(int cantidadInscritos) { 
        this.cantidadInscritos = cantidadInscritos;
        this.porcentajeOcupacion = capacidad > 0 ? (double) cantidadInscritos / capacidad * 100 : 0;
    }
    
    public long getCantidadSolicitudes() { return cantidadSolicitudes; }
    public void setCantidadSolicitudes(long cantidadSolicitudes) { this.cantidadSolicitudes = cantidadSolicitudes; }
    
    public double getPorcentajeOcupacion() { return porcentajeOcupacion; }
    public void setPorcentajeOcupacion(double porcentajeOcupacion) { this.porcentajeOcupacion = porcentajeOcupacion; }
}