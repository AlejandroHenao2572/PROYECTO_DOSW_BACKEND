package com.sirha.proyecto_sirha_dosw.service;


import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DecanoService {

    private final UsuarioRepository usuarioRepository;
    private final SolicitudRepository solicitudRepository;

    public DecanoService(UsuarioRepository usuarioRepository, SolicitudRepository solicitudRepository) {
        this.usuarioRepository = usuarioRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public List<Usuario> findEstudiantesByFacultad(String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByFacultadAndRol(facultadEnum, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByIdAndFacultad(String id, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByIdAndFacultadAndRol(id, facultadEnum, Rol.ESTUDIANTE);
    }

    public Usuario findEstudianteByEmailAndFacultad(String email, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByEmailAndFacultadAndRol(email, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreAndFacultad(String nombre, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByNombreAndFacultadAndRol(nombre, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByApellidoAndFacultad(String apellido, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByApellidoAndFacultadAndRol(apellido, facultadEnum, Rol.ESTUDIANTE);
    }

    public List<Usuario> findEstudiantesByNombreApellidoAndFacultad(String nombre, String apellido, String facultad) {
        Facultad facultadEnum = Facultad.valueOf(facultad.toUpperCase());
        return usuarioRepository.findByNombreAndApellidoAndFacultadAndRol(nombre, apellido, facultadEnum, Rol.ESTUDIANTE);
    }

    /**
     * Consulta todas las solicitudes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @return lista de solicitudes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultad(facultad);
    }

    /**
     * Consulta las solicitudes pendientes recibidas en el área de una facultad específica.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes pendientes
     * @return lista de solicitudes pendientes asociadas a la facultad
     */
    public List<Solicitud> consultarSolicitudesPendientesPorFacultad(Facultad facultad) {
        return solicitudRepository.findByFacultadAndEstado(facultad, SolicitudEstado.PENDIENTE);
    }

    /**
     * Consulta las solicitudes por facultad filtradas por estado específico.
     * @param facultad la facultad de la cual se quieren consultar las solicitudes
     * @param estado el estado específico de las solicitudes a consultar
     * @return lista de solicitudes filtradas por facultad y estado
     */
    public List<Solicitud> consultarSolicitudesPorFacultadYEstado(Facultad facultad, SolicitudEstado estado) {
        return solicitudRepository.findByFacultadAndEstado(facultad, estado);
    }

    /**
     * Consulta el horario académico de un estudiante para un semestre específico.
     * Permite al decano visualizar el horario del estudiante que solicita cambio.
     * @param idEstudiante ID del estudiante
     * @param semestre número de semestre a consultar
     * @return lista de RegistroMaterias correspondientes al semestre
     * @throws SirhaException si el estudiante no existe o no tiene registros
     */
    public List<RegistroMaterias> consultarHorarioEstudiante(String idEstudiante, int semestre) throws SirhaException {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        
        System.out.println(estudianteOpt.isEmpty());
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        
        try {
            Estudiante estudiante = estudianteOpt.get();
            List<RegistroMaterias> registroMaterias = estudiante.getRegistrosBySemestre(semestre);
            if (registroMaterias.isEmpty()) {
                throw new SirhaException(SirhaException.NO_HORARIO_ENCONTRADO);
            }
            return registroMaterias;
        } catch (Exception e) {
            throw new SirhaException(SirhaException.SEMESTRE_INVALIDO);
        }
    }

    /**
     * Consulta el semáforo académico de un estudiante.
     * Permite al decano consultar el rendimiento académico del estudiante.
     * @param idEstudiante ID del estudiante
     * @return mapa donde la clave es el acrónimo de la materia y el valor es el Semaforo
     * @throws SirhaException si el estudiante no existe
     */
    public Map<String, Semaforo> consultarSemaforoAcademicoEstudiante(String idEstudiante) throws SirhaException {
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }
        
        Estudiante estudiante = estudianteOpt.get();
        return estudiante.getSemaforo();
    }

    /**
     * Valida que la facultad proporcionada sea válida.
     * @param facultad nombre de la facultad a validar
     * @throws SirhaException si la facultad no es válida
     */
    public void validarFacultad(String facultad) throws SirhaException {
        if (facultad == null || facultad.trim().isEmpty()) {
            throw new SirhaException(SirhaException.FACULTAD_INVALIDA);
        }
        
        // Validar que la facultad exista en el enum Facultad
        try {
            Facultad.valueOf(facultad.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SirhaException(SirhaException.FACULTAD_INVALIDA + facultad);
        }
    }

    /**
     * Valida que el estudiante exista y pertenezca a la facultad especificada.
     * @param idEstudiante ID del estudiante
     * @param facultad facultad a la que debe pertenecer el estudiante
     * @throws SirhaException si el estudiante no existe o no pertenece a la facultad
     */
    public void validarEstudianteFacultad(String idEstudiante, String facultad) throws SirhaException {
        if (idEstudiante == null || idEstudiante.trim().isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + "ID vacío");
        }
        
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante);
        }
        Estudiante estudiante = estudianteOpt.get();
        Facultad facultadEnum = estudiante.getCarrera();
        if (!facultadEnum.name().equalsIgnoreCase(facultad)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante + " en la facultad " + facultad);
        }
    }

    /**
     * Valida que el semestre esté en el rango válido (1-10).
     * @param semestre número de semestre
     * @throws SirhaException si el semestre está fuera del rango válido
     */
    public void validarSemestre(int semestre) throws SirhaException {
        if (semestre < 1 || semestre > 10) {
            throw new SirhaException(SirhaException.SEMESTRE_FUERA_RANGO + " Recibido: " + semestre);
        }
    }

    /**
     * Obtiene la información básica de un estudiante (código, nombre, carrera, semestre).
     * @param idEstudiante ID del estudiante
     * @param facultad facultad del decano
     * @return EstudianteBasicoDTO con la información básica del estudiante
     * @throws SirhaException si ocurre algún error
     */
    public EstudianteBasicoDTO obtenerInformacionBasicaEstudiante(String idEstudiante, String facultad) throws SirhaException {
        // Validar que el estudiante existe y pertenece a la facultad
        validarEstudianteFacultad(idEstudiante, facultad);
        
        // Obtener el estudiante
        Optional<Estudiante> estudianteOpt = usuarioRepository.findById(idEstudiante)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast);
        
        if (estudianteOpt.isEmpty()) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO + idEstudiante);
        }
        
        Estudiante estudiante = estudianteOpt.get();
        
        // Calcular el semestre actual (número de semestres cursados)
        int semestreActual = estudiante.getSemestres().size();
        
        // Crear y retornar el DTO con la información básica
        return new EstudianteBasicoDTO(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getCarrera(),
                semestreActual
        );
    }
}

