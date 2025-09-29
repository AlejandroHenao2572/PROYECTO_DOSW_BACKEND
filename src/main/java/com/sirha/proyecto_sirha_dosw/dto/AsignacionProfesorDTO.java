package com.sirha.proyecto_sirha_dosw.dto;

/**
 * DTO para la asignación de profesores a grupos.
 * Se utiliza para enviar datos de asignación desde el cliente.
 */
public class AsignacionProfesorDTO {
    
    private String grupoId;
    private String profesorId;
    
    // Constructor por defecto
    public AsignacionProfesorDTO() {
    }
    
    // Constructor con parámetros
    public AsignacionProfesorDTO(String grupoId, String profesorId) {
        this.grupoId = grupoId;
        this.profesorId = profesorId;
    }
    
    // Getters y Setters
    public String getGrupoId() {
        return grupoId;
    }
    
    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }
    
    public String getProfesorId() {
        return profesorId;
    }
    
    public void setProfesorId(String profesorId) {
        this.profesorId = profesorId;
    }
}