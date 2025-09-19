package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Decano extends Usuario implements IGestorSolicitudes {

	public Decano(String nombre ,String email, String password) {
		this.setNombre(nombre);
		this.setEmail(email);
		this.setPassword(password);
		this.setRol(RolUsuario.DECANO);
	}

	public List<Solicitud> consultarSolicitudes() {
		return null;
	}

	public boolean responderSolicitud(String idSolicitud, estadoRespuesta respuesta, String razon) {
		return false;
	}

	public List<Grupo> monitorearCapacidadGrupo() {
		return null;
	}

	public boolean configurarPeriodos(Date fechaInicial, Date fechaFinal) {
		return false;
	}

	@Override
	public boolean gestionarSolicitud(Solicitud solicitud) {
		return false;
	}

}


