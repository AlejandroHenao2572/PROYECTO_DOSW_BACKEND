package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Facultad;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * DTO para la transferencia de datos relacionados con una Materia.
 * Se utiliza en los controladores para crear, actualizar o consultar materias,
 * sin exponer directamente la entidad del modelo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @NotNull(message = "La facultad no puede ser nula")
    private Facultad facultad;
}
