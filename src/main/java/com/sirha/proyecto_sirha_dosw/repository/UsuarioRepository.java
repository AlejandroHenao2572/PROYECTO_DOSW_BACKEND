package com.sirha.proyecto_sirha_dosw.repository;

import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.sirha.proyecto_sirha_dosw.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Usuario en la base de datos MongoDB.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    /**
     * Busca un usuario por su correo electrónico.
     * @param email email correo electrónico del usuario.
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o vacío si no.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca todos los usuarios que coincidan exactamente con el nombre indicado.
     * @param nombre nombre del usuario.
     * @return lista de {@link Usuario} con el nombre dado.
     */
    List<Usuario> findByNombre(String nombre);

    /**
     * Busca todos los usuarios que coincidan exactamente con el apellido indicado.
     * @param apellido apellido del usuario.
     * @return lista de {@link Usuario} con el apellido dado.
     */
    List<Usuario> findByApellido(String apellido);

    /**
     * Busca todos los usuarios que coincidan con un nombre y un apellido específico.
     * @param nombre nombre del usuario.
     * @param apellido apellido del usuario.
     * @return lista de {@link Usuario} con el nombre y apellido dados.
     */
    List<Usuario> findByNombreAndApellido(String nombre, String apellido);

    /**
     * Busca todos los usuarios que tengan un rol específico.
     * @param rol rol del usuario (ej. 'ESTUDIANTE', 'DECANO', 'ADMINISTRADOR', 'PROFESOR')
     * @return lista de {@link Usuario} con el rol indicado.
     */
    List<Usuario> findByRol(Rol rol);

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return lista de todos los {@link Usuario}
     */
    List<Usuario> findAll();
    @Query("{ 'facultad': ?0, 'rol': ?1 }")
    List<Usuario> findByFacultadAndRol(Facultad facultad, Rol rol);
    @Query("{ '_id': ?0, 'facultad': ?1, 'rol': ?2 }")
    Usuario findByIdAndFacultadAndRol(String id, Facultad facultad, Rol rol);
    @Query("{ 'email': ?0, 'facultad': ?1, 'rol': ?2 }")
    Usuario findByEmailAndFacultadAndRol(String email, Facultad facultad, Rol rol);
    @Query("{ 'nombre': ?0, 'facultad': ?1, 'rol': ?2 }")
    List<Usuario> findByNombreAndFacultadAndRol(String nombre, Facultad facultad, Rol rol);
    @Query("{ 'apellido': ?0, 'facultad': ?1, 'rol': ?2 }")
    List<Usuario> findByApellidoAndFacultadAndRol(String apellido, Facultad facultad, Rol rol);
    @Query("{ 'nombre': ?0, 'apellido': ?1, 'facultad': ?2, 'rol': ?3 }")
    List<Usuario> findByNombreAndApellidoAndFacultadAndRol(String nombre, String apellido, Facultad facultad, Rol rol);
}