package com.sirha.api.service;

import com.sirha.api.dto.GrupoDTO;
import com.sirha.api.model.Grupo;
import com.sirha.api.model.Materia;
import com.sirha.api.model.Profesor;
import com.sirha.api.model.Usuario;
import com.sirha.api.repository.GrupoRepository;
import com.sirha.api.repository.MateriaRepository;
import com.sirha.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public GrupoService(GrupoRepository grupoRepository, MateriaRepository materiaRepository,
                        UsuarioRepository usuarioRepository) {
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Grupo> getAllGrupos() {
        return grupoRepository.findAll();
    }

    public Optional<Grupo> getGrupoById(String id) {
        return grupoRepository.findById(id);
    }

    public Grupo createGrupo(@Valid GrupoDTO grupoDTO) {
        // Get the materia from the database using the ID provided in the DTO
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

        // Set up the grupo with the existing materia
        Grupo grupo = new Grupo(materia, grupoDTO.getCapacidad(), grupoDTO.getHorarios());

        // Assign profesor if provided
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

    public Grupo updateGrupo(String id, @Valid GrupoDTO grupoDTO) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(id);
        if (grupoOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo con ID " + id + " no encontrado");
        }

        Grupo grupo = grupoOpt.get();

        // Update materia if provided
        if (grupoDTO.getMateriaId() != null) {
            Optional<Materia> materiaOpt = materiaRepository.findById(grupoDTO.getMateriaId());
            if (materiaOpt.isEmpty()) {
                throw new IllegalArgumentException("La materia con ID " + grupoDTO.getMateriaId() + " no existe");
            }
            grupo.setMateria(materiaOpt.get());
        }

        // Update profesor if provided
        if (grupoDTO.getProfesorId() != null) {
            Optional<Usuario> profesorOpt = usuarioRepository.findById(grupoDTO.getProfesorId());
            if (profesorOpt.isEmpty() || !(profesorOpt.get() instanceof Profesor)) {
                throw new IllegalArgumentException("El profesor con ID " + grupoDTO.getProfesorId() + " no existe o no es un profesor");
            }
            grupo.setProfesor((Profesor) profesorOpt.get());
        }

        // Update other fields
        if (grupoDTO.getCapacidad() > 0) {
            grupo.setCapacidad(grupoDTO.getCapacidad());

            // Update estaCompleto based on new capacity and current enrollment
            grupo.setEstaCompleto(grupo.getCantidadInscritos() >= grupoDTO.getCapacidad());
        }

        if (grupoDTO.getHorarios() != null && !grupoDTO.getHorarios().isEmpty()) {
            grupo.setHorarios(grupoDTO.getHorarios());
        }

        return grupoRepository.save(grupo);
    }

    public void deleteGrupo(String id) {
        if (!grupoRepository.existsById(id)) {
            throw new IllegalArgumentException("Grupo con ID " + id + " no encontrado");
        }
        grupoRepository.deleteById(id);
    }

    public List<Grupo> getGruposByMateria(String materiaId) {
        return grupoRepository.findByMateria_Id(materiaId);
    }

    public List<Grupo> getGruposByProfesor(String profesorId) {
        return grupoRepository.findByProfesor_Id(profesorId);
    }

    public List<Grupo> getGruposDisponibles() {
        return grupoRepository.findByEstaCompletoFalse();
    }

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