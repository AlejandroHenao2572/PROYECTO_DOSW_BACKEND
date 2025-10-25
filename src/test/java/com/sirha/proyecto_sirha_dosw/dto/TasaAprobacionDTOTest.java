package com.sirha.proyecto_sirha_dosw.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class TasaAprobacionDTOTest {
	@Test
	void testSettersAndGettersTasas() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO();

		dto.setTasaAprobacion(75.5);
		assertEquals(75.5, dto.getTasaAprobacion());

		dto.setTasaRechazo(10.0);
		assertEquals(10.0, dto.getTasaRechazo());

		dto.setTasaPendientes(5.0);
		assertEquals(5.0, dto.getTasaPendientes());

		dto.setTasaEnRevision(9.9);
		assertEquals(9.9, dto.getTasaEnRevision());
	}

	@Test
	void testConstructorPorDefecto() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO();
		assertNotNull(dto.getFechaConsulta());
	}

	@Test
	void testConstructorConParametros() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO(100, 60, 20, 10, 10);
		assertEquals(100, dto.getTotalSolicitudes());
		assertEquals(60, dto.getSolicitudesAprobadas());
		assertEquals(20, dto.getSolicitudesRechazadas());
		assertEquals(10, dto.getSolicitudesPendientes());
		assertEquals(10, dto.getSolicitudesEnRevision());
		assertEquals(60.0, dto.getTasaAprobacion());
		assertEquals(20.0, dto.getTasaRechazo());
		assertEquals(10.0, dto.getTasaPendientes());
		assertEquals(10.0, dto.getTasaEnRevision());
	}

	@Test
	void testSettersAndGetters() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO();
		dto.setTotalSolicitudes(50);
		dto.setSolicitudesAprobadas(25);
		dto.setSolicitudesRechazadas(10);
		dto.setSolicitudesPendientes(10);
		dto.setSolicitudesEnRevision(5);
		dto.setFacultad("Ingeniería");
		dto.setTipoSolicitud("Cambio de grupo");
		LocalDateTime fecha = LocalDateTime.of(2025, 9, 30, 12, 0);
		dto.setFechaConsulta(fecha);

		assertEquals(50, dto.getTotalSolicitudes());
		assertEquals(25, dto.getSolicitudesAprobadas());
		assertEquals(10, dto.getSolicitudesRechazadas());
		assertEquals(10, dto.getSolicitudesPendientes());
		assertEquals(5, dto.getSolicitudesEnRevision());
		assertEquals("Ingeniería", dto.getFacultad());
		assertEquals("Cambio de grupo", dto.getTipoSolicitud());
		assertEquals(fecha, dto.getFechaConsulta());
	}

	@Test
	void testCalcularTasasConTotalCero() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO(0, 0, 0, 0, 0);
		assertEquals(0.0, dto.getTasaAprobacion());
		assertEquals(0.0, dto.getTasaRechazo());
		assertEquals(0.0, dto.getTasaPendientes());
		assertEquals(0.0, dto.getTasaEnRevision());
	}

	@Test
	void testActualizarTasas() {
		TasaAprobacionDTO dto = new TasaAprobacionDTO(100, 50, 30, 10, 10);
		dto.setSolicitudesAprobadas(60);
		dto.actualizarTasas();
		assertEquals(60.0, dto.getTasaAprobacion());
	}

}

