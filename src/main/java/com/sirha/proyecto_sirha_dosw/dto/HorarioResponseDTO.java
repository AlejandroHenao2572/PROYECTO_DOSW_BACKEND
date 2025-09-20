package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Dia;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HorarioResponseDTO {
    private String semestreId;
    private Map<Dia, List<ClaseHorarioDTO>> clasesPorDia;
    
    public HorarioResponseDTO() {}
    
    public HorarioResponseDTO(Horario horario) {
        this.semestreId = horario.getSemestreId();
    }
    
    // Getters y Setters
    public String getSemestreId() { return semestreId; }
    public void setSemestreId(String semestreId) { this.semestreId = semestreId; }
    
    public Map<Dia, List<ClaseHorarioDTO>> getClasesPorDia() { return clasesPorDia; }
    public void setClasesPorDia(Map<Dia, List<ClaseHorarioDTO>> clasesPorDia) { this.clasesPorDia = clasesPorDia; }
    
    public static class ClaseHorarioDTO {
        private String franja;
        private String materia;
        private String salon;
        
        // Constructores, getters y setters
        public ClaseHorarioDTO() {}
        
        public ClaseHorarioDTO(String franja, String materia, String salon) {
            this.franja = franja;
            this.materia = materia;
            this.salon = salon;
        }
        
        public String getFranja() { return franja; }
        public void setFranja(String franja) { this.franja = franja; }
        
        public String getMateria() { return materia; }
        public void setMateria(String materia) { this.materia = materia; }
        
        public String getSalon() { return salon; }
        public void setSalon(String salon) { this.salon = salon; }
    }
}
