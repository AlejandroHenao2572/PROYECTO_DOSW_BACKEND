package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.GrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.CapacidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
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
class GrupoServiceTest {
	@Test
	void testConvertirGrupoACapacidadDTOConProfesor() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		Profesor profesor = mock(Profesor.class);
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		when(grupo.getProfesor()).thenReturn(profesor);
		when(profesor.getId()).thenReturn("PROF1");
		when(profesor.getNombre()).thenReturn("Juan");
		when(profesor.getApellido()).thenReturn("Perez");
		// Usar reflection para invocar el método privado
		CapacidadGrupoDTO dto = invokeConvertirGrupoACapacidadDTO(grupoService, grupo);
		assertEquals("PROF1", dto.getProfesorId());
		assertEquals("Juan Perez", dto.getProfesorNombre());
	}

	// Helper para invocar método privado
	private CapacidadGrupoDTO invokeConvertirGrupoACapacidadDTO(GrupoService service, Grupo grupo) {
		try {
			java.lang.reflect.Method method = GrupoService.class.getDeclaredMethod("convertirGrupoACapacidadDTO", Grupo.class);
			method.setAccessible(true);
			return (CapacidadGrupoDTO) method.invoke(service, grupo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Mock
	private GrupoRepository grupoRepository;
	@Mock
	private MateriaRepository materiaRepository;
	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private GrupoService grupoService;

	private GrupoDTO getGrupoDTO() {
		GrupoDTO dto = new GrupoDTO();
		dto.setMateriaId("MAT1");
		dto.setProfesorId("PROF1");
		dto.setCapacidad(30);
	Horario horario = mock(Horario.class);
	dto.setHorarios(List.of(horario));
		return dto;
	}

	@Test
	void testGetAllGrupos() {
		List<Grupo> grupos = List.of(mock(Grupo.class), mock(Grupo.class));
		when(grupoRepository.findAll()).thenReturn(grupos);
		List<Grupo> result = grupoService.getAllGrupos();
		assertEquals(2, result.size());
	}

	@Test
	void testGetGrupoById() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		Optional<Grupo> result = grupoService.getGrupoById("G1");
		assertTrue(result.isPresent());
	}

	@Test
	void testCreateGrupoExitoso() throws SirhaException {
		GrupoDTO dto = getGrupoDTO();
		Materia materia = mock(Materia.class);
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.of(materia));
		Profesor profesor = mock(Profesor.class);
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(profesor));
		Grupo grupoMock = mock(Grupo.class);
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupoMock);
		Grupo result = grupoService.createGrupo(dto);
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testCreateGrupoMateriaNoEncontrada() {
		GrupoDTO dto = getGrupoDTO();
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.createGrupo(dto));
		assertTrue(ex.getMessage().contains(SirhaException.MATERIA_NO_ENCONTRADA));
	}

	@Test
	void testCreateGrupoProfesorNoEncontrado() {
		GrupoDTO dto = getGrupoDTO();
		Materia materia = mock(Materia.class);
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.of(materia));
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(mock(Usuario.class)));
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.createGrupo(dto));
		assertTrue(ex.getMessage().contains(SirhaException.PROFESOR_NO_ENCONTRADO));
	}

	@Test
	void testCreateGrupoFaltanDatos() {
		GrupoDTO dto = getGrupoDTO();
		dto.setMateriaId(null);
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.createGrupo(dto));
		assertTrue(ex.getMessage().contains(SirhaException.ERROR_FALTAN_DATOS));
	}

	@Test
	void testUpdateGrupoExitoso() throws SirhaException {
		GrupoDTO dto = getGrupoDTO();
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		Materia materia = mock(Materia.class);
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.of(materia));
		Profesor profesor = mock(Profesor.class);
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(profesor));
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupo);
		when(grupo.getCantidadInscritos()).thenReturn(0);
		Grupo result = grupoService.updateGrupo("G1", dto);
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testUpdateGrupoNoEncontrado() {
		GrupoDTO dto = getGrupoDTO();
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.updateGrupo("G1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testUpdateGrupoMateriaNoEncontrada() {
		GrupoDTO dto = getGrupoDTO();
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.updateGrupo("G1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.MATERIA_NO_ENCONTRADA));
	}

	@Test
	void testUpdateGrupoProfesorNoEncontrado() {
		GrupoDTO dto = getGrupoDTO();
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		Materia materia = mock(Materia.class);
		when(materiaRepository.findById("MAT1")).thenReturn(Optional.of(materia));
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(mock(Usuario.class)));
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.updateGrupo("G1", dto));
		assertTrue(ex.getMessage().contains(SirhaException.PROFESOR_NO_ENCONTRADO));
	}

	@Test
	void testDeleteGrupoExitoso() throws SirhaException {
		when(grupoRepository.existsById("G1")).thenReturn(true);
		doNothing().when(grupoRepository).deleteById("G1");
		assertDoesNotThrow(() -> grupoService.deleteGrupo("G1"));
		verify(grupoRepository).deleteById("G1");
	}

	@Test
	void testDeleteGrupoNoEncontrado() {
		when(grupoRepository.existsById("G1")).thenReturn(false);
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.deleteGrupo("G1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testGetGruposByMateria() {
		List<Grupo> grupos = List.of(mock(Grupo.class));
		when(grupoRepository.findByMateria_Id("MAT1")).thenReturn(grupos);
		List<Grupo> result = grupoService.getGruposByMateria("MAT1");
		assertEquals(1, result.size());
	}

	@Test
	void testGetGruposByProfesor() {
		List<Grupo> grupos = List.of(mock(Grupo.class));
		when(grupoRepository.findByProfesor_Id("PROF1")).thenReturn(grupos);
		List<Grupo> result = grupoService.getGruposByProfesor("PROF1");
		assertEquals(1, result.size());
	}

	@Test
	void testGetGruposDisponibles() {
		List<Grupo> grupos = List.of(mock(Grupo.class));
		when(grupoRepository.findByEstaCompletoFalse()).thenReturn(grupos);
		List<Grupo> result = grupoService.getGruposDisponibles();
		assertEquals(1, result.size());
	}

	@Test
	void testGetGruposDisponiblesPorMateria() {
		Grupo grupo = mock(Grupo.class);
		when(grupo.isEstaCompleto()).thenReturn(false);
		List<Grupo> grupos = List.of(grupo);
		when(grupoRepository.findByMateria_Id("MAT1")).thenReturn(grupos);
		List<Grupo> result = grupoService.getGruposDisponiblesPorMateria("MAT1");
		assertEquals(1, result.size());
	}

	@Test
	void testAddEstudianteToGrupoExitoso() throws SirhaException {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.isEstaCompleto()).thenReturn(false);
		when(grupo.getEstudiantesId()).thenReturn(new ArrayList<>());
		when(usuarioRepository.existsById("E1")).thenReturn(true);
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupo);
		Grupo result = grupoService.addEstudianteToGrupo("G1", "E1");
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testAddEstudianteToGrupoGrupoNoEncontrado() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.addEstudianteToGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testAddEstudianteToGrupoCompleto() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.isEstaCompleto()).thenReturn(true);
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.addEstudianteToGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_COMPLETO));
	}

	@Test
	void testAddEstudianteToGrupoYaInscrito() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.isEstaCompleto()).thenReturn(false);
		when(grupo.getEstudiantesId()).thenReturn(List.of("E1"));
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.addEstudianteToGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.ESTUDIANTE_YA_INSCRITO));
	}

	@Test
	void testAddEstudianteToGrupoEstudianteNoEncontrado() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.isEstaCompleto()).thenReturn(false);
		when(grupo.getEstudiantesId()).thenReturn(new ArrayList<>());
		when(usuarioRepository.existsById("E1")).thenReturn(false);
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.addEstudianteToGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.ESTUDIANTE_NO_ENCONTRADO));
	}

	@Test
	void testRemoveEstudianteFromGrupoExitoso() throws SirhaException {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.getEstudiantesId()).thenReturn(List.of("E1"));
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupo);
		Grupo result = grupoService.removeEstudianteFromGrupo("G1", "E1");
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testRemoveEstudianteFromGrupoGrupoNoEncontrado() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.removeEstudianteFromGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testRemoveEstudianteFromGrupoNoInscrito() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.getEstudiantesId()).thenReturn(new ArrayList<>());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.removeEstudianteFromGrupo("G1", "E1"));
		assertTrue(ex.getMessage().contains(SirhaException.ESTUDIANTE_NO_INSCRITO));
	}

	@Test
	void testObtenerCapacidadGrupoExitoso() throws SirhaException {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		CapacidadGrupoDTO result = grupoService.obtenerCapacidadGrupo("G1");
		assertNotNull(result);
		assertEquals("G1", result.getGrupoId());
	}

	@Test
	void testObtenerCapacidadGrupoNoEncontrado() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.obtenerCapacidadGrupo("G1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testObtenerCapacidadTodosLosGrupos() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupoRepository.findAll()).thenReturn(List.of(grupo));
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		List<CapacidadGrupoDTO> result = grupoService.obtenerCapacidadTodosLosGrupos();
		assertEquals(1, result.size());
	}

	@Test
	void testObtenerCapacidadGruposPorMateria() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupoRepository.findByMateria_Id("MAT1")).thenReturn(List.of(grupo));
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		List<CapacidadGrupoDTO> result = grupoService.obtenerCapacidadGruposPorMateria("MAT1");
		assertEquals(1, result.size());
	}

	@Test
	void testAsignarProfesorAGrupoExitoso() throws SirhaException {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		Profesor profesor = mock(Profesor.class);
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(profesor));
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupo);
		Grupo result = grupoService.asignarProfesorAGrupo("G1", "PROF1");
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testAsignarProfesorAGrupoGrupoNoEncontrado() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.asignarProfesorAGrupo("G1", "PROF1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testAsignarProfesorAGrupoProfesorNoEncontrado() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.asignarProfesorAGrupo("G1", "PROF1"));
		assertTrue(ex.getMessage().contains(SirhaException.PROFESOR_NO_ENCONTRADO));
	}

	@Test
	void testAsignarProfesorAGrupoUsuarioNoProfesor() {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		Usuario usuario = mock(Usuario.class);
		when(usuarioRepository.findById("PROF1")).thenReturn(Optional.of(usuario));
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.asignarProfesorAGrupo("G1", "PROF1"));
		assertTrue(ex.getMessage().contains("El usuario especificado no es un profesor"));
	}

	@Test
	void testRemoverProfesorDeGrupoExitoso() throws SirhaException {
		Grupo grupo = mock(Grupo.class);
		when(grupoRepository.findById("G1")).thenReturn(Optional.of(grupo));
		when(grupoRepository.save(any(Grupo.class))).thenReturn(grupo);
		Grupo result = grupoService.removerProfesorDeGrupo("G1");
		assertNotNull(result);
		verify(grupoRepository).save(any(Grupo.class));
	}

	@Test
	void testRemoverProfesorDeGrupoNoEncontrado() {
		when(grupoRepository.findById("G1")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> grupoService.removerProfesorDeGrupo("G1"));
		assertTrue(ex.getMessage().contains(SirhaException.GRUPO_NO_ENCONTRADO));
	}

	@Test
	void testObtenerGruposConCapacidadPorProfesor() {
		Grupo grupo = mock(Grupo.class);
		Materia materia = mock(Materia.class);
		when(grupoRepository.findByProfesor_Id("PROF1")).thenReturn(List.of(grupo));
		when(grupo.getMateria()).thenReturn(materia);
		when(grupo.getId()).thenReturn("G1");
		when(materia.getId()).thenReturn("MAT1");
		when(materia.getNombre()).thenReturn("Matemáticas");
		when(materia.getAcronimo()).thenReturn("MAT101");
		when(grupo.getCapacidad()).thenReturn(30);
		when(grupo.getCantidadInscritos()).thenReturn(10);
		List<CapacidadGrupoDTO> result = grupoService.obtenerGruposConCapacidadPorProfesor("PROF1");
		assertEquals(1, result.size());
	}
}
