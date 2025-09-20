package com.sirha.proyecto_sirha_dosw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioFactoryTest {

    @Test
    void testCrearEstudiante() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.ESTUDIANTE,
                "Maria",
                "Lopez",
                "maria@email.com",
                "password",
                CarreraTipo.INGENIERIA_SISTEMAS
        );

        assertNotNull(usuario);
        assertTrue(usuario instanceof Estudiante);
        assertEquals("Maria", usuario.getNombre());
        assertEquals("Lopez", usuario.getApellido());
        assertEquals(Rol.ESTUDIANTE, usuario.getRol());
    }

    @Test
    void testCrearProfesor() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.PROFESOR,
                "Carlos",
                "Garcia",
                "carlos@email.com",
                "password",
                CarreraTipo.INGENIERIA_CIVIL
        );

        assertNotNull(usuario);
        assertTrue(usuario instanceof Profesor);
        assertEquals("Carlos", usuario.getNombre());
        assertEquals(Rol.PROFESOR, usuario.getRol());

        Profesor profesor = (Profesor) usuario;
        assertEquals(CarreraTipo.INGENIERIA_CIVIL, profesor.getDepartamento());
    }

    @Test
    void testCrearDecano() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.DECANO,
                "Ana",
                "Martinez",
                "ana@email.com",
                "password",
                CarreraTipo.ADMINISTRACION
        );

        assertNotNull(usuario);
        assertTrue(usuario instanceof Decano);
        assertEquals("Ana", usuario.getNombre());
        assertEquals(Rol.DECANO, usuario.getRol());

        Decano decano = (Decano) usuario;
        assertEquals(CarreraTipo.ADMINISTRACION, decano.getFacultad());
    }

    @Test
    void testCrearAdministrador() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.ADMINISTRADOR,
                "Admin",
                "Sistema",
                "admin@email.com",
                "password",
                null
        );

        assertNotNull(usuario);
        assertTrue(usuario instanceof Administrador);
        assertEquals("Admin", usuario.getNombre());
        assertEquals(Rol.ADMINISTRADOR, usuario.getRol());
    }
}