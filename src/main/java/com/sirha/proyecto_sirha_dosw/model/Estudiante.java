package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Estudiante extends Usuario {

	private int semestre;
	private SemaforoAcademico semaforoAcademico;

	@Override
	public boolean autenticarUsuario() {
		System.out.println("Autenticando " + this.getClass().getSimpleName() + " con email: " + getEmail());
		return true;
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

	public int getSemestre() {
		return semestre;
	}

	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	public SemaforoAcademico getSemaforoAcademico() {
		return semaforoAcademico;
	}

	public void setSemaforoAcademico(SemaforoAcademico semaforoAcademico) {
		this.semaforoAcademico = semaforoAcademico;
	}
}

