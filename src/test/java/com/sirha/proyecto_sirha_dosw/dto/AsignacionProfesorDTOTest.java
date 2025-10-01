package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AsignacionProfesorDTOTest {

	@Test
	void testConstructorPorDefecto() {
		AsignacionProfesorDTO dto = new AsignacionProfesorDTO();
		assertNull(dto.getGrupoId());
		assertNull(dto.getProfesorId());
	}

	@Test
	void testConstructorConParametros() {
		AsignacionProfesorDTO dto = new AsignacionProfesorDTO("G1", "P1");
		assertEquals("G1", dto.getGrupoId());
		assertEquals("P1", dto.getProfesorId());
	}

	@Test
	void testSettersAndGetters() {
		AsignacionProfesorDTO dto = new AsignacionProfesorDTO();
		dto.setGrupoId("G2");
		dto.setProfesorId("P2");
		assertEquals("G2", dto.getGrupoId());
		assertEquals("P2", dto.getProfesorId());
	}

	@Test
	void testValoresNulos() {
		AsignacionProfesorDTO dto = new AsignacionProfesorDTO(null, null);
		assertNull(dto.getGrupoId());
		assertNull(dto.getProfesorId());
		dto.setGrupoId(null);
		dto.setProfesorId(null);
		assertNull(dto.getGrupoId());
		assertNull(dto.getProfesorId());
	}
}
