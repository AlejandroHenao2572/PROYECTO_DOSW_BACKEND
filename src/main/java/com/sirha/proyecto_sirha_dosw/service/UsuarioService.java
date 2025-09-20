package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.model.Rol;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.model.Usuario;

import java.util.*;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios.
 *
 * <p>Se encarga de acceder al repositorio {@code UsuarioRepository}
 * y proveer métodos para consultar usuarios por diferentes criterios
 * como ID, correo, rol, nombre y apellido.</p>
 */

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor para inyectar el repositorio de usuarios.
     *
     * @param usuarioRepository repositorio que gestiona la persistencia de {@link Usuario}
     */

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Antes de guardar el usuario, se valida que el correo electrónico no
     * esté previamente registrado en la base de datos. Si ya existe, se lanza
     * una excepción.</p>
     *
     * @param usuario objeto {@link Usuario} a registrar
     * @return el usuario guardado en la base de datos
     * @throws IllegalArgumentException si el correo ya está registrado
     */

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Autentica a un usuario en el sistema.
     *
     * <p>Busca un usuario por su correo electrónico y compara la contraseña
     * proporcionada con la almacenada en la base de datos.</p>
     *
     * <p><b>Nota:</b> actualmente la contraseña se compara en texto plano.
     * Para producción se recomienda usar un mecanismo seguro como
     * {@code BCryptPasswordEncoder}.</p>
     *
     * @param email correo electrónico del usuario
     * @param rawPassword contraseña en texto plano proporcionada por el usuario
     * @return {@code true} si las credenciales son correctas, {@code false} en caso contrario
     */

    public boolean autenticar(String email, String rawPassword) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent() && usuario.get().getPassword().equals(rawPassword);
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}
     */

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador único del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no
     */

    public Optional<Usuario> obtenerPorId(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no
     */

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca todos los usuarios que tengan el rol especificado.
     *
     * @param rol rol a filtrar
     * @return lista de {@link Usuario} con el rol indicado
     */

    public List<Usuario> obtenerPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Busca un usuario por su nombre.
     *
     * @param nombre nombre del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no
     */

    public Optional<Usuario> obtenerPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    /**
     * Busca un usuario por su apellido.
     *
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no
     */

    public Optional<Usuario> obtenerPorApellido(String apellido) {
        return usuarioRepository.findByApellido(apellido);
    }

    /**
     * Busca un usuario por su nombre y apellido.
     *
     * @param nombre   nombre del usuario
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no
     */

    public Optional<Usuario> obtenerPorNombreYApellido(String nombre, String apellido) {
        return usuarioRepository.findByNombreAndApellido(nombre, apellido);
    }


}

