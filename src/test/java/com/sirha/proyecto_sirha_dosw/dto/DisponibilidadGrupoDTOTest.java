package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import java.util.Arrays;
import java.util.List;

class DisponibilidadGrupoDTOTest {

	@Test
	void testConstructorPorDefecto() {
		DisponibilidadGrupoDTO dto = new DisponibilidadGrupoDTO();
		assertNull(dto.getGrupoId());
		assertNull(dto.getNombreMateria());
		assertNull(dto.getAcronimoMateria());
		assertEquals(0, dto.getCapacidadMaxima());
		assertEquals(0, dto.getCantidadInscritos());
		assertEquals(0, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
		assertNull(dto.getHorarios());
		assertNull(dto.getListaEspera());
	}

	@Test
	void testConstructorCompleto() {
		List<Horario> horarios = Arrays.asList();
		List<String> listaEspera = Arrays.asList("E1", "E2");
		DisponibilidadGrupoDTO dto = new DisponibilidadGrupoDTO.Builder()
			.grupoId("G1")
			.nombreMateria("Matemáticas")
			.acronimoMateria("MAT101")
			.capacidadMaxima(30)
			.cantidadInscritos(25)
			.estaCompleto(false)
			.horarios(horarios)
			.listaEspera(listaEspera)
			.build();
		assertEquals("G1", dto.getGrupoId());
		assertEquals("Matemáticas", dto.getNombreMateria());
		assertEquals("MAT101", dto.getAcronimoMateria());
		assertEquals(30, dto.getCapacidadMaxima());
		assertEquals(25, dto.getCantidadInscritos());
		assertEquals(5, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
		assertEquals(horarios, dto.getHorarios());
		assertEquals(listaEspera, dto.getListaEspera());
	}

	@Test
	void testSettersAndGetters() {
		DisponibilidadGrupoDTO dto = new DisponibilidadGrupoDTO();
		dto.setGrupoId("G2");
		dto.setNombreMateria("Física");
		dto.setAcronimoMateria("FIS201");
		dto.setCapacidadMaxima(40);
		dto.setCantidadInscritos(10);
		dto.setEstaCompleto(false);
		List<Horario> horarios = Arrays.asList();
		dto.setHorarios(horarios);
		List<String> listaEspera = Arrays.asList("E3", "E4");
		dto.setListaEspera(listaEspera);

		assertEquals("G2", dto.getGrupoId());
		assertEquals("Física", dto.getNombreMateria());
		assertEquals("FIS201", dto.getAcronimoMateria());
		assertEquals(40, dto.getCapacidadMaxima());
		assertEquals(10, dto.getCantidadInscritos());
		assertEquals(30, dto.getCuposDisponibles());
		assertFalse(dto.isEstaCompleto());
		assertEquals(horarios, dto.getHorarios());
		assertEquals(listaEspera, dto.getListaEspera());
	}

	@Test
	void testCuposDisponiblesActualizaConSetters() {
		DisponibilidadGrupoDTO dto = new DisponibilidadGrupoDTO();
		dto.setCapacidadMaxima(20);
		dto.setCantidadInscritos(15);
		assertEquals(5, dto.getCuposDisponibles());
		dto.setCantidadInscritos(20);
		assertEquals(0, dto.getCuposDisponibles());
	}

}

