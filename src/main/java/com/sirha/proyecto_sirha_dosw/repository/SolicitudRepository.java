package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
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
     * @return lista de {@link Solicitud} realizadas por el estudiante.
     */
    List<Solicitud> findByEstudianteId(String estudianteId);

    /**
     * Busca todas las solicitudes que se encuentren en un estado específico.
     * @param estado estado de la solicitud (ej. PENDIENTE, EN_REVISION, APROBADA, RECHAZADA)
     * @return lista de {@link Solicitud} en el estado indicado.
     */
    List<Solicitud> findByEstado(SolicitudEstado estado);
    
    /**
     * Busca todas las solicitudes asociadas a una facultad específica.
     * @param facultad facultad a la que pertenecen las solicitudes.
     * @return lista de {@link Solicitud} en la facultad indicada.
     */
    List<Solicitud> findByFacultad(Facultad facultad);

    /**
     * Busca todas las solicitudes asociadas a una facultad específica y un estado específico.
     * @param facultad facultad a la que pertenecen las solicitudes.
     * @param estado estado de la solicitud (ej. PENDIENTE, APROBADA, RECHAZADA).
     * @return lista de {@link Solicitud} en la facultad y estado indicados.
     */
    List<Solicitud> findByFacultadAndEstado(Facultad facultad, SolicitudEstado estado);

    /**
     * Busca todas las solicitudes de cambio de grupo por tipo de solicitud.
     * @param tipoSolicitud tipo específico de solicitud
     * @return lista de {@link Solicitud} del tipo indicado.
     */
    List<Solicitud> findByTipoSolicitud(TipoSolicitud tipoSolicitud);

    /**
     * Cuenta la cantidad de solicitudes de cambio hacia un grupo específico.
     * @param grupoId ID del grupo destino
     * @param tipoSolicitud tipo de solicitud (CAMBIO_GRUPO)
     * @return cantidad de solicitudes hacia ese grupo
     */
    long countByGrupoDestino_IdAndTipoSolicitud(String grupoId, TipoSolicitud tipoSolicitud);

    /**
     * Busca todas las solicitudes de cambio hacia un grupo específico.
     * @param grupoId ID del grupo destino
     * @param tipoSolicitud tipo de solicitud (CAMBIO_GRUPO)
     * @return lista de solicitudes hacia ese grupo
     */
    List<Solicitud> findByGrupoDestino_IdAndTipoSolicitud(String grupoId, TipoSolicitud tipoSolicitud);
}