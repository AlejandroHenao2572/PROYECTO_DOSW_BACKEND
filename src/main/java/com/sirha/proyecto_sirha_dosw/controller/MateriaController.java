package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.exception.Log;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    private final MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = java.util.Objects.requireNonNull(materiaService);
    }

    @PostMapping
    public ResponseEntity<Materia> createMateria(@Valid @RequestBody MateriaDTO materiaDTO) {
        try {
            Materia created = materiaService.createMateria(materiaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SirhaException e) {
            Log.logException(e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAllMaterias(@PathVariable String id) {
        try {
            materiaService.deleteMateria(id);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.logException(e);
            return new ResponseEntity<>(SirhaException.ERROR_ELIMINACION_MATERIA+e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}