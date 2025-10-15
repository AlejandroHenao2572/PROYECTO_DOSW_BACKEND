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
        // Este método se deja vacío intencionalmente para verificar que el contexto de Spring Boot se carga correctamente.
        // No requiere implementación porque su propósito es detectar errores de configuración al iniciar la aplicación.
    }

	@Test
	void testMainMethod() {
		// Ejecuta el método main y verifica que no lanza excepción
		assertDoesNotThrow(() -> {
			ProyectoSirhaApplication.main(new String[]{});
		});
	}

}
