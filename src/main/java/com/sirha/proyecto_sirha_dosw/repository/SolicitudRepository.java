package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de Solicitud en la base de datos MongoDB.
 */
@Repository
public interface SolicitudRepository extends MongoRepository<Solicitud, String> {

    /**
     * Busca todas las solicitudes asociadas a un estudiante específico.
     * @param estudianteId identificador único del estudiante.
     * @return lista de {@link Solicitud} realizadas pr el estudiante.
     */
    List<Solicitud> findByEstudianteId(String estudianteId);

    /**
     * Busca todas las solicitudes que se encuentren en un estado específico.
     * @param estado estado de la solicitud (ej. 'PENDIENTE', 'ACEPTADA', 'RECHAZADA')
     * @return lista de {@link Solicitud} en el estado indicado.
     */
    List<Solicitud> findByEstado(String estado);
}