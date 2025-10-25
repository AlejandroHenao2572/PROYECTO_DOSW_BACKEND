package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.dto.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la transferencia de datos relacionados con un Usuario.
 * 
 * <p>Este objeto se utiliza en los controladores para recibir o enviar información,
 * sin exponer directamente la entidad Usuario.</p>
 * 
 * <p><strong>Para el registro:</strong> Solo se requiere nombre, apellido, password, rol y facultad (excepto ADMINISTRADOR).
 * El ID y el email se generan automáticamente.</p>
 * 
 * <p>Ejemplo de registro:</p>
 * <pre>
 * {
 *   "nombre": "Juan",
 *   "apellido": "Pérez",
 *   "password": "Password123!",
 *   "rol": "ESTUDIANTE",
 *   "facultad": "INGENIERIA_SISTEMAS"
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    /**
     * Nombre del usuario.
     * <p>Campo obligatorio para registro.</p>
     */
    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre no puede estar vacío")
    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    /**
     * Apellido del usuario.
     * <p>Campo obligatorio para registro.</p>
     */
    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido no puede estar vacío")
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    /**
     * Email del usuario.
     * <p><strong>NO se debe enviar en el registro.</strong> Se genera automáticamente con el formato:
     * {nombre}.{apellido}-{primera_letra_apellido}@mail.escuelaing.edu.co</p>
     * <p>Este campo solo se usa para respuestas del servidor.</p>
     */
    @Schema(description = "Email del usuario (GENERADO AUTOMÁTICAMENTE - No enviar en registro)", 
            example = "juan.perez-p@mail.escuelaing.edu.co", 
            accessMode = Schema.AccessMode.READ_ONLY,
            hidden = true)
    private String email;

    /**
     * Contraseña del usuario.
     * <p>Campo obligatorio. Debe cumplir con los requisitos de seguridad:</p>
     * <ul>
     *   <li>Mínimo 8 caracteres</li>
     *   <li>Al menos una letra mayúscula</li>
     *   <li>Al menos una letra minúscula</li>
     *   <li>Al menos un dígito</li>
     *   <li>Al menos un carácter especial (@$!%*?&.#-_)</li>
     * </ul>
     */
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 1 número, 1 carácter especial)", 
            example = "Password123!", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @NotNull(message = "La contraseña no puede ser nula")
    @ValidPassword
    private String password;

    /**
     * Rol del usuario en el sistema.
     * <p>Valores válidos: ESTUDIANTE, DECANO, ADMINISTRADOR, PROFESOR</p>
     * <p>Campo obligatorio.</p>
     */
    @Schema(description = "Rol del usuario en el sistema", 
            example = "ESTUDIANTE", 
            allowableValues = {"ESTUDIANTE", "DECANO", "ADMINISTRADOR", "PROFESOR"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El rol es obligatorio")
    @NotNull(message = "El rol no puede ser nulo")
    private String rol;

    /**
     * Facultad del usuario.
     * <p>Valores válidos: INGENIERIA_SISTEMAS, INGENIERIA_CIVIL, ADMINISTRACION</p>
     * <p><strong>Obligatorio para ESTUDIANTE y DECANO.</strong> No debe incluirse para ADMINISTRADOR.</p>
     */
    @Schema(description = "Facultad del usuario (obligatoria para ESTUDIANTE y DECANO, no incluir para ADMINISTRADOR)", 
            example = "INGENIERIA_SISTEMAS", 
            allowableValues = {"INGENIERIA_SISTEMAS", "INGENIERIA_CIVIL", "ADMINISTRACION"},
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String facultad;
}