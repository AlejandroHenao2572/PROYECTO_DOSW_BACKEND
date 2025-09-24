package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends MongoRepository<Grupo, String> {

    // Find all grupos for a specific materia
    List<Grupo> findByMateria(Materia materia);

    // Find all grupos with a specific materia id
    List<Grupo> findByMateria_Id(String materiaId);

    // Find all grupos taught by a specific profesor
    List<Grupo> findByProfesor(Profesor profesor);

    // Find all grupos taught by a specific profesor id
    List<Grupo> findByProfesor_Id(String profesorId);

    // Find grupos that have slots available
    List<Grupo> findByEstaCompletoFalse();

    // Find grupos that are already full
    List<Grupo> findByEstaCompletoTrue();

    // Find a grupo by exact capacity
    List<Grupo> findByCapacidad(int capacidad);

}