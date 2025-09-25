/**
 * Clase que representa una materia académica en el sistema.
 */
package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "materias")
public class Materia {

	@Id
	private String id;

	@NotNull
	@NotBlank
	@Indexed(unique = true)
	private String nombre;

	@NotNull
	@NotBlank
	@Indexed(unique = true)
	private String acronimo;

	@NotNull
	@NotBlank
	private int creditos;

	/**
	 * Constructor por defecto.
	 */
	public Materia() {
	}

	/**
	 * Constructor con parámetros básicos.
	 * @param nombre Nombre de la materia
	 * @param acronimo Acrónimo único de la materia
	 * @param creditos Número de créditos de la materia
	 */
	public Materia(String nombre, String acronimo, int creditos) {
		this.nombre = nombre;
		this.acronimo = acronimo;
		this.creditos = creditos;
	}

	// Getters y setters
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getAcronimo() { return acronimo; }
	public void setAcronimo(String acronimo) { this.acronimo = acronimo; }
	public int getCreditos() { return creditos; }
	public void setCreditos(int creditos) { this.creditos = creditos; }
}