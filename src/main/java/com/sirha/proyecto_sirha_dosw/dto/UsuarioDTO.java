package com.sirha.proyecto_sirha_dosw.dto;

import jakarta.validation.constraints.Email;
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
 * DTO para la transferencia de datos relacionados con un Usuario.
 * Este objeto se utiliza en los controladores para recibir o enviar información,
 * sin exponer directamente la entidad Usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    @Indexed(unique = true)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @NotNull(message = "La contraseña no puede ser nula")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @NotNull(message = "El rol no puede ser nulo")
    private String rol;

    private String facultad;
}