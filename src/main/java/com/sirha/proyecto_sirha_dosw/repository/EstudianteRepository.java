package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface EstudianteRepository extends MongoRepository<Estudiante, String> {
    Optional<Estudiante> findByMail(String credencial);
}