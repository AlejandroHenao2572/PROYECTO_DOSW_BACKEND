package com.sirha.proyecto_sirha_dosw.controller;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sirha.proyecto_sirha_dosw.dto.CarreraDTO;
import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.CarreraService;

/**
 * Clase de pruebas unitarias para CarreraController.
 * Prueba todos los endpoints y casos de uso del controlador sin inicializar el contexto de Spring.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarreraController - Pruebas Unitarias")
class CarreraControllerTest {

    @Mock
    private CarreraService carreraService;

    @InjectMocks
    private CarreraController carreraController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carreraController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // ========== PRUEBAS PARA REGISTRAR CARRERA ==========

    @Test
    @DisplayName("Debe registrar carrera exitosamente")
    void testRegister_Success() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        Carrera carrera = new Carrera();
        carrera.setCodigo("CS001");
        carrera.setNombre(Facultad.INGENIERIA_SISTEMAS);
        when(carreraService.registrar(any(CarreraDTO.class))).thenReturn(carrera);

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Carrera registrada exitosamente"));

        verify(carreraService).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe manejar error al registrar carrera duplicada")
    void testRegister_DuplicateCarrera() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        doThrow(new SirhaException("Ya existe una carrera con ese código"))
            .when(carreraService).registrar(any(CarreraDTO.class));

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Ya existe una carrera con ese código")));

        verify(carreraService).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe rechazar carrera con datos inválidos - nombre vacío")
    void testRegister_InvalidData_EmptyName() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        carreraDTO.setNombre(""); // Nombre vacío

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe rechazar carrera con datos inválidos - código vacío")
    void testRegister_InvalidData_EmptyCode() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        carreraDTO.setCodigo(""); // Código vacío

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe rechazar carrera con duración inválida")
    void testRegister_InvalidData_InvalidDuration() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        carreraDTO.setDuracionSemestres(0); // Duración inválida

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe rechazar carrera con créditos inválidos")
    void testRegister_InvalidData_InvalidCredits() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        carreraDTO.setCreditosTotales(0); // Créditos inválidos

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).registrar(any(CarreraDTO.class));
    }

    @Test
    @DisplayName("Debe manejar error general al registrar carrera")
    void testRegister_GeneralError() throws Exception {
        // Given
        CarreraDTO carreraDTO = createValidCarreraDTO();
        doThrow(new SirhaException("Error interno del sistema"))
            .when(carreraService).registrar(any(CarreraDTO.class));

        // When & Then
        mockMvc.perform(post("/api/carreras/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carreraDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Error interno del sistema")));

        verify(carreraService).registrar(any(CarreraDTO.class));
    }

    // ========== PRUEBAS PARA AGREGAR NUEVA MATERIA ==========

    @Test
    @DisplayName("Debe agregar nueva materia a carrera exitosamente")
    void testAddNewMateria_Success() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        Materia materiaCreada = createMockMateria();
        
        when(carreraService.addMateria(any(MateriaDTO.class), eq(codigoCarrera)))
            .thenReturn(materiaCreada);

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Cálculo Diferencial"))
                .andExpect(jsonPath("$.acronimo").value("CALC1"));

        verify(carreraService).addMateria(any(MateriaDTO.class), eq(codigoCarrera));
    }

    @Test
    @DisplayName("Debe manejar error al agregar nueva materia - carrera no encontrada")
    void testAddNewMateria_CarreraNotFound() throws Exception {
        // Given
        String codigoCarrera = "ING_INEXISTENTE";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        
        when(carreraService.addMateria(any(MateriaDTO.class), eq(codigoCarrera)))
            .thenThrow(new SirhaException("Carrera no encontrada"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isConflict());

        verify(carreraService).addMateria(any(MateriaDTO.class), eq(codigoCarrera));
    }

    @Test
    @DisplayName("Debe manejar error al agregar nueva materia - materia duplicada")
    void testAddNewMateria_DuplicateMateria() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        
        when(carreraService.addMateria(any(MateriaDTO.class), eq(codigoCarrera)))
            .thenThrow(new SirhaException("La materia ya existe en la carrera"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isConflict());

        verify(carreraService).addMateria(any(MateriaDTO.class), eq(codigoCarrera));
    }

    @Test
    @DisplayName("Debe rechazar nueva materia con datos inválidos - nombre vacío")
    void testAddNewMateria_InvalidData_EmptyName() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        materiaDTO.setNombre(""); // Nombre vacío

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).addMateria(any(MateriaDTO.class), anyString());
    }

    @Test
    @DisplayName("Debe rechazar nueva materia con datos inválidos - acrónimo vacío")
    void testAddNewMateria_InvalidData_EmptyAcronym() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        materiaDTO.setAcronimo(""); // Acrónimo vacío

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).addMateria(any(MateriaDTO.class), anyString());
    }

    @Test
    @DisplayName("Debe rechazar nueva materia con acrónimo demasiado corto")
    void testAddNewMateria_InvalidData_ShortAcronym() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        materiaDTO.setAcronimo("ABC"); // Acrónimo muy corto

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).addMateria(any(MateriaDTO.class), anyString());
    }

    @Test
    @DisplayName("Debe rechazar nueva materia con créditos inválidos")
    void testAddNewMateria_InvalidData_InvalidCredits() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        materiaDTO.setCreditos(0); // Créditos inválidos

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).addMateria(any(MateriaDTO.class), anyString());
    }

    @Test
    @DisplayName("Debe rechazar nueva materia con créditos excesivos")
    void testAddNewMateria_InvalidData_ExcessiveCredits() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        MateriaDTO materiaDTO = createValidMateriaDTO();
        materiaDTO.setCreditos(5); // Créditos excesivos

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}", codigoCarrera)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(materiaDTO)))
                .andExpect(status().isBadRequest());

        verify(carreraService, never()).addMateria(any(MateriaDTO.class), anyString());
    }

    // ========== PRUEBAS PARA ASOCIAR MATERIA EXISTENTE ==========

    @Test
    @DisplayName("Debe asociar materia existente a carrera exitosamente")
    void testAddExistingMateria_Success() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        String codigoMateria = "CALC1";
        Carrera carreraActualizada = createMockCarrera();
        
        when(carreraService.addMateriaById(codigoCarrera, codigoMateria))
            .thenReturn(carreraActualizada);

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}/{codigoMateria}", 
                codigoCarrera, codigoMateria))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("INGENIERIA_SISTEMAS"))
                .andExpect(jsonPath("$.codigo").value("ING_SIS"));

        verify(carreraService).addMateriaById(codigoCarrera, codigoMateria);
    }

    @Test
    @DisplayName("Debe manejar error al asociar materia existente - carrera no encontrada")
    void testAddExistingMateria_CarreraNotFound() throws Exception {
        // Given
        String codigoCarrera = "ING_INEXISTENTE";
        String codigoMateria = "CALC1";
        
        when(carreraService.addMateriaById(codigoCarrera, codigoMateria))
            .thenThrow(new SirhaException("Carrera no encontrada"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}/{codigoMateria}", 
                codigoCarrera, codigoMateria))
                .andExpect(status().isConflict());

        verify(carreraService).addMateriaById(codigoCarrera, codigoMateria);
    }

    @Test
    @DisplayName("Debe manejar error al asociar materia existente - materia no encontrada")
    void testAddExistingMateria_MateriaNotFound() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        String codigoMateria = "MATERIA_INEXISTENTE";
        
        when(carreraService.addMateriaById(codigoCarrera, codigoMateria))
            .thenThrow(new SirhaException("Materia no encontrada"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}/{codigoMateria}", 
                codigoCarrera, codigoMateria))
                .andExpect(status().isConflict());

        verify(carreraService).addMateriaById(codigoCarrera, codigoMateria);
    }

    @Test
    @DisplayName("Debe manejar error al asociar materia existente - materia ya asociada")
    void testAddExistingMateria_AlreadyAssociated() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        String codigoMateria = "CALC1";
        
        when(carreraService.addMateriaById(codigoCarrera, codigoMateria))
            .thenThrow(new SirhaException("La materia ya está asociada a la carrera"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}/{codigoMateria}", 
                codigoCarrera, codigoMateria))
                .andExpect(status().isConflict());

        verify(carreraService).addMateriaById(codigoCarrera, codigoMateria);
    }

    @Test
    @DisplayName("Debe manejar error general al asociar materia existente")
    void testAddExistingMateria_GeneralError() throws Exception {
        // Given
        String codigoCarrera = "ING_SIS";
        String codigoMateria = "CALC1";
        
        when(carreraService.addMateriaById(codigoCarrera, codigoMateria))
            .thenThrow(new SirhaException("Error interno del sistema"));

        // When & Then
        mockMvc.perform(post("/api/carreras/materia/{codigoCarrera}/{codigoMateria}", 
                codigoCarrera, codigoMateria))
                .andExpect(status().isConflict());

        verify(carreraService).addMateriaById(codigoCarrera, codigoMateria);
    }

    // ========== MÉTODOS AUXILIARES ==========

    private CarreraDTO createValidCarreraDTO() {
        CarreraDTO dto = new CarreraDTO();
        dto.setNombre("Ingeniería de Sistemas");
        dto.setCodigo("ING_SIS");
        dto.setDuracionSemestres(10);
        dto.setCreditosTotales(160);
        return dto;
    }

    private MateriaDTO createValidMateriaDTO() {
        MateriaDTO dto = new MateriaDTO();
        dto.setNombre("Cálculo Diferencial");
        dto.setAcronimo("CALC1");
        dto.setCreditos(3);
        dto.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        return dto;
    }

    private Materia createMockMateria() {
        Materia materia = new Materia();
        materia.setId("MAT001");
        materia.setNombre("Cálculo Diferencial");
        materia.setAcronimo("CALC1");
        materia.setCreditos(3);
        materia.setFacultad(Facultad.INGENIERIA_SISTEMAS);
        return materia;
    }

    private Carrera createMockCarrera() {
        Carrera carrera = new Carrera();
        carrera.setCodigo("ING_SIS");
        carrera.setNombre(Facultad.INGENIERIA_SISTEMAS);
        carrera.setDuracionSemestres(10);
        carrera.setCreditosTotales(160);
        carrera.setMaterias(new ArrayList<>(Arrays.asList(createMockMateria())));
        return carrera;
    }
}