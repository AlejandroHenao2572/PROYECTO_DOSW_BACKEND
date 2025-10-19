package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.AsignacionProfesorDTO;
import com.sirha.proyecto_sirha_dosw.dto.CapacidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.Profesor;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Servicio que gestiona la lógica relacionada con los {@link Grupo}
 * Permite crear, actualizar, eliminar grupos, asignar materias y profesores, además
 *          también administra la inscripción de estudiantes.
 */

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;


    /**
     * Constructor con inyección de dependencias.
     * @param grupoRepository repositorio de {@link Grupo}
     * @param materiaRepository repositorio de {@link Materia}
     * @param usuarioRepository repositorio de {@link Usuario}
     */
    @Autowired
    public GrupoService(GrupoRepository grupoRepository, MateriaRepository materiaRepository,
                        UsuarioRepository usuarioRepository) {
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los grupos registrados.
     * @return lista de {@link Grupo}
     */
    public List<Grupo> getAllGrupos() {
        return grupoRepository.findAll();
    }

    /**
     * Busca un grupo por su ID.
     * @param id identificador del grupo.
     * @return un {@link Optional} con el grupo si existe.
     */
    public Optional<Grupo> getGrupoById(String id) {
        return grupoRepository.findById(id);
    }

    /**
     * Crea un nuevo grupo con base en la información del DTO y realiza validaciones.
     * @param grupoDTO datos del grupo.
     * @return el grupo creado.
     * @throws IllegalArgumentException si la materia o profesor no existen.
     */
    public Grupo createGrupo(@Valid GrupoDTO grupoDTO) throws SirhaException {
        Materia materia = null;
        if (grupoDTO.getMateriaId() != null) {
            Optional<Materia> materiaOpt = materiaRepository.findById(grupoDTO.getMateriaId());
            if (materiaOpt.isEmpty()) {
                throw new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA);
            }
            materia = materiaOpt.get();
        } else {
            throw new SirhaException(SirhaException.ERROR_FALTAN_DATOS);
        }
        Grupo grupo = new Grupo(materia, grupoDTO.getCapacidad(), grupoDTO.getHorarios());
        if (grupoDTO.getProfesorId() != null) {
            Optional<Usuario> profesorOpt = usuarioRepository.findById(grupoDTO.getProfesorId());
            if (profesorOpt.isPresent() && profesorOpt.get() instanceof Profesor profesor) {
                grupo.setProfesor(profesor);
            } else {
                throw new SirhaException(SirhaException.PROFESOR_NO_ENCONTRADO);
            }
        }
        return grupoRepository.save(grupo);
    }

    /**
     * Actualiza los datos de un grupo existente y realiza validaciones.
     * @param id ID del grupo a actualizar.
     * @param grupoDTO datos a modificar.
     * @return el grupo actualizado.
     * @throws IllegalArgumentException si el grupo, materia o profesor no existen.
     */
    public Grupo updateGrupo(String id, @Valid GrupoDTO grupoDTO) throws SirhaException {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }
        Grupo grupo = grupoOpt.get();
        if (grupoDTO.getMateriaId() != null) {
            Optional<Materia> materiaOpt = materiaRepository.findById(grupoDTO.getMateriaId());
            if (materiaOpt.isEmpty()) {
                throw new SirhaException(SirhaException.MATERIA_NO_ENCONTRADA);
            }
            grupo.setMateria(materiaOpt.get());
        }
        if (grupoDTO.getProfesorId() != null) {
            Optional<Usuario> profesorOpt = usuarioRepository.findById(grupoDTO.getProfesorId());
            if (profesorOpt.isEmpty() || !(profesorOpt.get() instanceof Profesor)) {
                throw new SirhaException(SirhaException.PROFESOR_NO_ENCONTRADO);
            }
            grupo.setProfesor((Profesor) profesorOpt.get());
        }
        if (grupoDTO.getCapacidad() > 0) {
            grupo.setCapacidad(grupoDTO.getCapacidad());
            grupo.setEstaCompleto(grupo.getCantidadInscritos() >= grupoDTO.getCapacidad());
        }
        if (grupoDTO.getHorarios() != null && !grupoDTO.getHorarios().isEmpty()) {
            grupo.setHorarios(grupoDTO.getHorarios());
        }
        return grupoRepository.save(grupo);
    }
    /**
     * Elimina un grupo por su ID.
     * @param id identificador del grupo.
     * @throws IllegalArgumentException si el grupo no existe.
     */
    public void deleteGrupo(String id) throws SirhaException{
        if (!grupoRepository.existsById(id)) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }
        grupoRepository.deleteById(id);
    }

    /**
     * Obtiene todos los grupos asociados a una materia.
     * @param materiaId ID de la materia.
     * @return lista de {@link Grupo}
     */
    public List<Grupo> getGruposByMateria(String materiaId) {
        return grupoRepository.findByMateria_Id(materiaId);
    }

    /**
     * Obtiene todos los grupos asignados a un profesor.
     * @param profesorId ID del profesor.
     * @return lista de {@link Grupo}
     */
    public List<Grupo> getGruposByProfesor(String profesorId) {
        return grupoRepository.findByProfesor_Id(profesorId);
    }

    /**
     * Obtiene todos los grupos que aún tienen cupos disponibles.
     * @return lista de {@link Grupo} con {@code estaCompleto = false}
     */
    public List<Grupo> getGruposDisponibles() {
        return grupoRepository.findByEstaCompletoFalse();
    }

    /**
     * Obtiene todos los grupos disponibles (con cupos) de una materia específica.
     * @param materiaId ID de la materia
     * @return lista de {@link Grupo} de la materia especificada con {@code estaCompleto = false}
     */
    public List<Grupo> getGruposDisponiblesPorMateria(String materiaId) {
        // Obtener todos los grupos de la materia
        List<Grupo> gruposMateria = grupoRepository.findByMateria_Id(materiaId);
        
        // Filtrar solo los que tienen cupos disponibles
        return gruposMateria.stream()
                .filter(grupo -> !grupo.isEstaCompleto())
                .toList();
    }

    /**
     * Agrega un estudiante a un grupo y realiza algunas validaciones.
     * @param grupoId ID del grupo.
     * @param estudianteId ID del estudiante.
     * @return grupo actualizado con el estudiante agregado.
     * @throws IllegalArgumentException si alguna validación falla.
     */
    public Grupo addEstudianteToGrupo(String grupoId, String estudianteId) throws SirhaException {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }

        Grupo grupo = grupoOpt.get();

        // Check if the grupo is already full
        if (grupo.isEstaCompleto()) {
            throw new SirhaException(SirhaException.GRUPO_COMPLETO);
        }

        // Check if the student is already in the grupo
        if (grupo.getEstudiantesId().contains(estudianteId)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_YA_INSCRITO);
        }

        // Verify the student exists
        if (!usuarioRepository.existsById(estudianteId)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO);
        }

        grupo.addEstudiante(estudianteId);
        return grupoRepository.save(grupo);
    }

    /**
     * Elimina a un estudiante de un grupo.
     * @param grupoId ID del grupo.
     * @param estudianteId ID del estudiante.
     * @return grupo actualizado sin el estudiante.
     * @throws IllegalArgumentException si el grupo no existe o si el estudiante no está inscrito.
     */
    public Grupo removeEstudianteFromGrupo(String grupoId, String estudianteId) throws SirhaException{
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }

        Grupo grupo = grupoOpt.get();

        // Check if the student is in the grupo
        if (!grupo.getEstudiantesId().contains(estudianteId)) {
            throw new SirhaException(SirhaException.ESTUDIANTE_NO_INSCRITO);
        }

        grupo.removeEstudiante(estudianteId);
        return grupoRepository.save(grupo);
    }

    /**
     * Obtiene la información de capacidad de un grupo específico.
     * @param grupoId ID del grupo
     * @return DTO con información de capacidad del grupo
     * @throws SirhaException si el grupo no existe
     */
    public CapacidadGrupoDTO obtenerCapacidadGrupo(String grupoId) throws SirhaException {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }
        
        return convertirGrupoACapacidadDTO(grupoOpt.get());
    }

    /**
     * Obtiene la información de capacidad de todos los grupos.
     * @return lista de DTOs con información de capacidad de todos los grupos
     */
    public List<CapacidadGrupoDTO> obtenerCapacidadTodosLosGrupos() {
        List<Grupo> grupos = grupoRepository.findAll();
        return grupos.stream()
                .map(this::convertirGrupoACapacidadDTO)
                .toList();
    }

    /**
     * Obtiene la información de capacidad de grupos por materia.
     * @param materiaId ID de la materia
     * @return lista de DTOs con información de capacidad de grupos de la materia
     */
    public List<CapacidadGrupoDTO> obtenerCapacidadGruposPorMateria(String materiaId) {
        List<Grupo> grupos = grupoRepository.findByMateria_Id(materiaId);
        return grupos.stream()
                .map(this::convertirGrupoACapacidadDTO)
                .toList();
    }

    /**
     * Convierte un grupo a DTO de capacidad.
     * @param grupo el grupo a convertir
     * @return DTO con información de capacidad
     */
    private CapacidadGrupoDTO convertirGrupoACapacidadDTO(Grupo grupo) {
        Materia materia = grupo.getMateria();
        CapacidadGrupoDTO dto = new CapacidadGrupoDTO(
            grupo.getId(),
            materia.getId(),
            materia.getNombre(),
            materia.getAcronimo(),
            grupo.getCapacidad(),
            grupo.getCantidadInscritos()
        );
        
        // Agregar información del profesor si está asignado
        if (grupo.getProfesor() != null) {
            dto.setProfesorId(grupo.getProfesor().getId());
            dto.setProfesorNombre(grupo.getProfesor().getNombre() + " " + grupo.getProfesor().getApellido());
        }
        
        return dto;
    }

    /**
     * Asigna un profesor a un grupo.
     * @param grupoId ID del grupo
     * @param profesorId ID del profesor
     * @return grupo actualizado con el profesor asignado
     * @throws SirhaException si el grupo no existe o el profesor no es válido
     */
    public Grupo asignarProfesorAGrupo(String grupoId, String profesorId) throws SirhaException {
        // Verificar que el grupo existe
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }

        // Verificar que el profesor existe y es un profesor
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(profesorId);
        if (usuarioOpt.isEmpty()) {
            throw new SirhaException(SirhaException.PROFESOR_NO_ENCONTRADO);
        }

        Usuario usuario = usuarioOpt.get();
        if (!(usuario instanceof Profesor)) {
            throw new SirhaException("El usuario especificado no es un profesor");
        }

        Grupo grupo = grupoOpt.get();
        grupo.setProfesor((Profesor) usuario);
        
        return grupoRepository.save(grupo);
    }

    /**
     * Remueve la asignación de profesor de un grupo.
     * @param grupoId ID del grupo
     * @return grupo actualizado sin profesor asignado
     * @throws SirhaException si el grupo no existe
     */
    public Grupo removerProfesorDeGrupo(String grupoId) throws SirhaException {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new SirhaException(SirhaException.GRUPO_NO_ENCONTRADO);
        }

        Grupo grupo = grupoOpt.get();
        grupo.setProfesor(null);
        
        return grupoRepository.save(grupo);
    }

    /**
     * Obtiene todos los grupos asignados a un profesor específico con información de capacidad.
     * @param profesorId ID del profesor
     * @return lista de DTOs con información de capacidad de grupos del profesor
     */
    public List<CapacidadGrupoDTO> obtenerGruposConCapacidadPorProfesor(String profesorId) {
        List<Grupo> grupos = grupoRepository.findByProfesor_Id(profesorId);
        return grupos.stream()
                .map(this::convertirGrupoACapacidadDTO)
                .toList();
    }
}