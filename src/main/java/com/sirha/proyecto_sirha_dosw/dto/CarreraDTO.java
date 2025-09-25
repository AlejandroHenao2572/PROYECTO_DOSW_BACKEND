package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para la transferencia de datos relacionados con una Carrera.
 * Este objeto se utiliza en los controladores para registrar o actualizar,
 *          carreras sin exponer directamente la entidad del modelo.
 */
public class CarreraDTO {

    @NotNull(message = "El nombre de la facultad no puede ser nulo")
    @NotBlank(message = "El nombre de la facultad no puede estar en blanco")
    private String nombre;

    @NotNull(message = "El código de la carrera no puede ser nulo")
    @NotBlank(message = "El código de la carrera no puede estar en blanco")
    private String codigo;

    @NotNull(message = "La duración en semestres no puede ser nulo")
    @Min(value = 1, message = "La duración debe ser al menos 1 semestre")
    private int duracionSemestres;

    @NotNull(message = "Los créditos totales no pueden ser nulo")
    @Min(value = 1, message = "Los créditos totales deben ser al menos 1")
    private int creditosTotales;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String  nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDuracionSemestres() {
        return duracionSemestres;
    }

    public void setDuracionSemestres(int duracionSemestres) {
        this.duracionSemestres = duracionSemestres;
    }

    public int getCreditosTotales() {
        return creditosTotales;
    }

    public void setCreditosTotales(int creditosTotales) {
        this.creditosTotales = creditosTotales;
    }
}
