package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.model.CarreraTipo;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.model.UsuarioFactory;
import com.sirha.proyecto_sirha_dosw.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios.
 *
 * <p>Expone endpoints para registrar, autenticar y consultar usuarios
 * por distintos criterios (ID, correo, rol, nombre y apellido).</p>
 *
 * <p>Los endpoints están disponibles bajo la ruta base {@code /api/auth}.</p>
 */

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * <p>Recibe los datos como JSON en el cuerpo de la petición, construye
     * un objeto {@link Usuario} según su rol usando la {@link UsuarioFactory},
     * y lo guarda en la base de datos.</p>
     *
     * @param data mapa con los campos: nombre, apellido, email, password y rol
     * @return {@link ResponseEntity} con el usuario registrado o error 409 si el correo ya existe
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> data) {
        try {
            String nombre = data.get("nombre");
            String apellido = data.get("apellido");
            String email = data.get("email");
            String password = data.get("password");
            Rol rol = Rol.valueOf(data.get("rol").toUpperCase());
            CarreraTipo facultad = CarreraTipo.valueOf(data.get("facultad").toUpperCase());

            Usuario nuevo = UsuarioFactory.crearUsuario(rol, nombre, apellido, email, password, facultad);
            Usuario guardado = usuarioService.registrar(nuevo);

            return ResponseEntity.ok(guardado);

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


    //  Login
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
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de {@link Usuario}
     */

    //  Listar todos
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return {@link Usuario} si existe, 404 si no
     */

    //  Buscar por ID
    @GetMapping("/usuario/{id}")
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

    //  Buscar por email
    @GetMapping("/usuario/email/{email}")
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

    //  Buscar por Rol
    @GetMapping("/usuarios/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable String rol) {
        try {
            Rol rolEnum = Rol.valueOf(rol.toUpperCase()); // 🔥 convierte String a Enum
            List<Usuario> usuarios = usuarioService.obtenerPorRol(rolEnum);

            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 🔥 si el rol no existe
        }
    }

    /**
     * Busca usuarios por su nombre.
     *
     * @param nombre nombre del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @GetMapping("/usuario/nombre/{nombre}")
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
    @GetMapping("/usuario/apellido/{apellido}")
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
     * @param nombre nombre del usuario
     * @param apellido apellido del usuario
     * @return lista de {@link Usuario} si existen, lista vacía si no
     */
    @GetMapping("/usuario/{nombre}/{apellido}")
    public ResponseEntity<List<Usuario>> obtenerPorNombreYApellido(@PathVariable String nombre,
                                                                   @PathVariable String apellido) {
        List<Usuario> usuarios = usuarioService.obtenerPorNombreYApellido(nombre, apellido);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }


}
