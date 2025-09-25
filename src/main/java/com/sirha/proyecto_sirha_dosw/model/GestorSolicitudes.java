/**
 * Interfaz que define los métodos para gestionar solicitudes en el sistema.
 */
package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public interface GestorSolicitudes {
	/**
	 * Lista de solicitudes (debe ser implementada por las clases que usen esta interfaz)
	 */
	List<Solicitud> solicitudes = new ArrayList<Solicitud>();

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