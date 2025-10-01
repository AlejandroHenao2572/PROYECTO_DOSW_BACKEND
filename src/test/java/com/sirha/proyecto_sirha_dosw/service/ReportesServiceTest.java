package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.dto.TasaAprobacionDTO;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
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
class ReportesServiceTest {

	@Mock
	private SolicitudRepository solicitudRepository;
	@Mock
	private GrupoRepository grupoRepository;
	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private CarreraRepository carreraRepository;

	@InjectMocks
	private ReportesService reportesService;

	@Test
	void testObtenerGruposMasSolicitadosSinFacultad() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(materia.getFacultad()).thenReturn(Facultad.values()[0]);
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		when(grupoRepository.findAll()).thenReturn(List.of(grupo));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("G1", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(5L);
		List<EstadisticasGrupoDTO> result = reportesService.obtenerGruposMasSolicitados();
		assertEquals(1, result.size());
		assertEquals("G1", result.get(0).getGrupoId());
	}

	@Test
	void testObtenerGruposMasSolicitadosConFacultad() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(materia.getFacultad()).thenReturn(Facultad.values()[0]);
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		when(grupoRepository.findByMateria_Facultad(Facultad.values()[0])).thenReturn(List.of(grupo));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("G1", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(5L);
		List<EstadisticasGrupoDTO> result = reportesService.obtenerGruposMasSolicitados(Facultad.values()[0], 10);
		assertEquals(1, result.size());
		assertEquals("G1", result.get(0).getGrupoId());
	}

	@Test
	void testObtenerEstadisticasGrupo() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(materia.getFacultad()).thenReturn(Facultad.values()[0]);
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("G1", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(5L);
		EstadisticasGrupoDTO result = reportesService.obtenerEstadisticasGrupo("G1");
		assertNotNull(result);
		assertEquals("G1", result.getGrupoId());
	}

	@Test
	void testObtenerEstadisticasGrupoNoExiste() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		EstadisticasGrupoDTO result = reportesService.obtenerEstadisticasGrupo("G1");
		assertNull(result);
	}

	@Test
	void testObtenerTotalSolicitudesCambio() {
		Solicitud solicitud = mock(Solicitud.class);
		when(solicitudRepository.findByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO)).thenReturn(List.of(solicitud, solicitud));
		long result = reportesService.obtenerTotalSolicitudesCambio();
		assertEquals(2, result);
	}

	@Test
	void testObtenerGruposMasSolicitadosPorFacultad() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(materia.getFacultad()).thenReturn(Facultad.values()[0]);
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		when(grupoRepository.findByMateria_Facultad(Facultad.values()[0])).thenReturn(List.of(grupo));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("G1", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(5L);
		List<EstadisticasGrupoDTO> result = reportesService.obtenerGruposMasSolicitadosPorFacultad(Facultad.values()[0]);
		assertEquals(1, result.size());
	}

	@Test
	void testObtenerTasasAprobacionGlobal() {
		when(solicitudRepository.count()).thenReturn(10L);
		when(solicitudRepository.countByEstado(SolicitudEstado.APROBADA)).thenReturn(4L);
		when(solicitudRepository.countByEstado(SolicitudEstado.RECHAZADA)).thenReturn(2L);
		when(solicitudRepository.countByEstado(SolicitudEstado.PENDIENTE)).thenReturn(3L);
		when(solicitudRepository.countByEstado(SolicitudEstado.EN_REVISION)).thenReturn(1L);
		TasaAprobacionDTO result = reportesService.obtenerTasasAprobacionGlobal();
		assertEquals(10, result.getTotalSolicitudes());
	assertEquals(4, result.getSolicitudesAprobadas());
	assertEquals(2, result.getSolicitudesRechazadas());
	assertEquals(3, result.getSolicitudesPendientes());
	assertEquals(1, result.getSolicitudesEnRevision());
	}

	@Test
	void testObtenerTasasAprobacionPorFacultad() {
		Facultad facultad = Facultad.values()[0];
		when(solicitudRepository.countByFacultad(facultad)).thenReturn(10L);
		when(solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.APROBADA)).thenReturn(4L);
		when(solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.RECHAZADA)).thenReturn(2L);
		when(solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.PENDIENTE)).thenReturn(3L);
		when(solicitudRepository.countByFacultadAndEstado(facultad, SolicitudEstado.EN_REVISION)).thenReturn(1L);
		TasaAprobacionDTO result = reportesService.obtenerTasasAprobacionPorFacultad(facultad);
		assertEquals(10, result.getTotalSolicitudes());
	assertEquals(4, result.getSolicitudesAprobadas());
	assertEquals(2, result.getSolicitudesRechazadas());
	assertEquals(3, result.getSolicitudesPendientes());
	assertEquals(1, result.getSolicitudesEnRevision());
	}

	@Test
	void testObtenerTasasAprobacionPorTipo() {
		TipoSolicitud tipo = TipoSolicitud.CAMBIO_GRUPO;
		Solicitud s1 = mock(Solicitud.class);
		Solicitud s2 = mock(Solicitud.class);
		when(solicitudRepository.countByTipoSolicitud(tipo)).thenReturn(2L);
		when(solicitudRepository.findByTipoSolicitud(tipo)).thenReturn(List.of(s1, s2));
		when(s1.getEstado()).thenReturn(SolicitudEstado.APROBADA);
		when(s2.getEstado()).thenReturn(SolicitudEstado.RECHAZADA);
		TasaAprobacionDTO result = reportesService.obtenerTasasAprobacionPorTipo(tipo);
		assertEquals(2, result.getTotalSolicitudes());
	assertEquals(1, result.getSolicitudesAprobadas());
	assertEquals(1, result.getSolicitudesRechazadas());
	}

	@Test
	void testObtenerTasasAprobacionPorFacultadYTipo() {
		Facultad facultad = Facultad.values()[0];
		TipoSolicitud tipo = TipoSolicitud.CAMBIO_GRUPO;
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.APROBADA, tipo)).thenReturn(4L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.RECHAZADA, tipo)).thenReturn(2L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.PENDIENTE, tipo)).thenReturn(3L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(facultad, SolicitudEstado.EN_REVISION, tipo)).thenReturn(1L);
		TasaAprobacionDTO result = reportesService.obtenerTasasAprobacionPorFacultadYTipo(facultad, tipo);
		assertEquals(10, result.getTotalSolicitudes());
	assertEquals(4, result.getSolicitudesAprobadas());
	assertEquals(2, result.getSolicitudesRechazadas());
	assertEquals(3, result.getSolicitudesPendientes());
	assertEquals(1, result.getSolicitudesEnRevision());
	}

	@Test
	void testCalcularIndicadoresAvanceEstudianteNoExiste() {
		when(usuarioRepository.findById("E1")).thenReturn(Optional.empty());
		IndicadoresAvanceDTO result = reportesService.calcularIndicadoresAvanceEstudiante("E1");
		assertNull(result);
	}

	@Test
	void testCalcularIndicadoresAvanceGlobalesSinEstudiantes() {
		when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());
		IndicadoresAvanceDTO result = reportesService.calcularIndicadoresAvanceGlobales(null);
		assertNotNull(result);
		assertEquals("ESTADISTICAS_GLOBALES", result.getTipoReporte());
	}
}
