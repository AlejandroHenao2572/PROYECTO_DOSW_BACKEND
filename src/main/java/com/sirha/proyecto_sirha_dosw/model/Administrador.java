package com.sirha.proyecto_sirha_dosw.model;

import java.util.List;

public class Administrador extends Usuario implements IGestorSolicitudes {

	private CalendarioAcademico calendarioAcademico;

	public void administrarUsuarios() {

	}

	public List<Reporte> generarReportesDelSystema() {
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
