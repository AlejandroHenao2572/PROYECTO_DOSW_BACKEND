package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.AuthRequestDTO;
import com.sirha.proyecto_sirha_dosw.dto.AuthResponseDTO;
import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el controlador de autenticación.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private UsuarioDTO usuarioDTO;
    private AuthRequestDTO authRequestDTO;
    private AuthResponseDTO authResponseDTO;

    @BeforeEach
    void setUp() {
        // Configurar DTO de registro de usuario
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setPassword("Password123!");
        usuarioDTO.setRol("ESTUDIANTE");
        usuarioDTO.setFacultad("INGENIERIA_SISTEMAS");

        // Configurar DTO de login
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("juan.perez-p@mail.escuelaing.edu.co");
        authRequestDTO.setPassword("Password123!");

        // Configurar respuesta de autenticación
        authResponseDTO = AuthResponseDTO.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token")
                .email("juan.perez-p@mail.escuelaing.edu.co")
                .rol(Rol.ESTUDIANTE)
                .nombre("Juan Pérez")
                .build();
    }

    // ==================== Tests para REGISTER ====================

    @Test
    void testRegister_Exitoso_Estudiante() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class))).thenReturn(authResponseDTO);

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthResponseDTO);
        
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token", responseBody.getToken());
        assertEquals("juan.perez-p@mail.escuelaing.edu.co", responseBody.getEmail());
        assertEquals(Rol.ESTUDIANTE, responseBody.getRol());
        assertEquals("Juan Pérez", responseBody.getNombre());

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_Exitoso_Decano() throws SirhaException {
        // Given
        usuarioDTO.setRol("DECANO");
        usuarioDTO.setFacultad("INGENIERIA_CIVIL");
        
        AuthResponseDTO decanoResponse = AuthResponseDTO.builder()
                .token("token.decano.test")
                .email("juan.perez-p@mail.escuelaing.edu.co")
                .rol(Rol.DECANO)
                .nombre("Juan Pérez")
                .build();

        when(authService.register(any(UsuarioDTO.class))).thenReturn(decanoResponse);

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthResponseDTO);
        
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(Rol.DECANO, responseBody.getRol());

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_Exitoso_Administrador() throws SirhaException {
        // Given
        usuarioDTO.setRol("ADMINISTRADOR");
        usuarioDTO.setFacultad(null); // ADMINISTRADOR no tiene facultad
        
        AuthResponseDTO adminResponse = AuthResponseDTO.builder()
                .token("token.admin.test")
                .email("juan.perez-p@mail.escuelaing.edu.co")
                .rol(Rol.ADMINISTRADOR)
                .nombre("Juan Pérez")
                .build();

        when(authService.register(any(UsuarioDTO.class))).thenReturn(adminResponse);

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthResponseDTO);
        
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(Rol.ADMINISTRADOR, responseBody.getRol());

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_EmailDuplicado() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException(SirhaException.EMAIL_YA_REGISTRADO));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains(SirhaException.EMAIL_YA_REGISTRADO));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_RolInvalido() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException(SirhaException.ROL_INVALIDO));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains(SirhaException.ROL_INVALIDO));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_FacultadInvalida() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException(SirhaException.FACULTAD_ERROR));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains(SirhaException.FACULTAD_ERROR));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_FacultadObligatoriaParaEstudiante() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException("La facultad es obligatoria para el rol ESTUDIANTE"));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("facultad es obligatoria"));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_DecanoYaExiste() throws SirhaException {
        // Given
        usuarioDTO.setRol("DECANO");
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException(SirhaException.DECANO_YA_EXISTE));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains(SirhaException.DECANO_YA_EXISTE));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_ErrorGenerico() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Error al registrar usuario"));

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_PasswordInvalido() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new IllegalArgumentException("Password no cumple con los requisitos"));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    // ==================== Tests para LOGIN ====================

    @Test
    void testLogin_Exitoso() {
        // Given
        when(authService.login(any(AuthRequestDTO.class))).thenReturn(authResponseDTO);

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthResponseDTO);
        
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token", responseBody.getToken());
        assertEquals("juan.perez-p@mail.escuelaing.edu.co", responseBody.getEmail());
        assertEquals(Rol.ESTUDIANTE, responseBody.getRol());
        assertEquals("Juan Pérez", responseBody.getNombre());

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_UsuarioNoEncontrado() {
        // Given
        when(authService.login(any(AuthRequestDTO.class)))
                .thenThrow(new UsernameNotFoundException("Usuario no encontrado"));

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Usuario no encontrado"));

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_CredencialesInvalidas() {
        // Given
        when(authService.login(any(AuthRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Credenciales inválidas"));

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_PasswordIncorrecta() {
        // Given
        authRequestDTO.setPassword("PasswordIncorrecta123!");
        when(authService.login(any(AuthRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Credenciales inválidas"));

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_ErrorInterno() {
        // Given
        when(authService.login(any(AuthRequestDTO.class)))
                .thenThrow(new RuntimeException("Error interno del servidor"));

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Error al procesar la solicitud"));

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_EmailVacio() {
        // Given
        authRequestDTO.setEmail("");
        when(authService.login(any(AuthRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testLogin_DiferentesRoles_Estudiante() {
        // Given
        AuthResponseDTO estudianteResponse = AuthResponseDTO.builder()
                .token("token.estudiante")
                .email("estudiante@mail.escuelaing.edu.co")
                .rol(Rol.ESTUDIANTE)
                .nombre("Estudiante Test")
                .build();

        when(authService.login(any(AuthRequestDTO.class))).thenReturn(estudianteResponse);

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(Rol.ESTUDIANTE, responseBody.getRol());
    }

    @Test
    void testLogin_DiferentesRoles_Decano() {
        // Given
        AuthResponseDTO decanoResponse = AuthResponseDTO.builder()
                .token("token.decano")
                .email("decano@mail.escuelaing.edu.co")
                .rol(Rol.DECANO)
                .nombre("Decano Test")
                .build();

        when(authService.login(any(AuthRequestDTO.class))).thenReturn(decanoResponse);

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(Rol.DECANO, responseBody.getRol());
    }

    @Test
    void testLogin_DiferentesRoles_Administrador() {
        // Given
        AuthResponseDTO adminResponse = AuthResponseDTO.builder()
                .token("token.admin")
                .email("admin@mail.escuelaing.edu.co")
                .rol(Rol.ADMINISTRADOR)
                .nombre("Admin Test")
                .build();

        when(authService.login(any(AuthRequestDTO.class))).thenReturn(adminResponse);

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponseDTO responseBody = (AuthResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals(Rol.ADMINISTRADOR, responseBody.getRol());
    }

    @Test
    void testLogin_ConEmailValido() {
        // Given
        authRequestDTO.setEmail("usuario.valido@mail.escuelaing.edu.co");
        when(authService.login(any(AuthRequestDTO.class))).thenReturn(authResponseDTO);

        // When
        ResponseEntity<Object> response = authController.login(authRequestDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).login(any(AuthRequestDTO.class));
    }

    @Test
    void testRegister_ConNombreCompuesto() throws SirhaException {
        // Given
        usuarioDTO.setNombre("Juan Carlos");
        usuarioDTO.setApellido("García López");
        
        when(authService.register(any(UsuarioDTO.class))).thenReturn(authResponseDTO);

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService, times(1)).register(any(UsuarioDTO.class));
    }

    @Test
    void testRegister_CarreraNoEncontrada() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new SirhaException(SirhaException.CARRERA_NO_ENCONTRADA + "INGENIERIA_SISTEMAS"));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains(SirhaException.CARRERA_NO_ENCONTRADA));
    }

    @Test
    void testRegister_NullPointerException() throws SirhaException {
        // Given
        when(authService.register(any(UsuarioDTO.class)))
                .thenThrow(new NullPointerException("Null value detected"));

        // When
        ResponseEntity<Object> response = authController.register(usuarioDTO);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Error al registrar usuario"));
    }
}
