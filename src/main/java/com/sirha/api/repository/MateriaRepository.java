package com.sirha.api.repository;

import com.sirha.api.model.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MateriaRepository extends MongoRepository<Materia, String> {
    Optional<Materia> findById(String id);
    Optional<Materia> findByAcronimo(String acronimo);
    Optional<Materia> findByNombre(String nombre);
}
