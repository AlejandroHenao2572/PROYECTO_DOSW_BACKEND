package com.sirha.proyecto_sirha_dosw.util;

import java.util.List;

import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;

/**
 * Reglas compartidas para validar y recuperar la información principal de estudiantes.
 */
public final class EstudianteValidationUtil {

    private EstudianteValidationUtil() {
    }

    public static Estudiante obtenerEstudiante(UsuarioRepository usuarioRepository, String idEstudiante) throws SirhaException {
        return usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast)
                .orElseThrow(() -> new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO));
    }

    public static List<RegistroMaterias> obtenerRegistrosPorSemestre(Estudiante estudiante, int semestre) throws SirhaException {
        try {
            List<RegistroMaterias> registros = estudiante.getRegistrosBySemestre(semestre);
            if (registros.isEmpty()) {
                throw new SirhaException(SirhaException.NO_HORARIO_ENCONTRADO);
            }
            return registros;
        } catch (IndexOutOfBoundsException e) {
            throw new SirhaException(SirhaException.SEMESTRE_INVALIDO);
        }
    }

    public static java.util.Map<String, Semaforo> obtenerSemaforo(Estudiante estudiante) {
        return estudiante.getSemaforo();
    }
}
