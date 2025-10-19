package com.sirha.proyecto_sirha_dosw.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudUtilTest {

	@Test
	void testGenerarNumeroRadicadoFormatoYUnicidad() {
		SolicitudUtil util = new SolicitudUtil();
		String radicado1 = util.generarNumeroRadicado();
		String radicado2 = util.generarNumeroRadicado();
		assertNotNull(radicado1);
		assertNotNull(radicado2);
		assertTrue(radicado1.matches("RAD-\\d{8}-\\d{4}"));
		assertTrue(radicado2.matches("RAD-\\d{8}-\\d{4}"));
		assertNotEquals(radicado1, radicado2);
	}

	@Test
	void testGenerarNumeroPrioridadSecuencialidad() {
		SolicitudUtil util = new SolicitudUtil();
		Integer prioridad1 = util.generarNumeroPrioridad();
		Integer prioridad2 = util.generarNumeroPrioridad();
		assertNotNull(prioridad1);
		assertNotNull(prioridad2);
		assertTrue(prioridad2 > prioridad1);
		assertEquals(prioridad1 + 1, prioridad2);
	}
}
