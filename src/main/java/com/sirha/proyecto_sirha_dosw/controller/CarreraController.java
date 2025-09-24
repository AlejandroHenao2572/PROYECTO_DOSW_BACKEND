package com.sirha.proyecto_sirha_dosw.controller;

import com.sirha.proyecto_sirha_dosw.dto.CarreraDTO;
import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.service.CarreraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las carreras y sus materias.
 * Expone Endpoints para registrar nuevas carreras y asociar materias a ellas.
 */

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    /**
     * Constructor con inyección de dependencias de CarreraService.
     * @param carreraService servicio que maneja la lógica de negocio para carreras
     */

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    /**
     * Registra una nueva carrera en el sistema.
     * @param dto objeto DTO
     * @return
     */

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
