package com.sirha.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "grupos")
public class Grupo {


	private String id;

    @NotNull
    @NotBlank
	private int capacidad = 0;

    @NotNull
    @NotBlank
	private int cantidadInscritos;

    @NotNull
    @NotBlank
	private boolean estaCompleto;

    @NotNull
    @NotBlank
	private List<Horario> horarios = new ArrayList<>();

    @NotNull
    @NotBlank
	private Materia materia;

    private List<String> estudiantesId = new ArrayList<String>();

    private Profesor profesor;

	public Grupo() {
	}

    public Grupo(Materia materia, int capacidad, List<Horario> horarios) {
        this.materia = materia;
        this.capacidad = capacidad;
        this.horarios = horarios;
        this.cantidadInscritos = 0;
        this.estaCompleto = false;
        this.estudiantesId = new ArrayList<String>();
    }

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}

	public List<String> getEstudiantesId() {
		return estudiantesId;
	}

	public void setEstudiantesId(List<String> estudiantesId) {
		this.estudiantesId = estudiantesId;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public boolean isEstaCompleto() {
		return estaCompleto;
	}

	public void setEstaCompleto(boolean estaCompleto) {
		this.estaCompleto = estaCompleto;
	}

	public int getCantidadInscritos() {
		return cantidadInscritos;
	}

	public void setCantidadInscritos(int cantidadInscritos) {
		this.cantidadInscritos = cantidadInscritos;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public void addEstudiante(String estudianteId) {
        if (!this.estudiantesId.contains(estudianteId)) {
            this.estudiantesId.add(estudianteId);
            this.cantidadInscritos++;
            if (this.cantidadInscritos >= this.capacidad) {
                this.estaCompleto = true;
            }
        }
    }

    public void removeEstudiante(String estudianteId) {
        this.estudiantesId.remove(estudianteId);
        this.cantidadInscritos--;
        if (this.cantidadInscritos < this.capacidad) {
            this.estaCompleto = false;
        }
    }
    public Profesor getProfesor() {
        return profesor;
    }
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
