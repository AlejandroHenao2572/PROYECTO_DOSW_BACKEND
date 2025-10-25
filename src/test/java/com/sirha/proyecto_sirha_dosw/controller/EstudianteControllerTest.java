package com.sirha.proyecto_sirha_dosw.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Dia;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;
import com.sirha.proyecto_sirha_dosw.model.PlazoSolicitudes;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.service.EstudianteService;

/**
 * Clase de pruebas unitarias para EstudianteController.
 * Prueba todos los endpoints y casos de uso del controlador sin inicializar el contexto de Spring.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EstudianteController - Pruebas Unitarias")
class EstudianteControllerTest {

    @Mock
    private EstudianteService estudianteService;

    @InjectMocks
    private EstudianteController estudianteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estudianteController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ========== PRUEBAS PARA HORARIOS ==========

    @Test
    @DisplayName("Debe consultar horario por semestre exitosamente")
    void testConsultarHorarioPorSemestre_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        int semestre = 5;
        
        List<RegistroMaterias> registroMaterias = createMockRegistroMaterias();
        when(estudianteService.consultarHorarioBySemester(idEstudiante, semestre))
            .thenReturn(registroMaterias);

        // When & Then
        mockMvc.perform(get("/api/estudiante/horario/{idEstudiante}/{semestre}", idEstudiante, semestre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['Cálculo Diferencial']").exists())
                .andExpect(jsonPath("$.['Programación I']").exists());

        verify(estudianteService).consultarHorarioBySemester(idEstudiante, semestre);
    }

    @Test
    @DisplayName("Debe retornar NOT_FOUND cuando no hay horarios")
    void testConsultarHorarioPorSemestre_NotFound() throws Exception {
        // Given
        String idEstudiante = "EST001";
        int semestre = 5;
        
        when(estudianteService.consultarHorarioBySemester(idEstudiante, semestre))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/estudiante/horario/{idEstudiante}/{semestre}", idEstudiante, semestre))
                .andExpect(status().isNotFound());

        verify(estudianteService).consultarHorarioBySemester(idEstudiante, semestre);
    }

    @Test
    @DisplayName("Debe manejar excepción en consulta de horario")
    void testConsultarHorarioPorSemestre_Exception() throws Exception {
        // Given
        String idEstudiante = "EST001";
        int semestre = 5;
        
        when(estudianteService.consultarHorarioBySemester(idEstudiante, semestre))
            .thenThrow(new SirhaException("Error al consultar horario"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/horario/{idEstudiante}/{semestre}", idEstudiante, semestre))
                .andExpect(status().isNotFound());

        verify(estudianteService).consultarHorarioBySemester(idEstudiante, semestre);
    }

    // ========== PRUEBAS PARA SEMÁFORO ACADÉMICO ==========

    @Test
    @DisplayName("Debe consultar semáforo académico exitosamente")
    void testConsultarSemaforoAcademico_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        Map<String, Semaforo> semaforoMap = new HashMap<>();
        semaforoMap.put("estado", Semaforo.VERDE);
        
        when(estudianteService.consultarSemaforoAcademico(idEstudiante))
            .thenReturn(semaforoMap);

        // When & Then
        mockMvc.perform(get("/api/estudiante/semaforo/{idEstudiante}", idEstudiante))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(estudianteService, times(1)).consultarSemaforoAcademico(idEstudiante);
    }

    @Test
    @DisplayName("Debe retornar NO_CONTENT cuando semáforo está vacío")
    void testConsultarSemaforoAcademico_NoContent() throws Exception {
        // Given
        String idEstudiante = "EST001";
        
        when(estudianteService.consultarSemaforoAcademico(idEstudiante))
            .thenReturn(Collections.emptyMap());

        // When & Then
        mockMvc.perform(get("/api/estudiante/semaforo/{idEstudiante}", idEstudiante))
                .andExpect(status().isNoContent());

        verify(estudianteService).consultarSemaforoAcademico(idEstudiante);
    }

    @Test
    @DisplayName("Debe manejar excepción en consulta de semáforo")
    void testConsultarSemaforoAcademico_Exception() throws Exception {
        // Given
        String idEstudiante = "EST001";
        
        when(estudianteService.consultarSemaforoAcademico(idEstudiante))
            .thenThrow(new SirhaException("Error al consultar semáforo"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/semaforo/{idEstudiante}", idEstudiante))
                .andExpect(status().isNotFound());

        verify(estudianteService).consultarSemaforoAcademico(idEstudiante);
    }

    // ========== PRUEBAS PARA CREACIÓN DE SOLICITUDES ==========

    @Test
    @DisplayName("Debe crear solicitud exitosamente")
    void testCrearSolicitud_Success() throws Exception {
        // Given
        SolicitudDTO solicitudDTO = createValidSolicitudDTO();
        Solicitud solicitudCreada = createMockSolicitud();
        
        // Mock PlazoSolicitudes to be in valid range
        mockValidTimeRange();
        
        when(estudianteService.crearSolicitud(any(SolicitudDTO.class)))
            .thenReturn(solicitudCreada);

        // When & Then
        mockMvc.perform(post("/api/estudiante/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(estudianteService).crearSolicitud(any(SolicitudDTO.class));
    }

    @Test
    @DisplayName("Debe rechazar solicitud fuera de plazo")
    void testCrearSolicitud_OutOfTimeRange() throws Exception {
        // Given
        SolicitudDTO solicitudDTO = createValidSolicitudDTO();
        solicitudDTO.setFechaSolicitud(LocalDate.now().minusDays(100)); // Fecha muy antigua

        // Configurar un plazo que NO incluya la fecha de la solicitud
        PlazoSolicitudes.INSTANCIA.setFechaInicio(LocalDate.now().minusDays(10));
        PlazoSolicitudes.INSTANCIA.setFechaFin(LocalDate.now().minusDays(1));
        
        // When & Then
        mockMvc.perform(post("/api/estudiante/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isBadRequest());

        verify(estudianteService, never()).crearSolicitud(any(SolicitudDTO.class));
    }

    @Test
    @DisplayName("Debe manejar excepción en creación de solicitud")
    void testCrearSolicitud_Exception() throws Exception {
        // Given
        SolicitudDTO solicitudDTO = createValidSolicitudDTO();
        
        mockValidTimeRange();
        
        when(estudianteService.crearSolicitud(any(SolicitudDTO.class)))
            .thenThrow(new SirhaException("Error al crear solicitud"));

        // When & Then
        mockMvc.perform(post("/api/estudiante/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isInternalServerError());

        verify(estudianteService).crearSolicitud(any(SolicitudDTO.class));
    }

    // ========== PRUEBAS PARA CONSULTA DE SOLICITUDES ==========

    @Test
    @DisplayName("Debe consultar solicitudes de estudiante exitosamente")
    void testConsultarSolicitudes_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        List<Solicitud> solicitudes = Arrays.asList(createMockSolicitud());
        
        when(estudianteService.consultarSolicitudes(idEstudiante))
            .thenReturn(solicitudes);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}", idEstudiante))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(estudianteService).consultarSolicitudes(idEstudiante);
    }

    @Test
    @DisplayName("Debe retornar NO_CONTENT cuando no hay solicitudes")
    void testConsultarSolicitudes_NoContent() throws Exception {
        // Given
        String idEstudiante = "EST001";
        
        when(estudianteService.consultarSolicitudes(idEstudiante))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}", idEstudiante))
                .andExpect(status().isNoContent());

        verify(estudianteService).consultarSolicitudes(idEstudiante);
    }

    @Test
    @DisplayName("Debe consultar solicitud por ID exitosamente")
    void testConsultarSolicitudesPorId_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String solicitudId = "SOL001";
        Solicitud solicitud = createMockSolicitud();
        
        when(estudianteService.consultarSolicitudesById(idEstudiante, solicitudId))
            .thenReturn(solicitud);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/{solicitudId}", 
                idEstudiante, solicitudId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(estudianteService).consultarSolicitudesById(idEstudiante, solicitudId);
    }

    // ========== PRUEBAS PARA CANCELAR MATERIA ==========

    @Test
    @DisplayName("Debe cancelar materia exitosamente")
    void testCancelarMateria_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String acronimoMateria = "CALC1";
        String resultado = "Materia cancelada exitosamente";
        
        when(estudianteService.cancelarMateria(idEstudiante, acronimoMateria))
            .thenReturn(resultado);

        // When & Then
        mockMvc.perform(put("/api/estudiante/materias/{idEstudiante}/{acronimoMateria}/cancelar", 
                idEstudiante, acronimoMateria))
                .andExpect(status().isOk());

        verify(estudianteService).cancelarMateria(idEstudiante, acronimoMateria);
    }

    @Test
    @DisplayName("Debe manejar error al cancelar materia")
    void testCancelarMateria_Error() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String acronimoMateria = "CALC1";
        
        when(estudianteService.cancelarMateria(idEstudiante, acronimoMateria))
            .thenThrow(new SirhaException("No se puede cancelar la materia"));

        // When & Then
        mockMvc.perform(put("/api/estudiante/materias/{idEstudiante}/{acronimoMateria}/cancelar", 
                idEstudiante, acronimoMateria))
                .andExpect(status().isBadRequest());

        verify(estudianteService).cancelarMateria(idEstudiante, acronimoMateria);
    }

    // ========== PRUEBAS PARA CONSULTAS POR ESTADO ==========

    @Test
    @DisplayName("Debe consultar solicitudes por estado exitosamente")
    void testConsultarSolicitudesPorEstado_Success() throws Exception {
        // Given
        String estado = "PENDIENTE";
        List<Solicitud> solicitudes = Arrays.asList(createMockSolicitud());
        
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE))
            .thenReturn(solicitudes);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/{estado}", estado))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.solicitudes").exists())
                .andExpect(jsonPath("$.estado").value(estado))
                .andExpect(jsonPath("$.total").value(1));

        verify(estudianteService).consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE);
    }

    @Test
    @DisplayName("Debe manejar estado inválido")
    void testConsultarSolicitudesPorEstado_InvalidState() throws Exception {
        // Given
        String estadoInvalido = "ESTADO_INEXISTENTE";

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/{estado}", estadoInvalido))
                .andExpect(status().isBadRequest());

        verify(estudianteService, never()).consultarSolicitudesPorEstado(any());
    }

    @Test
    @DisplayName("Debe retornar mensaje cuando no hay solicitudes por estado")
    void testConsultarSolicitudesPorEstado_Empty() throws Exception {
        // Given
        String estado = "APROBADA";
        
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.APROBADA))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/{estado}", estado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.total").value(0));

        verify(estudianteService).consultarSolicitudesPorEstado(SolicitudEstado.APROBADA);
    }

    @Test
    @DisplayName("Debe consultar todas las solicitudes exitosamente")
    void testConsultarTodasLasSolicitudes_Success() throws Exception {
        // Given
        List<Solicitud> solicitudes = Arrays.asList(createMockSolicitud(), createMockSolicitud());
        
        when(estudianteService.consultarTodasLasSolicitudes())
            .thenReturn(solicitudes);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/todas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solicitudes").exists())
                .andExpect(jsonPath("$.total").value(2));

        verify(estudianteService).consultarTodasLasSolicitudes();
    }

    @Test
    @DisplayName("Debe consultar solicitudes de estudiante por estado")
    void testConsultarSolicitudesEstudiantePorEstado_Success() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String estado = "PENDIENTE";
        List<Solicitud> solicitudes = Arrays.asList(createMockSolicitud());
        
        when(estudianteService.consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.PENDIENTE))
            .thenReturn(solicitudes);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/estado/{estado}", 
                idEstudiante, estado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solicitudes").exists())
                .andExpect(jsonPath("$.idEstudiante").value(idEstudiante))
                .andExpect(jsonPath("$.estado").value(estado))
                .andExpect(jsonPath("$.total").value(1));

        verify(estudianteService).consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.PENDIENTE);
    }

    @Test
    @DisplayName("Debe retornar mensaje cuando estudiante no tiene solicitudes en estado específico")
    void testConsultarSolicitudesEstudiantePorEstado_Empty() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String estado = "APROBADA";
        
        when(estudianteService.consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.APROBADA))
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/estado/{estado}", 
                idEstudiante, estado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.idEstudiante").value(idEstudiante))
                .andExpect(jsonPath("$.total").value(0));

        verify(estudianteService).consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.APROBADA);
    }

    @Test
    @DisplayName("Debe manejar estado inválido en consulta de estudiante por estado")
    void testConsultarSolicitudesEstudiantePorEstado_InvalidState() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String estadoInvalido = "ESTADO_INVALIDO";

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/estado/{estado}", 
                idEstudiante, estadoInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(estudianteService, never()).consultarSolicitudesEstudiantePorEstado(any(), any());
    }

    @Test
    @DisplayName("Debe manejar excepción en consulta de estudiante por estado")
    void testConsultarSolicitudesEstudiantePorEstado_Exception() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String estado = "PENDIENTE";
        
        when(estudianteService.consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.PENDIENTE))
            .thenThrow(new SirhaException("Estudiante no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/estado/{estado}", 
                idEstudiante, estado))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());

        verify(estudianteService).consultarSolicitudesEstudiantePorEstado(idEstudiante, SolicitudEstado.PENDIENTE);
    }

    @Test
    @DisplayName("Debe consultar solicitud por ID cuando no se encuentra")
    void testConsultarSolicitudesPorId_NotFound() throws Exception {
        // Given
        String idEstudiante = "EST001";
        String solicitudId = "SOL999";
        
        when(estudianteService.consultarSolicitudesById(idEstudiante, solicitudId))
            .thenThrow(new SirhaException("Solicitud no encontrada"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}/{solicitudId}", 
                idEstudiante, solicitudId))
                .andExpect(status().isNotFound());

        verify(estudianteService).consultarSolicitudesById(idEstudiante, solicitudId);
    }

    @Test
    @DisplayName("Debe manejar excepción en consulta de solicitudes por estudiante")
    void testConsultarSolicitudes_Exception() throws Exception {
        // Given
        String idEstudiante = "EST001";
        
        when(estudianteService.consultarSolicitudes(idEstudiante))
            .thenThrow(new SirhaException("Error al consultar solicitudes"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/{idEstudiante}", idEstudiante))
                .andExpect(status().isNotFound());

        verify(estudianteService).consultarSolicitudes(idEstudiante);
    }

    @Test
    @DisplayName("Debe retornar mensaje cuando no hay solicitudes en el sistema")
    void testConsultarTodasLasSolicitudes_Empty() throws Exception {
        // Given
        when(estudianteService.consultarTodasLasSolicitudes())
            .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/todas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.total").value(0));

        verify(estudianteService).consultarTodasLasSolicitudes();
    }

    @Test
    @DisplayName("Debe manejar excepción genérica en consulta de todas las solicitudes")
    void testConsultarTodasLasSolicitudes_Exception() throws Exception {
        // Given
        when(estudianteService.consultarTodasLasSolicitudes())
            .thenThrow(new RuntimeException("Error de conexión"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/todas"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());

        verify(estudianteService).consultarTodasLasSolicitudes();
    }

    @Test
    @DisplayName("Debe manejar excepción en consulta de solicitudes por estado")
    void testConsultarSolicitudesPorEstado_Exception() throws Exception {
        // Given
        String estado = "PENDIENTE";
        
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE))
            .thenThrow(new SirhaException("Error al consultar"));

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/{estado}", estado))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(estudianteService).consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE);
    }

    @Test
    @DisplayName("Debe crear solicitud con fecha actual cuando no se proporciona fecha")
    void testCrearSolicitud_WithoutFechaSolicitud() throws Exception {
        // Given
        SolicitudDTO solicitudDTO = createValidSolicitudDTO();
        solicitudDTO.setFechaSolicitud(null); // Sin fecha proporcionada
        Solicitud solicitudCreada = createMockSolicitud();
        
        mockValidTimeRange();
        
        when(estudianteService.crearSolicitud(any(SolicitudDTO.class)))
            .thenReturn(solicitudCreada);

        // When & Then
        mockMvc.perform(post("/api/estudiante/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(estudianteService).crearSolicitud(any(SolicitudDTO.class));
    }

    @Test
    @DisplayName("Debe validar múltiples horarios por materia")
    void testConsultarHorarioPorSemestre_MultipleHorarios() throws Exception {
        // Given
        String idEstudiante = "EST001";
        int semestre = 5;
        
        List<RegistroMaterias> registroMaterias = createMockRegistroMateriasWithMultipleSchedules();
        when(estudianteService.consultarHorarioBySemester(idEstudiante, semestre))
            .thenReturn(registroMaterias);

        // When & Then
        mockMvc.perform(get("/api/estudiante/horario/{idEstudiante}/{semestre}", idEstudiante, semestre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['Cálculo Diferencial']").exists());

        verify(estudianteService).consultarHorarioBySemester(idEstudiante, semestre);
    }

    @Test
    @DisplayName("Debe probar todos los estados válidos de SolicitudEstado")
    void testConsultarSolicitudesPorEstado_AllValidStates() throws Exception {
        // Test PENDIENTE
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE))
            .thenReturn(Arrays.asList(createMockSolicitud()));
        
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/PENDIENTE"))
                .andExpect(status().isOk());

        // Test EN_REVISION
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.EN_REVISION))
            .thenReturn(Arrays.asList(createMockSolicitud()));
        
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/EN_REVISION"))
                .andExpect(status().isOk());

        // Test APROBADA
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.APROBADA))
            .thenReturn(Arrays.asList(createMockSolicitud()));
        
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/APROBADA"))
                .andExpect(status().isOk());

        // Test RECHAZADA
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.RECHAZADA))
            .thenReturn(Arrays.asList(createMockSolicitud()));
        
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/RECHAZADA"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe manejar estado en minúsculas correctamente")
    void testConsultarSolicitudesPorEstado_LowerCase() throws Exception {
        // Given
        String estado = "pendiente";
        List<Solicitud> solicitudes = Arrays.asList(createMockSolicitud());
        
        when(estudianteService.consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE))
            .thenReturn(solicitudes);

        // When & Then
        mockMvc.perform(get("/api/estudiante/solicitudes/estado/{estado}", estado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solicitudes").exists());

        verify(estudianteService).consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE);
    }

    @Test
    @DisplayName("Debe crear solicitud con fecha proporcionada dentro del plazo")
    void testCrearSolicitud_WithProvidedFechaInRange() throws Exception {
        // Given
        SolicitudDTO solicitudDTO = createValidSolicitudDTO();
        LocalDate fechaEnPlazo = LocalDate.now();
        solicitudDTO.setFechaSolicitud(fechaEnPlazo);
        Solicitud solicitudCreada = createMockSolicitud();
        
        PlazoSolicitudes.INSTANCIA.setFechaInicio(LocalDate.now().minusDays(10));
        PlazoSolicitudes.INSTANCIA.setFechaFin(LocalDate.now().plusDays(10));
        
        when(estudianteService.crearSolicitud(any(SolicitudDTO.class)))
            .thenReturn(solicitudCreada);

        // When & Then
        mockMvc.perform(post("/api/estudiante/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isCreated());

        verify(estudianteService).crearSolicitud(any(SolicitudDTO.class));
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<RegistroMaterias> createMockRegistroMaterias() {
        List<RegistroMaterias> registros = new ArrayList<>();
        
        // Crear registro 1
        RegistroMaterias registro1 = new RegistroMaterias();
        Grupo grupo1 = new Grupo();
        Materia materia1 = new Materia();
        materia1.setNombre("Cálculo Diferencial");
        grupo1.setMateria(materia1);
        
        List<Horario> horarios1 = Arrays.asList(
            createHorario(Dia.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0))
        );
        grupo1.setHorarios(horarios1);
        registro1.setGrupo(grupo1);
        registros.add(registro1);
        
        // Crear registro 2
        RegistroMaterias registro2 = new RegistroMaterias();
        Grupo grupo2 = new Grupo();
        Materia materia2 = new Materia();
        materia2.setNombre("Programación I");
        grupo2.setMateria(materia2);
        
        List<Horario> horarios2 = Arrays.asList(
            createHorario(Dia.MARTES, LocalTime.of(10, 0), LocalTime.of(12, 0))
        );
        grupo2.setHorarios(horarios2);
        registro2.setGrupo(grupo2);
        registros.add(registro2);
        
        return registros;
    }

    private List<RegistroMaterias> createMockRegistroMateriasWithMultipleSchedules() {
        List<RegistroMaterias> registros = new ArrayList<>();
        
        // Crear registro con múltiples horarios
        RegistroMaterias registro = new RegistroMaterias();
        Grupo grupo = new Grupo();
        Materia materia = new Materia();
        materia.setNombre("Cálculo Diferencial");
        grupo.setMateria(materia);
        
        List<Horario> horarios = Arrays.asList(
            createHorario(Dia.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)),
            createHorario(Dia.MIERCOLES, LocalTime.of(8, 0), LocalTime.of(10, 0)),
            createHorario(Dia.VIERNES, LocalTime.of(8, 0), LocalTime.of(10, 0))
        );
        grupo.setHorarios(horarios);
        registro.setGrupo(grupo);
        registros.add(registro);
        
        return registros;
    }

    private Horario createHorario(Dia dia, LocalTime horaInicio, LocalTime horaFin) {
        Horario horario = new Horario();
        horario.setDia(dia);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFin(horaFin);
        return horario;
    }

    private SolicitudDTO createValidSolicitudDTO() {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setEstudianteId("EST001");
        dto.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        dto.setGrupoProblemaId("GRP001");
        dto.setMateriaProblemaAcronimo("CALC1");
        dto.setGrupoDestinoId("GRP002");
        dto.setMateriaDestinoAcronimo("CALC1");
        dto.setObservaciones("Solicitud de cambio por conflicto de horario");
        return dto;
    }

    private Solicitud createMockSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setId("SOL001");
        solicitud.setEstudianteId("EST001");
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitud.setEstado(SolicitudEstado.PENDIENTE);
        solicitud.setObservaciones("Solicitud de prueba");
        return solicitud;
    }

    private void mockValidTimeRange() {
        // Asegurar que la fecha actual esté dentro del plazo permitido
        PlazoSolicitudes.INSTANCIA.setFechaInicio(LocalDate.now().minusDays(365));
        PlazoSolicitudes.INSTANCIA.setFechaFin(LocalDate.now().plusDays(365));
    }
}
