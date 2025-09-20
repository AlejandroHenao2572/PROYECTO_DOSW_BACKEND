package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Estudiante extends Usuario {

    @NotBlank(message = "La carrera no puede estar vacío")
    private Carrera carrera;

    @Field("semestre")
    @NotBlank(message = "La semestre no puede estar vacío")
    private int semestreActual;
    private List<Semestre> semestres;
    private Double promedioAcumulado;

    // Nuevos campos para gestion de horarios y grupos
    private Map<String, Horario> horariosPorSemestre; // semestre -> horario
    private Map<String, List<String>> gruposInscritosPorSemestre; // semestre -> lista de grupoIds
    
    public Estudiante(String nombre, String apellido,String email, String password,Rol rol, CarreraTipo carreraTipo) {
        super(nombre,apellido, email,password,rol);
        this.carrera = CarreraFactory.crearCarrera(carreraTipo);
        this.semestres = new ArrayList<>();
        this.horariosPorSemestre = new HashMap<>();
        this.gruposInscritosPorSemestre = new HashMap<>();
    }
    
    public Estudiante(String nombre, String apellido,String email, String password,Rol rol) {
        super(nombre,apellido, email,password,rol);
        this.semestres = new ArrayList<>();
        this.horariosPorSemestre = new HashMap<>();
        this.gruposInscritosPorSemestre = new HashMap<>();
    }

    public void agregarSemestre(Semestre s) {
        semestres.add(s);
        updateSemestre();
        
        String semestreId = String.valueOf(semestres.size());
        horariosPorSemestre.put(semestreId, new Horario(semestreId));
        gruposInscritosPorSemestre.put(semestreId, new ArrayList<>());
    }

    public void updateSemestre(){
        this.semestreActual = semestres.size();
    }
    
    public Semestre getSemestreActual() {
        if (semestres.isEmpty()) return null;
        return semestres.get(semestres.size() - 1);
    }

    public List<Semestre> getHistorial() {
        if (semestres.size() <= 1) return new ArrayList<>();
        return semestres.subList(0, semestres.size() - 1);
    }

    public List<Materia> getMateriasPendientes() {
        if (carrera == null) return new ArrayList<>();
        
        List<Materia> vistas = semestres.stream()
                .flatMap(s -> s.getMaterias().stream())
                .filter(r -> r.getEstado() == SemaforoAcademico.VERDE)
                .map(RegistroMateria::getMateria)
                .collect(Collectors.toList());

        return carrera.getObligatorias().stream()
                .filter(m -> !vistas.contains(m))
                .collect(Collectors.toList());
    }

    /**
     * Agrega una materia al semestre actual del estudiante
     * @param materia Materia a agregar
     */
    public void agregarMateriaActual(Materia materia) {
        Semestre actual = getSemestreActual();
        if (actual != null) {
            actual.agregarRegistro(new RegistroMateria(materia));
        }
    }
    
    // Nuevos métodos para gestión de horarios
    public Horario getHorarioActual() {
        String semestreActualId = String.valueOf(semestreActual);
        return horariosPorSemestre.get(semestreActualId);
    }
    
    public Horario getHorarioPorSemestre(int numeroSemestre) {
        String semestreId = String.valueOf(numeroSemestre);
        return horariosPorSemestre.get(semestreId);
    }
    
    public void setHorarioPorSemestre(int numeroSemestre, Horario horario) {
        String semestreId = String.valueOf(numeroSemestre);
        horariosPorSemestre.put(semestreId, horario);
    }
    
    // Métodos para gestión de grupos
    public List<String> getGruposInscritos() {
        String semestreActualId = String.valueOf(semestreActual);
        return gruposInscritosPorSemestre.getOrDefault(semestreActualId, new ArrayList<>());
    }
    
    public List<String> getGruposInscritosPorSemestre(int numeroSemestre) {
        String semestreId = String.valueOf(numeroSemestre);
        return gruposInscritosPorSemestre.getOrDefault(semestreId, new ArrayList<>());
    }
    
    public void inscribirEnGrupo(String grupoId) {
        String semestreActualId = String.valueOf(semestreActual);
        gruposInscritosPorSemestre.computeIfAbsent(semestreActualId, k -> new ArrayList<>()).add(grupoId);
    }
    
    public void desinscribirDeGrupo(String grupoId) {
        String semestreActualId = String.valueOf(semestreActual);
        List<String> grupos = gruposInscritosPorSemestre.get(semestreActualId);
        if (grupos != null) {
            grupos.remove(grupoId);
        }
    }
    
    // Método para calcular el semáforo académico
    public Map<String, SemaforoAcademico> calcularSemaforoAcademico() {
        Map<String, SemaforoAcademico> semaforoPorMateria = new HashMap<>();
        
        if (getSemestreActual() == null) {
            return semaforoPorMateria;
        }
        
        // Analizar todas las materias del semestre actual
        for (RegistroMateria registro : getSemestreActual().getMaterias()) {
            String materiaId = registro.getMateria().getCodigo();
            semaforoPorMateria.put(materiaId, registro.getEstado());
        }
        
        return semaforoPorMateria;
    }
    
    // Getters y Setters adicionales
    public Map<String, Horario> getHorariosPorSemestre() {
        return horariosPorSemestre;
    }
    
    public void setHorariosPorSemestre(Map<String, Horario> horariosPorSemestre) {
        this.horariosPorSemestre = horariosPorSemestre;
    }
    
    public Map<String, List<String>> getGruposInscritosPorSemestre() {
        return gruposInscritosPorSemestre;
    }
    
    public void setGruposInscritosPorSemestre(Map<String, List<String>> gruposInscritosPorSemestre) {
        this.gruposInscritosPorSemestre = gruposInscritosPorSemestre;
    }

    // Getters originales
    public Carrera getCarrera() { return carrera; }
    public void setCarrera(Carrera carrera) { this.carrera = carrera; }
    
    public List<Semestre> getSemestres() { return semestres; }
    public void setSemestres(List<Semestre> semestres) { this.semestres = semestres; }
    
    public Double getPromedioAcumulado() { return promedioAcumulado; }
    public void setPromedioAcumulado(Double promedioAcumulado) { this.promedioAcumulado = promedioAcumulado; }
}