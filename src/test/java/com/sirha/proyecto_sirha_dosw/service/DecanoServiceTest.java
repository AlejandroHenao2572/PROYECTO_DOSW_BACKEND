package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.*;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecanoServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private SolicitudRepository solicitudRepository;
	@Mock
	private GrupoRepository grupoRepository;
	@Mock
	private MateriaRepository materiaRepository;

	@InjectMocks
	private DecanoService decanoService;

	@Test
	void testFindEstudiantesByFacultad() {
		List<Usuario> estudiantes = List.of(mock(Usuario.class));
		when(usuarioRepository.findByFacultadAndRol(Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiantes);
		List<Usuario> result = decanoService.findEstudiantesByFacultad(Facultad.values()[0].name());
		assertEquals(1, result.size());
	}

	@Test
	void testFindEstudianteByIdAndFacultad() {
		Usuario estudiante = mock(Usuario.class);
		when(usuarioRepository.findByIdAndFacultadAndRol("E1", Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiante);
		Usuario result = decanoService.findEstudianteByIdAndFacultad("E1", Facultad.values()[0].name());
		assertNotNull(result);
	}

	@Test
	void testFindEstudianteByEmailAndFacultad() {
		Usuario estudiante = mock(Usuario.class);
		when(usuarioRepository.findByEmailAndFacultadAndRol("mail", Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiante);
		Usuario result = decanoService.findEstudianteByEmailAndFacultad("mail", Facultad.values()[0].name());
		assertNotNull(result);
	}

	@Test
	void testFindEstudiantesByNombreAndFacultad() {
		List<Usuario> estudiantes = List.of(mock(Usuario.class));
		when(usuarioRepository.findByNombreAndFacultadAndRol("Juan", Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiantes);
		List<Usuario> result = decanoService.findEstudiantesByNombreAndFacultad("Juan", Facultad.values()[0].name());
		assertEquals(1, result.size());
	}

	@Test
	void testFindEstudiantesByApellidoAndFacultad() {
		List<Usuario> estudiantes = List.of(mock(Usuario.class));
		when(usuarioRepository.findByApellidoAndFacultadAndRol("Perez", Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiantes);
		List<Usuario> result = decanoService.findEstudiantesByApellidoAndFacultad("Perez", Facultad.values()[0].name());
		assertEquals(1, result.size());
	}

	@Test
	void testFindEstudiantesByNombreApellidoAndFacultad() {
		List<Usuario> estudiantes = List.of(mock(Usuario.class));
		when(usuarioRepository.findByNombreAndApellidoAndFacultadAndRol("Juan", "Perez", Facultad.values()[0], Rol.ESTUDIANTE)).thenReturn(estudiantes);
		List<Usuario> result = decanoService.findEstudiantesByNombreApellidoAndFacultad("Juan", "Perez", Facultad.values()[0].name());
		assertEquals(1, result.size());
	}

	@Test
	void testConsultarSolicitudesPorFacultad() {
		List<Solicitud> solicitudes = List.of(mock(Solicitud.class));
		when(solicitudRepository.findByFacultad(Facultad.values()[0])).thenReturn(solicitudes);
		List<Solicitud> result = decanoService.consultarSolicitudesPorFacultad(Facultad.values()[0]);
		assertEquals(1, result.size());
	}

	@Test
	void testConsultarSolicitudesPendientesPorFacultad() {
		List<Solicitud> solicitudes = List.of(mock(Solicitud.class));
		when(solicitudRepository.findByFacultadAndEstado(Facultad.values()[0], SolicitudEstado.PENDIENTE)).thenReturn(solicitudes);
		List<Solicitud> result = decanoService.consultarSolicitudesPendientesPorFacultad(Facultad.values()[0]);
		assertEquals(1, result.size());
	}

	@Test
	void testConsultarSolicitudesPorFacultadYEstado() {
		List<Solicitud> solicitudes = List.of(mock(Solicitud.class));
		when(solicitudRepository.findByFacultadAndEstado(Facultad.values()[0], SolicitudEstado.APROBADA)).thenReturn(solicitudes);
		List<Solicitud> result = decanoService.consultarSolicitudesPorFacultadYEstado(Facultad.values()[0], SolicitudEstado.APROBADA);
		assertEquals(1, result.size());
	}

	@Test
	void testValidarFacultadInvalida() {
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.validarFacultad(""));
		assertTrue(ex.getMessage().contains("Facultad no válida"));
	}

	@Test
	void testCalcularPorcentajeOcupacion() {
		Grupo grupo = mock(Grupo.class);
		when(grupo.getCapacidad()).thenReturn(10);
		when(grupo.getCantidadInscritos()).thenReturn(5);
		double result = decanoService.calcularPorcentajeOcupacion(grupo);
		assertEquals(50.0, result);
	}

	@Test
	void testTieneAlertaCapacidadTrue() {
		Grupo grupo = mock(Grupo.class);
		when(grupo.getCapacidad()).thenReturn(10);
		when(grupo.getCantidadInscritos()).thenReturn(9);
		assertTrue(decanoService.tieneAlertaCapacidad(grupo));
	}

	@Test
	void testTieneAlertaCapacidadFalse() {
		Grupo grupo = mock(Grupo.class);
		when(grupo.getCapacidad()).thenReturn(10);
		when(grupo.getCantidadInscritos()).thenReturn(5);
		assertFalse(decanoService.tieneAlertaCapacidad(grupo));
	}
}
