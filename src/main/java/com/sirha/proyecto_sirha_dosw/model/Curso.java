package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Curso {

	private String id;
	private String nombre;
	private int creditos;

	public Curso(String id, String nombre, int creditos) {
		this.id = id;
		this.nombre = nombre;
		this.creditos = creditos;
	}
	public List<Grupo> obtenerGruposDisponibles() {
		return null;
	}

}
