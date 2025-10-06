/**
 * Clase que representa una materia académica en el sistema.
 */
package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "materias")
@Getter
@Setter
@NoArgsConstructor
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

	@NotNull
	@NotBlank
	private Facultad facultad;

	/**
	 * Constructor con parámetros básicos.
	 * @param nombre Nombre de la materia
	 * @param acronimo Acrónimo único de la materia
	 * @param creditos Número de créditos de la materia
	 * @param facultad Facultad a la que pertenece la materia
	 */
	public Materia(String nombre, String acronimo, int creditos, Facultad facultad) {
		this.nombre = nombre;
		this.acronimo = acronimo;
		this.creditos = creditos;
		this.facultad = facultad;
	}
}