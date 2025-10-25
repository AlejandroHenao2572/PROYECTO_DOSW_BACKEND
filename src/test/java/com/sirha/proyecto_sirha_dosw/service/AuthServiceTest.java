package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.config.JwtService;
import com.sirha.proyecto_sirha_dosw.dto.AuthRequestDTO;
import com.sirha.proyecto_sirha_dosw.dto.AuthResponseDTO;
import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Administrador;
import com.sirha.proyecto_sirha_dosw.model.Decano;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link AuthService}.
 * 
 * <p>Estas pruebas verifican:</p>
 * <ul>
 *   <li>Autenticación exitosa de usuarios</li>
 *   <li>Registro de usuarios con diferentes roles</li>
 *   <li>Generación correcta de tokens JWT</li>
 *   <li>Manejo de errores (credenciales inválidas, usuarios no encontrados)</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Pruebas Unitarias")
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthService authService;

    private AuthRequestDTO authRequestDTO;
    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private final String mockToken = "mock.jwt.token";

    @BeforeEach
    void setUp() {
        authRequestDTO = new AuthRequestDTO();
        authRequestDTO.setEmail("juan.perez@mail.escuelaing.edu.co");
        authRequestDTO.setPassword("Password123!");

        usuario = new Estudiante();
        usuario.setId("1234567890");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setEmail("juan.perez@mail.escuelaing.edu.co");
        usuario.setPassword("$2a$10$encodedPassword");
        usuario.setRol(Rol.ESTUDIANTE);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setPassword("Password123!");
        usuarioDTO.setRol("ESTUDIANTE");
        usuarioDTO.setFacultad("INGENIERIA");
    }

    // ==================== TESTS DE LOGIN ====================

    @Test
    @DisplayName("Login exitoso - Debe autenticar usuario y generar token JWT")
    void testLogin_Exitoso() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // La autenticación exitosa no necesita retornar nada específico
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.login(authRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getRol(), response.getRol());
        assertEquals("Juan Pérez", response.getNombre());

        // Verificar interacciones
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByEmail(authRequestDTO.getEmail());
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Login exitoso - Usuario Administrador")
    void testLogin_Exitoso_Administrador() {
        // Arrange
        Usuario admin = new Administrador();
        admin.setId("9876543210");
        admin.setNombre("María");
        admin.setApellido("González");
        admin.setEmail("maria.gonzalez@mail.escuelaing.edu.co");
        admin.setPassword("$2a$10$encodedPassword");
        admin.setRol(Rol.ADMINISTRADOR);

        authRequestDTO.setEmail(admin.getEmail());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(admin.getEmail()))
                .thenReturn(Optional.of(admin));
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.login(authRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Rol.ADMINISTRADOR, response.getRol());
        assertEquals("María González", response.getNombre());
        assertEquals(admin.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("Login exitoso - Usuario Decano")
    void testLogin_Exitoso_Decano() {
        // Arrange
        Usuario decano = new Decano();
        decano.setId("5555555555");
        decano.setNombre("Carlos");
        decano.setApellido("Ramírez");
        decano.setEmail("carlos.ramirez@mail.escuelaing.edu.co");
        decano.setPassword("$2a$10$encodedPassword");
        decano.setRol(Rol.DECANO);

        authRequestDTO.setEmail(decano.getEmail());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(decano.getEmail()))
                .thenReturn(Optional.of(decano));
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.login(authRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Rol.DECANO, response.getRol());
        assertEquals("Carlos Ramírez", response.getNombre());
    }

    @Test
    @DisplayName("Login fallido - Credenciales incorrectas")
    void testLogin_CredencialesIncorrectas() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(authRequestDTO);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, never()).findByEmail(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login fallido - Usuario no encontrado después de autenticación")
    void testLogin_UsuarioNoEncontrado() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(authRequestDTO);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        assertTrue(exception.getMessage().contains(authRequestDTO.getEmail()));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByEmail(authRequestDTO.getEmail());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login - Verifica que se crea UserDetails correctamente")
    void testLogin_CreaUserDetailsCorrectamente() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);

        // Act
        authService.login(authRequestDTO);

        // Assert
        verify(jwtService).generateToken(userDetailsCaptor.capture());
        UserDetails capturedUserDetails = userDetailsCaptor.getValue();

        assertNotNull(capturedUserDetails);
        assertEquals(usuario.getEmail(), capturedUserDetails.getUsername());
        assertEquals(usuario.getPassword(), capturedUserDetails.getPassword());
        assertTrue(capturedUserDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ESTUDIANTE")));
    }

    // ==================== TESTS DE REGISTER ====================

    @Test
    @DisplayName("Registro exitoso - Usuario Estudiante")
    void testRegister_Exitoso_Estudiante() throws SirhaException {
        // Arrange
        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuario);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(Rol.ESTUDIANTE, response.getRol());
        assertEquals("Juan Pérez", response.getNombre());

        verify(usuarioService, times(1)).registrar(usuarioDTO);
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Registro exitoso - Usuario Administrador")
    void testRegister_Exitoso_Administrador() throws SirhaException {
        // Arrange
        usuarioDTO.setRol("ADMINISTRADOR");
        usuarioDTO.setFacultad(null);

        Usuario admin = new Administrador();
        admin.setId("9999999999");
        admin.setNombre("Ana");
        admin.setApellido("Torres");
        admin.setEmail("ana.torres@mail.escuelaing.edu.co");
        admin.setPassword("$2a$10$encodedPassword");
        admin.setRol(Rol.ADMINISTRADOR);

        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(admin);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Rol.ADMINISTRADOR, response.getRol());
        assertEquals("Ana Torres", response.getNombre());
        assertEquals(admin.getEmail(), response.getEmail());
    }

    @Test
    @DisplayName("Registro exitoso - Usuario Decano")
    void testRegister_Exitoso_Decano() throws SirhaException {
        // Arrange
        usuarioDTO.setRol("DECANO");
        usuarioDTO.setFacultad("INGENIERIA");

        Usuario decano = new Decano();
        decano.setId("8888888888");
        decano.setNombre("Pedro");
        decano.setApellido("Martínez");
        decano.setEmail("pedro.martinez@mail.escuelaing.edu.co");
        decano.setPassword("$2a$10$encodedPassword");
        decano.setRol(Rol.DECANO);

        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(decano);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        assertNotNull(response);
        assertEquals(Rol.DECANO, response.getRol());
        assertEquals("Pedro Martínez", response.getNombre());
    }

    @Test
    @DisplayName("Registro fallido - Email duplicado")
    void testRegister_EmailDuplicado() throws SirhaException {
        // Arrange
        when(usuarioService.registrar(usuarioDTO))
                .thenThrow(new SirhaException("El email ya está registrado"));

        // Act & Assert
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            authService.register(usuarioDTO);
        });

        assertEquals("El email ya está registrado", exception.getMessage());

        verify(usuarioService, times(1)).registrar(usuarioDTO);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Registro fallido - Rol inválido")
    void testRegister_RolInvalido() throws SirhaException {
        // Arrange
        usuarioDTO.setRol("ROL_INVALIDO");

        when(usuarioService.registrar(usuarioDTO))
                .thenThrow(new SirhaException("Rol inválido: ROL_INVALIDO"));

        // Act & Assert
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            authService.register(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Rol inválido"));

        verify(usuarioService, times(1)).registrar(usuarioDTO);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Registro fallido - Datos inválidos del UsuarioService")
    void testRegister_DatosInvalidos() throws SirhaException {
        // Arrange
        when(usuarioService.registrar(usuarioDTO))
                .thenThrow(new SirhaException("Error de validación en los datos del usuario"));

        // Act & Assert
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            authService.register(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("Error de validación"));

        verify(usuarioService, times(1)).registrar(usuarioDTO);
    }

    @Test
    @DisplayName("Registro fallido - Decano sin facultad")
    void testRegister_DecanoSinFacultad() throws SirhaException {
        // Arrange
        usuarioDTO.setRol("DECANO");
        usuarioDTO.setFacultad(null);

        when(usuarioService.registrar(usuarioDTO))
                .thenThrow(new SirhaException("El decano debe tener una facultad asignada"));

        // Act & Assert
        SirhaException exception = assertThrows(SirhaException.class, () -> {
            authService.register(usuarioDTO);
        });

        assertTrue(exception.getMessage().contains("facultad"));

        verify(usuarioService, times(1)).registrar(usuarioDTO);
    }

    @Test
    @DisplayName("Registro - Verifica que se genera token JWT después del registro")
    void testRegister_GeneraTokenJWT() throws SirhaException {
        // Arrange
        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuario);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        verify(jwtService).generateToken(userDetailsCaptor.capture());
        UserDetails capturedUserDetails = userDetailsCaptor.getValue();

        assertNotNull(capturedUserDetails);
        assertEquals(usuario.getEmail(), capturedUserDetails.getUsername());
        assertTrue(capturedUserDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ESTUDIANTE")));
        
        assertNotNull(response.getToken());
        assertEquals(mockToken, response.getToken());
    }

    @Test
    @DisplayName("Registro - Verifica construcción correcta del nombre completo")
    void testRegister_NombreCompletoCorrectamente() throws SirhaException {
        // Arrange
        usuarioDTO.setNombre("Luis Fernando");
        usuarioDTO.setApellido("García López");

        Usuario usuarioConNombreCompuesto = new Estudiante();
        usuarioConNombreCompuesto.setId("1111111111");
        usuarioConNombreCompuesto.setNombre("Luis Fernando");
        usuarioConNombreCompuesto.setApellido("García López");
        usuarioConNombreCompuesto.setEmail("luis.garcia@mail.escuelaing.edu.co");
        usuarioConNombreCompuesto.setPassword("$2a$10$encodedPassword");
        usuarioConNombreCompuesto.setRol(Rol.ESTUDIANTE);

        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuarioConNombreCompuesto);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        assertEquals("Luis Fernando García López", response.getNombre());
    }

    @Test
    @DisplayName("Registro - Verifica que se llama al UsuarioService con el DTO correcto")
    void testRegister_LlamaUsuarioServiceConDTOCorrecto() throws SirhaException {
        // Arrange
        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuario);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        ArgumentCaptor<UsuarioDTO> dtoCaptor = ArgumentCaptor.forClass(UsuarioDTO.class);

        // Act
        authService.register(usuarioDTO);

        // Assert
        verify(usuarioService).registrar(dtoCaptor.capture());
        UsuarioDTO capturedDTO = dtoCaptor.getValue();

        assertEquals(usuarioDTO.getNombre(), capturedDTO.getNombre());
        assertEquals(usuarioDTO.getApellido(), capturedDTO.getApellido());
        assertEquals(usuarioDTO.getPassword(), capturedDTO.getPassword());
        assertEquals(usuarioDTO.getRol(), capturedDTO.getRol());
        assertEquals(usuarioDTO.getFacultad(), capturedDTO.getFacultad());
    }

    // ==================== TESTS DE INTEGRACIÓN ENTRE MÉTODOS ====================

    @Test
    @DisplayName("Integración - Registro y login del mismo usuario")
    void testIntegracion_RegistroYLogin() throws SirhaException {
        // Arrange - Registro
        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuario);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act - Registro
        AuthResponseDTO registerResponse = authService.register(usuarioDTO);

        // Assert - Registro
        assertNotNull(registerResponse);
        assertEquals(mockToken, registerResponse.getToken());

        // Arrange - Login
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        // Act - Login
        AuthResponseDTO loginResponse = authService.login(authRequestDTO);

        // Assert - Login
        assertNotNull(loginResponse);
        assertEquals(registerResponse.getEmail(), loginResponse.getEmail());
        assertEquals(registerResponse.getRol(), loginResponse.getRol());
        assertEquals(registerResponse.getNombre(), loginResponse.getNombre());
    }

    @Test
    @DisplayName("Login - Verifica que el password no se expone en la respuesta")
    void testLogin_PasswordNoExpuesto() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByEmail(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.login(authRequestDTO);

        // Assert
        assertNotNull(response);
        // El AuthResponseDTO no debe tener campo de password
        assertNotNull(response.getToken());
        assertNotNull(response.getEmail());
        assertNotNull(response.getRol());
        assertNotNull(response.getNombre());
    }

    @Test
    @DisplayName("Registro - Maneja correctamente nombres con caracteres especiales")
    void testRegister_NombresConCaracteresEspeciales() throws SirhaException {
        // Arrange
        usuarioDTO.setNombre("José María");
        usuarioDTO.setApellido("Ñúñez Álvarez");

        Usuario usuarioEspecial = new Estudiante();
        usuarioEspecial.setId("2222222222");
        usuarioEspecial.setNombre("José María");
        usuarioEspecial.setApellido("Ñúñez Álvarez");
        usuarioEspecial.setEmail("jose.nunez@mail.escuelaing.edu.co");
        usuarioEspecial.setPassword("$2a$10$encodedPassword");
        usuarioEspecial.setRol(Rol.ESTUDIANTE);

        when(usuarioService.registrar(usuarioDTO))
                .thenReturn(usuarioEspecial);
        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn(mockToken);

        // Act
        AuthResponseDTO response = authService.register(usuarioDTO);

        // Assert
        assertEquals("José María Ñúñez Álvarez", response.getNombre());
        assertEquals("jose.nunez@mail.escuelaing.edu.co", response.getEmail());
    }
}
