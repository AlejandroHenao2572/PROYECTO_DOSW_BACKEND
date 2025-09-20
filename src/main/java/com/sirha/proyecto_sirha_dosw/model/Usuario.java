package com.sirha.proyecto_sirha_dosw.model;

import jdk.jfr.Name;
import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.TypeAlias;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import org.springframework.data.mongodb.core.index.CompoundIndex;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // nombre para diferenciar
        include = JsonTypeInfo.As.PROPERTY,
        property = "rol"           // este campo vendrá en el JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Estudiante.class, name = "estudiante"),
        @JsonSubTypes.Type(value = Decano.class, name = "decano"),
        @JsonSubTypes.Type(value = Administrador.class, name = "administrador"),
        @JsonSubTypes.Type(value = Administrador.class, name = "profesor")
})
@Document(collection = "usuarios")
@TypeAlias("Usuario")
public abstract class Usuario {

    @Id
    private String id;

    private String nombre;

    @Indexed(unique = true)
    @Email
    @NotBlank
    private String email;
    private String password;

    @DBRef  // referencia a la colección Rol
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String password, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean autenticarUsuario() {
    
        throw new UnsupportedOperationException("Unimplemented method 'autenticarUsuario'");
    }

}


