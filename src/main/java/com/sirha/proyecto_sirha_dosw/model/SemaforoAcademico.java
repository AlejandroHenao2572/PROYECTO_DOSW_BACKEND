package com.sirha.proyecto_sirha_dosw.model;

import java.util.Map;

public class SemaforoAcademico {

	private int estudiante;
	private Map<Map<Curso, Grupo>, EstadoSemaforo> estadoCursos;
	private EstadoSemaforo getEstadoCurso;

}
