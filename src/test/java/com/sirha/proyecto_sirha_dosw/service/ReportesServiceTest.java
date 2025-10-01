package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.EstadisticasGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.dto.TasaAprobacionDTO;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportesServiceTest {

	@Mock
	SolicitudRepository solicitudRepository;
	@Mock
	GrupoRepository grupoRepository;
	@Mock
	UsuarioRepository usuarioRepository;
	@Mock
	CarreraRepository carreraRepository;
	@InjectMocks
	ReportesService reportesService;

	private Grupo grupoA;
	private Grupo grupoB;

	@BeforeEach
	void setUp() {
		Materia materiaA = new Materia("Software", "SW1", 3, Facultad.INGENIERIA_SISTEMAS);
		materiaA.setId("MAT_A");
		grupoA = new Grupo(materiaA, 30, new ArrayList<>());
		grupoA.setId("GRP_A");
		grupoA.setCantidadInscritos(15);

		Materia materiaB = new Materia("Cálculo", "CAL1", 4, Facultad.INGENIERIA_CIVIL);
		materiaB.setId("MAT_B");
		grupoB = new Grupo(materiaB, 20, new ArrayList<>());
		grupoB.setId("GRP_B");
		grupoB.setCantidadInscritos(18);
	}

	@Test
	void testObtenerGruposMasSolicitadosDefault() {
		when(grupoRepository.findAll()).thenReturn(List.of(grupoA, grupoB));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("GRP_A", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(5L);
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("GRP_B", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(2L);

		List<EstadisticasGrupoDTO> resultados = reportesService.obtenerGruposMasSolicitados();
		assertEquals(2, resultados.size());
		assertEquals("GRP_A", resultados.get(0).getGrupoId());
	}

	@Test
	void testObtenerGruposMasSolicitadosFiltrado() {
		when(grupoRepository.findByMateria_Facultad(Facultad.INGENIERIA_SISTEMAS)).thenReturn(List.of(grupoA));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("GRP_A", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(3L);
		List<EstadisticasGrupoDTO> resultados = reportesService.obtenerGruposMasSolicitados(Facultad.INGENIERIA_SISTEMAS, 1);
		assertEquals(1, resultados.size());
		assertEquals("MAT_A", resultados.get(0).getMateriaId());
	}

	@Test
	void testObtenerGruposMasSolicitadosIgnoraSinMateria() {
		Grupo grupoSinMateria = new Grupo();
		grupoSinMateria.setId("VACIO");
		when(grupoRepository.findAll()).thenReturn(List.of(grupoSinMateria));
		List<EstadisticasGrupoDTO> resultados = reportesService.obtenerGruposMasSolicitados();
		assertTrue(resultados.isEmpty());
	}

	@Test
	void testObtenerEstadisticasGrupoExistente() {
		when(grupoRepository.findById("GRP_A")).thenReturn(Optional.of(grupoA));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("GRP_A", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(4L);
		EstadisticasGrupoDTO dto = reportesService.obtenerEstadisticasGrupo("GRP_A");
		assertNotNull(dto);
		assertEquals(4L, dto.getCantidadSolicitudes());
	}

	@Test
	void testObtenerEstadisticasGrupoSinMateria() {
		Grupo grupoSinMateria = new Grupo();
		grupoSinMateria.setId("VACIO");
		when(grupoRepository.findById("VACIO")).thenReturn(Optional.of(grupoSinMateria));
		assertNull(reportesService.obtenerEstadisticasGrupo("VACIO"));
	}

	@Test
	void testObtenerEstadisticasGrupoNoExiste() {
		when(grupoRepository.findById("GRP_X")).thenReturn(Optional.empty());
		assertNull(reportesService.obtenerEstadisticasGrupo("GRP_X"));
	}

	@Test
	void testObtenerTotalSolicitudesCambio() {
		when(solicitudRepository.findByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO)).thenReturn(List.of(new Solicitud(), new Solicitud()));
		assertEquals(2, reportesService.obtenerTotalSolicitudesCambio());
	}

	@Test
	void testObtenerGruposMasSolicitadosPorFacultad() {
		when(grupoRepository.findByMateria_Facultad(Facultad.INGENIERIA_SISTEMAS)).thenReturn(List.of(grupoA));
		when(solicitudRepository.countByGrupoDestino_IdAndTipoSolicitud("GRP_A", TipoSolicitud.CAMBIO_GRUPO)).thenReturn(6L);
		List<EstadisticasGrupoDTO> resultados = reportesService.obtenerGruposMasSolicitadosPorFacultad(Facultad.INGENIERIA_SISTEMAS);
		assertEquals(1, resultados.size());
		assertEquals("SW1", resultados.get(0).getMateriaAcronimo());
	}

	@Test
	void testObtenerTasasAprobacionGlobal() {
		when(solicitudRepository.count()).thenReturn(10L);
		when(solicitudRepository.countByEstado(SolicitudEstado.APROBADA)).thenReturn(4L);
		when(solicitudRepository.countByEstado(SolicitudEstado.RECHAZADA)).thenReturn(3L);
		when(solicitudRepository.countByEstado(SolicitudEstado.PENDIENTE)).thenReturn(2L);
		when(solicitudRepository.countByEstado(SolicitudEstado.EN_REVISION)).thenReturn(1L);
		TasaAprobacionDTO dto = reportesService.obtenerTasasAprobacionGlobal();
		assertEquals(10L, dto.getTotalSolicitudes());
		assertEquals("TODAS", dto.getTipoSolicitud());
	}

	@Test
	void testObtenerTasasAprobacionPorFacultad() {
		when(solicitudRepository.countByFacultad(Facultad.INGENIERIA_SISTEMAS)).thenReturn(5L);
		when(solicitudRepository.countByFacultadAndEstado(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.APROBADA)).thenReturn(2L);
		when(solicitudRepository.countByFacultadAndEstado(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.RECHAZADA)).thenReturn(1L);
		when(solicitudRepository.countByFacultadAndEstado(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.PENDIENTE)).thenReturn(1L);
		when(solicitudRepository.countByFacultadAndEstado(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.EN_REVISION)).thenReturn(1L);
		TasaAprobacionDTO dto = reportesService.obtenerTasasAprobacionPorFacultad(Facultad.INGENIERIA_SISTEMAS);
		assertEquals("INGENIERIA_SISTEMAS", dto.getFacultad());
	}

	@Test
	void testObtenerTasasAprobacionPorTipo() {
		Solicitud aprobada = new Solicitud();
		aprobada.setEstado(SolicitudEstado.APROBADA);
		Solicitud rechazada = new Solicitud();
		rechazada.setEstado(SolicitudEstado.RECHAZADA);
		Solicitud pendiente = new Solicitud();
		pendiente.setEstado(SolicitudEstado.PENDIENTE);
		Solicitud enRevision = new Solicitud();
		enRevision.setEstado(SolicitudEstado.EN_REVISION);
		when(solicitudRepository.countByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO)).thenReturn(4L);
		when(solicitudRepository.findByTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO)).thenReturn(List.of(aprobada, rechazada, pendiente, enRevision));
		TasaAprobacionDTO dto = reportesService.obtenerTasasAprobacionPorTipo(TipoSolicitud.CAMBIO_GRUPO);
		assertEquals("CAMBIO_GRUPO", dto.getTipoSolicitud());
		assertEquals(4L, dto.getTotalSolicitudes());
	}

	@Test
	void testObtenerTasasAprobacionPorFacultadYTipo() {
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.APROBADA, TipoSolicitud.CAMBIO_GRUPO)).thenReturn(2L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.RECHAZADA, TipoSolicitud.CAMBIO_GRUPO)).thenReturn(1L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.PENDIENTE, TipoSolicitud.CAMBIO_GRUPO)).thenReturn(0L);
		when(solicitudRepository.countByFacultadAndEstadoAndTipoSolicitud(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.EN_REVISION, TipoSolicitud.CAMBIO_GRUPO)).thenReturn(1L);
		TasaAprobacionDTO dto = reportesService.obtenerTasasAprobacionPorFacultadYTipo(Facultad.INGENIERIA_SISTEMAS, TipoSolicitud.CAMBIO_GRUPO);
		assertEquals(4L, dto.getTotalSolicitudes());
		assertEquals("CAMBIO_GRUPO", dto.getTipoSolicitud());
	}

	// Tests para los métodos con 0% de cobertura
	@Test
	void testCalcularIndicadoresAvanceEstudiante() {
		// Crear estudiante mock
		Estudiante estudiante = new Estudiante();
		estudiante.setNombre("Juan");
		estudiante.setApellido("Pérez");
		estudiante.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		// Crear carrera mock
		Carrera carrera = new Carrera();
		carrera.setCreditosTotales(160);
		// No podemos usar setTotalMaterias porque no existe, se calcula automáticamente

		// Mock del repository
		when(usuarioRepository.findById("EST123")).thenReturn(Optional.of(estudiante));
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_SISTEMAS)).thenReturn(Optional.of(carrera));

		// Ejecutar método
		IndicadoresAvanceDTO resultado = reportesService.calcularIndicadoresAvanceEstudiante("EST123");

		// Verificar resultado
		assertNotNull(resultado);
		assertEquals(estudiante.getId(), resultado.getEstudianteId());
		assertEquals("Juan", resultado.getEstudianteNombre());
		assertEquals("Pérez", resultado.getEstudianteApellido());
		assertEquals(Facultad.INGENIERIA_SISTEMAS, resultado.getFacultad());
		assertEquals(160, resultado.getCreditosTotales());
		assertEquals(0, resultado.getTotalMaterias()); // Carrera sin materias
		
		verify(usuarioRepository).findById("EST123");
		verify(carreraRepository).findByNombre(Facultad.INGENIERIA_SISTEMAS);
	}

	@Test
	void testCalcularIndicadoresAvanceEstudianteNoEncontrado() {
		when(usuarioRepository.findById("EST999")).thenReturn(Optional.empty());

		IndicadoresAvanceDTO resultado = reportesService.calcularIndicadoresAvanceEstudiante("EST999");

		assertNull(resultado);
		verify(usuarioRepository).findById("EST999");
	}

	@Test
	void testCalcularIndicadoresAvanceGlobales() {
		// Crear estudiantes mock con datos completos para ejecutar el método interno
		Estudiante est1 = new Estudiante();
		est1.setId("EST001");
		est1.setNombre("Juan");
		est1.setApellido("Pérez");
		est1.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		Estudiante est2 = new Estudiante();
		est2.setId("EST002");
		est2.setNombre("María");
		est2.setApellido("González");
		est2.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		// Usar una lista de Usuario ya que Estudiante extiende Usuario
		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(est1);
		usuarios.add(est2);

		// Crear carrera mock para que el método interno funcione
		Carrera carrera = new Carrera();
		carrera.setCreditosTotales(160);

		// Mock de repositorios
		when(usuarioRepository.findAll()).thenReturn(usuarios);
		// Mock para cada llamada individual del método calcularIndicadoresAvanceEstudiante
		when(usuarioRepository.findById("EST001")).thenReturn(Optional.of(est1));
		when(usuarioRepository.findById("EST002")).thenReturn(Optional.of(est2));
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_SISTEMAS)).thenReturn(Optional.of(carrera));

		// Ejecutar método
		IndicadoresAvanceDTO resultado = reportesService.calcularIndicadoresAvanceGlobales(Facultad.INGENIERIA_SISTEMAS);

		// Verificar resultado
		assertNotNull(resultado);
		assertEquals("ESTADISTICAS_GLOBALES", resultado.getTipoReporte());
		assertEquals(Facultad.INGENIERIA_SISTEMAS, resultado.getFacultad());
		
		// Verificar que se llamaron los métodos necesarios
		verify(usuarioRepository).findAll();
		verify(usuarioRepository).findById("EST001");
		verify(usuarioRepository).findById("EST002");
		verify(carreraRepository, times(2)).findByNombre(Facultad.INGENIERIA_SISTEMAS);
	}

	@Test
	void testCalcularIndicadoresAvanceGlobalesSinFiltro() {
		// Crear estudiantes de diferentes facultades
		Estudiante est1 = new Estudiante();
		est1.setId("EST003");
		est1.setNombre("Juan");
		est1.setApellido("Pérez");
		est1.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		Estudiante est2 = new Estudiante();
		est2.setId("EST004");
		est2.setNombre("María");
		est2.setApellido("González");
		est2.setCarrera(Facultad.INGENIERIA_CIVIL);

		// Usar una lista de Usuario
		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(est1);
		usuarios.add(est2);

		// Crear carreras mock
		Carrera carreraSistemas = new Carrera();
		carreraSistemas.setCreditosTotales(160);
		
		Carrera carreraCivil = new Carrera();
		carreraCivil.setCreditosTotales(180);

		when(usuarioRepository.findAll()).thenReturn(usuarios);
		when(usuarioRepository.findById("EST003")).thenReturn(Optional.of(est1));
		when(usuarioRepository.findById("EST004")).thenReturn(Optional.of(est2));
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_SISTEMAS)).thenReturn(Optional.of(carreraSistemas));
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_CIVIL)).thenReturn(Optional.of(carreraCivil));

		IndicadoresAvanceDTO resultado = reportesService.calcularIndicadoresAvanceGlobales(null);

		assertNotNull(resultado);
		assertEquals("ESTADISTICAS_GLOBALES", resultado.getTipoReporte());
		assertNull(resultado.getFacultad());
		
		verify(usuarioRepository).findAll();
		verify(usuarioRepository).findById("EST003");
		verify(usuarioRepository).findById("EST004");
	}

	@Test
	void testCalcularIndicadoresAvanceGlobalesConEstudiantesCompletos() {
		// Test adicional para cubrir más líneas del método calcularIndicadoresAvanceGlobales
		// Crear estudiantes con más información para activar el cálculo de distribución de estados
		Estudiante est1 = new Estudiante();
		est1.setId("EST005");
		est1.setNombre("Carlos");
		est1.setApellido("Mendoza");
		est1.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		Estudiante est2 = new Estudiante();
		est2.setId("EST006");
		est2.setNombre("Ana");
		est2.setApellido("Rodríguez");
		est2.setCarrera(Facultad.INGENIERIA_SISTEMAS);

		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(est1);
		usuarios.add(est2);

		Carrera carrera = new Carrera();
		carrera.setCreditosTotales(160);

		when(usuarioRepository.findAll()).thenReturn(usuarios);
		when(usuarioRepository.findById("EST005")).thenReturn(Optional.of(est1));
		when(usuarioRepository.findById("EST006")).thenReturn(Optional.of(est2));
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_SISTEMAS)).thenReturn(Optional.of(carrera));

		IndicadoresAvanceDTO resultado = reportesService.calcularIndicadoresAvanceGlobales(Facultad.INGENIERIA_SISTEMAS);

		assertNotNull(resultado);
		assertEquals("ESTADISTICAS_GLOBALES", resultado.getTipoReporte());
		assertEquals(Facultad.INGENIERIA_SISTEMAS, resultado.getFacultad());
		
		verify(usuarioRepository).findAll();
		verify(usuarioRepository).findById("EST005");
		verify(usuarioRepository).findById("EST006");
		verify(carreraRepository, times(2)).findByNombre(Facultad.INGENIERIA_SISTEMAS);
	}

}
