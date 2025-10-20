package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.config.JwtService;
import com.sirha.proyecto_sirha_dosw.dto.AuthRequestDTO;
import com.sirha.proyecto_sirha_dosw.dto.AuthResponseDTO;
import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación para gestionar el registro y login de usuarios.
 * 
 * <p>Este servicio maneja:</p>
 * <ul>
 *   <li>Registro de nuevos usuarios</li>
 *   <li>Autenticación de usuarios con email y contraseña</li>
 *   <li>Generación de tokens JWT</li>
 *   <li>Validación de credenciales</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * <p>Proceso de autenticación:</p>
     * <ol>
     *   <li>Valida las credenciales (email y contraseña)</li>
     *   <li>Si son correctas, busca el usuario en la base de datos</li>
     *   <li>Genera un token JWT con la información del usuario</li>
     *   <li>Retorna el token y la información del usuario</li>
     * </ol>
     * 
     * @param request DTO con el email y contraseña del usuario
     * @return DTO con el token JWT y la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     * @throws BadCredentialsException si las credenciales son incorrectas
     */
    public AuthResponseDTO login(AuthRequestDTO request) {
        // Autenticar al usuario con email y contraseña
        // Si las credenciales son incorrectas, lanza BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Si la autenticación fue exitosa, buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + request.getEmail()
                ));

        // Crear UserDetails para generar el token
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPassword())
                        .roles(usuario.getRol().name())
                        .build();

        // Generar el token JWT
        String jwtToken = jwtService.generateToken(userDetails);

        // Construir y retornar la respuesta con el token y la información del usuario
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .nombre(usuario.getNombre() + " " + usuario.getApellido())
                .build();
    }

    /**
     * Registra un nuevo usuario en el sistema y genera un token JWT.
     * 
     * <p>Proceso de registro:</p>
     * <ol>
     *   <li>Valida que el email no esté registrado</li>
     *   <li>Crea el usuario con la contraseña encriptada</li>
     *   <li>Guarda el usuario en la base de datos</li>
     *   <li>Genera un token JWT automáticamente</li>
     *   <li>Retorna el token y la información del usuario</li>
     * </ol>
     * 
     * @param usuarioDTO DTO con los datos del nuevo usuario
     * @return DTO con el token JWT y la información del usuario registrado
     */
    public AuthResponseDTO register(UsuarioDTO usuarioDTO) {
        // Registrar el usuario usando el servicio de usuarios
        // Este método valida y encripta la contraseña automáticamente
        Usuario usuario = usuarioService.registrar(usuarioDTO);

        // Crear UserDetails para generar el token
        org.springframework.security.core.userdetails.UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getPassword())
                        .roles(usuario.getRol().name())
                        .build();

        // Generar el token JWT
        String jwtToken = jwtService.generateToken(userDetails);

        // Construir y retornar la respuesta con el token y la información del usuario
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .nombre(usuario.getNombre() + " " + usuario.getApellido())
                .build();
    }
}
