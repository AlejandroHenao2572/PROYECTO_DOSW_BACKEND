package com.sirha.proyecto_sirha_dosw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.repository.RolRepository;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    // Obtener todos los roles
    @GetMapping
    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    // Obtener un rol por ID
    @GetMapping("/{id}")
    public Optional<Rol> getRolById(@PathVariable String id) {
        return rolRepository.findById(id);
    }

    // Crear un nuevo rol
    @PostMapping
    public Rol createRol(@RequestBody Rol rol) {
        return rolRepository.save(rol);
    }

    // Actualizar un rol
    @PutMapping("/{id}")
    public Rol updateRol(@PathVariable String id, @RequestBody Rol rolDetails) {
        return rolRepository.findById(id)
                .map(rol -> {
                    rol.setRol(rolDetails.getRol());
                    rol.setPermisos(rolDetails.getPermisos());
                    return rolRepository.save(rol);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
    }

    // Eliminar un rol
    @DeleteMapping("/{id}")
    public String deleteRol(@PathVariable String id) {
        rolRepository.deleteById(id);
        return "Rol eliminado con id: " + id;
    }
}
