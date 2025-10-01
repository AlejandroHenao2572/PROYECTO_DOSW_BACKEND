package com.sirha.proyecto_sirha_dosw.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

class logTest {

	@Test
	void testRecordNoException() {
		Exception e = new Exception("Test Exception");
		assertDoesNotThrow(() -> Log.record(e));
	}

	@Test
	void testLogFileCreated() {
		Exception e = new Exception("Test Exception");
		Log.record(e);
		File logFile = new File(Log.nombre + ".log");
		assertTrue(logFile.exists());
		assertTrue(logFile.length() > 0);
	}
}
