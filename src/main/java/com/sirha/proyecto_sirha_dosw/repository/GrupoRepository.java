package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad {@link Grupo}.
 * Extiende de {@link MongoRepository}, lo que permite realizar operaciones CRUD,
 *          sobre la colección de grupos en MongoDB.
 */
@Repository
public interface GrupoRepository extends MongoRepository<Grupo, String> {

    /**
     * Busca todos los grupos asociados a una materia específica.
     * @param materia objet {@link Materia}
     * @return lista de grupos que corresponden a la materia
     */
    List<Grupo> findByMateria(Materia materia);

    /**
     * Busca todos los grupos a partir de ID de una materia.
     * @param materiaId identificador de la materia.
     * @return lista de grupos relacionados con la materia indicada.
     */
    List<Grupo> findByMateria_Id(String materiaId);

    /**
     * Busca todos los grupos dictados por un profesor específico.
     * @param profesor objeto {@link Profesor}
     * @return lista de grupos asignados al profesor.
     */
    List<Grupo> findByProfesor(Profesor profesor);

    /**
     * Busca todos los grupos a partir del ID de un profesor.
     * @param profesorId identificador del profesor.
     * @return lista de grupos relacionados con ese profesor.
     */
    List<Grupo> findByProfesor_Id(String profesorId);

    /**
     * Busca todos los grupos que aún tienen cupos disponibles.
     * @return lista de grupos con disponibilidad.
     */
    List<Grupo> findByEstaCompletoFalse();

    /**
     * Busca todos los grupos que ya están completos.
     * @return lista de grupos sin cupos disponibles.
     */
    List<Grupo> findByEstaCompletoTrue();

    /**
     * Busca grupos que tengan exactamente la capacidad indicada.
     * @param capacidad número máximo de estudiantes permitidos en el grupo.
     * @return lista de grupos que cumplen con esa capacidad exacta.
     */
    List<Grupo> findByCapacidad(int capacidad);

}