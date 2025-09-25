package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
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
    public Grupo createGrupo(@Valid GrupoDTO grupoDTO) {
        Materia materia = null;
        if (grupoDTO.getMateriaId() != null) {
            Optional<Materia> materiaOpt = materiaRepository.findById(grupoDTO.getMateriaId());
            if (materiaOpt.isEmpty()) {
                throw new IllegalArgumentException("La materia con ID " + grupoDTO.getMateriaId() + " no existe");
            }
            materia = materiaOpt.get();
        } else {
            throw new IllegalArgumentException("Se requiere el ID de una materia existente para crear un grupo");
        }
        Grupo grupo = new Grupo(materia, grupoDTO.getCapacidad(), grupoDTO.getHorarios());
        if (grupoDTO.getProfesorId() != null) {
            Optional<Usuario> profesorOpt = usuarioRepository.findById(grupoDTO.getProfesorId());
            if (profesorOpt.isPresent() && profesorOpt.get() instanceof Profesor) {
                grupo.setProfesor((Profesor) profesorOpt.get());
            } else {
                throw new IllegalArgumentException("El profesor con ID " + grupoDTO.getProfesorId() + " no existe o no es un profesor");
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
    public Grupo updateGrupo(String id, @Valid GrupoDTO grupoDTO) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        if (grupoOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo con ID " + id + " no encontrado");
        }
        Grupo grupo = grupoOpt.get();
        if (grupoDTO.getMateriaId() != null) {
            Optional<Materia> materiaOpt = materiaRepository.findById(grupoDTO.getMateriaId());
            if (materiaOpt.isEmpty()) {
                throw new IllegalArgumentException("La materia con ID " + grupoDTO.getMateriaId() + " no existe");
            }
            grupo.setMateria(materiaOpt.get());
        }
        if (grupoDTO.getProfesorId() != null) {
            Optional<Usuario> profesorOpt = usuarioRepository.findById(grupoDTO.getProfesorId());
            if (profesorOpt.isEmpty() || !(profesorOpt.get() instanceof Profesor)) {
                throw new IllegalArgumentException("El profesor con ID " + grupoDTO.getProfesorId() + " no existe o no es un profesor");
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
    public void deleteGrupo(String id) {
        if (!grupoRepository.existsById(id)) {
            throw new IllegalArgumentException("Grupo con ID " + id + " no encontrado");
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
     * Agrega un estudiante a un grupo y realiza algunas validaciones.
     * @param grupoId ID del grupo.
     * @param estudianteId ID del estudiante.
     * @return grupo actualizado con el estudiante agregado.
     * @throws IllegalArgumentException si alguna validación falla.
     */
    public Grupo addEstudianteToGrupo(String grupoId, String estudianteId) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo con ID " + grupoId + " no encontrado");
        }

        Grupo grupo = grupoOpt.get();

        // Check if the grupo is already full
        if (grupo.isEstaCompleto()) {
            throw new IllegalArgumentException("El grupo está completo, no se pueden agregar más estudiantes");
        }

        // Check if the student is already in the grupo
        if (grupo.getEstudiantesId().contains(estudianteId)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este grupo");
        }

        // Verify the student exists
        if (!usuarioRepository.existsById(estudianteId)) {
            throw new IllegalArgumentException("El estudiante con ID " + estudianteId + " no existe");
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
    public Grupo removeEstudianteFromGrupo(String grupoId, String estudianteId) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        if (grupoOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo con ID " + grupoId + " no encontrado");
        }

        Grupo grupo = grupoOpt.get();

        // Check if the student is in the grupo
        if (!grupo.getEstudiantesId().contains(estudianteId)) {
            throw new IllegalArgumentException("El estudiante no está inscrito en este grupo");
        }

        grupo.removeEstudiante(estudianteId);
        return grupoRepository.save(grupo);
    }
}