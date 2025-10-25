/**
 * DTO para representar indicadores de avance en planes de estudio con semaforización global.
 * Contiene métricas sobre el progreso académico de estudiantes y estadísticas globales.
 */
package com.sirha.proyecto_sirha_dosw.dto;

import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import java.time.LocalDateTime;
import java.util.Map;

public class IndicadoresAvanceDTO {
    
    // Información del estudiante (para reportes individuales)
    private String estudianteId;
    private String estudianteNombre;
    private String estudianteApellido;
    private Facultad facultad;
    
    // Métricas de progreso académico
    private int totalMaterias;
    private int materiasAprobadas;      // VERDE
    private int materiasEnProgreso;     // AZUL
    private int materiasConProblemas;   // ROJO
    private int materiasCanceladas;     // CANCELADO
    
    // Indicadores de avance (porcentajes)
    private double porcentajeAvanceGeneral;
    private double porcentajeAprobacion;
    private double porcentajeProblemas;
    private double porcentajeCancelaciones;
    
    // Semaforización global
    private Semaforo estadoGlobal;
    private String descripcionEstado;
    
    // Información adicional
    private int semestreActual;
    private int creditosAprobados;
    private int creditosTotales;
    private double promedioGeneral;
    
    // Metadata
    private LocalDateTime fechaCalculo;
    private String tipoReporte; // "INDIVIDUAL" o "ESTADISTICAS_GLOBALES"
    
    // Para estadísticas globales
    private Map<Semaforo, Long> distribucionEstados;
    private Map<Facultad, Double> avancePorFacultad;
    
    /**
     * Constructor por defecto.
     */
    public IndicadoresAvanceDTO() {
        this.fechaCalculo = LocalDateTime.now();
    }
    
    /**
     * Constructor para reporte individual de estudiante.
     * @param estudianteId ID del estudiante
     * @param estudianteNombre Nombre del estudiante
     * @param estudianteApellido Apellido del estudiante
     * @param facultad Facultad del estudiante
     */
    public IndicadoresAvanceDTO(String estudianteId, String estudianteNombre, 
                               String estudianteApellido, Facultad facultad) {
        this();
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.estudianteApellido = estudianteApellido;
        this.facultad = facultad;
        this.tipoReporte = "INDIVIDUAL";
    }
    
    /**
     * Calcula los porcentajes de avance basado en las métricas.
     */
    public void calcularPorcentajes() {
        if (totalMaterias > 0) {
            this.porcentajeAvanceGeneral = (double) materiasAprobadas / totalMaterias * 100;
            this.porcentajeAprobacion = (double) materiasAprobadas / totalMaterias * 100;
            this.porcentajeProblemas = (double) materiasConProblemas / totalMaterias * 100;
            this.porcentajeCancelaciones = (double) materiasCanceladas / totalMaterias * 100;
        } else {
            this.porcentajeAvanceGeneral = 0.0;
            this.porcentajeAprobacion = 0.0;
            this.porcentajeProblemas = 0.0;
            this.porcentajeCancelaciones = 0.0;
        }
        
        // Calcular estado global basado en los porcentajes
        calcularEstadoGlobal();
    }
    
    /**
     * Determina el estado global del semáforo basado en las métricas.
     */
    private void calcularEstadoGlobal() {
        if (porcentajeProblemas >= 30 || porcentajeCancelaciones >= 20) {
            this.estadoGlobal = Semaforo.ROJO;
            this.descripcionEstado = "Situación crítica: Alto porcentaje de problemas o cancelaciones";
        } else if (porcentajeAprobacion >= 80) {
            this.estadoGlobal = Semaforo.VERDE;
            this.descripcionEstado = "Excelente progreso académico";
        } else if (porcentajeAprobacion >= 60) {
            this.estadoGlobal = Semaforo.AZUL;
            this.descripcionEstado = "Progreso académico satisfactorio";
        } else {
            this.estadoGlobal = Semaforo.ROJO;
            this.descripcionEstado = "Requiere atención: Bajo porcentaje de aprobación";
        }
    }
    
    // Getters y setters
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    
    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }
    
    public String getEstudianteApellido() { return estudianteApellido; }
    public void setEstudianteApellido(String estudianteApellido) { this.estudianteApellido = estudianteApellido; }
    
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
    
    public int getTotalMaterias() { return totalMaterias; }
    public void setTotalMaterias(int totalMaterias) { 
        this.totalMaterias = totalMaterias;
        calcularPorcentajes();
    }
    
    public int getMateriasAprobadas() { return materiasAprobadas; }
    public void setMateriasAprobadas(int materiasAprobadas) { 
        this.materiasAprobadas = materiasAprobadas;
        calcularPorcentajes();
    }
    
    public int getMateriasEnProgreso() { return materiasEnProgreso; }
    public void setMateriasEnProgreso(int materiasEnProgreso) { 
        this.materiasEnProgreso = materiasEnProgreso;
        calcularPorcentajes();
    }
    
    public int getMateriasConProblemas() { return materiasConProblemas; }
    public void setMateriasConProblemas(int materiasConProblemas) { 
        this.materiasConProblemas = materiasConProblemas;
        calcularPorcentajes();
    }
    
    public int getMateriasCanceladas() { return materiasCanceladas; }
    public void setMateriasCanceladas(int materiasCanceladas) { 
        this.materiasCanceladas = materiasCanceladas;
        calcularPorcentajes();
    }
    
    public double getPorcentajeAvanceGeneral() { return porcentajeAvanceGeneral; }
    public void setPorcentajeAvanceGeneral(double porcentajeAvanceGeneral) { this.porcentajeAvanceGeneral = porcentajeAvanceGeneral; }
    
    public double getPorcentajeAprobacion() { return porcentajeAprobacion; }
    public void setPorcentajeAprobacion(double porcentajeAprobacion) { this.porcentajeAprobacion = porcentajeAprobacion; }
    
    public double getPorcentajeProblemas() { return porcentajeProblemas; }
    public void setPorcentajeProblemas(double porcentajeProblemas) { this.porcentajeProblemas = porcentajeProblemas; }
    
    public double getPorcentajeCancelaciones() { return porcentajeCancelaciones; }
    public void setPorcentajeCancelaciones(double porcentajeCancelaciones) { this.porcentajeCancelaciones = porcentajeCancelaciones; }
    
    public Semaforo getEstadoGlobal() { return estadoGlobal; }
    public void setEstadoGlobal(Semaforo estadoGlobal) { this.estadoGlobal = estadoGlobal; }
    
    public String getDescripcionEstado() { return descripcionEstado; }
    public void setDescripcionEstado(String descripcionEstado) { this.descripcionEstado = descripcionEstado; }
    
    public int getSemestreActual() { return semestreActual; }
    public void setSemestreActual(int semestreActual) { this.semestreActual = semestreActual; }
    
    public int getCreditosAprobados() { return creditosAprobados; }
    public void setCreditosAprobados(int creditosAprobados) { this.creditosAprobados = creditosAprobados; }
    
    public int getCreditosTotales() { return creditosTotales; }
    public void setCreditosTotales(int creditosTotales) { this.creditosTotales = creditosTotales; }
    
    public double getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(double promedioGeneral) { this.promedioGeneral = promedioGeneral; }
    
    public LocalDateTime getFechaCalculo() { return fechaCalculo; }
    public void setFechaCalculo(LocalDateTime fechaCalculo) { this.fechaCalculo = fechaCalculo; }
    
    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }
    
    public Map<Semaforo, Long> getDistribucionEstados() { return distribucionEstados; }
    public void setDistribucionEstados(Map<Semaforo, Long> distribucionEstados) { this.distribucionEstados = distribucionEstados; }
    
    public Map<Facultad, Double> getAvancePorFacultad() { return avancePorFacultad; }
    public void setAvancePorFacultad(Map<Facultad, Double> avancePorFacultad) { this.avancePorFacultad = avancePorFacultad; }
}