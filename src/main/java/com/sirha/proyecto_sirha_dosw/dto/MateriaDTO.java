package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * DTO para la transferencia de datos relacionados con una Materia.
 * Se utiliza en los controladores para crear, actualizar o consultar materias,
 *          sin exponer directamente la entidad del modelo.
 */
public class MateriaDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre no puede ser nulo")
    @Indexed(unique = true)
    private String nombre;

    @NotBlank(message = "El acronimo no puede estar vacío")
    @NotNull(message = "El acronimo no puede ser nulo")
    @Indexed(unique = true)
    @Size(min = 4, max = 10, message = "El acronimo debe tener entre 4 y 10 caracteres")
    private String acronimo;

    @NotNull(message = "Los créditos no pueden ser nulos")
    @Min(value = 1, message = "Los créditos deben ser mínimo 1")
    @Max(value = 4, message = "Los créditos deben ser máximo 4")
    private int creditos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}
