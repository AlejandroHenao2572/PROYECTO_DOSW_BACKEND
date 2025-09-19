package com.sirha.proyecto_sirha_dosw.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Name;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;

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
public abstract class Usuario {

    @Id
    private String id;

    private String nombre;

    @Indexed(unique = true)
    @Email
    @NotBlank
    private String email;

    private String password;
    private RolUsuario rol;

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

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}


