package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Carrera.
 * Permite operaciones CRUD sobre la colección 'carreras' en MongoDB.
 */
@Repository
public interface CarreraRepository extends MongoRepository<Carrera, String> {

    /**
     * Busca una carrera por el nombre de su facultad.
     * @param nombre objeto {@link Facultad} asociado.
     * @return un {@link Optional} con la carrera encontrada, vacío si no existe.
     */
    Optional<Carrera> findByNombre(Facultad nombre);

    /**
     * Busca una carrera a partir de su código único.
     * @param codigo identificador único de la carrera.
     * @return un {@link Optional} con la carrera encontrada, vacío si no existe.
     */
    Optional<Carrera> findByCodigo(String codigo);

    /**
     * Busca una carrera a partir de su identificador único.
     * @param id identificador único en la base de datos.
     * @return un {@link Optional} con la carrera encontrada, vacío si no existe.
     */
    Optional<Carrera> findById(String id);
}