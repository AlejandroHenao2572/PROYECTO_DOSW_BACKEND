package com.sirha.proyecto_sirha_dosw.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sirha.proyecto_sirha_dosw.dto.CapacidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Dia;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.GrupoService;

/**
 * Clase de pruebas unitarias para GrupoController.
 * Prueba todos los endpoints y casos de uso del controlador sin inicializar el contexto de Spring.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GrupoController - Pruebas Unitarias")
class GrupoControllerTest {

    @Mock
    private GrupoService grupoService;

    @InjectMocks
    private GrupoController grupoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(grupoController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ========== PRUEBAS PARA CONSULTAR TODOS LOS GRUPOS ==========

    @Test
    @DisplayName("Debe obtener todos los grupos exitosamente")
    void testGetAllGrupos_Success() throws Exception {
        // Given
        List<Grupo> grupos = Arrays.asList(createMockGrupo("GRP001"), createMockGrupo("GRP002"));
        when(grupoService.getAllGrupos()).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(grupoService).getAllGrupos();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay grupos")
    void testGetAllGrupos_Empty() throws Exception {
        // Given
        when(grupoService.getAllGrupos()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/grupos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(grupoService).getAllGrupos();
    }

    // ========== PRUEBAS PARA CONSULTAR GRUPO POR ID ==========

    @Test
    @DisplayName("Debe obtener grupo por ID exitosamente")
    void testGetGrupoById_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        Grupo grupo = createMockGrupo(grupoId);
        when(grupoService.getGrupoById(grupoId)).thenReturn(Optional.of(grupo));

        // When & Then
        mockMvc.perform(get("/api/grupos/{id}", grupoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).getGrupoById(grupoId);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando grupo no existe")
    void testGetGrupoById_NotFound() throws Exception {
        // Given
        String grupoId = "GRP999";
        when(grupoService.getGrupoById(grupoId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/grupos/{id}", grupoId))
                .andExpect(status().isNotFound());

        verify(grupoService).getGrupoById(grupoId);
    }

    // ========== PRUEBAS PARA CREAR GRUPO ==========

    @Test
    @DisplayName("Debe crear grupo exitosamente")
    void testCreateGrupo_Success() throws Exception {
        // Given
        GrupoDTO grupoDTO = createValidGrupoDTO();
        Grupo grupoCreado = createMockGrupo("GRP001");
        when(grupoService.createGrupo(any(GrupoDTO.class))).thenReturn(grupoCreado);

        // When & Then
        mockMvc.perform(post("/api/grupos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupoDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("GRP001"));

        verify(grupoService).createGrupo(any(GrupoDTO.class));
    }

    @Test
    @DisplayName("Debe manejar error al crear grupo")
    void testCreateGrupo_Error() throws Exception {
        // Given
        GrupoDTO grupoDTO = createValidGrupoDTO();
        when(grupoService.createGrupo(any(GrupoDTO.class)))
            .thenThrow(new SirhaException("Error al crear grupo"));

        // When & Then
        mockMvc.perform(post("/api/grupos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupoDTO)))
                .andExpect(status().isConflict());

        verify(grupoService).createGrupo(any(GrupoDTO.class));
    }

    // ========== PRUEBAS PARA ACTUALIZAR GRUPO ==========

    @Test
    @DisplayName("Debe actualizar grupo exitosamente")
    void testUpdateGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        GrupoDTO grupoDTO = createValidGrupoDTO();
        Grupo grupoActualizado = createMockGrupo(grupoId);
        when(grupoService.updateGrupo(eq(grupoId), any(GrupoDTO.class))).thenReturn(grupoActualizado);

        // When & Then
        mockMvc.perform(put("/api/grupos/{id}", grupoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).updateGrupo(eq(grupoId), any(GrupoDTO.class));
    }

    @Test
    @DisplayName("Debe manejar error al actualizar grupo")
    void testUpdateGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        GrupoDTO grupoDTO = createValidGrupoDTO();
        when(grupoService.updateGrupo(eq(grupoId), any(GrupoDTO.class)))
            .thenThrow(new SirhaException("Grupo no encontrado"));

        // When & Then
        mockMvc.perform(put("/api/grupos/{id}", grupoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grupoDTO)))
                .andExpect(status().isConflict());

        verify(grupoService).updateGrupo(eq(grupoId), any(GrupoDTO.class));
    }

    // ========== PRUEBAS PARA ELIMINAR GRUPO ==========

    @Test
    @DisplayName("Debe eliminar grupo exitosamente")
    void testDeleteGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        doNothing().when(grupoService).deleteGrupo(grupoId);

        // When & Then
        mockMvc.perform(delete("/api/grupos/{id}", grupoId))
                .andExpect(status().isNoContent());

        verify(grupoService).deleteGrupo(grupoId);
    }

    @Test
    @DisplayName("Debe manejar error al eliminar grupo")
    void testDeleteGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        doThrow(new SirhaException("Grupo no encontrado")).when(grupoService).deleteGrupo(grupoId);

        // When & Then
        mockMvc.perform(delete("/api/grupos/{id}", grupoId))
                .andExpect(status().isConflict());

        verify(grupoService).deleteGrupo(grupoId);
    }

    // ========== PRUEBAS PARA CONSULTAR GRUPOS POR MATERIA ==========

    @Test
    @DisplayName("Debe obtener grupos por materia exitosamente")
    void testGetGruposByMateria_Success() throws Exception {
        // Given
        String materiaId = "MAT001";
        List<Grupo> grupos = Arrays.asList(createMockGrupo("GRP001"), createMockGrupo("GRP002"));
        when(grupoService.getGruposByMateria(materiaId)).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos/materia/{materiaId}", materiaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(grupoService).getGruposByMateria(materiaId);
    }

    @Test
    @DisplayName("Debe obtener grupos por profesor exitosamente")
    void testGetGruposByProfesor_Success() throws Exception {
        // Given
        String profesorId = "PROF001";
        List<Grupo> grupos = Arrays.asList(createMockGrupo("GRP001"));
        when(grupoService.getGruposByProfesor(profesorId)).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos/profesor/{profesorId}", profesorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(grupoService).getGruposByProfesor(profesorId);
    }

    // ========== PRUEBAS PARA GRUPOS DISPONIBLES ==========

    @Test
    @DisplayName("Debe obtener grupos disponibles exitosamente")
    void testGetGruposDisponibles_Success() throws Exception {
        // Given
        List<Grupo> grupos = Arrays.asList(createMockGrupo("GRP001"));
        when(grupoService.getGruposDisponibles()).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos/disponibles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(grupoService).getGruposDisponibles();
    }

    @Test
    @DisplayName("Debe obtener grupos disponibles por materia exitosamente")
    void testGetGruposDisponiblesPorMateria_Success() throws Exception {
        // Given
        String materiaId = "MAT001";
        List<Grupo> grupos = Arrays.asList(createMockGrupo("GRP001"));
        when(grupoService.getGruposDisponiblesPorMateria(materiaId)).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos/materia/{materiaId}/disponibles", materiaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(grupoService).getGruposDisponiblesPorMateria(materiaId);
    }

    // ========== PRUEBAS PARA GESTIÓN DE ESTUDIANTES ==========

    @Test
    @DisplayName("Debe agregar estudiante a grupo exitosamente")
    void testAddEstudianteToGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        String estudianteId = "EST001";
        Grupo grupoActualizado = createMockGrupo(grupoId);
        when(grupoService.addEstudianteToGrupo(grupoId, estudianteId)).thenReturn(grupoActualizado);

        // When & Then
        mockMvc.perform(post("/api/grupos/{grupoId}/estudiantes/{estudianteId}", grupoId, estudianteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).addEstudianteToGrupo(grupoId, estudianteId);
    }

    @Test
    @DisplayName("Debe manejar error al agregar estudiante a grupo")
    void testAddEstudianteToGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        String estudianteId = "EST001";
        when(grupoService.addEstudianteToGrupo(grupoId, estudianteId))
            .thenThrow(new SirhaException("Grupo lleno"));

        // When & Then
        mockMvc.perform(post("/api/grupos/{grupoId}/estudiantes/{estudianteId}", grupoId, estudianteId))
                .andExpect(status().isConflict());

        verify(grupoService).addEstudianteToGrupo(grupoId, estudianteId);
    }

    @Test
    @DisplayName("Debe remover estudiante de grupo exitosamente")
    void testRemoveEstudianteFromGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        String estudianteId = "EST001";
        Grupo grupoActualizado = createMockGrupo(grupoId);
        when(grupoService.removeEstudianteFromGrupo(grupoId, estudianteId)).thenReturn(grupoActualizado);

        // When & Then
        mockMvc.perform(delete("/api/grupos/{grupoId}/estudiantes/{estudianteId}", grupoId, estudianteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).removeEstudianteFromGrupo(grupoId, estudianteId);
    }

    @Test
    @DisplayName("Debe manejar error al remover estudiante de grupo")
    void testRemoveEstudianteFromGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        String estudianteId = "EST001";
        when(grupoService.removeEstudianteFromGrupo(grupoId, estudianteId))
            .thenThrow(new SirhaException("Estudiante no encontrado en el grupo"));

        // When & Then
        mockMvc.perform(delete("/api/grupos/{grupoId}/estudiantes/{estudianteId}", grupoId, estudianteId))
                .andExpect(status().isConflict());

        verify(grupoService).removeEstudianteFromGrupo(grupoId, estudianteId);
    }

    // ========== PRUEBAS PARA CAPACIDAD DE GRUPOS ==========

    @Test
    @DisplayName("Debe consultar capacidad de grupo exitosamente")
    void testConsultarCapacidadGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        CapacidadGrupoDTO capacidad = createMockCapacidadGrupoDTO(grupoId);
        when(grupoService.obtenerCapacidadGrupo(grupoId)).thenReturn(capacidad);

        // When & Then
        mockMvc.perform(get("/api/grupos/{id}/capacidad", grupoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.grupoId").value(grupoId));

        verify(grupoService).obtenerCapacidadGrupo(grupoId);
    }

    @Test
    @DisplayName("Debe manejar error al consultar capacidad de grupo")
    void testConsultarCapacidadGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        when(grupoService.obtenerCapacidadGrupo(grupoId))
            .thenThrow(new SirhaException("Grupo no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/grupos/{id}/capacidad", grupoId))
                .andExpect(status().isNotFound());

        verify(grupoService).obtenerCapacidadGrupo(grupoId);
    }

    @Test
    @DisplayName("Debe consultar capacidad de todos los grupos exitosamente")
    void testConsultarCapacidadTodosLosGrupos_Success() throws Exception {
        // Given
        List<CapacidadGrupoDTO> capacidades = Arrays.asList(
            createMockCapacidadGrupoDTO("GRP001"),
            createMockCapacidadGrupoDTO("GRP002")
        );
        when(grupoService.obtenerCapacidadTodosLosGrupos()).thenReturn(capacidades);

        // When & Then
        mockMvc.perform(get("/api/grupos/capacidad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(grupoService).obtenerCapacidadTodosLosGrupos();
    }

    @Test
    @DisplayName("Debe consultar capacidad de grupos por materia exitosamente")
    void testConsultarCapacidadGruposPorMateria_Success() throws Exception {
        // Given
        String materiaId = "MAT001";
        List<CapacidadGrupoDTO> capacidades = Arrays.asList(createMockCapacidadGrupoDTO("GRP001"));
        when(grupoService.obtenerCapacidadGruposPorMateria(materiaId)).thenReturn(capacidades);

        // When & Then
        mockMvc.perform(get("/api/grupos/materia/{materiaId}/capacidad", materiaId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(grupoService).obtenerCapacidadGruposPorMateria(materiaId);
    }

    // ========== PRUEBAS PARA GESTIÓN DE PROFESORES ==========

    @Test
    @DisplayName("Debe asignar profesor a grupo exitosamente")
    void testAsignarProfesorAGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        String profesorId = "PROF001";
        Grupo grupoActualizado = createMockGrupo(grupoId);
        when(grupoService.asignarProfesorAGrupo(grupoId, profesorId)).thenReturn(grupoActualizado);

        // When & Then
        mockMvc.perform(put("/api/grupos/{grupoId}/profesor/{profesorId}", grupoId, profesorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).asignarProfesorAGrupo(grupoId, profesorId);
    }

    @Test
    @DisplayName("Debe manejar error al asignar profesor a grupo")
    void testAsignarProfesorAGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        String profesorId = "PROF001";
        when(grupoService.asignarProfesorAGrupo(grupoId, profesorId))
            .thenThrow(new SirhaException("Profesor no encontrado"));

        // When & Then
        mockMvc.perform(put("/api/grupos/{grupoId}/profesor/{profesorId}", grupoId, profesorId))
                .andExpect(status().isBadRequest());

        verify(grupoService).asignarProfesorAGrupo(grupoId, profesorId);
    }

    @Test
    @DisplayName("Debe remover profesor de grupo exitosamente")
    void testRemoverProfesorDeGrupo_Success() throws Exception {
        // Given
        String grupoId = "GRP001";
        Grupo grupoActualizado = createMockGrupo(grupoId);
        when(grupoService.removerProfesorDeGrupo(grupoId)).thenReturn(grupoActualizado);

        // When & Then
        mockMvc.perform(delete("/api/grupos/{grupoId}/profesor", grupoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(grupoId));

        verify(grupoService).removerProfesorDeGrupo(grupoId);
    }

    @Test
    @DisplayName("Debe manejar error al remover profesor de grupo")
    void testRemoverProfesorDeGrupo_Error() throws Exception {
        // Given
        String grupoId = "GRP001";
        when(grupoService.removerProfesorDeGrupo(grupoId))
            .thenThrow(new SirhaException("Grupo no encontrado"));

        // When & Then
        mockMvc.perform(delete("/api/grupos/{grupoId}/profesor", grupoId))
                .andExpect(status().isNotFound());

        verify(grupoService).removerProfesorDeGrupo(grupoId);
    }

    @Test
    @DisplayName("Debe consultar grupos con capacidad por profesor exitosamente")
    void testConsultarGruposConCapacidadPorProfesor_Success() throws Exception {
        // Given
        String profesorId = "PROF001";
        List<CapacidadGrupoDTO> grupos = Arrays.asList(createMockCapacidadGrupoDTO("GRP001"));
        when(grupoService.obtenerGruposConCapacidadPorProfesor(profesorId)).thenReturn(grupos);

        // When & Then
        mockMvc.perform(get("/api/grupos/profesor/{profesorId}/capacidad", profesorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(grupoService).obtenerGruposConCapacidadPorProfesor(profesorId);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Grupo createMockGrupo(String id) {
        Grupo grupo = new Grupo();
        grupo.setId(id);
        grupo.setCapacidad(30);
        grupo.setCantidadInscritos(15);
        grupo.setEstaCompleto(false);
        
        // Crear materia mock
        Materia materia = new Materia();
        materia.setId("MAT001");
        materia.setNombre("Cálculo Diferencial");
        materia.setAcronimo("CALC1");
        grupo.setMateria(materia);
        
        // Crear horarios mock
        List<Horario> horarios = Arrays.asList(createHorario(Dia.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0)));
        grupo.setHorarios(horarios);
        
        return grupo;
    }

    private GrupoDTO createValidGrupoDTO() {
        GrupoDTO dto = new GrupoDTO();
        dto.setCapacidad(30);
        dto.setCantidadInscritos(0);
        dto.setEstaCompleto(false);
        dto.setMateriaId("MAT001");
        dto.setHorarios(Arrays.asList(createHorario(Dia.LUNES, LocalTime.of(8, 0), LocalTime.of(10, 0))));
        dto.setEstudiantesId(new ArrayList<>());
        return dto;
    }

    private CapacidadGrupoDTO createMockCapacidadGrupoDTO(String grupoId) {
        CapacidadGrupoDTO dto = new CapacidadGrupoDTO();
        dto.setGrupoId(grupoId);
        dto.setMateriaId("MAT001");
        dto.setMateriaNombre("Cálculo Diferencial");
        dto.setMateriaAcronimo("CALC1");
        dto.setCapacidadMaxima(30);
        dto.setEstudiantesInscritos(15);
        dto.setPorcentajeOcupacion(50.0);
        dto.setCuposDisponibles(15);
        dto.setEstaCompleto(false);
        return dto;
    }

    private Horario createHorario(Dia dia, LocalTime horaInicio, LocalTime horaFin) {
        Horario horario = new Horario();
        horario.setDia(dia);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFin(horaFin);
        return horario;
    }
}