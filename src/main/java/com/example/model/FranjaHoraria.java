package com.example.model;

import java.time.LocalTime;

public enum FranjaHoraria {
    FRANJA_1(LocalTime.of(7, 0), LocalTime.of(8, 30)),
    FRANJA_2(LocalTime.of(8, 30), LocalTime.of(10, 0)),
    FRANJA_3(LocalTime.of(10, 0), LocalTime.of(11, 30)),
    FRANJA_4(LocalTime.of(11, 30), LocalTime.of(13, 0)),
    FRANJA_5(LocalTime.of(13, 0), LocalTime.of(14, 30)),
    FRANJA_6(LocalTime.of(14, 30), LocalTime.of(16, 0)),
    FRANJA_7(LocalTime.of(16, 0), LocalTime.of(17, 30)),
    FRANJA_8(LocalTime.of(17, 30), LocalTime.of(19, 0));

    private final LocalTime inicio;
    private final LocalTime fin;

    FranjaHoraria(LocalTime inicio, LocalTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFin() {
        return fin;
    }

    public String getRangoFormateado() {
        return String.format("%02d:%02d - %02d:%02d",
                inicio.getHour(), inicio.getMinute(),
                fin.getHour(), fin.getMinute());
    }
}
