package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad Materia.
 */
@Repository
public interface MateriaRepository extends MongoRepository<Materia, String> {

    /**
     * Busca una {@link  Materia} por su identificador único.
     * @param id identificador de la materia
     * @return un {@link Optional} que contiene la materia si existe, vacío si no.
     */
    Optional<Materia> findById(String id);

    /**
     * Busca una {@link Materia} por su acrónimo.
     * @param acronimo acrónimo de la materia.
     * @return un {@link Optional} que contiene la materia si existe, vacío si no.
     */
    Optional<Materia> findByAcronimo(String acronimo);

    /**
     * Busca una {@link Materia} por su nombre completo.
     * @param nombre nombre de la materia.
     * @return un {@link Optional} que contiene la materia si existe, vacío si no.
     */
    Optional<Materia> findByNombre(String nombre);
}
