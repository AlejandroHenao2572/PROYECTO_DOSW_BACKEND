package com.sirha.proyecto_sirha_dosw.model.builder;

import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ImpCarreraBuilderTest {

	@Test
	void testBuildCarreraCompleta() {
		ImpCarreraBuilder builder = new ImpCarreraBuilder();
		Materia materia1 = new Materia();
		Materia materia2 = new Materia();
		Carrera carrera = builder
				.nombre(Facultad.valueOf("INGENIERIA_SISTEMAS"))
				.codigo("ING01")
				.duracionSemestres(10)
				.materias(List.of(materia1, materia2))
				.creditosTotales(160)
				.build();
	assertEquals(Facultad.valueOf("INGENIERIA_SISTEMAS"), carrera.getNombre());
		assertEquals("ING01", carrera.getCodigo());
		assertEquals(10, carrera.getDuracionSemestres());
		assertEquals(2, carrera.getMaterias().size());
		assertEquals(160, carrera.getCreditosTotales());
	}

	@Test
	void testBuildCarreraSinMaterias() {
		ImpCarreraBuilder builder = new ImpCarreraBuilder();
		Carrera carrera = builder
				.nombre(Facultad.valueOf("ADMINISTRACION"))
				.codigo("MED01")
				.duracionSemestres(12)
				.materias(null)
				.creditosTotales(200)
				.build();
	assertEquals(Facultad.valueOf("ADMINISTRACION"), carrera.getNombre());
		assertEquals("MED01", carrera.getCodigo());
		assertEquals(12, carrera.getDuracionSemestres());
		assertNull(carrera.getMaterias());
		assertEquals(200, carrera.getCreditosTotales());
	}

	@Test
	void testSettersFluidez() {
		ImpCarreraBuilder builder = new ImpCarreraBuilder();
	assertSame(builder, builder.nombre(Facultad.valueOf("INGENIERIA_SISTEMAS")));
		assertSame(builder, builder.codigo("X"));
		assertSame(builder, builder.duracionSemestres(8));
		assertSame(builder, builder.materias(List.of()));
		assertSame(builder, builder.creditosTotales(100));
	}
}
