package com.sirha.proyecto_sirha_dosw.dto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.dto.IndicadoresAvanceDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class IndicadoresAvanceDTOTest {
	@Test
	void testGetSetPorcentajesYEstado() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();

		dto.setPorcentajeAvanceGeneral(75.5);
		assertEquals(75.5, dto.getPorcentajeAvanceGeneral());

		dto.setPorcentajeAprobacion(60.0);
		assertEquals(60.0, dto.getPorcentajeAprobacion());

		dto.setPorcentajeProblemas(10.0);
		assertEquals(10.0, dto.getPorcentajeProblemas());

		dto.setPorcentajeCancelaciones(5.0);
		assertEquals(5.0, dto.getPorcentajeCancelaciones());

		dto.setEstadoGlobal(Semaforo.ROJO);
		assertEquals(Semaforo.ROJO, dto.getEstadoGlobal());

		dto.setDescripcionEstado("Alerta");
		assertEquals("Alerta", dto.getDescripcionEstado());
	}

	@Test
	void testConstructorPorDefecto() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		assertNotNull(dto.getFechaCalculo());
	}

	@Test
	void testConstructorIndividual() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO("E1", "Juan", "Pérez", Facultad.INGENIERIA_SISTEMAS);
		assertEquals("E1", dto.getEstudianteId());
		assertEquals("Juan", dto.getEstudianteNombre());
		assertEquals("Pérez", dto.getEstudianteApellido());
		assertEquals(Facultad.INGENIERIA_SISTEMAS, dto.getFacultad());
		assertEquals("INDIVIDUAL", dto.getTipoReporte());
	}

	@Test
	void testSettersAndGetters() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setEstudianteId("E2");
		dto.setEstudianteNombre("Ana");
		dto.setEstudianteApellido("Gómez");
		dto.setFacultad(Facultad.ADMINISTRACION);
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(8);
		dto.setMateriasEnProgreso(1);
		dto.setMateriasConProblemas(1);
		dto.setMateriasCanceladas(0);
		dto.setSemestreActual(3);
		dto.setCreditosAprobados(60);
		dto.setCreditosTotales(80);
		dto.setPromedioGeneral(4.5);
		LocalDateTime fecha = LocalDateTime.of(2025, 9, 30, 10, 0);
		dto.setFechaCalculo(fecha);
		dto.setTipoReporte("ESTADISTICAS_GLOBALES");
		Map<Semaforo, Long> distEstados = new HashMap<>();
		distEstados.put(Semaforo.VERDE, 5L);
		dto.setDistribucionEstados(distEstados);
		Map<Facultad, Double> avanceFac = new HashMap<>();
		avanceFac.put(Facultad.ADMINISTRACION, 75.0);
		dto.setAvancePorFacultad(avanceFac);

		assertEquals("E2", dto.getEstudianteId());
		assertEquals("Ana", dto.getEstudianteNombre());
		assertEquals("Gómez", dto.getEstudianteApellido());
		assertEquals(Facultad.ADMINISTRACION, dto.getFacultad());
		assertEquals(10, dto.getTotalMaterias());
		assertEquals(8, dto.getMateriasAprobadas());
		assertEquals(1, dto.getMateriasEnProgreso());
		assertEquals(1, dto.getMateriasConProblemas());
		assertEquals(0, dto.getMateriasCanceladas());
		assertEquals(3, dto.getSemestreActual());
		assertEquals(60, dto.getCreditosAprobados());
		assertEquals(80, dto.getCreditosTotales());
		assertEquals(4.5, dto.getPromedioGeneral());
		assertEquals(fecha, dto.getFechaCalculo());
		assertEquals("ESTADISTICAS_GLOBALES", dto.getTipoReporte());
		assertEquals(distEstados, dto.getDistribucionEstados());
		assertEquals(avanceFac, dto.getAvancePorFacultad());
	}

	@Test
	void testCalcularPorcentajesYEstadoGlobalVerde() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(9);
		dto.setMateriasConProblemas(0);
		dto.setMateriasCanceladas(0);
		// Estado global debe ser VERDE
		assertEquals(Semaforo.VERDE, dto.getEstadoGlobal());
		assertEquals("Excelente progreso académico", dto.getDescripcionEstado());
		assertEquals(90.0, dto.getPorcentajeAprobacion());
	}

	@Test
	void testCalcularPorcentajesYEstadoGlobalRojoPorProblemas() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(5);
		dto.setMateriasConProblemas(4);
		dto.setMateriasCanceladas(0);
		// Estado global debe ser ROJO por problemas
		assertEquals(Semaforo.ROJO, dto.getEstadoGlobal());
		assertTrue(dto.getDescripcionEstado().contains("crítica"));
	}

	@Test
	void testCalcularPorcentajesYEstadoGlobalRojoPorCancelaciones() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(5);
		dto.setMateriasConProblemas(0);
		dto.setMateriasCanceladas(3);
		// Estado global debe ser ROJO por cancelaciones
		assertEquals(Semaforo.ROJO, dto.getEstadoGlobal());
		assertTrue(dto.getDescripcionEstado().contains("crítica"));
	}

	@Test
	void testCalcularPorcentajesYEstadoGlobalAzul() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(7);
		dto.setMateriasConProblemas(1);
		dto.setMateriasCanceladas(0);
		// Estado global debe ser AZUL
		assertEquals(Semaforo.AZUL, dto.getEstadoGlobal());
		assertTrue(dto.getDescripcionEstado().contains("satisfactorio"));
	}

	@Test
	void testCalcularPorcentajesYEstadoGlobalRojoPorBajaAprobacion() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(10);
		dto.setMateriasAprobadas(5);
		dto.setMateriasConProblemas(0);
		dto.setMateriasCanceladas(0);
		// Estado global debe ser ROJO por baja aprobación
		assertEquals(Semaforo.ROJO, dto.getEstadoGlobal());
		assertTrue(dto.getDescripcionEstado().contains("Requiere atención"));
	}

	@Test
	void testCalcularPorcentajesConTotalMateriasCero() {
		IndicadoresAvanceDTO dto = new IndicadoresAvanceDTO();
		dto.setTotalMaterias(0);
		dto.setMateriasAprobadas(0);
		dto.setMateriasConProblemas(0);
		dto.setMateriasCanceladas(0);
		assertEquals(0.0, dto.getPorcentajeAvanceGeneral());
		assertEquals(0.0, dto.getPorcentajeAprobacion());
		assertEquals(0.0, dto.getPorcentajeProblemas());
		assertEquals(0.0, dto.getPorcentajeCancelaciones());
	}

}


