package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "grupos")
public class Grupo {
    @Id
    private String id;
    private String numero;
    private String materiaId;
    private String profesorId;
    private Integer capacidadMaxima;
    private List<String> estudiantesInscritos;
    private Horario horario;
    private String salon;
    private String semestre;
    
    public Grupo() {
        this.estudiantesInscritos = new ArrayList<>();
    }
    
    public Grupo(String numero, String materiaId, String profesorId, Integer capacidadMaxima, String semestre) {
        this();
        this.numero = numero;
        this.materiaId = materiaId;
        this.profesorId = profesorId;
        this.capacidadMaxima = capacidadMaxima;
        this.semestre = semestre;
    }
    
    // Métodos de lógica de negocio
    public boolean estaCompleto() {
        return estudiantesInscritos.size() >= capacidadMaxima;
    }
    
    public int getCuposDisponibles() {
        return capacidadMaxima - estudiantesInscritos.size();
    }
    
    public int getCantidadInscritos() {
        return estudiantesInscritos.size();
    }
    
    public boolean inscribirEstudiante(String estudianteId) {
        if (!estaCompleto() && !estudiantesInscritos.contains(estudianteId)) {
            estudiantesInscritos.add(estudianteId);
            return true;
        }
        return false;
    }
    
    public boolean desinscribirEstudiante(String estudianteId) {
        return estudiantesInscritos.remove(estudianteId);
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    
    public String getProfesorId() { return profesorId; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }
    
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    
    public List<String> getEstudiantesInscritos() { return estudiantesInscritos; }
    public void setEstudiantesInscritos(List<String> estudiantesInscritos) { this.estudiantesInscritos = estudiantesInscritos; }
    
    public Horario getHorario() { return horario; }
    public void setHorario(Horario horario) { this.horario = horario; }
    
    public String getSalon() { return salon; }
    public void setSalon(String salon) { this.salon = salon; }
    
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }
}