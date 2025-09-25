package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 * Expone endpoints para registrar, autenticar y consultar usuarios por,
 * distintos criterios (ID, correo, rol, nombre y apellido).
 * Los endpoints están disponibles bajo la ruta base {@code /api/auth}.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Regsitra un nuevo usuario en el sistema.
     * @param dto Estructura con los datos del usuario a registrar.
     * @return 201 Created si se registró con éxito,
     *         409 Conflict si el email ya está en uso.
     */
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UsuarioDTO dto) {
        try {
            Usuario nuevo = usuarioService.registrar(dto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Endpoint para autenticar un usuario (login).
     *
     * @param data mapa con los campos: email y password.
     * @return 200 OK si las credenciales son correctas,
     *         401 Unauthorized si no coinciden.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String password = data.get("password");

        boolean valido = usuarioService.autenticar(email, password);

        if (valido) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param usuarioId identificador único del usuario.
     * @param dto datos actualziados.
     * @return usuario actualizado o 404 si no existe.
     */
    @PutMapping("/{usuarioId}")
    public ResponseEntity updateUsuario(@PathVariable String usuarioId, @RequestBody UsuarioDTO dto) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(usuarioId, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Elimina un usuario del sistema.
     * @param usuarioId identificador único del usuario.
     * @return 204 No Content si se eliminó, 404 si no existe.
     */
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity deleteUsuario(@PathVariable String usuarioId) {
        try {
            usuarioService.eliminarUsuario(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return lista de {@link Usuario}
     */
    @GetMapping("/")
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if (usuarios.size() > 0) {
            return ResponseEntity.ok(usuarioService.listarUsuarios());
        } else {
            return new ResponseEntity<>("No se encontraron Usuarios", HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Busca un usuario por su identificador único.
     * @param id identificador del usuario
     * @return {@link Usuario} si existe, 404 si no
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @param email correo electrónico
     * @return {@link Usuario} si existe, 404 si no
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca todos los usuarios que tengan el rol indicado.
     * @param rol nombre del rol (ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR)
     * @return lista de usuarios, 204 si no hay ninguno, 400 si el rol es inválido
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable String rol) {
        try {
            Rol rolEnum = Rol.valueOf(rol.toUpperCase());
            List<Usuario> usuarios = usuarioService.obtenerPorRol(rolEnum);

            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Busca usuarios por su nombre.
     * @param nombre nombre del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Usuario>> obtenerPorNombre(@PathVariable String nombre) {
        List<Usuario> usuarios = usuarioService.obtenerPorNombre(nombre);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca usuarios por su apellido.
     * @param apellido apellido del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorApellido(@PathVariable String apellido) {
        List<Usuario> usuarios = usuarioService.obtenerPorApellido(apellido);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca usuarios por su nombre y apellido.
     * @param nombre   nombre del usuario
     * @param apellido apellido del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @GetMapping("/nombre/{nombre}/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorNombreYApellido(@PathVariable String nombre,
            @PathVariable String apellido) {
        List<Usuario> usuarios = usuarioService.obtenerPorNombreYApellido(nombre, apellido);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

}