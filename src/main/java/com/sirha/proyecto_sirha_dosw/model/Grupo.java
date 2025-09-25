/**
 * Clase que representa un grupo académico en el sistema.
 * Contiene información sobre la materia, capacidad, horarios y estudiantes inscritos.
 */
package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "grupos")
public class Grupo {
	// Campos
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

	/**
	 * Constructor por defecto.
	 */
	public Grupo() {
	}

	/**
	 * Constructor con parámetros básicos.
	 * @param materia Materia del grupo
	 * @param capacidad Capacidad máxima del grupo
	 * @param horarios Lista de horarios del grupo
	 */
	public Grupo(Materia materia, int capacidad, List<Horario> horarios) {
		this.materia = materia;
		this.capacidad = capacidad;
		this.horarios = horarios;
		this.cantidadInscritos = 0;
		this.estaCompleto = false;
		this.estudiantesId = new ArrayList<String>();
	}

	// Getters y setters con documentación básica
	public Materia getMateria() { return materia; }
	public void setMateria(Materia materia) { this.materia = materia; }
	public List<String> getEstudiantesId() { return estudiantesId; }
	public void setEstudiantesId(List<String> estudiantesId) { this.estudiantesId = estudiantesId; }
	public List<Horario> getHorarios() { return horarios; }
	public void setHorarios(List<Horario> horarios) { this.horarios = horarios; }
	public boolean isEstaCompleto() { return estaCompleto; }
	public void setEstaCompleto(boolean estaCompleto) { this.estaCompleto = estaCompleto; }
	public int getCantidadInscritos() { return cantidadInscritos; }
	public void setCantidadInscritos(int cantidadInscritos) { this.cantidadInscritos = cantidadInscritos; }
	public int getCapacidad() { return capacidad; }
	public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public Profesor getProfesor() { return profesor; }
	public void setProfesor(Profesor profesor) { this.profesor = profesor; }

	/**
	 * Agrega un estudiante al grupo si no está ya inscrito.
	 * Actualiza la cantidad de inscritos y verifica si el grupo está completo.
	 * @param estudianteId ID del estudiante a agregar
	 */
	public void addEstudiante(String estudianteId) {
		if (!this.estudiantesId.contains(estudianteId)) {
			this.estudiantesId.add(estudianteId);
			this.cantidadInscritos++;
			if (this.cantidadInscritos >= this.capacidad) {
				this.estaCompleto = true;
			}
		}
	}

	/**
	 * Remueve un estudiante del grupo.
	 * Actualiza la cantidad de inscritos y verifica si el grupo deja de estar completo.
	 * @param estudianteId ID del estudiante a remover
	 */
	public void removeEstudiante(String estudianteId) {
		this.estudiantesId.remove(estudianteId);
		this.cantidadInscritos--;
		if (this.cantidadInscritos < this.capacidad) {
			this.estaCompleto = false;
		}
	}
}