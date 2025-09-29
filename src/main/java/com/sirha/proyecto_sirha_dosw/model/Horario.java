/**
 * Clase que representa un horario con hora de inicio, hora de fin y día de la semana.
 * Se utiliza para definir los horarios de clases de los grupos.
 */
package com.sirha.proyecto_sirha_dosw.model;

import java.time.LocalTime;

public class Horario {
    // Campos y métodos con documentación básica
    private LocalTime horaInicio;
    private LocalTime horaFin;
    public Dia dia;

    /**
     * Obtiene la hora de fin del horario.
     * @return Hora de fin como LocalTime
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Establece la hora de fin del horario.
     * @param horaFin Hora de fin como LocalTime
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Obtiene la hora de inicio del horario.
     * @return Hora de inicio como LocalTime
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Establece la hora de inicio del horario.
     * @param horaInicio Hora de inicio como LocalTime
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Obtiene el día de la semana del horario.
     * @return Objeto Dia que representa el día
     */
    public Dia getDia() {
        return dia;
    }

    /**
     * Establece el día de la semana del horario.
     * @param dia Objeto Dia que representa el día
     */
    public void setDia(Dia dia) {
        this.dia = dia;
    }
}