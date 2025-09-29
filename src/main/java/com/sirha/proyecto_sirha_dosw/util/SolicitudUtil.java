package com.sirha.proyecto_sirha_dosw.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utilidad para generar números de radicado y calcular prioridades automáticas para solicitudes.
 */
@Component
public class SolicitudUtil {
    
    // Contador atómico para generar números únicos de radicado
    private static final AtomicLong contadorRadicado = new AtomicLong(1000);
    
    // Contador atómico para generar números secuenciales de prioridad
    private static final AtomicLong contadorPrioridad = new AtomicLong(1);
    
    /**
     * Genera un número de radicado único basado en la fecha actual y un contador secuencial.
     * Formato: RAD-YYYYMMDD-XXXX
     * @return Número de radicado único
     */
    public String generarNumeroRadicado() {
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long numero = contadorRadicado.getAndIncrement();
        return String.format("RAD-%s-%04d", fecha, numero);
    }
    
    /**
     * Genera el siguiente número de prioridad secuencial.
     * Las solicitudes se procesan en orden de llegada (menor número = mayor prioridad).
     * @return Número de prioridad secuencial
     */
    public Integer generarNumeroPrioridad() {
        return (int) contadorPrioridad.getAndIncrement();
    }
}