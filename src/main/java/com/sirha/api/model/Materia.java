package com.sirha.api.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "materias")
public class Materia {


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

	public Materia() {
	}

	public Materia(String nombre, String acronimo, int creditos) {
		this.nombre = nombre;
		this.acronimo = acronimo;
		this.creditos = creditos;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public int getCreditos() {
		return creditos;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
}
