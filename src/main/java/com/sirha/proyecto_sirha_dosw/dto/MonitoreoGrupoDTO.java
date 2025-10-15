package com.sirha.proyecto_sirha_dosw.dto;

import java.util.List;

import com.sirha.proyecto_sirha_dosw.util.GrupoOcupacionHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para el monitoreo de carga de grupos.
 * Contiene información sobre la ocupación de un grupo y alertas de capacidad.
 */
@Getter
@Setter
@NoArgsConstructor
public class MonitoreoGrupoDTO {

    private String grupoId;
    private String materiaId;
    private String materiaNombre;
    private int capacidad;
    private int cantidadInscritos;
    private double porcentajeOcupacion;
    private boolean alertaCapacidad;
    private String nivelAlerta; // "NORMAL", "ADVERTENCIA", "CRITICO"
    private String mensaje;
    private List<String> estudiantesId;
    private String profesorId;

    public MonitoreoGrupoDTO(String grupoId, String materiaId, String materiaNombre,
                              int capacidad, int cantidadInscritos) {
        this.grupoId = grupoId;
        this.materiaId = materiaId;
        this.materiaNombre = materiaNombre;
        this.capacidad = capacidad;
        this.cantidadInscritos = cantidadInscritos;
        actualizarIndicadores();
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
        actualizarIndicadores();
    }

    public void setCantidadInscritos(int cantidadInscritos) {
        this.cantidadInscritos = cantidadInscritos;
        actualizarIndicadores();
    }

    private void actualizarIndicadores() {
        this.porcentajeOcupacion = GrupoOcupacionHelper.calcularPorcentajeOcupacion(capacidad, cantidadInscritos);
        this.alertaCapacidad = GrupoOcupacionHelper.esAlertaCapacidad(porcentajeOcupacion);
        this.nivelAlerta = GrupoOcupacionHelper.determinarNivelAlerta(porcentajeOcupacion);
        this.mensaje = GrupoOcupacionHelper.construirMensajeCapacidad(nivelAlerta, cantidadInscritos, capacidad);
    }
}