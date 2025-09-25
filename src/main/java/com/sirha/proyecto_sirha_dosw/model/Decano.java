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
public class Decano extends Profesor implements GestorSolicitudes {
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
    public Decano(String nombre, String apellido, String email, String contraseña, Facultad carrera) {
        super(nombre, apellido, email, contraseña);
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
    public Decano(String nombre, String apellido, String email, String contraseña, Rol rol, Facultad carrera) {
        super(nombre, apellido, email, contraseña, rol);
        this.carrera = carrera;
    }

    // Getters y setters con documentación básica
    public Facultad getCarrera() { return carrera; }
    public void setCarrera(Facultad carrera) { this.carrera = carrera; }

    /**
     * Implementación del método para agregar solicitudes (pendiente de implementación).
     * @param solicitud Solicitud a agregar
     */
    @Override
    public void agregarSolicitud(Solicitud solicitud) {
        // Pendiente de implementación
    }

    /**
     * Implementación del método para gestionar solicitudes (pendiente de implementación).
     * @param solicitud Solicitud a gestionar
     * @param accion Acción a realizar sobre la solicitud
     */
    @Override
    public void gestionarSolicitud(Solicitud solicitud, String accion) {
        // Pendiente de implementación
    }
}