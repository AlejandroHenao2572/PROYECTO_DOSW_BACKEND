package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MateriaServiceTest {

	@Mock
	private MateriaRepository materiaRepository;

	@InjectMocks
	private MateriaService materiaService;

	@Test
	void testCreateMateriaExitoso() throws SirhaException {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.empty());
		when(materiaRepository.findByNombre("Matemáticas")).thenReturn(Optional.empty());
		Materia materiaMock = new Materia();
		when(materiaRepository.save(any(Materia.class))).thenReturn(materiaMock);
		Materia result = materiaService.createMateria(dto);
		assertNotNull(result);
		verify(materiaRepository).save(any(Materia.class));
	}

	@Test
	void testCreateMateriaYaExistePorAcronimo() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.of(new Materia()));
		SirhaException ex = assertThrows(SirhaException.class, () -> materiaService.createMateria(dto));
		assertEquals(SirhaException.MATERIA_YA_EXISTE, ex.getMessage());
	}

	@Test
	void testCreateMateriaYaExistePorNombre() {
		MateriaDTO dto = new MateriaDTO();
		dto.setAcronimo("MAT101");
		dto.setNombre("Matemáticas");
		dto.setCreditos(3);
		when(materiaRepository.findByAcronimo("MAT101")).thenReturn(Optional.empty());
		when(materiaRepository.findByNombre("Matemáticas")).thenReturn(Optional.of(new Materia()));
		SirhaException ex = assertThrows(SirhaException.class, () -> materiaService.createMateria(dto));
		assertEquals(SirhaException.MATERIA_YA_EXISTE, ex.getMessage());
	}

	@Test
	void testDeleteMateriaExitoso() throws SirhaException {
		when(materiaRepository.existsById("MAT101")).thenReturn(true);
		doNothing().when(materiaRepository).deleteById("MAT101");
		assertDoesNotThrow(() -> materiaService.deleteMateria("MAT101"));
		verify(materiaRepository).deleteById("MAT101");
	}

	@Test
	void testDeleteMateriaNoEncontrada() {
		when(materiaRepository.existsById("MAT101")).thenReturn(false);
		SirhaException ex = assertThrows(SirhaException.class, () -> materiaService.deleteMateria("MAT101"));
		assertEquals(SirhaException.MATERIA_NO_ENCONTRADA, ex.getMessage());
	}
}
