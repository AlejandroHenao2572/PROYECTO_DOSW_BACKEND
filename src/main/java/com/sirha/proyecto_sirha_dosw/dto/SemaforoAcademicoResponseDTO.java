package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.SemaforoAcademico;
import java.util.Map;

public class SemaforoAcademicoResponseDTO {
    private String estudianteId;
    private int semestreActual;
    private Map<String, SemaforoAcademico> estadoPorMateria;
    private EstadisticasAvance estadisticas;
    
    public SemaforoAcademicoResponseDTO() {}
    
    // Getters y Setters
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    
    public int getSemestreActual() { return semestreActual; }
    public void setSemestreActual(int semestreActual) { this.semestreActual = semestreActual; }
    
    public Map<String, SemaforoAcademico> getEstadoPorMateria() { return estadoPorMateria; }
    public void setEstadoPorMateria(Map<String, SemaforoAcademico> estadoPorMateria) { this.estadoPorMateria = estadoPorMateria; }
    
    public EstadisticasAvance getEstadisticas() { return estadisticas; }
    public void setEstadisticas(EstadisticasAvance estadisticas) { this.estadisticas = estadisticas; }
    
    public static class EstadisticasAvance {
        private int materiasVerde;
        private int materiasAzul;
        private int materiasRojo;
        private int materiasBlanco;
        private double porcentajeAvance;
        
        // Constructores, getters y setters
        public EstadisticasAvance() {}
        
        public int getMateriasVerde() { return materiasVerde; }
        public void setMateriasVerde(int materiasVerde) { this.materiasVerde = materiasVerde; }
        
        public int getMateriasAzul() { return materiasAzul; }
        public void setMateriasAzul(int materiasAzul) { this.materiasAzul = materiasAzul; }
        
        public int getMateriasRojo() { return materiasRojo; }
        public void setMateriasRojo(int materiasRojo) { this.materiasRojo = materiasRojo; }
        
        public int getMateriasBlanco() { return materiasBlanco; }
        public void setMateriasBlanco(int materiasBlanco) { this.materiasBlanco = materiasBlanco; }
        
        public double getPorcentajeAvance() { return porcentajeAvance; }
        public void setPorcentajeAvance(double porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }
    }
}