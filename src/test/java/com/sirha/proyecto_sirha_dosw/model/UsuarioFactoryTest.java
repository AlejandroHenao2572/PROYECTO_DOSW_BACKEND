package com.sirha.proyecto_sirha_dosw.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioFactoryTest {

    @Test
    void testCrearEstudiante() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.ESTUDIANTE,
                "Juan", "Perez", "juan@test.com", "pass123",
                Facultad.INGENIERIA_SISTEMAS
        );

        usuario.setId("estudiante1");
        assertInstanceOf(Estudiante.class, usuario);
        assertEquals("Juan", usuario.getNombre());
        assertEquals("estudiante1", usuario.getId());
        assertEquals("juan@test.com", usuario.getEmail());
        assertEquals("Perez", usuario.getApellido());
        assertEquals(Rol.ESTUDIANTE, usuario.getRol());
    }

    @Test
    void testCrearProfesor() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.PROFESOR,
                "Maria", "Gomez", "maria@test.com", "pass123",
                null
        );

        assertInstanceOf(Profesor.class, usuario);
        assertEquals("Maria", usuario.getNombre());
    }

    @Test
    void testCrearDecano() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.DECANO,
                "Jose", "Perez", "Jose@test.com", "pass123",
                Facultad.INGENIERIA_SISTEMAS
        );

        assertInstanceOf(Decano.class, usuario);
        assertEquals("Jose", usuario.getNombre());
        assertEquals(Rol.DECANO, usuario.getRol());
    }

    @Test
    void testCrearAdministrador() {
        Usuario usuario = UsuarioFactory.crearUsuario(
                Rol.ADMINISTRADOR,
                "Luiz", "Gomez", "Luiz@test.com", "pass123",
                null
        );

        assertInstanceOf(Administrador.class, usuario);
        assertEquals("Luiz", usuario.getNombre());
    }

    @Test
    void testRolNoSoportado() {
        assertThrows(NullPointerException.class, () -> {
            UsuarioFactory.crearUsuario(null, "Test", "User", "test@test.com", "pass", null);
        });
    }

}
