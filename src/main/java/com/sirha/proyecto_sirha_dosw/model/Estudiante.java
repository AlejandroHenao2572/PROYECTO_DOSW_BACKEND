package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Estudiante extends Usuario {

	private int semestre = 1;

	private SemaforoAcademico semaforoAcademico;

	public Estudiante(String nombre ,String email, String password) {
		this.setNombre(nombre);
		this.setEmail(email);
		this.setPassword(password);
		this.setRol(RolUsuario.ESTUDIANTE);
	}

	public Horario consultarHorario() {
		return null;
	}

	public SemaforoAcademico consultarSemaforoAcademico() {
		return null;
	}

	public Solicitud crearSolicitudDeCambio(Curso cursoOrigen, Grupo grupoOrigen, Curso cursoDestino, Grupo grupoDestino) {
		return null;
	}

	public List<Solicitud> consultarHistorialDeSolicitudes() {
		return null;
	}

}

