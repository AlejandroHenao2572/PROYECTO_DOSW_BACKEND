package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;

public class RespuestaSolicitudDTOTest {

	@Test
	void testConstructorPorDefecto() {
		RespuestaSolicitudDTO dto = new RespuestaSolicitudDTO();
		assertNull(dto.getSolicitudId());
		assertNull(dto.getNuevoEstado());
		assertNull(dto.getObservacionesRespuesta());
		assertNull(dto.getDecanoId());
	}

	@Test
	void testConstructorConParametros() {
		RespuestaSolicitudDTO dto = new RespuestaSolicitudDTO("S1", SolicitudEstado.APROBADA, "Todo correcto", "D1");
		assertEquals("S1", dto.getSolicitudId());
		assertEquals(SolicitudEstado.APROBADA, dto.getNuevoEstado());
		assertEquals("Todo correcto", dto.getObservacionesRespuesta());
		assertEquals("D1", dto.getDecanoId());
	}

	@Test
	void testSettersAndGetters() {
		RespuestaSolicitudDTO dto = new RespuestaSolicitudDTO();
		dto.setSolicitudId("S2");
		dto.setNuevoEstado(SolicitudEstado.RECHAZADA);
		dto.setObservacionesRespuesta("Faltan documentos");
		dto.setDecanoId("D2");

		assertEquals("S2", dto.getSolicitudId());
		assertEquals(SolicitudEstado.RECHAZADA, dto.getNuevoEstado());
		assertEquals("Faltan documentos", dto.getObservacionesRespuesta());
		assertEquals("D2", dto.getDecanoId());
	}

}

