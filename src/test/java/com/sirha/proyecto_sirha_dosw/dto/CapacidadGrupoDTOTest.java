package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CapacidadGrupoDTOTest {

	@Test
	void testConstructorPorDefecto() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO();
		assertNull(dto.getGrupoId());
		assertNull(dto.getMateriaId());
		assertNull(dto.getMateriaNombre());
		assertNull(dto.getMateriaAcronimo());
		assertEquals(0, dto.getCapacidadMaxima());
		assertEquals(0, dto.getEstudiantesInscritos());
		assertEquals(0.0, dto.getPorcentajeOcupacion());
		assertEquals(0, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
		assertNull(dto.getProfesorId());
		assertNull(dto.getProfesorNombre());
	}

	@Test
	void testConstructorConParametros() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO("G1", "M1", "Matemáticas", "MAT101", 30, 25);
		assertEquals("G1", dto.getGrupoId());
		assertEquals("M1", dto.getMateriaId());
		assertEquals("Matemáticas", dto.getMateriaNombre());
		assertEquals("MAT101", dto.getMateriaAcronimo());
		assertEquals(30, dto.getCapacidadMaxima());
		assertEquals(25, dto.getEstudiantesInscritos());
		assertEquals(83.33, dto.getPorcentajeOcupacion());
		assertEquals(5, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
	}

	@Test
	void testSettersAndGetters() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO();
		dto.setGrupoId("G2");
		dto.setMateriaId("M2");
		dto.setMateriaNombre("Física");
		dto.setMateriaAcronimo("FIS201");
		dto.setCapacidadMaxima(40);
		dto.setEstudiantesInscritos(10);
		dto.setPorcentajeOcupacion(25.0);
		dto.setCuposDisponibles(30);
		dto.setEstaCompleto(false);
		dto.setProfesorId("P1");
		dto.setProfesorNombre("Prof. López");

		assertEquals("G2", dto.getGrupoId());
		assertEquals("M2", dto.getMateriaId());
		assertEquals("Física", dto.getMateriaNombre());
		assertEquals("FIS201", dto.getMateriaAcronimo());
		assertEquals(40, dto.getCapacidadMaxima());
		assertEquals(10, dto.getEstudiantesInscritos());
		assertEquals(25.0, dto.getPorcentajeOcupacion());
		assertEquals(30, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
		assertEquals("P1", dto.getProfesorId());
		assertEquals("Prof. López", dto.getProfesorNombre());
	}

	@Test
	void testActualizarCalculosCompleto() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO("G3", "M3", "Química", "QUI301", 20, 20);
		assertTrue(dto.isEstaCompleto());
		assertEquals(0, dto.getCuposDisponibles());
		assertEquals(100.0, dto.getPorcentajeOcupacion());
	}

	@Test
	void testActualizarCalculosCapacidadCero() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO("G4", "M4", "Historia", "HIS101", 0, 10);
		assertEquals(0.0, dto.getPorcentajeOcupacion());
		assertEquals(-10, dto.getCuposDisponibles());
		assertTrue(dto.isEstaCompleto());
	}

	@Test
	void testSetCapacidadMaximaActualizaCalculos() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO("G5", "M5", "Geografía", "GEO201", 10, 9);
		assertFalse(dto.isEstaCompleto());
		dto.setCapacidadMaxima(9);
		assertTrue(dto.isEstaCompleto());
	}

	@Test
	void testSetEstudiantesInscritosActualizaCalculos() {
		CapacidadGrupoDTO dto = new CapacidadGrupoDTO("G6", "M6", "Arte", "ART101", 10, 5);
		assertFalse(dto.isEstaCompleto());
		dto.setEstudiantesInscritos(10);
		assertTrue(dto.isEstaCompleto());
	}

}
