package com.sirha.proyecto_sirha_dosw.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.dto.TasaAprobacionDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.service.ReportesService;

/**
 * Tests unitarios para ReportesController.
 * Valida todos los endpoints de reportes y estadísticas del sistema.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReportesController Tests")
class ReportesControllerTest {

    @Mock
    private ReportesService reportesService;

    @InjectMocks
    private ReportesController reportesController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportesController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ========== PRUEBAS PARA GRUPOS MÁS SOLICITADOS ==========

    @Test
    @DisplayName("Debe obtener grupos más solicitados exitosamente")
    void testObtenerGruposMasSolicitados_Success() throws Exception {
        // Given
        List<EstadisticasGrupoDTO> estadisticas = Arrays.asList(
            createEstadisticasGrupoDTO("1", "GRUPO1", 25L),
            createEstadisticasGrupoDTO("2", "GRUPO2", 20L)
        );
        when(reportesService.obtenerGruposMasSolicitados(null, 10))
            .thenReturn(estadisticas);

        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].grupoId").value("1"))
                .andExpect(jsonPath("$[0].cantidadSolicitudes").value(25))
                .andExpect(jsonPath("$[1].grupoId").value("2"))
                .andExpect(jsonPath("$[1].cantidadSolicitudes").value(20));
    }

    @Test
    @DisplayName("Debe obtener grupos más solicitados con límite personalizado")
    void testObtenerGruposMasSolicitados_ConLimite() throws Exception {
        // Given
        List<EstadisticasGrupoDTO> estadisticas = Arrays.asList(
            createEstadisticasGrupoDTO("1", "GRUPO1", 25L)
        );
        when(reportesService.obtenerGruposMasSolicitados(null, 5))
            .thenReturn(estadisticas);

        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados")
                .param("limite", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Debe manejar error interno al obtener grupos más solicitados")
    void testObtenerGruposMasSolicitados_Error() throws Exception {
        // Given
        when(reportesService.obtenerGruposMasSolicitados(null, 10))
            .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Debe obtener grupos más solicitados por facultad exitosamente")
    void testObtenerGruposMasSolicitadosPorFacultad_Success() throws Exception {
        // Given
        List<EstadisticasGrupoDTO> estadisticas = Arrays.asList(
            createEstadisticasGrupoDTO("1", "GRUPO1", 15L)
        );
        when(reportesService.obtenerGruposMasSolicitados(Facultad.INGENIERIA_SISTEMAS, 5))
            .thenReturn(estadisticas);

        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados/facultad/INGENIERIA_SISTEMAS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cantidadSolicitudes").value(15));
    }

    @Test
    @DisplayName("Debe retornar error 400 por facultad inválida")
    void testObtenerGruposMasSolicitadosPorFacultad_FacultadInvalida() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados/facultad/FACULTAD_INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe manejar error interno por facultad")
    void testObtenerGruposMasSolicitadosPorFacultad_ErrorInterno() throws Exception {
        // Given
        when(reportesService.obtenerGruposMasSolicitados(any(Facultad.class), anyInt()))
            .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/api/reportes/grupos-mas-solicitados/facultad/INGENIERIA_SISTEMAS"))
                .andExpect(status().isInternalServerError());
    }

    // ========== PRUEBAS PARA ESTADÍSTICAS DE GRUPO ==========

    @Test
    @DisplayName("Debe obtener estadísticas de grupo exitosamente")
    void testObtenerEstadisticasGrupo_Success() throws Exception {
        // Given
        EstadisticasGrupoDTO estadisticas = createEstadisticasGrupoDTO("1", "GRUPO1", 10L);
        when(reportesService.obtenerEstadisticasGrupo("1")).thenReturn(estadisticas);

        // When & Then
        mockMvc.perform(get("/api/reportes/grupo/1/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupoId").value("1"))
                .andExpect(jsonPath("$.cantidadSolicitudes").value(10));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando grupo no existe")
    void testObtenerEstadisticasGrupo_NotFound() throws Exception {
        // Given
        when(reportesService.obtenerEstadisticasGrupo("999")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/reportes/grupo/999/estadisticas"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe manejar error interno al obtener estadísticas de grupo")
    void testObtenerEstadisticasGrupo_Error() throws Exception {
        // Given
        when(reportesService.obtenerEstadisticasGrupo("1"))
            .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/api/reportes/grupo/1/estadisticas"))
                .andExpect(status().isInternalServerError());
    }

    // ========== PRUEBAS PARA TOTAL SOLICITUDES ==========

    @Test
    @DisplayName("Debe obtener total de solicitudes de cambio exitosamente")
    void testObtenerTotalSolicitudesCambio_Success() throws Exception {
        // Given
        when(reportesService.obtenerTotalSolicitudesCambio()).thenReturn(150L);

        // When & Then
        mockMvc.perform(get("/api/reportes/total-solicitudes-cambio"))
                .andExpect(status().isOk())
                .andExpect(content().string("150"));
    }

    @Test
    @DisplayName("Debe manejar error al obtener total de solicitudes")
    void testObtenerTotalSolicitudesCambio_Error() throws Exception {
        // Given
        when(reportesService.obtenerTotalSolicitudesCambio())
            .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/api/reportes/total-solicitudes-cambio"))
                .andExpect(status().isInternalServerError());
    }

    // ========== PRUEBAS PARA TASAS DE APROBACIÓN ==========

    @Test
    @DisplayName("Debe obtener tasas de aprobación globales exitosamente")
    void testObtenerTasasAprobacionGlobal_Success() throws Exception {
        // Given
        TasaAprobacionDTO tasas = createTasaAprobacionDTO(100L, 80L, 15L);
        when(reportesService.obtenerTasasAprobacionGlobal()).thenReturn(tasas);

        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSolicitudes").value(100))
                .andExpect(jsonPath("$.solicitudesAprobadas").value(80))
                .andExpect(jsonPath("$.solicitudesRechazadas").value(15));
    }

    @Test
    @DisplayName("Debe manejar error al obtener tasas globales")
    void testObtenerTasasAprobacionGlobal_Error() throws Exception {
        // Given
        when(reportesService.obtenerTasasAprobacionGlobal())
            .thenThrow(new RuntimeException("Error interno"));

        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Debe obtener tasas de aprobación por facultad exitosamente")
    void testObtenerTasasAprobacionPorFacultad_Success() throws Exception {
        // Given
        TasaAprobacionDTO tasas = createTasaAprobacionDTO(50L, 40L, 8L);
        when(reportesService.obtenerTasasAprobacionPorFacultad(Facultad.INGENIERIA_SISTEMAS))
            .thenReturn(tasas);

        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/facultad/INGENIERIA_SISTEMAS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSolicitudes").value(50))
                .andExpect(jsonPath("$.solicitudesAprobadas").value(40));
    }

    @Test
    @DisplayName("Debe retornar error 400 por facultad inválida en tasas")
    void testObtenerTasasAprobacionPorFacultad_FacultadInvalida() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/facultad/FACULTAD_INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe obtener tasas de aprobación por tipo exitosamente")
    void testObtenerTasasAprobacionPorTipo_Success() throws Exception {
        // Given
        TasaAprobacionDTO tasas = createTasaAprobacionDTO(30L, 25L, 5L);
        when(reportesService.obtenerTasasAprobacionPorTipo(TipoSolicitud.CAMBIO_GRUPO))
            .thenReturn(tasas);

        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/tipo/CAMBIO_GRUPO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSolicitudes").value(30))
                .andExpect(jsonPath("$.solicitudesAprobadas").value(25));
    }

    @Test
    @DisplayName("Debe retornar error 400 por tipo de solicitud inválido")
    void testObtenerTasasAprobacionPorTipo_TipoInvalido() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/tipo/TIPO_INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe obtener tasas por facultad y tipo exitosamente")
    void testObtenerTasasAprobacionPorFacultadYTipo_Success() throws Exception {
        // Given
        TasaAprobacionDTO tasas = createTasaAprobacionDTO(20L, 18L, 2L);
        when(reportesService.obtenerTasasAprobacionPorFacultadYTipo(
            Facultad.INGENIERIA_SISTEMAS, TipoSolicitud.INSCRIPCION_GRUPO))
            .thenReturn(tasas);

        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/facultad/INGENIERIA_SISTEMAS/tipo/INSCRIPCION_GRUPO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSolicitudes").value(20))
                .andExpect(jsonPath("$.solicitudesAprobadas").value(18));
    }

    @Test
    @DisplayName("Debe retornar error 400 por facultad y tipo inválidos")
    void testObtenerTasasAprobacionPorFacultadYTipo_ParametrosInvalidos() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/tasas-aprobacion/facultad/INEXISTENTE/tipo/INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    // ========== PRUEBAS PARA TIPOS DE SOLICITUD ==========

    @Test
    @DisplayName("Debe obtener tipos de solicitud exitosamente")
    void testObtenerTiposSolicitud_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/tipos-solicitud"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("INSCRIPCION_GRUPO"))
                .andExpect(jsonPath("$[1]").value("CAMBIO_GRUPO"))
                .andExpect(jsonPath("$[2]").value("CANCELACION_GRUPO"));
    }

    // ========== PRUEBAS PARA INDICADORES DE AVANCE ==========

    @Test
    @DisplayName("Debe obtener indicadores de avance de estudiante exitosamente")
    void testObtenerIndicadoresAvanceEstudiante_Success() throws Exception {
        // Given
        IndicadoresAvanceDTO indicadores = createIndicadoresAvanceDTO("EST001");
        when(reportesService.calcularIndicadoresAvanceEstudiante("EST001"))
            .thenReturn(indicadores);

        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/estudiante/EST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("EST001"));
    }

    @Test
    @DisplayName("Debe retornar 404 cuando estudiante no existe")
    void testObtenerIndicadoresAvanceEstudiante_NotFound() throws Exception {
        // Given
        when(reportesService.calcularIndicadoresAvanceEstudiante("EST999"))
            .thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/estudiante/EST999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debe obtener indicadores globales sin facultad")
    void testObtenerIndicadoresAvanceGlobales_SinFacultad() throws Exception {
        // Given
        IndicadoresAvanceDTO indicadores = createIndicadoresAvanceDTO(null);
        when(reportesService.calcularIndicadoresAvanceGlobales(null))
            .thenReturn(indicadores);

        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/global"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe obtener indicadores globales con facultad específica")
    void testObtenerIndicadoresAvanceGlobales_ConFacultad() throws Exception {
        // Given
        IndicadoresAvanceDTO indicadores = createIndicadoresAvanceDTO(null);
        when(reportesService.calcularIndicadoresAvanceGlobales(Facultad.INGENIERIA_SISTEMAS))
            .thenReturn(indicadores);

        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/global")
                .param("facultad", "INGENIERIA_SISTEMAS"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe retornar error 400 por facultad inválida en indicadores")
    void testObtenerIndicadoresAvanceGlobales_FacultadInvalida() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/global")
                .param("facultad", "FACULTAD_INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe manejar parámetro facultad vacío")
    void testObtenerIndicadoresAvanceGlobales_FacultadVacia() throws Exception {
        // Given
        IndicadoresAvanceDTO indicadores = createIndicadoresAvanceDTO(null);
        when(reportesService.calcularIndicadoresAvanceGlobales(null))
            .thenReturn(indicadores);

        // When & Then
        mockMvc.perform(get("/api/reportes/indicadores-avance/global")
                .param("facultad", ""))
                .andExpect(status().isOk());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private EstadisticasGrupoDTO createEstadisticasGrupoDTO(String grupoId, String materiaId, Long solicitudes) {
        EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO();
        dto.setGrupoId(grupoId);
        dto.setMateriaId(materiaId);
        dto.setMateriaNombre("Matemáticas");
        dto.setMateriaAcronimo("MAT1");
        dto.setFacultad("INGENIERIA_SISTEMAS");
        dto.setCapacidad(30);
        dto.setCantidadInscritos(25);
        dto.setCantidadSolicitudes(solicitudes);
        dto.setPorcentajeOcupacion(83.33);
        return dto;
    }

    private TasaAprobacionDTO createTasaAprobacionDTO(Long total, Long aprobadas, Long rechazadas) {
        TasaAprobacionDTO dto = new TasaAprobacionDTO();
        dto.setTotalSolicitudes(total);
        dto.setSolicitudesAprobadas(aprobadas);
        dto.setSolicitudesRechazadas(rechazadas);
        dto.setSolicitudesPendientes(total - aprobadas - rechazadas);
        dto.setTasaAprobacion((aprobadas.doubleValue() / total) * 100);
        dto.setTasaRechazo((rechazadas.doubleValue() / total) * 100);
        dto.setFechaConsulta(LocalDateTime.now());
        return dto;
    }

    private IndicadoresAvanceDTO createIndicadoresAvanceDTO(String estudianteId) {
        IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
        dto.setEstudianteId(estudianteId);
        dto.setEstudianteNombre("Juan");
        dto.setEstudianteApellido("Pérez");
        dto.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        dto.setCreditosAprobados(45);
        dto.setCreditosTotales(180);
        dto.setPorcentajeAvanceGeneral(25.0);
        dto.setFechaCalculo(LocalDateTime.now());
        return dto;
    }
}