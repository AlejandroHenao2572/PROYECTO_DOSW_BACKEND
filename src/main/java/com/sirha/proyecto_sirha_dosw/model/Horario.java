/**
 * Clase que representa un horario con hora de inicio, hora de fin y día de la semana.
 * Se utiliza para definir los horarios de clases de los grupos.
 */
package com.sirha.proyecto_sirha_dosw.model;

public class Horario {
    // Campos y métodos con documentación básica
    private String HoraInicio;
    private String HoraFin;
    public Dia dia;

    /**
     * Obtiene la hora de fin del horario.
     * @return Hora de fin en formato String
     */
    public String getHoraFin() {
        return HoraFin;
    }

    /**
     * Establece la hora de fin del horario.
     * @param horaFin Hora de fin en formato String
     */
    public void setHoraFin(String horaFin) {
        HoraFin = horaFin;
    }

    /**
     * Obtiene la hora de inicio del horario.
     * @return Hora de inicio en formato String
     */
    public String getHoraInicio() {
        return HoraInicio;
    }

    /**
     * Establece la hora de inicio del horario.
     * @param horaInicio Hora de inicio en formato String
     */
    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
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