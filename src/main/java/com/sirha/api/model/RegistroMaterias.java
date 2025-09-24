package com.sirha.api.model;

public class RegistroMaterias {

	private Semaforo estado;

	private Grupo grupo;

	public Semaforo getEstado() {
		return estado;
	}

	public void setEstado(Semaforo estado) {
		this.estado = estado;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
		this.estado = Semaforo.AZUL;
	}

}
