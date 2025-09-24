package com.sirha.api.controller;

import com.sirha.api.dto.CarreraDTO;
import com.sirha.api.dto.MateriaDTO;
import com.sirha.api.model.Carrera;
import com.sirha.api.model.Materia;
import com.sirha.api.service.CarreraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody CarreraDTO dto) {
        try {
            Carrera nuevo = carreraService.registrar(dto);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping("/materia/{codigoCarrera}")
    public ResponseEntity addMateria(@PathVariable String codigoCarrera, @Valid @RequestBody MateriaDTO dto) {
        try {
            Materia actualizado = carreraService.addMateria(dto,codigoCarrera);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

}
