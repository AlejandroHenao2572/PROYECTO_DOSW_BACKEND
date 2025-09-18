package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sirha.proyecto_sirha_dosw.model.Usuario;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.registrar(usuario); // ✅ usamos la instancia, no estático
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String password = data.get("password");

        boolean valido = usuarioService.autenticar(email, password);

        if (valido) {
            return ResponseEntity.ok("✅ Login exitoso");
        } else {
            return ResponseEntity.status(401).body("❌ Credenciales incorrectas");
        }
    }

}