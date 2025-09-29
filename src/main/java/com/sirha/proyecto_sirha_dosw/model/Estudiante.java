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

	/**
	 * Verifica si una materia específica fue cancelada en el semestre actual.
	 * @param acronimoMateria Acrónimo de la materia a verificar
	 * @return true si la materia fue cancelada en el semestre actual, false en caso contrario
	 */
	public boolean tieneMateriaCandeladaEnSemestreActual(String acronimoMateria) {
		if (semestres.isEmpty()) {
			return false;
		}
		
		// Obtener el semestre actual (último semestre)
		Semestre semestreActual = semestres.get(semestres.size() - 1);
		
		for (RegistroMaterias registro : semestreActual.getRegistros()) {
			if (registro.getGrupo().getMateria().getAcronimo().equals(acronimoMateria) 
				&& registro.getEstado() == Semaforo.CANCELADO) {
				return true;
			}
		}
		
		return false;
	}

	public Semestre getSemestreActual() {
		if (semestres.isEmpty()) {
			return null; 
		}
		return semestres.get(semestres.size() - 1);
	}

	public void addGrupo(Grupo grupo) {
		Semestre semestreActual = semestres.get(semestres.size() - 1);
		RegistroMaterias nuevoRegistro = new RegistroMaterias(grupo);
		semestreActual.addRegistro(nuevoRegistro);
	}

	public void removeGrupo(Grupo grupo) {
		Semestre semestreActual = semestres.get(semestres.size() - 1);
		RegistroMaterias registroARemover = null;
		for (RegistroMaterias registro : semestreActual.getRegistros()) {
			if (registro.getGrupo().equals(grupo)) {
				registroARemover = registro;
				break;
			}
		}
		if (registroARemover != null) {
			semestreActual.removeRegistro(registroARemover);
		}
	}

}