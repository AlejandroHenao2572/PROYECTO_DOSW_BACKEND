package com.sirha.proyecto_sirha_dosw.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Clase abstracta que representa a un Usuario dentro del sistema.
 *
 * <p>Esta clase modela los datos básicos que debe tener cualquier usuario,
 * incluyendo información personal, credenciales de acceso y su rol dentro
 * de la aplicación.</p>
 *
 * <p>Se utiliza con MongoDB como documento dentro de la colección "usuarios".</p>
 *
 * Validaciones:
 * <ul>
 *   <li>Los campos {@code nombre}, {@code apellido}, {@code email}, {@code rol} y {@code password} no pueden estar vacíos.</li>
 *   <li>El campo {@code email} debe tener un formato válido y debe ser único en la base de datos.</li>
 * </ul>
 */


@Document(collection = "usuarios")
public abstract class Usuario {

    @Id
    private String id;

    @Field("nombre")
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Field("apellido")
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @Field("correo")
    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo no puede estar vacío")
    @Indexed(unique = true)
    private String email;

    @Field("rol")
    @NotBlank(message = "El rol no puede estar vacío")
    private Rol rol;

    @Field("password")
    @NotBlank(message = "La contraseña no puede estar vacío")
    private String password;


    /**
     * Constructor con parámetros para inicializar un usuario.
     *
     * @param nombre   Nombre del usuario
     * @param apellido Apellido del usuario
     * @param email    Correo electrónico válido y único
     * @param password Contraseña del usuario
     * @param rol      Rol asignado al usuario
     */

    public Usuario(String nombre, String apellido, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    //geters

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Rol getRol() { return rol; }
    public String getPassword() { return password; }
    public String getApellido() { return apellido; }

    //setters
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setPassword(String password) { this.password = password; }
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setRol(Rol rol) { this.rol = rol; }

    // Método abstracto pendiente de implementación en subclases
    //public abstract void mostrarMenu();
}