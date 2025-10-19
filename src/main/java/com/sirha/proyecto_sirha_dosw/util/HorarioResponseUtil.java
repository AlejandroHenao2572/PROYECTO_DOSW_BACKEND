package com.sirha.proyecto_sirha_dosw.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;

/**
 * Utilidad centralizada para transformar registros de materias en la estructura
 * esperada por las respuestas REST de horario.
 */
public final class HorarioResponseUtil {

    private HorarioResponseUtil() {
    }

    public static Map<String, List<Horario>> mapearHorariosPorMateria(List<RegistroMaterias> registros) {
        Map<String, List<Horario>> horariosPorMateria = new HashMap<>();
        if (registros == null) {
            return horariosPorMateria;
        }

        for (RegistroMaterias registro : registros) {
            Grupo grupo = registro.getGrupo();
            if (grupo == null || grupo.getHorarios() == null || grupo.getHorarios().isEmpty()) {
                continue;
            }
            horariosPorMateria.put(grupo.getMateria().getNombre(), grupo.getHorarios());
        }
        return horariosPorMateria;
    }
}
