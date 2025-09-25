package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.CarreraDTO;
import com.sirha.proyecto_sirha_dosw.dto.MateriaDTO;
import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.builder.ImpCarreraBuilder;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio que gestiona la lógica de negocio relacionada con {@link Carrera}
 * Permite registrar nuevas carreras y asociar materias a una carrera existente.
 */
@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;
    private final MateriaRepository materiaRepository;

    /**
     * Constructor con inyección de dependencias para repositorios.
     * @param carreraRepository repositorio de {@link Carrera}
     * @param materiaRepository repositorio de {@link Materia}
     */
    @Autowired
    public CarreraService(CarreraRepository carreraRepository,MateriaRepository materiaRepository) {
        this.carreraRepository = carreraRepository;
        this.materiaRepository = materiaRepository;
    }

    /**
     * Registra una nueva carrera en el sistema.
     * Válida que no existe otra carrera con el mismo código o nombre.
     * @param dto objeto {@link CarreraDTO} con los datos de la carrera.
     * @return la {@link Carrera} registrada.
     * @throws IllegalArgumentException si ya existe una carrera con el mismo código o nombre.
     */
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

    /**
     * Agrega una nueva materia a una carrera existente.
     * Valida que no existe una materia con el mismo acrónimo o nombre antes de insertarla.
     * @param dto objeto {@link MateriaDTO} con los datos de la materia.
     * @param codigoCarrera código único de la carrera a la que se agregará la materia.
     * @return la {@link Materia} creada y asociada a la carrera.
     * @throws IllegalArgumentException si la carrera no existe, o si ya existe,
     *          una materia con el mismo acrónimo o nombre.
     */
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