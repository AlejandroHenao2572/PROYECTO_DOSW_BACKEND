package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Grupo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface GrupoRepository extends MongoRepository<Grupo, String> {
    List<Grupo> findByMateriaId(String materiaId);
    List<Grupo> findBySemestre(String semestre);
    List<Grupo> findByMateriaIdAndSemestre(String materiaId, String semestre);
    Optional<Grupo> findByNumeroAndMateriaIdAndSemestre(String numero, String materiaId, String semestre);
    List<Grupo> findByProfesorId(String profesorId);
}