/**
 * Clase abstracta que representa un usuario base del sistema.
 * Contiene información común a todos los tipos de usuarios.
 */
package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public abstract class Usuario {
	// Campos
	@Id
	@Indexed(unique = true)
	private String id;

	@NotNull
	@NotBlank
	private String nombre;

	@NotNull
	@NotBlank
	private String apellido;

	@Indexed(unique = true)
	@NotBlank
	@NotNull
	private String email;

	@NotNull
	@NotBlank
	private String password;

	@NotNull
	@NotBlank
	private Rol rol;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	private LocalDateTime fechaActualizacion;

	/**
	 * Constructor con parámetros básicos.
	 * @param nombre Nombre del usuario
	 * @param apellido Apellido del usuario
	 * @param email Email del usuario
	 * @param contraseña Contraseña del usuario
	 */
	protected Usuario(String nombre, String apellido, String email, String contrasena) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = contrasena;
	}

	/**
	 * Constructor completo con todos los parámetros.
	 * @param nombre Nombre del usuario
	 * @param apellido Apellido del usuario
	 * @param email Email del usuario
	 * @param contraseña Contraseña del usuario
	 * @param rol Rol del usuario
	 */
	protected Usuario(String nombre, String apellido, String email, String contrasena, Rol rol) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = contrasena;
		this.rol = rol;
	}
}