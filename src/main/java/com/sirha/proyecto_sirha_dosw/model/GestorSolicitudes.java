/**
 * Interfaz que define los métodos para gestionar solicitudes en el sistema.
 */
package com.sirha.proyecto_sirha_dosw.model;

public interface GestorSolicitudes {

	/**
	 * Método para agregar una solicitud al gestor.
	 * @param solicitud Solicitud a agregar
	 */
	void agregarSolicitud(Solicitud solicitud);

	/**
	 * Método para gestionar una solicitud existente.
	 * @param solicitud Solicitud a gestionar
	 * @param accion Acción a realizar sobre la solicitud
	 */
	void gestionarSolicitud(Solicitud solicitud, String accion);
}