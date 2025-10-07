
/**
 * Servicio para la generación de reportes y estadísticas del sistema.
 * Proporciona métodos para obtener estadísticas de grupos más solicitados,
 * reportes de solicitudes y otras métricas del sistema.
 */
package com.sirha.proyecto_sirha_dosw.service;
import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.dto.TasaAprobacionDTO;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import com.sirha.proyecto_sirha_dosw.model.Semestre;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportesService {
    
    private final SolicitudRepository solicitudRepository;
    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    
    /**
     * Constructor para inyección de dependencias.
     * @param solicitudRepository Repositorio de solicitudes
     * @param grupoRepository Repositorio de grupos
     * @param usuarioRepository Repositorio de usuarios (estudiantes)
     * @param carreraRepository Repositorio de carreras
     */
    public ReportesService(SolicitudRepository solicitudRepository, GrupoRepository grupoRepository,
                          UsuarioRepository usuarioRepository, CarreraRepository carreraRepository) {
        this.solicitudRepository = solicitudRepository;
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.carreraRepository = carreraRepository;
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados para cambio.
     * @return Lista de EstadisticasGrupoDTO ordenada por cantidad de solicitudes (descendente)
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitados() {
        return obtenerGruposMasSolicitados(null, 10);
    }
    
    /**
     * Obtiene las estadísticas de los grupos más solicitados para cambio con filtros.
     * @param facultad Facultad específica (null para todas)
     * @param limite Número máximo de grupos a retornar
     * @return Lista de EstadisticasGrupoDTO ordenada por cantidad de solicitudes (descendente)
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitados(Facultad facultad, int limite) {
        List<Grupo> grupos;
        
        // Obtener grupos según la facultad
        if (facultad != null) {
            grupos = grupoRepository.findByMateria_Facultad(facultad);
        } else {
            grupos = grupoRepository.findAll();
        }
        
        List<EstadisticasGrupoDTO> estadisticas = new ArrayList<>();
        
        // Para cada grupo, calcular las estadísticas
        for (Grupo grupo : grupos) {
            if (grupo.getMateria() != null) {
                // Contar solicitudes de cambio hacia este grupo
                long cantidadSolicitudes = solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud(
                    grupo.getId(), TipoSolicitud.CAMBIO_GRUPO);
                
                // Solo incluir grupos que tienen al menos una solicitud
                if (cantidadSolicitudes > 0) {
                    EstadisticasGrupoDTO estadistica = new EstadisticasGrupoDTO(
                        grupo.getId(),
                        grupo.getMateria().getId(),
                        grupo.getMateria().getNombre(),
                        grupo.getMateria().getAcronimo(),
                        grupo.getMateria().getFacultad().name()
                    );
                    
                    estadistica.setCapacidad(grupo.getCapacidad());
                    estadistica.setCantidadInscritos(grupo.getCantidadInscritos());
                    estadistica.setCantidadSolicitudes(cantidadSolicitudes);
                    
                    estadisticas.add(estadistica);
                }
            }
        }
        
        // Ordenar por cantidad de solicitudes (descendente) y aplicar límite
        return estadisticas.stream()
                .sorted(Comparator.comparing(EstadisticasGrupoDTO::getCantidadSolicitudes).reversed())
                .limit(limite)
                .toList();
    }
    
    /**
     * Obtiene estadísticas detalladas de un grupo específico.
     * @param grupoId ID del grupo
     * @return EstadisticasGrupoDTO con información del grupo o null si no existe
     */
    public EstadisticasGrupoDTO obtenerEstadisticasGrupo(String grupoId) {
        return grupoRepository.findById(grupoId)
                .map(grupo -> {
                    if (grupo.getMateria() != null) {
                        long cantidadSolicitudes = solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud(
                            grupo.getId(), TipoSolicitud.CAMBIO_GRUPO);
                        
                        EstadisticasGrupoDTO estadistica = new EstadisticasGrupoDTO(
                            grupo.getId(),
                            grupo.getMateria().getId(),
                            grupo.getMateria().getNombre(),
                            grupo.getMateria().getAcronimo(),
                            grupo.getMateria().getFacultad().name()
                        );
                        
                        estadistica.setCapacidad(grupo.getCapacidad());
                        estadistica.setCantidadInscritos(grupo.getCantidadInscritos());
                        estadistica.setCantidadSolicitudes(cantidadSolicitudes);
                        
                        return estadistica;
                    }
                    return null;
                })
                .orElse(null);
    }
    
    /**
     * Obtiene el total de solicitudes de cambio de grupo en el sistema.
     * @return Número total de solicitudes de cambio
     */
    public long obtenerTotalSolicitudesCambio() {
        return solicitudRepository.findByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO).size();
    }
    
    /**
     * Obtiene estadísticas de grupos más solicitados por facultad.
     * @param facultad Facultad específica
     * @return Lista de EstadisticasGrupoDTO para la facultad especificada
     */
    public List<EstadisticasGrupoDTO> obtenerGruposMasSolicitadosPorFacultad(Facultad facultad) {
        return obtenerGruposMasSolicitados(facultad, 5); // Top 5 por facultad
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo de todas las solicitudes del sistema.
     * @return TasaAprobacionDTO con las métricas globales
     */
    public TasaAprobacionDTO obtenerTasasAprobacionGlobal() {
        long totalSolicitudes = solicitudRepository.count();
        long aprobadas = solicitudRepository.countByEstado(SolicitudEstado.APROBADA);
        long rechazadas = solicitudRepository.countByEstado(SolicitudEstado.RECHAZADA);
        long pendientes = solicitudRepository.countByEstado(SolicitudEstado.PENDIENTE);
        long enRevision = solicitudRepository.countByEstado(SolicitudEstado.EN_REVISION);

        TasaAprobacionDTO tasas = new TasaAprobacionDTO(totalSolicitudes, aprobadas, rechazadas, pendientes, enRevision);
        tasas.setTipoSolicitud("TODAS");
        
        return tasas;
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por facultad.
     * @param facultad Facultad específica
     * @return TasaAprobacionDTO con las métricas de la facultad
     */
    public TasaAprobacionDTO obtenerTasasAprobacionPorFacultad(Facultad facultad) {
        long totalSolicitudes = solicitudRepository.countByFacultad(facultad);
        long aprobadas = solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.APROBADA);
        long rechazadas = solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.RECHAZADA);
        long pendientes = solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.PENDIENTE);
        long enRevision = solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.EN_REVISION);

        TasaAprobacionDTO tasas = new TasaAprobacionDTO(totalSolicitudes, aprobadas, rechazadas, pendientes, enRevision);
        tasas.setFacultad(facultad.name());
        tasas.setTipoSolicitud("TODAS");
        
        return tasas;
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por tipo de solicitud.
     * @param tipoSolicitud Tipo específico de solicitud
     * @return TasaAprobacionDTO con las métricas del tipo de solicitud
     */
    public TasaAprobacionDTO obtenerTasasAprobacionPorTipo(TipoSolicitud tipoSolicitud) {
        // Contar total por tipo de solicitud
        long totalSolicitudes = solicitudRepository.countByTipoSolicitud(tipoSolicitud);
        
        // Para obtener las cantidades por estado y tipo, necesitamos usar las solicitudes y filtrar
        List<Solicitud> solicitudesTipo = solicitudRepository.findByTipoSolicitud(tipoSolicitud);
        
        long aprobadas = solicitudesTipo.stream()
                .mapToLong(s -> s.getEstado() == SolicitudEstado.APROBADA ? 1 : 0)
                .sum();
        long rechazadas = solicitudesTipo.stream()
                .mapToLong(s -> s.getEstado() == SolicitudEstado.RECHAZADA ? 1 : 0)
                .sum();
        long pendientes = solicitudesTipo.stream()
                .mapToLong(s -> s.getEstado() == SolicitudEstado.PENDIENTE ? 1 : 0)
                .sum();
        long enRevision = solicitudesTipo.stream()
                .mapToLong(s -> s.getEstado() == SolicitudEstado.EN_REVISION ? 1 : 0)
                .sum();

        TasaAprobacionDTO tasas = new TasaAprobacionDTO(totalSolicitudes, aprobadas, rechazadas, pendientes, enRevision);
        tasas.setTipoSolicitud(tipoSolicitud.name());
        
        return tasas;
    }

    /**
     * Obtiene las tasas de aprobación vs rechazo por facultad y tipo de solicitud.
     * @param facultad Facultad específica
     * @param tipoSolicitud Tipo específico de solicitud
     * @return TasaAprobacionDTO con las métricas combinadas
     */
    public TasaAprobacionDTO obtenerTasasAprobacionPorFacultadYTipo(Facultad facultad, TipoSolicitud tipoSolicitud) {
        long aprobadas = solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.APROBADA, tipoSolicitud);
        long rechazadas = solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.RECHAZADA, tipoSolicitud);
        long pendientes = solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.PENDIENTE, tipoSolicitud);
        long enRevision = solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.EN_REVISION, tipoSolicitud);
        
        long totalSolicitudes = aprobadas + rechazadas + pendientes + enRevision;

        TasaAprobacionDTO tasas = new TasaAprobacionDTO(totalSolicitudes, aprobadas, rechazadas, pendientes, enRevision);
        tasas.setFacultad(facultad.name());
        tasas.setTipoSolicitud(tipoSolicitud.name());
        
        return tasas;
    }

    /**
     * Calcula los indicadores de avance académico para un estudiante específico.
     * @param estudianteId ID del estudiante
     * @return IndicadoresAvanceDTO con métricas individuales del estudiante
     */
    public IndicadoresAvanceDTO calcularIndicadoresAvanceEstudiante(String estudianteId) {
        // Obtener el estudiante
        Estudiante estudiante = usuarioRepository.findById(estudianteId)
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast)
                .orElse(null);
        
        if (estudiante == null) {
            return null;
        }

        // Crear DTO base
        IndicadoresAvanceDTO indicadores = new IndicadoresAvanceDTO(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getCarrera()
        );

        // Obtener carrera para calcular total de materias y créditos
        Carrera carrera = carreraRepository.findByNombre(estudiante.getCarrera()).orElse(null);
        if (carrera != null) {
            indicadores.setCreditosTotales(carrera.getCreditosTotales());
            indicadores.setTotalMaterias(carrera.getTotalMaterias());
        }

        // Contadores para los estados de semáforo
        int materiasAprobadas = 0;
        int materiasEnProgreso = 0;
        int materiasConProblemas = 0;
        int materiasCanceladas = 0;
        int creditosAprobados = 0;
        double sumaNotas = 0.0;
        int materiasTotales = 0;

        // Calcular el semestre actual
        int semestreActual = estudiante.getSemestres().size();
        indicadores.setSemestreActual(semestreActual);

        // Recorrer todos los semestres y registros
        for (Semestre semestre : estudiante.getSemestres()) {
            for (RegistroMaterias registro : semestre.getRegistros()) {
                materiasTotales++;
                Semaforo estado = registro.getEstado();
                int creditos = registro.getGrupo().getMateria().getCreditos();

                switch (estado) {
                    case VERDE:
                        materiasAprobadas++;
                        creditosAprobados += creditos;
                        // Asumimos una nota promedio para materias aprobadas (se puede mejorar)
                        sumaNotas += 4.0; // Nota promedio aprobatoria
                        break;
                    case AZUL:
                        materiasEnProgreso++;
                        break;
                    case ROJO:
                        materiasConProblemas++;
                        sumaNotas += 2.0; // Nota promedio para materias con problemas
                        break;
                    case CANCELADO:
                        materiasCanceladas++;
                        break;
                }
            }
        }

        // Establecer métricas
        indicadores.setMateriasAprobadas(materiasAprobadas);
        indicadores.setMateriasEnProgreso(materiasEnProgreso);
        indicadores.setMateriasConProblemas(materiasConProblemas);
        indicadores.setMateriasCanceladas(materiasCanceladas);
        indicadores.setCreditosAprobados(creditosAprobados);

        // Calcular promedio general
        if (materiasTotales > 0) {
            double promedioGeneral = sumaNotas / materiasTotales;
            indicadores.setPromedioGeneral(promedioGeneral);
        }

        // Los porcentajes y estado global se calculan automáticamente en el DTO
        indicadores.calcularPorcentajes();

        return indicadores;
    }

    /**
     * Calcula estadísticas globales de indicadores de avance académico.
     * @param facultad Facultad específica (null para todas las facultades)
     * @return IndicadoresAvanceDTO con estadísticas globales
     */
    public IndicadoresAvanceDTO calcularIndicadoresAvanceGlobales(Facultad facultad) {
        // Crear DTO para estadísticas globales
        IndicadoresAvanceDTO estadisticasGlobales = new IndicadoresAvanceDTO();
        estadisticasGlobales.setTipoReporte("ESTADISTICAS_GLOBALES");
        estadisticasGlobales.setFacultad(facultad);

        // Obtener todos los estudiantes (filtrado por facultad si se especifica)
        List<Estudiante> estudiantes = usuarioRepository.findAll().stream()
                .filter(Estudiante.class::isInstance)
                .map(Estudiante.class::cast)
                .filter(estudiante -> facultad == null || estudiante.getCarrera().equals(facultad))
                .toList();

        // Contadores globales
        Map<Semaforo, Long> distribucionEstados = new EnumMap<>(Semaforo.class);
        Map<Facultad, Double> avancePorFacultad = new EnumMap<>(Facultad.class);
        
        int totalMateriasGlobal = 0;
        int materiasAprobadasGlobal = 0;
        int materiasEnProgresoGlobal = 0;
        int materiasConProblemasGlobal = 0;
        int materiasCanceladasGlobal = 0;
        int creditosAprobadasGlobal = 0;
        int creditosTotalesGlobal = 0;
        double sumaPromediosGlobal = 0.0;

        // Inicializar distribución de estados
        distribucionEstados.put(Semaforo.AZUL, 0L);
        distribucionEstados.put(Semaforo.VERDE, 0L);
        distribucionEstados.put(Semaforo.ROJO, 0L);
        distribucionEstados.put(Semaforo.CANCELADO, 0L);

        // Procesar cada estudiante
        for (Estudiante estudiante : estudiantes) {
            IndicadoresAvanceDTO indicadoresEstudiante = calcularIndicadoresAvanceEstudiante(estudiante.getId());
            
            if (indicadoresEstudiante != null) {
                totalMateriasGlobal += indicadoresEstudiante.getTotalMaterias();
                materiasAprobadasGlobal += indicadoresEstudiante.getMateriasAprobadas();
                materiasEnProgresoGlobal += indicadoresEstudiante.getMateriasEnProgreso();
                materiasConProblemasGlobal += indicadoresEstudiante.getMateriasConProblemas();
                materiasCanceladasGlobal += indicadoresEstudiante.getMateriasCanceladas();
                creditosAprobadasGlobal += indicadoresEstudiante.getCreditosAprobados();
                creditosTotalesGlobal += indicadoresEstudiante.getCreditosTotales();
                sumaPromediosGlobal += indicadoresEstudiante.getPromedioGeneral();

                // Actualizar distribución de estados
                distribucionEstados.put(Semaforo.AZUL, 
                    distribucionEstados.get(Semaforo.AZUL) + indicadoresEstudiante.getMateriasEnProgreso());
                distribucionEstados.put(Semaforo.VERDE, 
                    distribucionEstados.get(Semaforo.VERDE) + indicadoresEstudiante.getMateriasAprobadas());
                distribucionEstados.put(Semaforo.ROJO, 
                    distribucionEstados.get(Semaforo.ROJO) + indicadoresEstudiante.getMateriasConProblemas());
                distribucionEstados.put(Semaforo.CANCELADO, 
                    distribucionEstados.get(Semaforo.CANCELADO) + indicadoresEstudiante.getMateriasCanceladas());

                // Actualizar avance por facultad
                Facultad facultadEstudiante = indicadoresEstudiante.getFacultad();
                double avanceEstudiante = indicadoresEstudiante.getPorcentajeAvanceGeneral();
                avancePorFacultad.merge(facultadEstudiante, avanceEstudiante, 
                    (existente, nuevo) -> (existente + nuevo) / 2);
            }
        }

        // Establecer estadísticas globales
        estadisticasGlobales.setTotalMaterias(totalMateriasGlobal);
        estadisticasGlobales.setMateriasAprobadas(materiasAprobadasGlobal);
        estadisticasGlobales.setMateriasEnProgreso(materiasEnProgresoGlobal);
        estadisticasGlobales.setMateriasConProblemas(materiasConProblemasGlobal);
        estadisticasGlobales.setMateriasCanceladas(materiasCanceladasGlobal);
        estadisticasGlobales.setCreditosAprobados(creditosAprobadasGlobal);
        estadisticasGlobales.setCreditosTotales(creditosTotalesGlobal);
        
        // Calcular promedio global
        if (!estudiantes.isEmpty()) {
            estadisticasGlobales.setPromedioGeneral(sumaPromediosGlobal / estudiantes.size());
        }

        // Establecer distribuciones
        estadisticasGlobales.setDistribucionEstados(distribucionEstados);
        estadisticasGlobales.setAvancePorFacultad(avancePorFacultad);

        // Calcular porcentajes y estado global
        estadisticasGlobales.calcularPorcentajes();

        return estadisticasGlobales;
    }

}