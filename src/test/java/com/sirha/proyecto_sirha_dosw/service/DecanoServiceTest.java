package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.CalendarioAcademicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.DisponibilidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.MonitoreoGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.PlazoSolicitudesDTO;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.CalendarioAcademico;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.PlazoSolicitudes;
import com.sirha.proyecto_sirha_dosw.model.Profesor;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import com.sirha.proyecto_sirha_dosw.model.Semestre;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecanoServiceTest {

	private static final Facultad FACULTAD_ENUM = Facultad.INGENIERIA_SISTEMAS;
	private static final String FACULTAD = FACULTAD_ENUM.name();

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

	private Materia materiaBase;
	private Grupo grupoBase;

	@BeforeEach
	void setUp() {
		materiaBase = new Materia("Arquitectura", "ARQ101", 3, FACULTAD_ENUM);
		materiaBase.setId("MAT1");
		grupoBase = buildGrupo("GRP1", materiaBase, 30, List.of("EST1", "EST2"));
	}

	@Test
	void testFindEstudiantesByFacultad() {
		List<com.sirha.proyecto_sirha_dosw.model.Usuario> estudiantes = List.of(mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class));
		when(usuarioRepository.findByFacultadAndRol(FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiantes);
		List<com.sirha.proyecto_sirha_dosw.model.Usuario> result = decanoService.findEstudiantesByFacultad(FACULTAD);
		assertEquals(1, result.size());
	}

	@Test
	void testFindEstudianteByIdAndFacultad() {
		com.sirha.proyecto_sirha_dosw.model.Usuario estudiante = mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class);
		when(usuarioRepository.findByIdAndFacultadAndRol("E1", FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiante);
		assertNotNull(decanoService.findEstudianteByIdAndFacultad("E1", FACULTAD));
	}

	@Test
	void testFindEstudianteByEmailAndFacultad() {
		com.sirha.proyecto_sirha_dosw.model.Usuario estudiante = mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class);
		when(usuarioRepository.findByEmailAndFacultadAndRol("mail", FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiante);
		assertNotNull(decanoService.findEstudianteByEmailAndFacultad("mail", FACULTAD));
	}

	@Test
	void testFindEstudiantesByNombreAndFacultad() {
		List<com.sirha.proyecto_sirha_dosw.model.Usuario> estudiantes = List.of(mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class));
		when(usuarioRepository.findByNombreAndFacultadAndRol("Juan", FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiantes);
		assertEquals(1, decanoService.findEstudiantesByNombreAndFacultad("Juan", FACULTAD).size());
	}

	@Test
	void testFindEstudiantesByApellidoAndFacultad() {
		List<com.sirha.proyecto_sirha_dosw.model.Usuario> estudiantes = List.of(mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class));
		when(usuarioRepository.findByApellidoAndFacultadAndRol("Perez", FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiantes);
		assertEquals(1, decanoService.findEstudiantesByApellidoAndFacultad("Perez", FACULTAD).size());
	}

	@Test
	void testFindEstudiantesByNombreApellidoAndFacultad() {
		List<com.sirha.proyecto_sirha_dosw.model.Usuario> estudiantes = List.of(mock(com.sirha.proyecto_sirha_dosw.model.Usuario.class));
		when(usuarioRepository.findByNombreAndApellidoAndFacultadAndRol("Ana", "Lopez", FACULTAD_ENUM, Rol.ESTUDIANTE)).thenReturn(estudiantes);
		assertEquals(1, decanoService.findEstudiantesByNombreApellidoAndFacultad("Ana", "Lopez", FACULTAD).size());
	}

	@Test
	void testConsultarSolicitudesPorFacultad() {
		List<Solicitud> solicitudes = List.of(new Solicitud());
		when(solicitudRepository.findByFacultad(FACULTAD_ENUM)).thenReturn(solicitudes);
		assertEquals(1, decanoService.consultarSolicitudesPorFacultad(FACULTAD_ENUM).size());
	}

	@Test
	void testConsultarSolicitudesPendientesPorFacultad() {
		List<Solicitud> solicitudes = List.of(new Solicitud());
		when(solicitudRepository.findByFacultadAndEstado(FACULTAD_ENUM, SolicitudEstado.PENDIENTE)).thenReturn(solicitudes);
		assertEquals(1, decanoService.consultarSolicitudesPendientesPorFacultad(FACULTAD_ENUM).size());
	}

	@Test
	void testConsultarSolicitudesPorFacultadYEstado() {
		List<Solicitud> solicitudes = List.of(new Solicitud());
		when(solicitudRepository.findByFacultadAndEstado(FACULTAD_ENUM, SolicitudEstado.APROBADA)).thenReturn(solicitudes);
		assertEquals(1, decanoService.consultarSolicitudesPorFacultadYEstado(FACULTAD_ENUM, SolicitudEstado.APROBADA).size());
	}

	@Test
	void testConsultarHorarioEstudianteExitoso() throws SirhaException {
		Estudiante estudiante = buildEstudiante("EST1", FACULTAD_ENUM, grupoBase);
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		List<RegistroMaterias> resultado = decanoService.consultarHorarioEstudiante("EST1", 1);
		assertEquals(1, resultado.size());
	}

	@Test
	void testConsultarHorarioEstudianteNoEncontrado() {
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarHorarioEstudiante("EST1", 1));
		assertEquals(SirhaException.ESTUDIANTE_NO_ENCONTRADO, ex.getMessage());
	}

	@Test
	void testConsultarHorarioEstudianteSemestreInvalido() {
		Estudiante estudiante = new Estudiante();
		when(usuarioRepository.findById("EST3")).thenReturn(Optional.of(estudiante));
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarHorarioEstudiante("EST3", 2));
		assertEquals(SirhaException.SEMESTRE_INVALIDO, ex.getMessage());
	}

	@Test
	void testConsultarSemaforoAcademicoExitoso() throws SirhaException {
		Estudiante estudiante = spy(new Estudiante());
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		Map<String, Semaforo> semaforo = new HashMap<>();
		semaforo.put("ARQ101", Semaforo.ROJO);
		when(estudiante.getSemaforo()).thenReturn(semaforo);
		Map<String, Semaforo> resultado = decanoService.consultarSemaforoAcademicoEstudiante("EST1");
		assertEquals(Semaforo.ROJO, resultado.get("ARQ101"));
	}

	@Test
	void testConsultarSemaforoAcademicoNoEncontrado() {
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarSemaforoAcademicoEstudiante("EST1"));
		assertEquals(SirhaException.ESTUDIANTE_NO_ENCONTRADO, ex.getMessage());
	}

	@Test
	void testValidarFacultadInvalida() {
		assertThrows(SirhaException.class, () -> decanoService.validarFacultad(" "));
	}

	@Test
	void testValidarFacultadNoReconocida() {
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.validarFacultad("fake"));
		assertTrue(ex.getMessage().contains(SirhaException.FACULTAD_INVALIDA));
	}

	@Test
	void testValidarFacultadValida() {
		assertDoesNotThrow(() -> decanoService.validarFacultad(FACULTAD));
	}

	@Test
	void testValidarEstudianteFacultadIdVacio() {
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.validarEstudianteFacultad(" ", FACULTAD));
		assertTrue(ex.getMessage().contains(SirhaException.ESTUDIANTE_NO_ENCONTRADO));
	}

	@Test
	void testValidarEstudianteFacultadCarreraDistinta() {
		Estudiante estudiante = new Estudiante();
		estudiante.setCarrera(Facultad.INGENIERIA_CIVIL);
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.validarEstudianteFacultad("EST1", FACULTAD));
		assertTrue(ex.getMessage().contains("EST1"));
	}

	@Test
	void testValidarEstudianteFacultadValido() {
		Estudiante estudiante = new Estudiante();
		estudiante.setCarrera(FACULTAD_ENUM);
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		assertDoesNotThrow(() -> decanoService.validarEstudianteFacultad("EST1", FACULTAD));
	}

	@Test
	void testValidarSemestreFueraRango() {
		assertThrows(SirhaException.class, () -> decanoService.validarSemestre(0));
		assertThrows(SirhaException.class, () -> decanoService.validarSemestre(11));
	}

	@Test
	void testValidarSemestreDentroRango() {
		assertDoesNotThrow(() -> decanoService.validarSemestre(5));
	}

	@Test
	void testObtenerInformacionBasicaEstudiante() throws SirhaException {
		Estudiante estudiante = buildEstudiante("EST1", FACULTAD_ENUM, grupoBase);
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		EstudianteBasicoDTO dto = decanoService.obtenerInformacionBasicaEstudiante("EST1", FACULTAD);
		assertEquals(estudiante.getNombre(), dto.getNombre());
		assertEquals(1, dto.getSemestreActual());
	}

	@Test
	void testConsultarDisponibilidadGruposExitoso() throws SirhaException {
		when(materiaRepository.findByAcronimo("ARQ101")).thenReturn(Optional.of(materiaBase));
		when(grupoRepository.findByMateria_Id("MAT1")).thenReturn(List.of(grupoBase));
		List<DisponibilidadGrupoDTO> dtos = decanoService.consultarDisponibilidadGrupos("ARQ101", FACULTAD);
		assertEquals(1, dtos.size());
		assertEquals(grupoBase.getId(), dtos.get(0).getGrupoId());
	}

	@Test
	void testConsultarDisponibilidadGruposMateriaNoEncontrada() {
		when(materiaRepository.findByAcronimo("ARQ101")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarDisponibilidadGrupos("ARQ101", FACULTAD));
		assertTrue(ex.getMessage().contains("Materia no encontrada"));
	}

	@Test
	void testConsultarDisponibilidadGruposSinGrupos() {
		when(materiaRepository.findByAcronimo("ARQ101")).thenReturn(Optional.of(materiaBase));
		when(grupoRepository.findByMateria_Id("MAT1")).thenReturn(List.of());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarDisponibilidadGrupos("ARQ101", FACULTAD));
		assertTrue(ex.getMessage().contains("No se encontraron grupos"));
	}

	@Test
	void testConsultarDisponibilidadGruposFacultadInvalida() {
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.consultarDisponibilidadGrupos("ARQ101", ""));
		assertTrue(ex.getMessage().contains(SirhaException.FACULTAD_INVALIDA));
	}

	@Test
	void testResponderSolicitudApruebaSinCruces() throws Exception {
		Estudiante estudiante = buildEstudiante("EST1", FACULTAD_ENUM, grupoBase);
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		Grupo grupoDestino = buildGrupo("GRP2", materiaBase, 30, List.of());
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, grupoDestino);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));
		when(grupoRepository.save(any(Grupo.class))).thenAnswer(inv -> inv.getArgument(0));
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))) {
			RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
			respuesta.setSolicitudId("SOL1");
			respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
			respuesta.setObservacionesRespuesta("OK");
			assertDoesNotThrow(() -> decanoService.responderSolicitud(respuesta, FACULTAD));
		}
		verify(grupoRepository, atLeastOnce()).save(any(Grupo.class));
		verify(usuarioRepository, atLeastOnce()).save(any(Estudiante.class));
	}

	@Test
	void testResponderSolicitudNoEncontrada() {
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.empty());
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
		assertTrue(ex.getMessage().contains("Solicitud no encontrada"));
	}

	@Test
	void testResponderSolicitudFacultadDistinta() {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		solicitud.setFacultad(Facultad.ADMINISTRACION);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.RECHAZADA);
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
		assertTrue(ex.getMessage().contains("no pertenece"));
	}

	@Test
	void testResponderSolicitudEstadoNoPendiente() {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		solicitud.setEstado(SolicitudEstado.APROBADA);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.RECHAZADA);
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
		assertTrue(ex.getMessage().contains("Solo se pueden responder"));
	}

	@Test
	void testResponderSolicitudRechazada() throws SirhaException {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.RECHAZADA);
		decanoService.responderSolicitud(respuesta, FACULTAD);
		assertEquals(SolicitudEstado.RECHAZADA, solicitud.getEstado());
	}

	@Test
	void testResponderSolicitudEnRevision() throws SirhaException {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		when(solicitudRepository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.EN_REVISION);
		decanoService.responderSolicitud(respuesta, FACULTAD);
		assertEquals(SolicitudEstado.EN_REVISION, solicitud.getEstado());
	}

	@Test
	void testResponderSolicitudEstadoDesconocido() {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(null);
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
		assertTrue(ex.getMessage().contains("Estado de respuesta inválido"));
	}

	@Test
	void testResponderSolicitudAprobadaFueraCalendario() {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().plusDays(5), LocalDate.now().plusDays(10))) {
			SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
			assertTrue(ex.getMessage().contains("fuera del calendario"));
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testResponderSolicitudAprobadaGrupoDestinoCompleto() {
		Grupo grupoDestino = spy(buildGrupo("GRP2", materiaBase, 1, List.of("EST5")));
		when(grupoDestino.isEstaCompleto()).thenReturn(true);
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, grupoDestino);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))) {
			SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
			assertTrue(ex.getMessage().contains("ya está lleno"));
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testResponderSolicitudAprobadaPlazoVencido() {
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, null);
		solicitud.setFechaCreacion(LocalDateTime.now().minusDays(10));
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))) {
			SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
			assertTrue(ex.getMessage().contains("ha vencido"));
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testResponderSolicitudAprobadaCruceHorarios() {
		Grupo grupoDestino = spy(buildGrupo("GRP2", materiaBase, 30, List.of()));
		when(grupoDestino.isEstaCompleto()).thenReturn(false);
		when(grupoDestino.tieneCruceDeHorario(any())).thenReturn(true);
		Estudiante estudiante = spy(buildEstudiante("EST1", FACULTAD_ENUM, grupoBase));
		when(estudiante.getGruposExcluyendo(any())).thenReturn(List.of(grupoBase));
		when(usuarioRepository.findById("EST1")).thenReturn(Optional.of(estudiante));
		Solicitud solicitud = buildSolicitud("SOL1", grupoBase, grupoDestino);
		when(solicitudRepository.findById("SOL1")).thenReturn(Optional.of(solicitud));
		RespuestaSolicitudDTO respuesta = new RespuestaSolicitudDTO();
		respuesta.setSolicitudId("SOL1");
		respuesta.setNuevoEstado(SolicitudEstado.APROBADA);
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))) {
			SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.responderSolicitud(respuesta, FACULTAD));
			assertTrue(ex.getMessage().contains("cruce de horarios"));
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testConfigurarCalendarioAcademicoExitoso() throws Exception {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO(LocalDate.now(), LocalDate.now().plusDays(5));
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5))) {
			decanoService.configurarCalendarioAcademico(dto, FACULTAD);
			assertEquals(dto.getFechaInicio(), CalendarioAcademico.INSTANCIA.getFechaInicio());
		}
	}

	@Test
	void testConfigurarCalendarioAcademicoFechasInvalidas() {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO(LocalDate.now().plusDays(5), LocalDate.now());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.configurarCalendarioAcademico(dto, FACULTAD));
		assertTrue(ex.getMessage().contains("Las fechas del calendario"));
	}

	@Test
	void testConfigurarPlazoSolicitudesExitoso() throws Exception {
		try (AutoCloseable calendario = overrideCalendario(LocalDate.now().minusDays(5), LocalDate.now().plusDays(10));
		     AutoCloseable plazo = overridePlazo(LocalDate.now(), LocalDate.now())) {
			PlazoSolicitudesDTO dto = new PlazoSolicitudesDTO(LocalDate.now(), LocalDate.now().plusDays(3));
			decanoService.configurarPlazoSolicitudes(dto, FACULTAD);
			assertEquals(dto.getFechaFin(), PlazoSolicitudes.INSTANCIA.getFechaFin());
		}
	}

	@Test
	void testConfigurarPlazoSolicitudesFechasInvalidas() {
		PlazoSolicitudesDTO dto = new PlazoSolicitudesDTO(LocalDate.now().plusDays(3), LocalDate.now());
		SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.configurarPlazoSolicitudes(dto, FACULTAD));
		assertTrue(ex.getMessage().contains("Las fechas del plazo"));
	}

	@Test
	void testConfigurarPlazoSolicitudesFueraCalendario() throws Exception {
		try (AutoCloseable ignored = overrideCalendario(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5))) {
			PlazoSolicitudesDTO dto = new PlazoSolicitudesDTO(LocalDate.now(), LocalDate.now().plusDays(2));
			SirhaException ex = assertThrows(SirhaException.class, () -> decanoService.configurarPlazoSolicitudes(dto, FACULTAD));
			assertTrue(ex.getMessage().contains("dentro del calendario"));
		}
	}

	@Test
	void testObtenerCalendarioAcademico() throws SirhaException {
		CalendarioAcademicoDTO dto = decanoService.obtenerCalendarioAcademico(FACULTAD);
		assertNotNull(dto.getFechaInicio());
	}

	@Test
	void testObtenerPlazoSolicitudes() throws SirhaException {
		PlazoSolicitudesDTO dto = decanoService.obtenerPlazoSolicitudes(FACULTAD);
		assertNotNull(dto.getFechaFin());
	}

	@Test
	void testEsPlazoSolicitudesActivo() throws Exception {
		try (AutoCloseable ignored = overridePlazo(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))) {
			assertTrue(decanoService.esPlazoSolicitudesActivo(FACULTAD));
		}
	}

	@Test
	void testEsPlazoSolicitudesNoActivo() throws Exception {
		try (AutoCloseable ignored = overridePlazo(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1))) {
			assertFalse(decanoService.esPlazoSolicitudesActivo(FACULTAD));
		}
	}

	@Test
	void testCalcularPorcentajeOcupacion() {
		Grupo grupo = buildGrupo("G", materiaBase, 10, List.of("E1", "E2", "E3", "E4", "E5"));
		assertEquals(50.0, decanoService.calcularPorcentajeOcupacion(grupo));
	}

	@Test
	void testTieneAlertaCapacidadTrue() {
		Grupo grupo = buildGrupo("G", materiaBase, 10, List.of("1","2","3","4","5","6","7","8","9"));
		assertTrue(decanoService.tieneAlertaCapacidad(grupo));
	}

	@Test
	void testTieneAlertaCapacidadFalse() {
		Grupo grupo = buildGrupo("G", materiaBase, 10, List.of("1","2"));
		assertFalse(decanoService.tieneAlertaCapacidad(grupo));
	}

	@Test
	void testConvertirGrupoAMonitoreoDTO() {
		Profesor profesor = new Profesor("Ana", "Diaz", "ana@example.com", "pwd", Rol.PROFESOR);
		profesor.setId("PROF1");
		grupoBase.setProfesor(profesor);
		grupoBase.setEstudiantesId(new ArrayList<>(List.of("EST1", "EST2")));
		MonitoreoGrupoDTO dto = decanoService.convertirGrupoAMonitoreoDTO(grupoBase);
		assertEquals("PROF1", dto.getProfesorId());
		assertEquals(grupoBase.getEstudiantesId(), dto.getEstudiantesId());
	}

	@Test
	void testMonitorearGruposPorFacultad() throws SirhaException {
		when(grupoRepository.findByMateria_Facultad(FACULTAD_ENUM)).thenReturn(List.of(grupoBase));
		List<MonitoreoGrupoDTO> dtos = decanoService.monitorearGruposPorFacultad(FACULTAD);
		assertEquals(1, dtos.size());
	}

	@Test
	void testObtenerGruposConAlerta() throws SirhaException {
		Grupo grupoAlerta = buildGrupo("GRP_ALERTA", materiaBase, 10, List.of("1","2","3","4","5","6","7","8","9"));
		when(grupoRepository.findByMateria_Facultad(FACULTAD_ENUM)).thenReturn(List.of(grupoBase, grupoAlerta));
		List<MonitoreoGrupoDTO> dtos = decanoService.obtenerGruposConAlerta(FACULTAD);
		assertEquals(1, dtos.size());
		assertEquals("GRP_ALERTA", dtos.get(0).getGrupoId());
	}

	@Test
	void testObtenerEstadisticasAlertas() throws SirhaException {
		Grupo grupoAlerta = buildGrupo("GRP_ALERTA", materiaBase, 10, List.of("1","2","3","4","5","6","7","8","9"));
		when(grupoRepository.findByMateria_Facultad(FACULTAD_ENUM)).thenReturn(List.of(grupoBase, grupoAlerta));
		Map<String, Long> estadisticas = decanoService.obtenerEstadisticasAlertas(FACULTAD);
		assertTrue(estadisticas.containsKey("NORMAL"));
	}

	private Grupo buildGrupo(String id, Materia materia, int capacidad, List<String> estudiantes) {
		Grupo grupo = new Grupo(materia, capacidad, new ArrayList<>());
		grupo.setId(id);
		grupo.setEstudiantesId(new ArrayList<>(estudiantes));
		grupo.setCantidadInscritos(estudiantes.size());
		grupo.setEstaCompleto(estudiantes.size() >= capacidad);
		return grupo;
	}

	private Estudiante buildEstudiante(String id, Facultad facultad, Grupo grupo) {
		Estudiante estudiante = new Estudiante();
		estudiante.setId(id);
		estudiante.setNombre("Carlos");
		estudiante.setApellido("Lopez");
		estudiante.setCarrera(facultad);
		Semestre semestre = new Semestre();
		RegistroMaterias registro = new RegistroMaterias(grupo);
		semestre.setRegistros(new ArrayList<>(List.of(registro)));
		estudiante.setSemestres(new ArrayList<>(List.of(semestre)));
		return estudiante;
	}

	private Solicitud buildSolicitud(String id, Grupo grupoProblema, Grupo grupoDestino) {
		Solicitud solicitud = new Solicitud();
		solicitud.setId(id);
		solicitud.setEstudianteId("EST1");
		solicitud.setFacultad(FACULTAD_ENUM);
		solicitud.setEstado(SolicitudEstado.PENDIENTE);
		solicitud.setGrupoProblema(grupoProblema);
		solicitud.setGrupoDestino(grupoDestino);
		solicitud.setFechaCreacion(LocalDateTime.now().minusDays(1));
		solicitud.setNumeroRadicado("RAD1");
		return solicitud;
	}

	private AutoCloseable overrideCalendario(LocalDate inicio, LocalDate fin) {
		CalendarioAcademico calendario = CalendarioAcademico.INSTANCIA;
		LocalDate originalInicio = calendario.getFechaInicio();
		LocalDate originalFin = calendario.getFechaFin();
		calendario.setFechaInicio(inicio);
		calendario.setFechaFin(fin);
		return () -> {
			calendario.setFechaInicio(originalInicio);
			calendario.setFechaFin(originalFin);
		};
	}

	private AutoCloseable overridePlazo(LocalDate inicio, LocalDate fin) {
		PlazoSolicitudes plazo = PlazoSolicitudes.INSTANCIA;
		LocalDate originalInicio = plazo.getFechaInicio();
		LocalDate originalFin = plazo.getFechaFin();
		plazo.setFechaInicio(inicio);
		plazo.setFechaFin(fin);
		return () -> {
			plazo.setFechaInicio(originalInicio);
			plazo.setFechaFin(originalFin);
		};
	}
}
