package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.SolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.util.SolicitudUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el servicio EstudianteService.
 */
@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private MateriaRepository materiaRepository;

    @Mock
    private SolicitudUtil solicitudUtil;

    @InjectMocks
    private EstudianteService estudianteService;

    private Estudiante estudiante;
    private Grupo grupoProblema;
    private Materia materiaProblema;
    private SolicitudDTO solicitudDTO;

    @BeforeEach
    void setUp() {
        // Configurar estudiante de prueba
        estudiante = new Estudiante();
        estudiante.setId("est123");
        estudiante.setNombre("Juan Pérez");

        // Configurar materia de prueba
        materiaProblema = new Materia();
        materiaProblema.setId("mat123");
        materiaProblema.setNombre("Desarrollo de Software");
        materiaProblema.setAcronimo("DOSW");
        materiaProblema.setCreditos(3);

        // Configurar grupo de prueba
        grupoProblema = new Grupo();
        grupoProblema.setId("grupo123");
        grupoProblema.setMateria(materiaProblema);
        grupoProblema.setEstudiantesId(List.of("est123"));

        // Configurar DTO de solicitud
        solicitudDTO = new SolicitudDTO();
        solicitudDTO.setEstudianteId("est123");
        solicitudDTO.setTipoSolicitud(TipoSolicitud.CANCELACION_GRUPO);
        solicitudDTO.setGrupoProblemaId("grupo123");
        solicitudDTO.setMateriaProblemaAcronimo("DOSW");
        solicitudDTO.setObservaciones("Solicitud de prueba");
    }

    @Test
    void testCrearSolicitud_ConRadicadoYPrioridadAutomaticos() throws SirhaException {
        // Given
        String radicadoEsperado = "RAD-20241201-1001";
        Integer prioridadEsperada = 1;
        
        when(usuarioRepository.findById("est123")).thenReturn(Optional.of(estudiante));
        when(grupoRepository.findById("grupo123")).thenReturn(Optional.of(grupoProblema));
        when(materiaRepository.findByAcronimo("DOSW")).thenReturn(Optional.of(materiaProblema));
        when(solicitudUtil.generarNumeroRadicado()).thenReturn(radicadoEsperado);
        when(solicitudUtil.generarNumeroPrioridad()).thenReturn(prioridadEsperada);
        
        // Configurar el mock para save()
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud solicitud = invocation.getArgument(0);
            solicitud.setId("solicitud123"); // Simular ID generado
            return solicitud;
        });

        // When
        Solicitud resultado = estudianteService.crearSolicitud(solicitudDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("est123", resultado.getEstudianteId());
        assertEquals(TipoSolicitud.CANCELACION_GRUPO, resultado.getTipoSolicitud());
        assertEquals(radicadoEsperado, resultado.getNumeroRadicado());
        assertEquals(prioridadEsperada, resultado.getPrioridad());
        assertEquals("Solicitud de prueba", resultado.getObservaciones());
        assertEquals(grupoProblema, resultado.getGrupoProblema());
        assertEquals(materiaProblema, resultado.getMateriaProblema());

        // Verificar que se llamaron las utilidades de radicado y prioridad
        verify(solicitudUtil, times(1)).generarNumeroRadicado();
        verify(solicitudUtil, times(1)).generarNumeroPrioridad();
        verify(solicitudRepository, times(1)).save(any(Solicitud.class));
    }

    @Test
    void testCrearSolicitud_CambioGrupo_ConPrioridadSecuencial() throws SirhaException {
        // Given
        solicitudDTO.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        solicitudDTO.setGrupoDestinoId("grupoDestino123");
        solicitudDTO.setMateriaDestinoAcronimo("DOSW");

        Grupo grupoDestino = new Grupo();
        grupoDestino.setId("grupoDestino123");
        grupoDestino.setMateria(materiaProblema);
        grupoDestino.setCapacidad(30);
        grupoDestino.setEstudiantesId(List.of("otroEst"));

        String radicadoEsperado = "RAD-20241201-1002";
        Integer prioridadEsperada = 2;

        when(usuarioRepository.findById("est123")).thenReturn(Optional.of(estudiante));
        when(grupoRepository.findById("grupo123")).thenReturn(Optional.of(grupoProblema));
        when(grupoRepository.findById("grupoDestino123")).thenReturn(Optional.of(grupoDestino));
        when(materiaRepository.findByAcronimo("DOSW")).thenReturn(Optional.of(materiaProblema));
        when(solicitudUtil.generarNumeroRadicado()).thenReturn(radicadoEsperado);
        when(solicitudUtil.generarNumeroPrioridad()).thenReturn(prioridadEsperada);

        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud solicitud = invocation.getArgument(0);
            solicitud.setId("solicitud124");
            return solicitud;
        });

        // When
        Solicitud resultado = estudianteService.crearSolicitud(solicitudDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(TipoSolicitud.CAMBIO_GRUPO, resultado.getTipoSolicitud());
        assertEquals(radicadoEsperado, resultado.getNumeroRadicado());
        assertEquals(prioridadEsperada, resultado.getPrioridad());
        assertEquals(grupoDestino, resultado.getGrupoDestino());
        assertEquals(materiaProblema, resultado.getMateriaDestino());

        verify(solicitudUtil).generarNumeroPrioridad();
    }

    @Test
    void testCrearSolicitud_InscripcionGrupo_ConPrioridadSecuencial() throws SirhaException {
        // Given
        solicitudDTO.setTipoSolicitud(TipoSolicitud.INSCRIPCION_GRUPO);
        
        String radicadoEsperado = "RAD-20241201-1003";
        Integer prioridadEsperada = 3;

        when(usuarioRepository.findById("est123")).thenReturn(Optional.of(estudiante));
        when(grupoRepository.findById("grupo123")).thenReturn(Optional.of(grupoProblema));
        when(materiaRepository.findByAcronimo("DOSW")).thenReturn(Optional.of(materiaProblema));
        when(solicitudUtil.generarNumeroRadicado()).thenReturn(radicadoEsperado);
        when(solicitudUtil.generarNumeroPrioridad()).thenReturn(prioridadEsperada);

        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud solicitud = invocation.getArgument(0);
            solicitud.setId("solicitud125");
            return solicitud;
        });

        // When
        Solicitud resultado = estudianteService.crearSolicitud(solicitudDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(TipoSolicitud.INSCRIPCION_GRUPO, resultado.getTipoSolicitud());
        assertEquals(radicadoEsperado, resultado.getNumeroRadicado());
        assertEquals(prioridadEsperada, resultado.getPrioridad());

        verify(solicitudUtil).generarNumeroPrioridad();
    }

    @Test
    void testCrearSolicitud_EstudianteNoEncontrado_LanzaExcepcion() {
        // Given
        when(usuarioRepository.findById("est123")).thenReturn(Optional.empty());

        // When & Then
        SirhaException exception = assertThrows(SirhaException.class, 
                () -> estudianteService.crearSolicitud(solicitudDTO));
        
        assertEquals(SirhaException.ESTUDIANTE_NO_ENCONTRADO, exception.getMessage());
        
        // Verificar que no se llamaron las utilidades si falla la validación
        verify(solicitudUtil, never()).generarNumeroRadicado();
        verify(solicitudUtil, never()).generarNumeroPrioridad();
        verify(solicitudRepository, never()).save(any());
    }

    @Test
    void testCrearSolicitud_VerificarPrioridadSecuencial() throws SirhaException {
        // Given
        String radicado1 = "RAD-20241201-1004";
        String radicado2 = "RAD-20241201-1005";
        Integer prioridad1 = 4;
        Integer prioridad2 = 5;
        
        when(usuarioRepository.findById("est123")).thenReturn(Optional.of(estudiante));
        when(grupoRepository.findById("grupo123")).thenReturn(Optional.of(grupoProblema));
        when(materiaRepository.findByAcronimo("DOSW")).thenReturn(Optional.of(materiaProblema));
        
        // Simular prioridades secuenciales
        when(solicitudUtil.generarNumeroRadicado()).thenReturn(radicado1, radicado2);
        when(solicitudUtil.generarNumeroPrioridad()).thenReturn(prioridad1, prioridad2);
        
        when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(invocation -> {
            Solicitud solicitud = invocation.getArgument(0);
            solicitud.setId("solicitud" + System.currentTimeMillis());
            return solicitud;
        });

        // When
        Solicitud resultado1 = estudianteService.crearSolicitud(solicitudDTO);
        Solicitud resultado2 = estudianteService.crearSolicitud(solicitudDTO);

        // Then
        assertEquals(radicado1, resultado1.getNumeroRadicado());
        assertEquals(radicado2, resultado2.getNumeroRadicado());
        assertEquals(prioridad1, resultado1.getPrioridad());
        assertEquals(prioridad2, resultado2.getPrioridad());
        assertNotEquals(resultado1.getNumeroRadicado(), resultado2.getNumeroRadicado(), 
                "Los radicados deben ser únicos");
        assertNotEquals(resultado1.getPrioridad(), resultado2.getPrioridad(), 
                "Las prioridades deben ser secuenciales y diferentes");

        verify(solicitudUtil, times(2)).generarNumeroRadicado();
        verify(solicitudUtil, times(2)).generarNumeroPrioridad();
    }
}