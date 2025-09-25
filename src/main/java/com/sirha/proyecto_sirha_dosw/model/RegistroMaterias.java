/**
 * Clase que representa el registro de una materia en un semestre específico.
 * Contiene información sobre el estado del registro y el grupo asociado.
 */
package com.sirha.proyecto_sirha_dosw.model;

public class RegistroMaterias {
	// Campos
	private Semaforo estado;
	private Grupo grupo;

	/**
	 * Obtiene el estado actual del registro (semáforo).
	 * @return Estado del semáforo (AZUL, VERDE, ROJO)
	 */
	public Semaforo getEstado() {
		return estado;
	}

	/**
	 * Establece el estado del registro.
	 * @param estado Nuevo estado del semáforo
	 */
	public void setEstado(Semaforo estado) {
		this.estado = estado;
	}

	/**
	 * Obtiene el grupo asociado al registro.
	 * @return Grupo de la materia
	 */
	public Grupo getGrupo() {
		return grupo;
	}

	/**
	 * Establece el grupo asociado al registro y inicializa el estado a AZUL.
	 * @param grupo Grupo de la materia
	 */
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
		this.estado = Semaforo.AZUL;
	}
}