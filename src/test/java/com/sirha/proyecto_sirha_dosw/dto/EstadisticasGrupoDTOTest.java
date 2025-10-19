package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadisticasGrupoDTOTest {

	@Test
	void testConstructorPorDefecto() {
		EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO();
		assertNull(dto.getGrupoId());
		assertNull(dto.getMateriaId());
		assertNull(dto.getMateriaNombre());
		assertNull(dto.getMateriaAcronimo());
		assertNull(dto.getFacultad());
		assertEquals(0, dto.getCapacidad());
		assertEquals(0, dto.getCantidadInscritos());
		assertEquals(0, dto.getCantidadSolicitudes());
		assertEquals(0.0, dto.getPorcentajeOcupacion());
	}

	@Test
	void testConstructorConParametros() {
		EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO("G1", "M1", "Matemáticas", "MAT101", "Ingeniería");
		assertEquals("G1", dto.getGrupoId());
		assertEquals("M1", dto.getMateriaId());
		assertEquals("Matemáticas", dto.getMateriaNombre());
		assertEquals("MAT101", dto.getMateriaAcronimo());
		assertEquals("Ingeniería", dto.getFacultad());
	}

	@Test
	void testSettersAndGetters() {
		EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO();
		dto.setGrupoId("G2");
		dto.setMateriaId("M2");
		dto.setMateriaNombre("Física");
		dto.setMateriaAcronimo("FIS201");
		dto.setFacultad("Ciencias");
		dto.setCapacidad(40);
		dto.setCantidadInscritos(10);
		dto.setCantidadSolicitudes(5L);
		dto.setPorcentajeOcupacion(25.0);

		assertEquals("G2", dto.getGrupoId());
		assertEquals("M2", dto.getMateriaId());
		assertEquals("Física", dto.getMateriaNombre());
		assertEquals("FIS201", dto.getMateriaAcronimo());
		assertEquals("Ciencias", dto.getFacultad());
		assertEquals(40, dto.getCapacidad());
		assertEquals(10, dto.getCantidadInscritos());
		assertEquals(5L, dto.getCantidadSolicitudes());
		assertEquals(25.0, dto.getPorcentajeOcupacion());
	}

	@Test
	void testPorcentajeOcupacionConCapacidad() {
		EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO();
		dto.setCapacidad(20);
		dto.setCantidadInscritos(10);
		assertEquals(50.0, dto.getPorcentajeOcupacion());
	}

	@Test
	void testPorcentajeOcupacionCapacidadCero() {
		EstadisticasGrupoDTO dto = new EstadisticasGrupoDTO();
		dto.setCapacidad(0);
		dto.setCantidadInscritos(10);
		assertEquals(0.0, dto.getPorcentajeOcupacion());
	}

}

