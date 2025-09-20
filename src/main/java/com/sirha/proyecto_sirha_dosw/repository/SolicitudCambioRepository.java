package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.SolicitudCambio;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface SolicitudCambioRepository extends MongoRepository<SolicitudCambio, String> {
    // Busca todas las solicitudes de un estudiante específico, ordenadas por fecha de creación descendente
    List<SolicitudCambio> findByIdEstudianteOrderByFechaCreacionDesc(String idEstudiante);
}
