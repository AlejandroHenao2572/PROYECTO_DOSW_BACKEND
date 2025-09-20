package com.sirha.proyecto_sirha_dosw.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sirha.proyecto_sirha_dosw.model.Curso;

@Repository
public interface CursoRepository extends MongoRepository<Curso, String> {
    // Buscar cursos por facultad
    List<Curso> findByFacultad(String facultad);

    // Buscar cursos por nombre exacto
    List<Curso> findByNombre(String nombre);

    // Buscar cursos que contengan cierta palabra en el nombre (ignora mayúsculas)
    List<Curso> findByNombreContainingIgnoreCase(String palabraClave);

    // Buscar curso por código único
    Curso findByCodigo(String codigo);

    // Buscar cursos por profesor asignado
    List<Curso> findByProfesorId(String profesorId);

    // Buscar cursos por cantidad de créditos
    List<Curso> findByCreditos(int creditos);

    // Buscar cursos con más de X créditos
    List<Curso> findByCreditosGreaterThan(int minCreditos);

    // Buscar cursos donde esté inscrito un estudiante
    List<Curso> findByEstudiantesContaining(String estudianteId);

    // Buscar cursos por programa académico
    List<Curso> findByPrograma(String programa);

    // Buscar cursos activos o inactivos
    List<Curso> findByActivo(boolean activo);
}

