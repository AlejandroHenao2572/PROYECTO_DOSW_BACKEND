package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Horario;
import java.util.List;

/**
 * DTO para mostrar la disponibilidad de un grupo específico.
 * Incluye información sobre capacidad, cupos disponibles y lista de espera.
 */
public class DisponibilidadGrupoDTO {
    
    private String grupoId;
    private String nombreMateria;
    private String acronimoMateria;
    private int capacidadMaxima;
    private int cantidadInscritos;
    private int cuposDisponibles;
    private boolean estaCompleto;
    private List<Horario> horarios;
    private List<String> listaEspera; // IDs de estudiantes en lista de espera
    
    // Constructor por defecto
    public DisponibilidadGrupoDTO() {}
    
    // Constructor completo
    public DisponibilidadGrupoDTO(String grupoId, String nombreMateria, String acronimoMateria, 
                                 int capacidadMaxima, int cantidadInscritos, boolean estaCompleto, 
                                 List<Horario> horarios, List<String> listaEspera) {
        this.grupoId = grupoId;
        this.nombreMateria = nombreMateria;
        this.acronimoMateria = acronimoMateria;
        this.capacidadMaxima = capacidadMaxima;
        this.cantidadInscritos = cantidadInscritos;
        this.cuposDisponibles = capacidadMaxima - cantidadInscritos;
        this.estaCompleto = estaCompleto;
        this.horarios = horarios;
        this.listaEspera = listaEspera;
    }
    
    // Getters y Setters
    public String getGrupoId() {
        return grupoId;
    }
    
    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }
    
    public String getNombreMateria() {
        return nombreMateria;
    }
    
    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }
    
    public String getAcronimoMateria() {
        return acronimoMateria;
    }
    
    public void setAcronimoMateria(String acronimoMateria) {
        this.acronimoMateria = acronimoMateria;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.cuposDisponibles = this.capacidadMaxima - this.cantidadInscritos;
    }
    
    public int getCantidadInscritos() {
        return cantidadInscritos;
    }
    
    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
        this.cuposDisponibles = this.capacidadMaxima - this.cantidadInscritos;
    }
    
    public int getCuposDisponibles() {
        return cuposDisponibles;
    }
    
    public boolean isEstaCompleto() {
        return estaCompleto;
    }
    
    public void setEstaCompleto(boolean estaCompleto) {
        this.estaCompleto = estaCompleto;
    }
    
    public List<Horario> getHorarios() {
        return horarios;
    }
    
    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
    
    public List<String> getListaEspera() {
        return listaEspera;
    }
    
    public void setListaEspera(List<String> listaEspera) {
        this.listaEspera = listaEspera;
    }
}