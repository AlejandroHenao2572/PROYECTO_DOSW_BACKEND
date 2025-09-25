/**
 * Clase que representa un semestre académico.
 * Contiene un número de semestre y una lista de registros de materias.
 */
package com.sirha.proyecto_sirha_dosw.model;

import java.util.ArrayList;
import java.util.List;

public class Semestre {
    // Campos
    private int numero;
    private List<RegistroMaterias> registros = new ArrayList<>();

    /**
     * Constructor por defecto.
     */
    public Semestre() {
    }

    // Getters y setters con documentación básica
    public List<RegistroMaterias> getRegistros() { return registros; }
    public void setRegistros(List<RegistroMaterias> registros) { this.registros = registros; }

    /**
     * Agrega un registro de materia al semestre.
     * @param registro Registro de materia a agregar
     */
    public void addRegistro(RegistroMaterias registro) {
        this.registros.add(registro);
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
}