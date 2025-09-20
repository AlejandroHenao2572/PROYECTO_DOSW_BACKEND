package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Horario {
    private Map<Dia, List<ClaseHorario>> clasesPorDia;
    private String semestreId;
    
    public Horario(String semestreId) {
        this.semestreId = semestreId;
        this.clasesPorDia = new HashMap<>();
        // Inicializar todos los días
        for (Dia dia : Dia.values()) {
            this.clasesPorDia.put(dia, new ArrayList<>());
        }
    }
    
    public void agregarClase(Dia dia, FranjaHoraria franja, Materia materia, String salon) {
        ClaseHorario clase = new ClaseHorario(franja, materia, salon);
        this.clasesPorDia.get(dia).add(clase);
    }
    
    public List<ClaseHorario> getClasesPorDia(Dia dia) {
        return clasesPorDia.getOrDefault(dia, new ArrayList<>());
    }
    
    public Map<Dia, List<ClaseHorario>> getClasesPorDia() {
        return clasesPorDia;
    }
    
    public String getSemestreId() {
        return semestreId;
    }
    
    public boolean tieneConflicto(Dia dia, FranjaHoraria franja) {
        return clasesPorDia.get(dia).stream()
                .anyMatch(clase -> clase.getFranja().equals(franja));
    }
    
    public static class ClaseHorario {
        private FranjaHoraria franja;
        private Materia materia;
        private String salon;
        
        public ClaseHorario(FranjaHoraria franja, Materia materia, String salon) {
            this.franja = franja;
            this.materia = materia;
            this.salon = salon;
        }
        
        // Getters
        public FranjaHoraria getFranja() { return franja; }
        public Materia getMateria() { return materia; }
        public String getSalon() { return salon; }
        
        // Setters
        public void setFranja(FranjaHoraria franja) { this.franja = franja; }
        public void setMateria(Materia materia) { this.materia = materia; }
        public void setSalon(String salon) { this.salon = salon; }
    }
}