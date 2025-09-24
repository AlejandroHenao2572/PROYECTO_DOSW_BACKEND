package com.sirha.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Document(collection = "usuarios")
public class Estudiante extends Usuario {

	private Facultad carrera;

	private List<Semestre> semestres = new ArrayList<>();

	private List<Solicitud> solicitudes = new ArrayList<Solicitud>();

	public Estudiante() {
		super();
	}

	public Estudiante(String nombre, String apellido, String email, String contraseña, Facultad carrera) {
		super(nombre, apellido, email, contraseña);
		this.carrera = carrera;
	}

	public Estudiante(String nombre, String apellido, String email, String contraseña, Rol rol, Facultad carrera) {
		super(nombre, apellido, email, contraseña, rol);
		this.carrera = carrera;
	}

	public Facultad getCarrera() {
		return carrera;
	}

	public void setCarrera(Facultad carrera) {
		this.carrera = carrera;
	}

	public List<Semestre> getSemestres() {
		return semestres;
	}

	public void setSemestres(List<Semestre> semestres) {
		this.semestres = semestres;
	}


	public List<Solicitud> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<Solicitud> solicitudes) {
		this.solicitudes = solicitudes;
	}

	public void addSolicitud(Solicitud solicitud) {
		this.solicitudes.add(solicitud);
	}


	public List<RegistroMaterias> getRegistrosBySemestre(int semestre) {
		List<RegistroMaterias> registros = semestres.get(semestre-1).getRegistros();
		return registros;
	}
	public Map<String, Semaforo> getSemaforo() {
		HashMap<String, Semaforo> semaforo = new HashMap<>();
		for (Semestre semestre : semestres) {
			for (RegistroMaterias registro : semestre.getRegistros()) {
				String nombre = registro.getGrupo().getMateria().getNombre();
				Semaforo estado = registro.getEstado();
				semaforo.put(nombre, estado);
			}
		}
		return semaforo;
	}
}