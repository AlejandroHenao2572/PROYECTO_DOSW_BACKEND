package com.sirha.api.repository;

import com.sirha.api.model.Solicitud;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends MongoRepository<Solicitud, String> {
    List<Solicitud> findByEstudianteId(String estudianteId);
    List<Solicitud> findByEstado(String estado);
}