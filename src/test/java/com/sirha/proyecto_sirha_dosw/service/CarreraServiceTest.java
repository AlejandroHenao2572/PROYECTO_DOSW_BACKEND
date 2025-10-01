package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.CarreraDTO;
import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarreraServiceTest {

	@Mock
	private CarreraRepository carreraRepository;

	@Mock
	private MateriaRepository materiaRepository;

	@InjectMocks
	private CarreraService carreraService;

	@Test
	void testRegistrarCarreraExitoso() throws SirhaException {
		CarreraDTO dto = new CarreraDTO();
		dto.setCodigo("CARR01");
	dto.setNombre(Facultad.values()[0].name());
		dto.setDuracionSemestres(10);
		dto.setCreditosTotales(160);
		when(carreraRepository.findByCodigo("CARR01")).thenReturn(Optional.empty());
	when(carreraRepository.findByNombre(Facultad.valueOf(Facultad.values()[0].name()))).thenReturn(Optional.empty());
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.insert(any(Carrera.class))).thenReturn(carreraMock);
		Carrera result = carreraService.registrar(dto);
		assertNotNull(result);
		verify(carreraRepository).insert(any(Carrera.class));
	}

	@Test
	void testRegistrarCarreraCodigoYaExiste() {
		CarreraDTO dto = new CarreraDTO();
		dto.setCodigo("CARR01");
		dto.setNombre("INGENIERIA");
		when(carreraRepository.findByCodigo("CARR01")).thenReturn(Optional.of(new Carrera()));
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.CARRERA_YA_EXISTE));
	}

	@Test
	void testRegistrarCarreraFacultadInvalida() {
		CarreraDTO dto = new CarreraDTO();
		dto.setCodigo("CARR02");
		dto.setNombre("FACULTAD_INVALIDA");
		when(carreraRepository.findByCodigo("CARR02")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.registrar(dto));
		assertTrue(ex.getMessage().contains(SirhaException.FACULTAD_ERROR));
	}


	@Test
	void testAddMateriaExitoso() throws SirhaException {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
	dto.setFacultad(Facultad.values()[0]);
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.empty());
		when(materiaRepository.findByNombre("Matemáticas")).thenReturn(Optional.empty());
		Materia materiaMock = mock(Materia.class);
		when(materiaRepository.insert(any(Materia.class))).thenReturn(materiaMock);
		when(carreraRepository.save(any(Carrera.class))).thenReturn(carreraMock);
		Materia result = carreraService.addMateria(dto, "CARR01");
		assertNotNull(result);
		verify(materiaRepository).insert(any(Materia.class));
		verify(carreraRepository).save(any(Carrera.class));
	}

	@Test
	void testAddMateriaCarreraNoEncontrada() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
	dto.setFacultad(Facultad.values()[0]);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateria(dto, "CARR01"));
		assertTrue(ex.getMessage().contains(SirhaException.CARRERA_NO_ENCONTRADA));
	}

	@Test
	void testAddMateriaMateriaYaExistePorAcronimo() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
	dto.setFacultad(Facultad.values()[0]);
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.of(new Materia()));
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateria(dto, "CARR01"));
		assertTrue(ex.getMessage().contains(SirhaException.MATERIA_YA_EXISTE));
	}

	@Test
	void testAddMateriaMateriaYaExistePorNombre() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
	dto.setFacultad(Facultad.values()[0]);
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.empty());
		when(materiaRepository.findByNombre("Matemáticas")).thenReturn(Optional.of(new Materia()));
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateria(dto, "CARR01"));
		assertTrue(ex.getMessage().contains(SirhaException.MATERIA_YA_EXISTE));
	}

	@Test
	void testAddMateriaFacultadError() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
		dto.setFacultad(null);
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.empty());
		when(materiaRepository.findByNombre("Matemáticas")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateria(dto, "CARR01"));
		assertTrue(ex.getMessage().contains(SirhaException.FACULTAD_ERROR));
	}

	@Test
	void testAddMateriaByIdExitoso() throws SirhaException {
		Carrera carreraMock = mock(Carrera.class);
		Materia materiaMock = mock(Materia.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findById("MAT101")).thenReturn(Optional.of(materiaMock));
		when(carreraRepository.save(any(Carrera.class))).thenReturn(carreraMock);
		Carrera result = carreraService.addMateriaById("CARR01", "MAT101");
		assertNotNull(result);
		verify(carreraRepository).save(any(Carrera.class));
	}

	@Test
	void testAddMateriaByIdCarreraNoEncontrada() {
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateriaById("CARR01", "MAT101"));
		assertTrue(ex.getMessage().contains(SirhaException.CARRERA_NO_ENCONTRADA));
	}

	@Test
	void testAddMateriaByIdMateriaNoEncontrada() {
		Carrera carreraMock = mock(Carrera.class);
		when(carreraRepository.findById("CARR01")).thenReturn(Optional.of(carreraMock));
		when(materiaRepository.findById("MAT101")).thenReturn(Optional.empty());
		SirhaException ex = assertThrows(SirhaException.class, () -> carreraService.addMateriaById("CARR01", "MAT101"));
		assertTrue(ex.getMessage().contains(SirhaException.MATERIA_NO_ENCONTRADA));
	}
}
