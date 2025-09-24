package com.sirha.api.repository;

import com.sirha.api.model.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sirha.api.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNombre(String nombre);

    List<Usuario> findByApellido(String apellido);

    List<Usuario> findByNombreAndApellido(String nombre, String apellido);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findAll();
}