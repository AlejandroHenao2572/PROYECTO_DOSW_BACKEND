package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.dto.CalendarioAcademicoDTO;
import java.time.LocalDate;

public class CalendarioAcademicoDTOTest {

	@Test
	void testConstructorPorDefecto() {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO();
		assertNull(dto.getFechaInicio());
		assertNull(dto.getFechaFin());
	}

	@Test
	void testConstructorConParametros() {
		LocalDate inicio = LocalDate.of(2025, 1, 15);
		LocalDate fin = LocalDate.of(2025, 6, 15);
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO(inicio, fin);
		assertEquals(inicio, dto.getFechaInicio());
		assertEquals(fin, dto.getFechaFin());
	}

	@Test
	void testSettersAndGetters() {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO();
		LocalDate inicio = LocalDate.of(2025, 2, 1);
		LocalDate fin = LocalDate.of(2025, 7, 1);
		dto.setFechaInicio(inicio);
		dto.setFechaFin(fin);
		assertEquals(inicio, dto.getFechaInicio());
		assertEquals(fin, dto.getFechaFin());
	}

	@Test
	void testFechasValidas() {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO();
		LocalDate inicio = LocalDate.of(2025, 3, 1);
		LocalDate fin = LocalDate.of(2025, 3, 1);
		dto.setFechaInicio(inicio);
		dto.setFechaFin(fin);
		assertTrue(dto.esFechasValidas());

		dto.setFechaFin(LocalDate.of(2025, 4, 1));
		assertTrue(dto.esFechasValidas());

		dto.setFechaFin(LocalDate.of(2025, 2, 1));
		assertFalse(dto.esFechasValidas());
	}

	@Test
	void testFechasValidasNull() {
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO();
		assertFalse(dto.esFechasValidas());
		dto.setFechaInicio(LocalDate.of(2025, 5, 1));
		assertFalse(dto.esFechasValidas());
		dto.setFechaFin(null);
		assertFalse(dto.esFechasValidas());
	}

	@Test
	void testToString() {
		LocalDate inicio = LocalDate.of(2025, 1, 10);
		LocalDate fin = LocalDate.of(2025, 6, 10);
		CalendarioAcademicoDTO dto = new CalendarioAcademicoDTO(inicio, fin);
		String str = dto.toString();
		assertTrue(str.contains("2025-01-10"));
		assertTrue(str.contains("2025-06-10"));
	}

}

