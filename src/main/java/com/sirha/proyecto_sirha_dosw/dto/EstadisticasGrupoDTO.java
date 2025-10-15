package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.util.GrupoOcupacionHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para representar las estadísticas de grupos más solicitados para cambio.
 * Contiene información del grupo y la cantidad de solicitudes realizadas.
 */
@Getter
@Setter
@NoArgsConstructor
public class EstadisticasGrupoDTO {

    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private String materiaAcronimo;
    private String facultad;
    private int capacidad;
    private int cantidadInscritos;
    private long cantidadSolicitudes;
    private double porcentajeOcupacion;

    public EstadisticasGrupoDTO(String grupoId, String materiaId, String materiaNombre,
                                String materiaAcronimo, String facultad) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.materiaAcronimo = materiaAcronimo;
        this.facultad = facultad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
        recalcularPorcentaje();
    }

    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
        recalcularPorcentaje();
    }

    private void recalcularPorcentaje() {
        this.porcentajeOcupacion = GrupoOcupacionHelper.calcularPorcentajeOcupacion(capacidad, cantidadInscritos);
    }
}