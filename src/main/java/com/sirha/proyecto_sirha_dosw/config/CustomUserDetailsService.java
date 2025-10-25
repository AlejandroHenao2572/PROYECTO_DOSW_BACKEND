package com.sirha.proyecto_sirha_dosw.config;

import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Servicio personalizado para cargar los detalles del usuario desde MongoDB.
 * 
 * <p>Spring Security utiliza esta clase para:</p>
 * <ul>
 *   <li>Cargar información del usuario durante la autenticación</li>
 *   <li>Convertir el modelo Usuario a UserDetails de Spring Security</li>
 *   <li>Configurar los roles y permisos del usuario</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su email (username en términos de Spring Security).
     * 
     * @param email el email del usuario
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));

        // Convertir el rol del usuario a formato Spring Security (ROLE_XXX)
        String roleName = "ROLE_" + usuario.getRol().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // Crear y retornar UserDetails de Spring Security
        return User.builder()
                .username(usuario.getEmail()) // Email como username
                .password(usuario.getPassword()) // Password hasheado
                .authorities(Collections.singletonList(authority)) // Rol del usuario
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
