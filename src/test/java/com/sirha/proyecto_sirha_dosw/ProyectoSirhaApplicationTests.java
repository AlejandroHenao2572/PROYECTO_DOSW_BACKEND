package com.sirha.proyecto_sirha_dosw;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProyectoSirhaApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMainMethod() {
		// Ejecuta el método main y verifica que no lanza excepción
		assertDoesNotThrow(() -> {
			ProyectoSirhaApplication.main(new String[]{});
		});
	}

}
