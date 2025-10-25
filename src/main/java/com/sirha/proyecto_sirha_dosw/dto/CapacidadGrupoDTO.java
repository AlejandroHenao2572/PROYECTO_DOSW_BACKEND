package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.util.GrupoOcupacionHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para mostrar información de capacidad de un grupo.
 * Incluye datos sobre inscripciones actuales, capacidad máxima y porcentaje de ocupación.
 */
@Getter
@Setter
@NoArgsConstructor
public class CapacidadGrupoDTO {

    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private String materiaAcronimo;
    private int capacidadMaxima;
    private int estudiantesInscritos;
    private double porcentajeOcupacion;
    private int cuposDisponibles;
    private boolean estaCompleto;
    private String profesorId;
    private String profesorNombre;

    public CapacidadGrupoDTO(String grupoId, String materiaId, String materiaNombre,
                             String materiaAcronimo, int capacidadMaxima, int estudiantesInscritos) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.materiaAcronimo = materiaAcronimo;
        this.capacidadMaxima = capacidadMaxima;
        this.estudiantesInscritos = estudiantesInscritos;
        recalcularMetricas();
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        recalcularMetricas();
    }

    public void setEstudiantesInscritos(int estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
        recalcularMetricas();
    }

    private void recalcularMetricas() {
        this.porcentajeOcupacion = GrupoOcupacionHelper.calcularPorcentajeOcupacion(capacidadMaxima, estudiantesInscritos);
        this.cuposDisponibles = GrupoOcupacionHelper.calcularCuposDisponibles(capacidadMaxima, estudiantesInscritos);
        this.estaCompleto = GrupoOcupacionHelper.estaCompleto(capacidadMaxima, estudiantesInscritos);
    }
}