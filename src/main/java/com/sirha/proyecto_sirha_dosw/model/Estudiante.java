/**
 * Clase que representa a un estudiante en el sistema.
 * Extiende de Usuario y contiene información específica como carrera, semestres y solicitudes.
 */
package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "usuarios")
@TypeAlias("estudiante")
public class Estudiante extends Usuario {
	// Campos
	private Facultad carrera;
	private List<Semestre> semestres = new ArrayList<>();
	private List<Solicitud> solicitudes = new ArrayList<Solicitud>();

	/**
	 * Constructor por defecto.
	 */
	public Estudiante() {
		super();
	}

	/**
	 * Constructor con parámetros básicos.
	 * @param nombre Nombre del estudiante
	 * @param apellido Apellido del estudiante
	 * @param email Email del estudiante
	 * @param contraseña Contraseña del estudiante
	 * @param carrera Facultad a la que pertenece el estudiante
	 */
	public Estudiante(String nombre, String apellido, String email, String contraseña, Facultad carrera) {
		super(nombre, apellido, email, contraseña);
		this.carrera = carrera;
	}

	/**
	 * Constructor completo con todos los parámetros.
	 * @param nombre Nombre del estudiante
	 * @param apellido Apellido del estudiante
	 * @param email Email del estudiante
	 * @param contraseña Contraseña del estudiante
	 * @param rol Rol del usuario
	 * @param carrera Facultad a la que pertenece el estudiante
	 */
	public Estudiante(String nombre, String apellido, String email, String contraseña, Rol rol, Facultad carrera) {
		super(nombre, apellido, email, contraseña, rol);
		this.carrera = carrera;
	}

	// Getters y setters con documentación básica
	public Facultad getCarrera() { return carrera; }
	public void setCarrera(Facultad carrera) { this.carrera = carrera; }
	public List<Semestre> getSemestres() { return semestres; }
	public void setSemestres(List<Semestre> semestres) { this.semestres = semestres; }
	public List<Solicitud> getSolicitudes() { return solicitudes; }
	public void setSolicitudes(List<Solicitud> solicitudes) { this.solicitudes = solicitudes; }

	/**
	 * Agrega una solicitud a la lista de solicitudes del estudiante.
	 * @param solicitud Solicitud a agregar
	 */
	public void addSolicitud(Solicitud solicitud) {
		this.solicitudes.add(solicitud);
	}

	/**
	 * Obtiene los registros de materias para un semestre específico.
	 * @param semestre Número del semestre (1-based)
	 * @return Lista de registros de materias del semestre especificado
	 */
	public List<RegistroMaterias> getRegistrosBySemestre(int semestre) {
		List<RegistroMaterias> registros = semestres.get(semestre-1).getRegistros();
		return registros;
	}

	/**
	 * Obtiene un mapa con el estado del semáforo para cada materia del estudiante.
	 * @return Mapa donde la clave es el nombre de la materia y el valor es el estado del semáforo
	 */
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