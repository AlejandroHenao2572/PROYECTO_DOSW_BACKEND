package com.sirha.proyecto_sirha_dosw.dto;

/**
 * DTO para mostrar información de capacidad de un grupo.
 * Incluye datos sobre inscripciones actuales, capacidad máxima y porcentaje de ocupación.
 */
public class CapacidadGrupoDTO {
    
    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private String materiaAcronimo;
    private int capacidadMaxima;
    private int estudiantesInscritos;
    private double porcentajeOcupacion;
    private int cuposDisponibles;
    private boolean estaCompleto;
    private String profesorId;
    private String profesorNombre;
    
    // Constructor por defecto
    public CapacidadGrupoDTO() {
    }
    
    // Constructor con parámetros principales
    public CapacidadGrupoDTO(String grupoId, String materiaId, String materiaNombre, 
                            String materiaAcronimo, int capacidadMaxima, int estudiantesInscritos) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.materiaAcronimo = materiaAcronimo;
        this.capacidadMaxima = capacidadMaxima;
        this.estudiantesInscritos = estudiantesInscritos;
        this.porcentajeOcupacion = calcularPorcentajeOcupacion();
        this.cuposDisponibles = capacidadMaxima - estudiantesInscritos;
        this.estaCompleto = estudiantesInscritos >= capacidadMaxima;
    }
    
    /**
     * Calcula el porcentaje de ocupación del grupo.
     * @return porcentaje de ocupación (0-100)
     */
    private double calcularPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0.0;
        return Math.round(((double) estudiantesInscritos / capacidadMaxima) * 100.0 * 100.0) / 100.0;
    }
    
    /**
     * Actualiza los cálculos cuando se modifican los valores base.
     */
    private void actualizarCalculos() {
        this.porcentajeOcupacion = calcularPorcentajeOcupacion();
        this.cuposDisponibles = capacidadMaxima - estudiantesInscritos;
        this.estaCompleto = estudiantesInscritos >= capacidadMaxima;
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
    
    public String getMateriaAcronimo() {
        return materiaAcronimo;
    }
    
    public void setMateriaAcronimo(String materiaAcronimo) {
        this.materiaAcronimo = materiaAcronimo;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        actualizarCalculos();
    }
    
    public int getEstudiantesInscritos() {
        return estudiantesInscritos;
    }
    
    public void setEstudiantesInscritos(int estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
        actualizarCalculos();
    }
    
    public double getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }
    
    public void setPorcentajeOcupacion(double porcentajeOcupacion) {
        this.porcentajeOcupacion = porcentajeOcupacion;
    }
    
    public int getCuposDisponibles() {
        return cuposDisponibles;
    }
    
    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }
    
    public boolean isEstaCompleto() {
        return estaCompleto;
    }
    
    public void setEstaCompleto(boolean estaCompleto) {
        this.estaCompleto = estaCompleto;
    }
    
    public String getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(String profesorId) {
        this.profesorId = profesorId;
    }
    
    public String getProfesorNombre() {
        return profesorNombre;
    }
    
    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }
}