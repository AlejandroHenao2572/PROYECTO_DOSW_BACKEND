package com.sirha.api.controller;

import com.sirha.api.dto.UsuarioDTO;
import com.sirha.api.model.Rol;
import com.sirha.api.model.Usuario;
import com.sirha.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 *
 * <p>
 * Expone endpoints para registrar, autenticar y consultar usuarios
 * por distintos criterios (ID, correo, rol, nombre y apellido).
 * </p>
 *
 * <p>
 * Los endpoints están disponibles bajo la ruta base {@code /api/auth}.
 * </p>
 */

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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
     * @param data mapa con los campos: email y password
     * @return 200 OK si las credenciales son correctas,
     *         401 Unauthorized si no coinciden
     */

    // Login
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

    @PutMapping("/{usuarioId}")
    public ResponseEntity updateUsuario(@PathVariable String usuarioId, @RequestBody UsuarioDTO dto) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(usuarioId, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

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
     *
     * @return lista de {@link Usuario}
     */

    // Listar todos
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
     *
     * @param id identificador del usuario
     * @return {@link Usuario} si existe, 404 si no
     */

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico
     * @return {@link Usuario} si existe, 404 si no
     */

    // Buscar por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca todos los usuarios que tengan el rol indicado.
     *
     * @param rol nombre del rol (ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR)
     * @return lista de usuarios, 204 si no hay ninguno, 400 si el rol es inválido
     */

    // Buscar por Rol
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
     *
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
     *
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
     *
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