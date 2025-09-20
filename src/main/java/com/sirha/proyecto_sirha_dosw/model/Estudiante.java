package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Estudiante extends Usuario {

    @Field("carrera")
    @NotNull(message = "La carrera no puede ser nula")
    private Carrera carrera;

    @Field("semestre_actual")
    @NotNull(message = "El semestre actual no puede ser nulo")
    private Integer semestreActual;

    @Field("semestres")
    private List<Semestre> semestres = new ArrayList<>();

    @Field("promedio_acumulado")
    private Double promedioAcumulado;

    public Estudiante(String nombre, String apellido,String email, String password,Rol rol, CarreraTipo carreraTipo) {
        super(nombre,apellido, email,password,rol);
        updateSemestre();
        this.carrera = CarreraFactory.crearCarrera(carreraTipo);
    }
    public Estudiante(String nombre, String apellido,String email, String password,Rol rol) {
        super(nombre,apellido, email,password,rol);
        this.semestres = new ArrayList<>();
    }
    public void agregarSemestre(Semestre s) {
        semestres.add(s);
        updateSemestre();
    }

    public void updateSemestre(){
        this.semestreActual = semestres.size();
    }
    public Semestre getSemestreActual() {
        return semestres.get(semestres.size() - 1);
    }

    public List<Semestre> getHistorial() {
        return semestres.subList(0, semestres.size() - 1);
    }

    public List<Materia> getMateriasPendientes() {
        List<Materia> vistas = semestres.stream()
                .flatMap(s -> s.getMaterias().stream())
                .filter(r -> r.getEstado() == SemaforoAcademico.BLANCO)
                .map(RegistroMateria::getMateria)
                .collect(Collectors.toList());

        return carrera.getObligatorias().stream()
                .filter(m -> !vistas.contains(m))
                .collect(Collectors.toList());
    }


}

