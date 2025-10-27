package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	// Métodos auxiliares para reducir complejidad en los tests
	private Estudiante crearEstudianteBase() {
		Estudiante usuario = new Estudiante("Juan", "Perez", "juan@example.com", "1234", Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS);
		usuario.setId("1");
		return usuario;
	}

	private UsuarioDTO crearUsuarioDTOBase() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setNombre("Carlos");
		dto.setApellido("Lopez");
		dto.setEmail("nuevo@example.com");
		dto.setPassword("5678");
		dto.setRol(Rol.PROFESOR.name());
		return dto;
	}

	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private CarreraRepository carreraRepository;
	@Mock
	private SolicitudRepository solicitudRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioService usuarioService;

	@BeforeEach
	void injectEncoder() {
		ReflectionTestUtils.setField(usuarioService, "passwordEncoder", passwordEncoder);
	}

	private UsuarioDTO getUsuarioDTO() {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNombre("Juan");
		dto.setApellido("Perez");
		dto.setEmail("juan@example.com");
		dto.setPassword("1234");
		dto.setRol(Rol.ESTUDIANTE.name());
		dto.setFacultad(Facultad.values()[0].name());
		return dto;
	}

	@Test
	void testRegistrarExitoso() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		// Email autogenerado será: juan.perez-p@mail.escuelaing.edu.co (password: "1234")
		// Formato: nombre.apellido-primeraLetraApellido@mail.escuelaing.edu.co
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals("hashed", registrado.getPassword());
		assertEquals(emailEsperado, registrado.getEmail());
	}

	@Test
	void testRegistrarDecanoDuplicado() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("DECANO");
		
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		Decano decanoExistente = new Decano();
		decanoExistente.setFacultad(Facultad.valueOf(dto.getFacultad()));
		when(usuarioRepository.findByRol(Rol.DECANO)).thenReturn(List.of(decanoExistente));
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.DECANO_YA_EXISTE));
	}

	@Test
	void testRegistrarFacultadInvalida() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setFacultad("NO_EXISTE");
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.FACULTAD_ERROR));
	}

	@Test
	void testVerificarFacultadCarreraNoEncontrada() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("DECANO");
		
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.empty());
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.CARRERA_NO_ENCONTRADA));
	}


	@Test
	void testRegistrarEmailYaRegistrado() {
		UsuarioDTO dto = getUsuarioDTO();
		// Email autogenerado será: juan.perez-p@mail.escuelaing.edu.co (password: "1234")
		// Formato: nombre.apellido-primeraLetraApellido@mail.escuelaing.edu.co
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		
		// Mock de la carrera (necesario para que el flujo llegue a la validación de email)
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		// Mock del email ya existente
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.of(mock(Usuario.class)));
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		// Verifica que el mensaje de la excepción sea exactamente el esperado
		assertEquals(SirhaException.EMAIL_YA_REGISTRADO, ex.getMessage());
	}

	@Test
	void testRegistrarRolInvalido() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("NO_EXISTE");
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.ROL_INVALIDO));
	}

	@Test
	void testActualizarUsuarioNoEncontrado() {
		UsuarioDTO dto = getUsuarioDTO();
		when(usuarioRepository.findById("1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.actualizarUsuario("1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.USUARIO_NO_ENCONTRADO));
	}

	@Test
	void testActualizarUsuarioExitoso() throws SirhaException {
		UsuarioDTO dto = crearUsuarioDTOBase();
		Estudiante usuario = crearEstudianteBase();
		mockActualizarUsuario(usuario, dto);
		Usuario actualizado = usuarioService.actualizarUsuario("1", dto);
		assertEquals("Carlos", actualizado.getNombre());
		assertEquals(Rol.PROFESOR, actualizado.getRol());
	}

	private void mockActualizarUsuario(Estudiante usuario, UsuarioDTO dto) {
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
	}

	@Test
	void testEliminarUsuarioExitoso(){
		when(usuarioRepository.existsById("1")).thenReturn(true);
		doNothing().when(usuarioRepository).deleteById("1");
		assertDoesNotThrow(() -> usuarioService.eliminarUsuario("1"));
		verify(usuarioRepository).deleteById("1");
	}

	@Test
	void testEliminarUsuarioNoEncontrado() {
		when(usuarioRepository.existsById("1")).thenReturn(false);
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.eliminarUsuario("1"));
		assertTrue(ex.getMessage().contains(SirhaException.USUARIO_NO_ENCONTRADO));
	}

	@Test
	void testAutenticarUsuarioNoEncontrado() {
		when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.empty());
		boolean result = usuarioService.autenticar("juan@example.com", "1234");
		assertFalse(result);
	}

	@Test
	void testAutenticarUsuarioExitoso() {
		Usuario usuario = new Estudiante("Juan", "Perez", "juan@example.com", "hash", Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS);
		when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));
		when(passwordEncoder.matches("1234", "hash")).thenReturn(true);
		assertTrue(usuarioService.autenticar("juan@example.com", "1234"));
	}

	@Test
	void testListarUsuarios() {
	List<Usuario> usuarios = List.of(mock(Usuario.class), mock(Usuario.class));
		when(usuarioRepository.findAll()).thenReturn(usuarios);
		List<Usuario> result = usuarioService.listarUsuarios();
		assertEquals(2, result.size());
	}

	@Test
	void testObtenerPorId() {
	Usuario usuario = mock(Usuario.class);
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		Optional<Usuario> result = usuarioService.obtenerPorId("1");
		assertTrue(result.isPresent());
	}

	@Test
	void testObtenerPorEmail() {
	Usuario usuario = mock(Usuario.class);
		when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));
		Optional<Usuario> result = usuarioService.obtenerPorEmail("juan@example.com");
		assertTrue(result.isPresent());
	}

	@Test
	void testObtenerPorRol() {
	List<Usuario> usuarios = List.of(mock(Usuario.class));
		when(usuarioRepository.findByRol(Rol.ESTUDIANTE)).thenReturn(usuarios);
		List<Usuario> result = usuarioService.obtenerPorRol(Rol.ESTUDIANTE);
		assertEquals(1, result.size());
	}

	@Test
	void testObtenerPorNombre() {
	List<Usuario> usuarios = List.of(mock(Usuario.class));
		when(usuarioRepository.findByNombre("Juan")).thenReturn(usuarios);
		List<Usuario> result = usuarioService.obtenerPorNombre("Juan");
		assertEquals(1, result.size());
	}

	@Test
	void testObtenerPorApellido() {
	List<Usuario> usuarios = List.of(mock(Usuario.class));
		when(usuarioRepository.findByApellido("Perez")).thenReturn(usuarios);
		List<Usuario> result = usuarioService.obtenerPorApellido("Perez");
		assertEquals(1, result.size());
	}

	@Test
	void testObtenerPorNombreYApellido() {
	List<Usuario> usuarios = List.of(mock(Usuario.class));
		when(usuarioRepository.findByNombreAndApellido("Juan", "Perez")).thenReturn(usuarios);
		List<Usuario> result = usuarioService.obtenerPorNombreYApellido("Juan", "Perez");
		assertEquals(1, result.size());
	}

	@Test
	void testConsultarSolicitudesPorEstadoExitoso() throws SirhaException {
		List<Solicitud> solicitudes = List.of(new Solicitud());
		when(solicitudRepository.findByEstado(SolicitudEstado.PENDIENTE)).thenReturn(solicitudes);
		List<Solicitud> result = usuarioService.consultarSolicitudesPorEstado(SolicitudEstado.PENDIENTE);
		assertEquals(1, result.size());
	}

	@Test
	void testConsultarSolicitudesPorEstadoNulo() {
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.consultarSolicitudesPorEstado(null));
		assertTrue(ex.getMessage().contains("El estado de la solicitud no puede ser nulo"));
	}

	@Test
	void testConsultarTodasLasSolicitudes() {
		List<Solicitud> solicitudes = List.of(new Solicitud(), new Solicitud());
		when(solicitudRepository.findAll()).thenReturn(solicitudes);
		List<Solicitud> result = usuarioService.consultarTodasLasSolicitudes();
		assertEquals(2, result.size());
	}

	// ========== NUEVOS TESTS PARA COMPLETAR COVERTURA ==========

	// Tests de registro con diferentes roles
	@Test
	void testRegistrarAdministradorExitoso() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("ADMINISTRADOR");
		dto.setFacultad(null); // ADMINISTRADOR no debe tener facultad
		
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(Rol.ADMINISTRADOR, registrado.getRol());
		assertTrue(registrado instanceof Administrador);
	}

	@Test
	void testRegistrarProfesorExitoso() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("PROFESOR");
		dto.setFacultad(null); // PROFESOR no requiere facultad obligatoria
		
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(Rol.PROFESOR, registrado.getRol());
	}

	@Test
	void testRegistrarDecanoExitoso() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("DECANO");
		dto.setFacultad(Facultad.INGENIERIA_CIVIL.name());
		
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(carreraRepository.findByNombre(Facultad.INGENIERIA_CIVIL)).thenReturn(Optional.of(new Carrera()));
		when(usuarioRepository.findByRol(Rol.DECANO)).thenReturn(Collections.emptyList());
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(Rol.DECANO, registrado.getRol());
		assertEquals(Facultad.INGENIERIA_CIVIL, ((Decano) registrado).getFacultad());
	}

	@Test
	void testRegistrarEstudianteSinFacultad() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("ESTUDIANTE");
		dto.setFacultad(null); // ESTUDIANTE requiere facultad
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains("La facultad es obligatoria para el rol ESTUDIANTE"));
	}

	@Test
	void testRegistrarAdministradorConFacultad() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("ADMINISTRADOR");
		dto.setFacultad(Facultad.INGENIERIA_SISTEMAS.name()); // ADMINISTRADOR NO debe tener facultad
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains("El rol ADMINISTRADOR no debe tener facultad asignada"));
	}

	@Test
	void testRegistrarFacultadVacia() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("PROFESOR");
		dto.setFacultad(""); // Facultad vacía
		
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(Rol.PROFESOR, registrado.getRol());
	}

	// Tests de actualización con validaciones
	@Test
	void testActualizarUsuarioEmailDuplicado() {
		UsuarioDTO dto = crearUsuarioDTOBase();
		dto.setEmail("otro@example.com");
		
		Estudiante usuario = crearEstudianteBase();
		Usuario otroUsuario = new Estudiante("Otro", "Usuario", "otro@example.com", "pass", Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS);
		otroUsuario.setId("2");
		
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.findByEmail("otro@example.com")).thenReturn(Optional.of(otroUsuario));
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.actualizarUsuario("1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.EMAIL_YA_REGISTRADO));
	}

	@Test
	void testActualizarUsuarioConCamposNulos() throws SirhaException {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNombre(null);
		dto.setApellido(null);
		dto.setEmail(null);
		dto.setPassword(null);
		dto.setRol(null);
		
		Estudiante usuario = crearEstudianteBase();
		String nombreOriginal = usuario.getNombre();
		String apellidoOriginal = usuario.getApellido();
		
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario actualizado = usuarioService.actualizarUsuario("1", dto);
		// Los campos nulos no deben modificar el usuario
		assertEquals(nombreOriginal, actualizado.getNombre());
		assertEquals(apellidoOriginal, actualizado.getApellido());
	}

	@Test
	void testActualizarUsuarioConCamposVacios() throws SirhaException {
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNombre("   ");
		dto.setApellido("   ");
		dto.setEmail("   ");
		dto.setPassword("   ");
		dto.setRol("   ");
		
		Estudiante usuario = crearEstudianteBase();
		String nombreOriginal = usuario.getNombre();
		Rol rolOriginal = usuario.getRol();
		
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario actualizado = usuarioService.actualizarUsuario("1", dto);
		// Los campos vacíos (solo espacios) no deben modificar el usuario
		assertEquals(nombreOriginal, actualizado.getNombre());
		assertEquals(rolOriginal, actualizado.getRol());
	}

	@Test
	void testActualizarUsuarioRolInvalido() {
		UsuarioDTO dto = crearUsuarioDTOBase();
		dto.setRol("ROL_INEXISTENTE");
		
		Estudiante usuario = crearEstudianteBase();
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.actualizarUsuario("1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.ROL_INVALIDO));
	}

	@Test
	void testActualizarUsuarioMismoEmail() throws SirhaException {
		UsuarioDTO dto = crearUsuarioDTOBase();
		dto.setEmail("juan@example.com");
		
		Estudiante usuario = crearEstudianteBase();
		
		when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		// No debe lanzar excepción porque es el mismo usuario
		Usuario actualizado = usuarioService.actualizarUsuario("1", dto);
		assertEquals("juan@example.com", actualizado.getEmail());
	}

	// Tests de autenticación
	@Test
	void testAutenticarContraseñaIncorrecta() {
		Usuario usuario = new Estudiante("Juan", "Perez", "juan@example.com", "hash", Rol.ESTUDIANTE, Facultad.INGENIERIA_SISTEMAS);
		when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));
		when(passwordEncoder.matches("incorrecta", "hash")).thenReturn(false);
		
		boolean result = usuarioService.autenticar("juan@example.com", "incorrecta");
		assertFalse(result);
	}

	// Tests de generación de email con caracteres especiales
	@Test
	void testRegistrarConNombreConAcentos() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setNombre("María José");
		dto.setApellido("Pérez García");
		dto.setRol("ESTUDIANTE");
		
		// Email esperado: mariajose.perezgarcia-p@mail.escuelaing.edu.co
		String emailEsperado = "mariajose.perezgarcia-p@mail.escuelaing.edu.co";
		
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(emailEsperado, registrado.getEmail());
	}

	@Test
	void testRegistrarConNombreConEspaciosMultiples() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setNombre("  Carlos   Alberto  ");
		dto.setApellido("  López   Martínez  ");
		dto.setRol("ESTUDIANTE");
		
		// Email esperado: carlosalberto.lopezmartinez-l@mail.escuelaing.edu.co
		String emailEsperado = "carlosalberto.lopezmartinez-l@mail.escuelaing.edu.co";
		
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertEquals(emailEsperado, registrado.getEmail());
	}

	// Tests de generación de ID único
	@Test
	void testGenerarIdUnicoConColisiones() throws SirhaException {
		UsuarioDTO dto = getUsuarioDTO();
		String emailEsperado = "juan.perez-p@mail.escuelaing.edu.co";
		
		when(usuarioRepository.findByEmail(emailEsperado)).thenReturn(Optional.empty());
		when(carreraRepository.findByNombre(Facultad.valueOf(dto.getFacultad()))).thenReturn(Optional.of(new Carrera()));
		when(passwordEncoder.encode("1234")).thenReturn("hashed");
		
		// Simular que los primeros IDs ya existen
		when(usuarioRepository.existsById(anyString()))
			.thenReturn(true)  // Primera llamada: ID existe
			.thenReturn(true)  // Segunda llamada: ID existe
			.thenReturn(false); // Tercera llamada: ID disponible
		
		when(usuarioRepository.insert(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		
		Usuario registrado = usuarioService.registrar(dto);
		assertNotNull(registrado.getId());
		assertEquals(10, registrado.getId().length());
	}

	// Tests para casos edge de consultas
	@Test
	void testObtenerPorIdNoEncontrado() {
		when(usuarioRepository.findById("999")).thenReturn(Optional.empty());
		Optional<Usuario> result = usuarioService.obtenerPorId("999");
		assertFalse(result.isPresent());
	}

	@Test
	void testObtenerPorEmailNoEncontrado() {
		when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
		Optional<Usuario> result = usuarioService.obtenerPorEmail("noexiste@example.com");
		assertFalse(result.isPresent());
	}

	@Test
	void testObtenerPorRolListaVacia() {
		when(usuarioRepository.findByRol(Rol.PROFESOR)).thenReturn(Collections.emptyList());
		List<Usuario> result = usuarioService.obtenerPorRol(Rol.PROFESOR);
		assertTrue(result.isEmpty());
	}

	@Test
	void testObtenerPorNombreListaVacia() {
		when(usuarioRepository.findByNombre("NoExiste")).thenReturn(Collections.emptyList());
		List<Usuario> result = usuarioService.obtenerPorNombre("NoExiste");
		assertTrue(result.isEmpty());
	}

	@Test
	void testListarUsuariosVacio() {
		when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());
		List<Usuario> result = usuarioService.listarUsuarios();
		assertTrue(result.isEmpty());
	}

	@Test
	void testRegistrarDecanoSinFacultad() {
		UsuarioDTO dto = getUsuarioDTO();
		dto.setRol("DECANO");
		dto.setFacultad(null);
		
		SirhaException ex = assertThrows(SirhaException.class, () -> usuarioService.registrar(dto));
		assertTrue(ex.getMessage().contains("La facultad es obligatoria para el rol DECANO"));
	}
}
