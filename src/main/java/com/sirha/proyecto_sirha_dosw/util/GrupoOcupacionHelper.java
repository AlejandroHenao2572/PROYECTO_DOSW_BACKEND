package com.sirha.proyecto_sirha_dosw.util;

/**
 * Helper methods shared by DTOs that expose información sobre la ocupación de grupos.
 */
public final class GrupoOcupacionHelper {

    private static final double ALERTA_THRESHOLD = 90.0;

    private GrupoOcupacionHelper() {
    }

    public static double calcularPorcentajeOcupacion(int capacidad, int inscritos) {
        if (capacidad <= 0) {
            return 0.0;
        }
        double porcentaje = ((double) inscritos / capacidad) * 100.0;
        return Math.round(porcentaje * 100.0) / 100.0;
    }

    public static int calcularCuposDisponibles(int capacidad, int inscritos) {
        return Math.max(capacidad - inscritos, 0);
    }

    public static boolean estaCompleto(int capacidad, int inscritos) {
        if (capacidad <= 0) {
            return inscritos > 0;
        }
        return inscritos >= capacidad;
    }

    public static boolean esAlertaCapacidad(double porcentajeOcupacion) {
        return porcentajeOcupacion >= ALERTA_THRESHOLD;
    }

    public static String determinarNivelAlerta(double porcentajeOcupacion) {
        if (porcentajeOcupacion >= 100.0) {
            return "CRITICO";
        }
        if (porcentajeOcupacion >= ALERTA_THRESHOLD) {
            return "ADVERTENCIA";
        }
        return "NORMAL";
    }

    public static String construirMensajeCapacidad(String nivelAlerta, int inscritos, int capacidad) {
        String sufijo = " estudiantes)";
        return switch (nivelAlerta) {
            case "CRITICO" -> "El grupo ha alcanzado su capacidad máxima (" + capacidad + sufijo;
            case "ADVERTENCIA" -> "ALERTA: El grupo ha superado el 90% de su capacidad (" + inscritos + "/" + capacidad + sufijo;
            case "NORMAL" -> "El grupo tiene capacidad disponible (" + inscritos + "/" + capacidad + sufijo;
            default -> "Estado del grupo: " + inscritos + "/" + capacidad + sufijo;
        };
    }
}
