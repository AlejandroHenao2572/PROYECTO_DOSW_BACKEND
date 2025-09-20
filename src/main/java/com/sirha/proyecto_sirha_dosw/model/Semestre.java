package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.Transient;
import java.util.ArrayList;
import java.util.List;

public class Semestre {
    private int numero;
    private List<RegistroMateria> materias;

    @Transient
    private Double promedio; // No se persiste, se calcula on-demand

    public Semestre(int numero) {
        this.numero = numero;
        this.materias = new ArrayList<>();
        this.promedio = null; // Se calculará cuando se necesite
    }

    public void agregarRegistro(RegistroMateria registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo");
        }

        // Verificar si la materia ya está registrada en este semestre
        boolean materiaExistente = materias.stream()
                .anyMatch(rm -> rm.getMateria().getCodigo().equals(registro.getMateria().getCodigo()));

        if (materiaExistente) {
            throw new IllegalArgumentException("La materia " + registro.getMateria().getNombre() +
                    " ya está registrada en este semestre");
        }

        materias.add(registro);
        promedio = null; // Invalidar el promedio cacheado
    }

    public boolean removerRegistro(String codigoMateria) {
        boolean removed = materias.removeIf(rm -> rm.getMateria().getCodigo().equals(codigoMateria));
        if (removed) {
            promedio = null; // Invalidar el promedio cacheado
        }
        return removed;
    }

    public RegistroMateria buscarRegistroPorCodigo(String codigoMateria) {
        return materias.stream()
                .filter(rm -> rm.getMateria().getCodigo().equals(codigoMateria))
                .findFirst()
                .orElse(null);
    }

    public double calcularPromedio() {
        if (promedio != null) {
            return promedio; // Devolver el valor cacheado
        }

        if (materias.isEmpty()) {
            promedio = 0.0;
            return promedio;
        }

        // Calcular promedio ponderado por créditos
        double sumaPonderada = 0;
        int totalCreditos = 0;
        int materiasConNota = 0;

        for (RegistroMateria registro : materias) {
            if (registro.getEstado() != SemaforoAcademico.BLANCO &&
                    registro.getEstado() != SemaforoAcademico.AZUL) {
                // Solo materias calificadas (VERDE o ROJO)
                double notaFinal = registro.getNotaFinal();
                int creditos = registro.getMateria().getCreditos();
                sumaPonderada += notaFinal * creditos;
                totalCreditos += creditos;
                materiasConNota++;
            }
        }

        if (totalCreditos == 0) {
            promedio = 0.0;
        } else {
            promedio = sumaPonderada / totalCreditos;
        }

        return promedio;
    }


    public int getTotalCreditos() {
        return materias.stream()
                .mapToInt(rm -> rm.getMateria().getCreditos())
                .sum();
    }

    public int getCreditosAprobados() {
        return materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.VERDE)
                .mapToInt(rm -> rm.getMateria().getCreditos())
                .sum();
    }

    public int getCreditosPerdidos() {
        return materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.ROJO)
                .mapToInt(rm -> rm.getMateria().getCreditos())
                .sum();
    }

    public int getCreditosEnProgreso() {
        return materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.AZUL ||
                        rm.getEstado() == SemaforoAcademico.BLANCO)
                .mapToInt(rm -> rm.getMateria().getCreditos())
                .sum();
    }

    public int getMateriasAprobadas() {
        return (int) materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.VERDE)
                .count();
    }

    public int getMateriasPerdidas() {
        return (int) materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.ROJO)
                .count();
    }

    public int getMateriasEnProgreso() {
        return (int) materias.stream()
                .filter(rm -> rm.getEstado() == SemaforoAcademico.AZUL ||
                        rm.getEstado() == SemaforoAcademico.BLANCO)
                .count();
    }

    public boolean estaCompletado() {
        return materias.stream()
                .allMatch(rm -> rm.getEstado() == SemaforoAcademico.VERDE ||
                        rm.getEstado() == SemaforoAcademico.ROJO);
    }


    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public List<RegistroMateria> getMaterias() {
        return new ArrayList<>(materias); // Devolver copia para evitar modificaciones externas
    }

    public void setMaterias(List<RegistroMateria> materias) {
        this.materias = new ArrayList<>(materias);
        promedio = null; // Invalidar el promedio cacheado
    }

    public Double getPromedio() {
        return calcularPromedio();
    }

}