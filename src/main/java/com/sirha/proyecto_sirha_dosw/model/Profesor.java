package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Profesor extends Usuario {

	private String especializacion;
	private Facultad facultad;

	@Override
	public boolean autenticarUsuario() {
		System.out.println("Autenticando " + this.getClass().getSimpleName() + " con email: " + getEmail());
		return true;
	}

	public List<Grupo> obtenerGruposAsignados() {
		return null;
	}

	public String getEspecializacion() {
		return especializacion;
	}

	public void setEspecializacion(String especializacion) {
		this.especializacion = especializacion;
	}

	public Facultad getFacultad() {
		return facultad;
	}

	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}
}


