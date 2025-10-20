package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.dto.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // El email se genera automáticamente: {nombre}.{apellido}-{primera letra del apellido}@mail.escuelaing.edu.co
    // Por lo tanto, no se requiere en el JSON de registro
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @NotNull(message = "La contraseña no puede ser nula")
    @ValidPassword
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @NotNull(message = "El rol no puede ser nulo")
    private String rol;

    private String facultad;
}