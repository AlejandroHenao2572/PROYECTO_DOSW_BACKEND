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

    Optional<Carrera> findByNombre(Facultad nombre);
    Optional<Carrera> findByCodigo(String codigo);
    Optional<Carrera> findById(String id);
}