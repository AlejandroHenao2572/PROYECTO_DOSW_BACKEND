package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.UsuarioFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import com.sirha.proyecto_sirha_dosw.model.Usuario;

import java.util.*;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios.
 *
 * <p>
 * Se encarga de acceder al repositorio {@code UsuarioRepository}
 * y proveer métodos para consultar usuarios por diferentes criterios
 * como ID, correo, rol, nombre y apellido.
 * </p>
 */

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor para inyectar el repositorio de usuarios.
     *
     * @param usuarioRepository repositorio que gestiona la persistencia de
     *                          {@link Usuario}
     */

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>
     * Valida que los campos obligatorios estén presentes y que
     * el correo no esté ya registrado. Luego crea una instancia
     * de {@link Usuario} usando la {@code UsuarioFactory} y la
     * guarda en la base de datos.
     * </p>
     *
     * @param dto objeto {@link UsuarioDTO} con los datos del nuevo usuario
     * @return el {@link Usuario} recién creado y persistido
     * @throws IllegalArgumentException si faltan campos obligatorios o el email ya
     *                                  existe
     */
    public Usuario registrar(UsuarioDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Rol rol;
        try {
            rol = Rol.valueOf(dto.getRol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol no válido: " + dto.getRol() +
                    ". Roles válidos: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR");
        }
        Facultad facultad;
        try {
            if (dto.getFacultad() != null) {
                facultad = Facultad.valueOf(dto.getFacultad().toUpperCase());
            } else {
                facultad = null;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Facultad no válido: " + dto.getFacultad() +
                    ". Facultades válidas: INGENIERIA_SISTEMAS, INGENIERIA_CIVIL, ADMINISTRACION");
        }
        Usuario usuario = UsuarioFactory.crearUsuario(
                rol,
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getPassword(),
                facultad);
        return usuarioRepository.insert(usuario);
    }

    /**
     * Actualiza a un usuario.
     * @param usuarioId ID del usuario.
     * @param dto datos a modificar.
     * @return usuario actualizado.
     */
    public Usuario actualizarUsuario(String usuarioId, UsuarioDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
        }
        Usuario usuario = usuarioOpt.get();
        if (dto.getNombre() != null && !dto.getNombre().trim().isEmpty()) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null && !dto.getApellido().trim().isEmpty()) {
            usuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            // Verificar que el nuevo email no esté ya en uso por otro usuario
            Optional<Usuario> existente = usuarioRepository.findByEmail(dto.getEmail());
            if (existente.isPresent() && !existente.get().getId().equals(usuarioId)) {
                throw new IllegalArgumentException("El correo ya está registrado: " + dto.getEmail());
            }
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setContraseña(dto.getPassword());
        }
        if (dto.getRol() != null && !dto.getRol().trim().isEmpty()) {
            try {
                usuario.setRol(Rol.valueOf(dto.getRol().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rol no válido: " + dto.getRol() +
                        ". Roles válidos: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR");
            }
        }
        return usuarioRepository.save(usuario);
    }

    /**
     * Para eliminar un usuario.
     * @param usuarioId ID del usuario.
     */
    public void eliminarUsuario(String usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId);
        }
        usuarioRepository.deleteById(usuarioId);
    }

    /**
     * Autentica a un usuario en el sistema.
     *
     * <p>
     * Busca un usuario por su correo electrónico y compara la contraseña
     * proporcionada con la almacenada en la base de datos.
     * </p>
     *
     * <p>
     * <b>Nota:</b> actualmente la contraseña se compara en texto plano.
     * Para producción se recomienda usar un mecanismo seguro como
     * {@code BCryptPasswordEncoder}.
     * </p>
     *
     * @param email       correo electrónico del usuario
     * @param rawPassword contraseña en texto plano proporcionada por el usuario
     * @return {@code true} si las credenciales son correctas, {@code false} en caso
     *         contrario
     */

    public boolean autenticar(String email, String rawPassword) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent() && usuario.get().getContraseña().equals(rawPassword);
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
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public Optional<Usuario> obtenerPorId(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
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
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    /**
     * Busca un usuario por su apellido.
     *
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorApellido(String apellido) {
        return usuarioRepository.findByApellido(apellido);
    }

    /**
     * Busca un usuario por su nombre y apellido.
     *
     * @param nombre   nombre del usuario
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorNombreYApellido(String nombre, String apellido) {
        return usuarioRepository.findByNombreAndApellido(nombre, apellido);
    }

}
