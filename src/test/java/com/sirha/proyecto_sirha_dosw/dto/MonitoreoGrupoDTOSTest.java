package com.sirha.proyecto_sirha_dosw.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class MonitoreoGrupoDTOSTest {
	@Test
	void testAlertaCapacidadAsignacion() {
		// Caso porcentajeOcupacion < 90.0
		MonitoreoGrupoDTO dtoNormal = new MonitoreoGrupoDTO("G8", "M8", "Educación Física", 100, 80);
		assertFalse(dtoNormal.isAlertaCapacidad());
		// Caso porcentajeOcupacion >= 90.0
		MonitoreoGrupoDTO dtoAlerta = new MonitoreoGrupoDTO("G9", "M9", "Música", 10, 9);
		assertTrue(dtoAlerta.isAlertaCapacidad());
		MonitoreoGrupoDTO dtoCritico = new MonitoreoGrupoDTO("G10", "M10", "Dibujo", 10, 10);
		assertTrue(dtoCritico.isAlertaCapacidad());
	}
	@Test
	void testPorcentajeOcupacionConCapacidadCero() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G7", "M7", "Biología", 0, 10);
		assertEquals(0.0, dto.getPorcentajeOcupacion());
		assertEquals("NORMAL", dto.getNivelAlerta());
	}

	@Test
	void testConstructorPorDefecto() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO();
		assertNull(dto.getGrupoId());
		assertNull(dto.getMateriaId());
		assertNull(dto.getMateriaNombre());
		assertEquals(0, dto.getCapacidad());
		assertEquals(0, dto.getCantidadInscritos());
		assertEquals(0.0, dto.getPorcentajeOcupacion());
		assertFalse(dto.isAlertaCapacidad());
		assertNull(dto.getNivelAlerta());
		assertNull(dto.getMensaje());
		assertNull(dto.getEstudiantesId());
		assertNull(dto.getProfesorId());
	}

	@Test
	void testConstructorConParametrosPrincipales() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G1", "M1", "Matemáticas", 30, 27);
		assertEquals("G1", dto.getGrupoId());
		assertEquals("M1", dto.getMateriaId());
		assertEquals("Matemáticas", dto.getMateriaNombre());
		assertEquals(30, dto.getCapacidad());
		assertEquals(27, dto.getCantidadInscritos());
		assertEquals(((double)27/30)*100.0, dto.getPorcentajeOcupacion());
		assertTrue(dto.isAlertaCapacidad());
		assertEquals("ADVERTENCIA", dto.getNivelAlerta());
		assertTrue(dto.getMensaje().contains("ALERTA"));
	}

	@Test
	void testSettersAndGetters() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO();
		dto.setGrupoId("G2");
		dto.setMateriaId("M2");
		dto.setMateriaNombre("Física");
		dto.setCapacidad(40);
		dto.setCantidadInscritos(10);
		dto.setPorcentajeOcupacion(25.0);
		dto.setAlertaCapacidad(false);
		dto.setNivelAlerta("NORMAL");
		dto.setMensaje("Mensaje de prueba");
		List<String> estudiantes = Arrays.asList("E1", "E2");
		dto.setEstudiantesId(estudiantes);
		dto.setProfesorId("P1");

		assertEquals("G2", dto.getGrupoId());
		assertEquals("M2", dto.getMateriaId());
		assertEquals("Física", dto.getMateriaNombre());
		assertEquals(40, dto.getCapacidad());
		assertEquals(10, dto.getCantidadInscritos());
		assertEquals(25.0, dto.getPorcentajeOcupacion());
		assertFalse(dto.isAlertaCapacidad());
		assertEquals("NORMAL", dto.getNivelAlerta());
		assertEquals("Mensaje de prueba", dto.getMensaje());
		assertEquals(estudiantes, dto.getEstudiantesId());
		assertEquals("P1", dto.getProfesorId());
	}

	@Test
	void testAlertaCritico() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G3", "M3", "Química", 20, 20);
		assertEquals("CRITICO", dto.getNivelAlerta());
		assertTrue(dto.getMensaje().contains("capacidad máxima"));
	}

	@Test
	void testAlertaNormal() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G4", "M4", "Historia", 50, 10);
		assertEquals("NORMAL", dto.getNivelAlerta());
		assertTrue(dto.getMensaje().contains("capacidad disponible"));
	}

	@Test
	void testSetCapacidadActualizaAlertas() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G5", "M5", "Geografía", 10, 9);
		assertEquals("ADVERTENCIA", dto.getNivelAlerta());
		dto.setCapacidad(20);
		assertEquals("NORMAL", dto.getNivelAlerta());
	}

	@Test
	void testSetCantidadInscritosActualizaAlertas() {
		MonitoreoGrupoDTO dto = new MonitoreoGrupoDTO("G6", "M6", "Arte", 10, 5);
		assertEquals("NORMAL", dto.getNivelAlerta());
		dto.setCantidadInscritos(10);
		assertEquals("CRITICO", dto.getNivelAlerta());
	}

    
}
