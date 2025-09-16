package com.proyecto.model;

import java.util.List;

public class Adminstrador extends Usuario implements IGestorSolicitudes {

	private CalendarioAcademico calendarioAcademico;

	public void adminstrarUsuarios() {

	}

	public List<Reporte> generarReportesDelSystema() {
		return null;
	}

	public void admintrarConfiguracionDelSistema() {

	}

	public void establecerPeriodoCademico(Date fechaInicio, Date fechFinal) {

	}

	@Override
	public boolean gestionarSolicitud(Solicitud solicitud) {
		return false;
	}
}
