package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public interface GestorSolicitudes {

	List<Solicitud> solicitudes = new ArrayList<Solicitud>();

	void agregarSolicitud(Solicitud solicitud);
	void gestionarSolicitud(Solicitud solicitud, String accion);

}