package com.sirha.api.service;

import com.sirha.api.dto.CarreraDTO;
import com.sirha.api.dto.MateriaDTO;
import com.sirha.api.model.Carrera;
import com.sirha.api.model.Facultad;
import com.sirha.api.model.Materia;
import com.sirha.api.model.builder.ImpCarreraBuilder;
import com.sirha.api.repository.CarreraRepository;
import com.sirha.api.repository.MateriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final MateriaRepository materiaRepository;

    @Autowired
    public CarreraService(CarreraRepository carreraRepository,MateriaRepository materiaRepository) {
        this.carreraRepository = carreraRepository;
        this.materiaRepository = materiaRepository;
    }

    public Carrera registrar(@Valid CarreraDTO dto) {
        Optional<Carrera> carreraOpt = carreraRepository.findByCodigo(dto.getCodigo());
        if (carreraOpt.isPresent()) {
            throw new IllegalArgumentException("Carrera con codigo" + dto.getCodigo() + " ya existe");
        }
        try {
            Optional<Carrera> carreraOptName = carreraRepository.findByNombre(Facultad.valueOf(dto.getNombre().toUpperCase()));
            if (carreraOptName.isPresent()) {
                throw new IllegalArgumentException("Carrera con nombre" + dto.getCodigo() + " ya existe");
            }
        }catch (Exception e){
            throw new IllegalArgumentException("tipo no válido: " + dto.getNombre() + ", formas correctas son: " +
                    "INGENIERIA_SISTEMAS, INGENIERIA_CIVIL, ADMINISTRACION.");
        }
        Carrera carrera = new ImpCarreraBuilder()
                .nombre(Facultad.valueOf(dto.getNombre().toUpperCase()))
                .codigo(dto.getCodigo())
                .duracionSemestres(dto.getDuracionSemestres())
                .creditosTotales(dto.getCreditosTotales())
                .build();
        return carreraRepository.insert(carrera);
   }

    public Materia addMateria(@Valid MateriaDTO dto, String codigoCarrera) {
        Optional<Carrera> carreraOpt = carreraRepository.findById(codigoCarrera);
        if (carreraOpt.isEmpty()) {
            throw new IllegalArgumentException("Carrera con codigo " + codigoCarrera + " no existe");
        }

        Optional<Materia> materiaOpt = materiaRepository.findByAcronimo(dto.getAcronimo());
        if (materiaOpt.isPresent()) {
            throw new IllegalArgumentException("Materia con acronimo " + dto.getAcronimo() + " ya existe");
        }

        Optional<Materia> materiaOptName = materiaRepository.findByNombre(dto.getNombre());
        if (materiaOptName.isPresent()) {
            throw new IllegalArgumentException("Materia con el nombre " + dto.getNombre() + " ya existe");
        }

        // Insert the materia first
        Materia materia = new Materia(dto.getNombre(), dto.getAcronimo(), dto.getCreditos());
        Materia savedMateria = materiaRepository.insert(materia);

        // Now update the carrera with the new materia
        Carrera carrera = carreraOpt.get();
        carrera.addMateria(savedMateria);
        carreraRepository.save(carrera);  // Save the updated carrera

        return savedMateria;
    }
}