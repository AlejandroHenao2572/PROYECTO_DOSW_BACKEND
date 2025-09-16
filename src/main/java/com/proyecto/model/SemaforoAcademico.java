package com.example.model;

import java.util.Map;

public class SemaforoAcademico {

	private int estudiante;

	private Map<Map<Curso, Grupo>, EstadoSemaforo> estadoCursos;

	private EstadoSemaforo getEstadoCurso;

}
