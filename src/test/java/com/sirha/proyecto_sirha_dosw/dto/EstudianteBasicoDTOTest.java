package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;

public class EstudianteBasicoDTOTest {

	@Test
	void testConstructorPorDefecto() {
		EstudianteBasicoDTO dto = new EstudianteBasicoDTO();
		assertNull(dto.getCodigo());
		assertNull(dto.getNombre());
		assertNull(dto.getApellido());
		assertNull(dto.getCarrera());
		assertEquals(0, dto.getSemestreActual());
	}

	@Test
	void testConstructorConParametros() {
		EstudianteBasicoDTO dto = new EstudianteBasicoDTO("2025001", "Juan", "Pérez", Facultad.INGENIERIA_SISTEMAS, 5);
		assertEquals("2025001", dto.getCodigo());
		assertEquals("Juan", dto.getNombre());
		assertEquals("Pérez", dto.getApellido());
		assertEquals(Facultad.INGENIERIA_SISTEMAS, dto.getCarrera());
		assertEquals(5, dto.getSemestreActual());
	}

	@Test
	void testSettersAndGetters() {
		EstudianteBasicoDTO dto = new EstudianteBasicoDTO();
		dto.setCodigo("2025002");
		dto.setNombre("Ana");
		dto.setApellido("Gómez");
		dto.setCarrera(Facultad.ADMINISTRACION);
		dto.setSemestreActual(3);

		assertEquals("2025002", dto.getCodigo());
		assertEquals("Ana", dto.getNombre());
		assertEquals("Gómez", dto.getApellido());
		assertEquals(Facultad.ADMINISTRACION, dto.getCarrera());
		assertEquals(3, dto.getSemestreActual());
	}

	@Test
	void testToString() {
		EstudianteBasicoDTO dto = new EstudianteBasicoDTO("2025003", "Luis", "Martínez", Facultad.INGENIERIA_CIVIL, 2);
		String str = dto.toString();
		assertTrue(str.contains("2025003"));
		assertTrue(str.contains("Luis"));
		assertTrue(str.contains("Martínez"));
		assertTrue(str.contains("INGENIERIA_CIVIL"));
		assertTrue(str.contains("2"));
	}

}

