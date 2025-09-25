package com.sirha.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfaz que define el comportamiento de un gestor de solicitudes académicas.
 * Puede ser implementada por actores como {@link Administrador} o {@link Decano}
 */

public interface GestorSolicitudes {

	List<Solicitud> solicitudes = new ArrayList<Solicitud>();

	/**
	 * Agrega una solicitud al conjunto de solicitudes que el gestor administra.
	 * @param solicitud la solicitud a agregar.
	 */

	void agregarSolicitud(Solicitud solicitud);

	/**
	 * Gestiona una solicitud específica aplicando una acción determinada.
	 * @param solicitud la solicitud a gestionar.
	 * @param accion acción a aplicar sobre la solicitud.
	 */
	void gestionarSolicitud(Solicitud solicitud, String accion);

}