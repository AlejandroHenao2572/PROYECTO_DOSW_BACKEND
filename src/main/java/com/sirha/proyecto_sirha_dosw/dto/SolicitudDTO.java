package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;

/**
 * DTO para la transferencia de datos relacionados con una Solicitud, de cambio de grupo o materia.
 * Este objeto se utiliza en los controladores para recibir la información necesaria,
 *          al crear una solicitud, sin exponer directamente la entidad del modelo.
 */
public class SolicitudDTO {

    @NotBlank(message = "El ID del estudiante no puede estar vacío")
    private String estudianteId;

    @NotNull(message = "El tipo de solicitud no puede ser nulo")
    private TipoSolicitud tipoSolicitud;

    @NotBlank(message = "El ID del grupo con problemas no puede estar vacío")
    private String grupoProblemaId;

    @NotBlank(message = "El acrónimo de la materia con problemas no puede estar vacío")
    private String materiaProblemaAcronimo;

    // Para solicitudes de CAMBIO_GRUPO, estos campos serán obligatorios
    private String grupoDestinoId;
    private String materiaDestinoAcronimo;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String observaciones;

    // Getters and setters
    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getGrupoProblemaId() {
        return grupoProblemaId;
    }

    public void setGrupoProblemaId(String grupoProblemaId) {
        this.grupoProblemaId = grupoProblemaId;
    }

    public String getMateriaProblemaAcronimo() {
        return materiaProblemaAcronimo;
    }

    public void setMateriaProblemaAcronimo(String materiaProblemaAcronimo) {
        this.materiaProblemaAcronimo = materiaProblemaAcronimo;
    }

    public String getGrupoDestinoId() {
        return grupoDestinoId;
    }

    public void setGrupoDestinoId(String grupoDestinoId) {
        this.grupoDestinoId = grupoDestinoId;
    }

    public String getMateriaDestinoAcronimo() {
        return materiaDestinoAcronimo;
    }

    public void setMateriaDestinoAcronimo(String materiaDestinoAcronimo) {
        this.materiaDestinoAcronimo = materiaDestinoAcronimo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}