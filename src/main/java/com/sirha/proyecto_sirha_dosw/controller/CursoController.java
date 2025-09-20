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

import com.sirha.proyecto_sirha_dosw.model.Curso;
import com.sirha.proyecto_sirha_dosw.repository.CursoRepository;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    // Crear curso
    @PostMapping
    public Curso crearCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    // Listar todos los cursos
    @GetMapping
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    // Buscar curso por ID
    @GetMapping("/{id}")
    public Optional<Curso> obtenerPorId(@PathVariable String id) {
        return cursoRepository.findById(id);
    }

    // Actualizar curso
    @PutMapping("/{id}")
    public Curso actualizarCurso(@PathVariable String id, @RequestBody Curso cursoActualizado) {
        return cursoRepository.findById(id)
                .map(curso -> {
                    curso.setNombre(cursoActualizado.getNombre());
                    curso.setCreditos(cursoActualizado.getCreditos());
                    curso.setFacultad(cursoActualizado.getFacultad());
                    return cursoRepository.save(curso);
                })
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id " + id));
    }

    // Eliminar curso
    @DeleteMapping("/{id}")
    public void eliminarCurso(@PathVariable String id) {
        cursoRepository.deleteById(id);
    }


}

