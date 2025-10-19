/**
 * Clase que representa a un decano en el sistema.
 * Extiende de Profesor e implementa la interfaz GestorSolicitudes.
 * Un decano está asociado a una facultad específica.
 */
package com.sirha.proyecto_sirha_dosw.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@TypeAlias("decano")
public class Decano extends Profesor{
    // Campo específico del decano
    private Facultad carrera;

    /**
     * Constructor por defecto.
     */
    public Decano() {
        super();
    }

    /**
     * Constructor con parámetros básicos.
     * @param nombre Nombre del decano
     * @param apellido Apellido del decano
     * @param email Email del decano
     * @param contraseña Contraseña del decano
     * @param carrera Facultad que dirige el decano
     */
    public Decano(String nombre, String apellido, String email, String contrasena, Facultad carrera) {
        super(nombre, apellido, email, contrasena);
        this.carrera = carrera;
    }

    /**
     * Constructor completo con todos los parámetros.
     * @param nombre Nombre del decano
     * @param apellido Apellido del decano
     * @param email Email del decano
     * @param contraseña Contraseña del decano
     * @param rol Rol del decano
     * @param carrera Facultad que dirige el decano
     */
    public Decano(String nombre, String apellido, String email, String contrasena, Rol rol, Facultad carrera) {
        super(nombre, apellido, email, contrasena, rol);
        this.carrera = carrera;
    }

    // Getters y setters con documentación básica
    public Facultad getFacultad() { return carrera; }
    public void setFacultad(Facultad carrera) { this.carrera = carrera; }


}