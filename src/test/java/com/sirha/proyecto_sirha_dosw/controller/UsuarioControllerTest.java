package com.sirha.proyecto_sirha_dosw.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.dto.UsuarioLoginDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.service.UsuarioService;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLoginExitoso() {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setEmail("test@test.com");
        loginDTO.setPassword("123");
        when(usuarioService.autenticar("test@test.com", "123")).thenReturn(true);

        ResponseEntity<String> response = usuarioController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login exitoso", response.getBody());
    }

    @Test
    void testLoginCredencialesInvalidas() {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO();
        loginDTO.setEmail("test@test.com");
        loginDTO.setPassword("wrong");
        when(usuarioService.autenticar("test@test.com", "wrong")).thenReturn(false);

        ResponseEntity<String> response = usuarioController.login(loginDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(SirhaException.CREDENCIALES_INVALIDAS, response.getBody());
    }


    @Test
    void testUpdateUsuarioNotFound() throws SirhaException {
        UsuarioDTO dto = new UsuarioDTO();
        when(usuarioService.actualizarUsuario("id1", dto)).thenThrow(new SirhaException("No existe"));

        ResponseEntity response = usuarioController.updateUsuario("id1", dto);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteUsuarioOk() throws SirhaException {
        doNothing().when(usuarioService).eliminarUsuario("id1");
        ResponseEntity response = usuarioController.deleteUsuario("id1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUsuarioNotFound() throws SirhaException {
        doThrow(new SirhaException("No existe")).when(usuarioService).eliminarUsuario("id1");
        ResponseEntity response = usuarioController.deleteUsuario("id1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains(SirhaException.ERROR_ELIMINACION_USUARIO));
    }

    

    @Test
    void testListarUsuariosNoContent() {
        when(usuarioService.listarUsuarios()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = usuarioController.listarUsuarios();
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testObtenerPorIdNotFound() {
        when(usuarioService.obtenerPorId("id1")).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.obtenerPorId("id1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    

    @Test
    void testObtenerPorEmailNotFound() {
        when(usuarioService.obtenerPorEmail("mail")).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.obtenerPorEmail("mail");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    

    @Test
    void testObtenerPorRolNoContent() {
        when(usuarioService.obtenerPorRol(Rol.ESTUDIANTE)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Usuario>> response = usuarioController.obtenerPorRol("estudiante");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testObtenerPorRolInvalido() {
        ResponseEntity<List<Usuario>> response = usuarioController.obtenerPorRol("invalido");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testObtenerPorNombreNotFound() {
        when(usuarioService.obtenerPorNombre("Juan")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Usuario>> response = usuarioController.obtenerPorNombre("Juan");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    

    @Test
    void testObtenerPorApellidoNotFound() {
        when(usuarioService.obtenerPorApellido("Perez")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Usuario>> response = usuarioController.obtenerPorApellido("Perez");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

   

    @Test
    void testObtenerPorNombreYApellidoNotFound() {
        when(usuarioService.obtenerPorNombreYApellido("Juan", "Perez")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Usuario>> response = usuarioController.obtenerPorNombreYApellido("Juan", "Perez");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}