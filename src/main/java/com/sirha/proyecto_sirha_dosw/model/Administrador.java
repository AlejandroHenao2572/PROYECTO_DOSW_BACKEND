package com.sirha.proyecto_sirha_dosw.model;

import java.util.Date;
import java.util.List;

public class Administrador extends Usuario implements IGestorSolicitudes {

	private CalendarioAcademico calendarioAcademico;

	@Override
	public boolean autenticarUsuario() {
		System.out.println("Autenticando " + this.getClass().getSimpleName() + " con email: " + getEmail());
		return true;
	}

	public void administrarUsuarios() {

	}

	public List<Reporte> generarReportesDelSistema() {
		return null;
	}

	public void administrarConfiguracionDelSistema() {

	}

	public void establecerPeriodoAcademico(Date fechaInicio, Date fechaFinal) {

	}

	@Override
	public boolean gestionarSolicitud(Solicitud solicitud) {
		return false;
	}
}


