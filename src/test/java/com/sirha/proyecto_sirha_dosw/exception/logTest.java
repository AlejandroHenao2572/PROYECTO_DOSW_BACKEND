package com.sirha.proyecto_sirha_dosw.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {

	@BeforeEach
	void resetLogName() {
		Log.nombre = "Plan15";
	}

	@AfterEach
	void cleanUp() {
		File logFile = new File(Log.nombre + ".log");
		if (logFile.exists() && !logFile.delete()) {
			logFile.deleteOnExit();
		}
		Log.nombre = "Plan15";
	}

	@Test
	void testRecordNoException() {
		Log.nombre = "test-log";
		Exception e = new Exception("Test Exception");
		assertDoesNotThrow(() -> Log.record(e));
	}

	@Test
	void testLogFileCreated() {
		Log.nombre = "log-creado";
		Exception e = new Exception("Test Exception");
		Log.record(e);
		File logFile = new File(Log.nombre + ".log");
		assertTrue(logFile.exists());
		assertTrue(logFile.length() > 0);
	}

	@Test
	void testRecordCoversLogClass() {
		Log.nombre = "log-cobertura"; // Nombre válido para el archivo de log
		Exception e = new Exception("Cobertura Log");
		Log.record(e); // Solo se invoca, no se espera excepción
	}
}
