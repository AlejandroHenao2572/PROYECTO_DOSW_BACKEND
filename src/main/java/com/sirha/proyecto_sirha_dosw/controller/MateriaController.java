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
        this.materiaService = materiaService;
    }

    @PostMapping
    public ResponseEntity<?> createMateria(@Valid @RequestBody MateriaDTO materiaDTO) {
        try {
            Materia created = materiaService.createMateria(materiaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_CREACION_MATERIA+e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAllMaterias(@PathVariable String id) {
        try {
            materiaService.deleteMateria(id);
            return ResponseEntity.noContent().build();
        } catch (SirhaException e) {
            Log.record(e);
            return ResponseEntity.status(409).body(SirhaException.ERROR_ELIMINACION_MATERIA+e.getMessage());
        }
    }
}