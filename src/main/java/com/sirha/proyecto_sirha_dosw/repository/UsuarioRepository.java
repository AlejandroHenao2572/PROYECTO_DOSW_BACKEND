package com.sirha.proyecto_sirha_dosw.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sirha.proyecto_sirha_dosw.model.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
     // Buscar usuario por email
    Usuario findByEmail(String email);

    // Buscar usuarios por nombre exacto
    List<Usuario> findByNombre(String nombre);

    // Buscar usuarios que contengan parte del nombre (ignora mayúsculas/minúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Buscar todos los usuarios con un rol específico
    List<Usuario> findByRol_Rol(String rol);

    // Buscar usuarios que tengan cierto permiso en su rol
    List<Usuario> findByRol_PermisosContaining(String permiso);

    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);

    // Eliminar usuario por email
    void deleteByEmail(String email);
}
