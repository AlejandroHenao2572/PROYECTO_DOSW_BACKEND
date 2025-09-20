package com.sirha.proyecto_sirha_dosw.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sirha.proyecto_sirha_dosw.model.Rol;

@Repository
public interface RolRepository extends MongoRepository<Rol, String> {

    // Encuentra por nombre exacto del rol
    Rol findByRol(String rol);

    // Encuentra todos los roles que contengan un permiso específico
    List<Rol> findByPermisosContaining(String permiso);

    // Encuentra roles cuyo nombre empiece con una cadena
    List<Rol> findByRolStartingWith(String prefix);

    // Encuentra roles cuyo nombre termine en una cadena
    List<Rol> findByRolEndingWith(String suffix);

    // Encuentra roles que tengan exactamente una lista de permisos
    List<Rol> findByPermisos(List<String> permisos);

    // Verifica si existe un rol con un nombre específico
    boolean existsByRol(String rol);

    // Elimina un rol por nombre
    void deleteByRol(String rol);
}
