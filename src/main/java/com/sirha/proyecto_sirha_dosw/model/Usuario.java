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

@Document(collection = "usuarios")
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
	private String contraseña;

	@NotNull
	@NotBlank
	private Rol rol;

	@CreatedDate
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	private LocalDateTime fechaActualizacion;

	/**
	 * Constructor por defecto.
	 */
	public Usuario() {
	}

	/**
	 * Constructor con parámetros básicos.
	 * @param nombre Nombre del usuario
	 * @param apellido Apellido del usuario
	 * @param email Email del usuario
	 * @param contraseña Contraseña del usuario
	 */
	public Usuario(String nombre, String apellido, String email, String contraseña) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.contraseña = contraseña;
	}

	/**
	 * Constructor completo con todos los parámetros.
	 * @param nombre Nombre del usuario
	 * @param apellido Apellido del usuario
	 * @param email Email del usuario
	 * @param contraseña Contraseña del usuario
	 * @param rol Rol del usuario
	 */
	public Usuario(String nombre, String apellido, String email, String contraseña, Rol rol) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.contraseña = contraseña;
		this.rol = rol;
	}

	// Getters y setters con documentación básica
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getApellido() { return apellido; }
	public void setApellido(String apellido) { this.apellido = apellido; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getContraseña() { return contraseña; }
	public void setContraseña(String contraseña) { this.contraseña = contraseña; }
	public Rol getRol() { return rol; }
	public void setRol(Rol rol) { this.rol = rol; }
}