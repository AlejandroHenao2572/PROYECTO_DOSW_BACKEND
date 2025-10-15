package com.sirha.proyecto_sirha_dosw.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioLoginDTOTest {

	@Test
	void testSetAndGetEmail() {
		UsuarioLoginDTO dto = new UsuarioLoginDTO();
		String email = "test@example.com";
		dto.setEmail(email);
		assertEquals(email, dto.getEmail());
	}

	@Test
	void testSetAndGetPassword() {
		UsuarioLoginDTO dto = new UsuarioLoginDTO();
		String password = "123456";
		dto.setPassword(password);
		assertEquals(password, dto.getPassword());
	}

	@Test
	void testEmailNull() {
		UsuarioLoginDTO dto = new UsuarioLoginDTO();
		dto.setEmail(null);
		assertNull(dto.getEmail());
	}

	@Test
	void testPasswordNull() {
		UsuarioLoginDTO dto = new UsuarioLoginDTO();
		dto.setPassword(null);
		assertNull(dto.getPassword());
	}

}
