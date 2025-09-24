package com.sirha.proyecto_sirha_dosw.config;

import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataSeedConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Verificar si ya existe el usuario para evitar duplicados
            String testEmail = "juan.perez@ejemplo.com";

            try {
                // Intentar encontrar y eliminar cualquier usuario duplicado
                List<Usuario> usuariosExistentes = usuarioRepository.findAll().stream()
                    .filter(u -> testEmail.equals(u.getEmail()))
                    .toList();

                if (!usuariosExistentes.isEmpty()) {
                    System.out.println("Encontrados " + usuariosExistentes.size() + " usuarios con el email: " + testEmail);
                    System.out.println("Eliminando usuarios duplicados antes de insertar datos de prueba...");

                    for (Usuario u : usuariosExistentes) {
                        usuarioRepository.delete(u);
                    }
                }

                // Crear una Carrera
                Carrera ingenieriaSistemas = new Carrera();
                ingenieriaSistemas.setNombre(Facultad.INGENIERIA_SISTEMAS);

                // Crear Materias
                Materia calculo = new Materia();
                calculo.setNombre("Cálculo Diferencial");
                calculo.setAcronimo("CALC");
                calculo.setCreditos(4);

                Materia programacion = new Materia();
                programacion.setNombre("Programación Orientada a Objetos");
                programacion.setAcronimo("POO");
                programacion.setCreditos(3);

                // Asociar materias a la carrera
                ingenieriaSistemas.setMaterias(Arrays.asList(calculo, calculo));

                // Crear Horarios para Cálculo
                Horario horarioCalculo1 = new Horario();
                horarioCalculo1.setDia(Dia.LUNES);
                horarioCalculo1.setHoraInicio("08:00");
                horarioCalculo1.setHoraFin("10:00");

                Horario horarioCalculo2 = new Horario();
                horarioCalculo2.setDia(Dia.MIERCOLES);
                horarioCalculo2.setHoraInicio("08:00");
                horarioCalculo2.setHoraFin("10:00");

                Horario horarioCalculo3 = new Horario();
                horarioCalculo3.setDia(Dia.VIERNES);
                horarioCalculo3.setHoraInicio("08:00");
                horarioCalculo3.setHoraFin("10:00");

                // Crear Horarios para Programación
                Horario horarioProgramacion1 = new Horario();
                horarioProgramacion1.setDia(Dia.MARTES);
                horarioProgramacion1.setHoraInicio("14:00");
                horarioProgramacion1.setHoraFin("16:00");

                Horario horarioProgramacion2 = new Horario();
                horarioProgramacion2.setDia(Dia.JUEVES);
                horarioProgramacion2.setHoraInicio("14:00");
                horarioProgramacion2.setHoraFin("16:00");

                Horario horarioProgramacion3 = new Horario();
                horarioProgramacion3.setDia(Dia.VIERNES);
                horarioProgramacion3.setHoraInicio("16:00");
                horarioProgramacion3.setHoraFin("18:00");

                Horario horarioProgramacion4 = new Horario();
                horarioProgramacion4.setDia(Dia.SABADO);
                horarioProgramacion4.setHoraInicio("10:00");
                horarioProgramacion4.setHoraFin("12:00");

                // Crear Grupos
                Grupo grupoCalculo = new Grupo();
                grupoCalculo.setId("CALC-01");
                grupoCalculo.setCapacidad(30);
                grupoCalculo.setCantidadInscritos(25);
                grupoCalculo.setEstaCompleto(false);
                grupoCalculo.setMateria(calculo);
                grupoCalculo.setHorarios(Arrays.asList(horarioCalculo1, horarioCalculo2, horarioCalculo3));

                Grupo grupoProgramacion = new Grupo();
                grupoProgramacion.setId("POO-01");
                grupoProgramacion.setCapacidad(25);
                grupoProgramacion.setCantidadInscritos(20);
                grupoProgramacion.setEstaCompleto(false);
                grupoProgramacion.setMateria(programacion);
                grupoProgramacion.setHorarios(Arrays.asList(
                    horarioProgramacion1,
                    horarioProgramacion2,
                    horarioProgramacion3,
                    horarioProgramacion4
                ));

                // Crear Registros de Materias
                RegistroMaterias registroCalculo = new RegistroMaterias();
                registroCalculo.setGrupo(grupoCalculo);
                registroCalculo.setEstado(Semaforo.VERDE);

                RegistroMaterias registroProgramacion = new RegistroMaterias();
                registroProgramacion.setGrupo(grupoProgramacion);
                registroProgramacion.setEstado(Semaforo.AZUL);

                // Crear un Semestre con los registros
                Semestre primerSemestre = new Semestre();
                primerSemestre.setNumero(1);
                primerSemestre.setRegistros(Arrays.asList(registroCalculo, registroProgramacion));

                // Crear el Estudiante
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre("Juan");
                estudiante.setApellido("Pérez");
                estudiante.setEmail(testEmail);
                estudiante.setContraseña("password123");
                estudiante.setRol(Rol.ESTUDIANTE);
                estudiante.setCarrera(Facultad.INGENIERIA_SISTEMAS);
                estudiante.setSemestres(Arrays.asList(primerSemestre));

                // Guardar el estudiante (esto guardará toda la estructura anidada)
                usuarioRepository.save(estudiante);

                System.out.println("¡Base de datos semilla creada con éxito!");

            } catch (DataAccessException e) {
                System.err.println("Error al acceder a la base de datos: " + e.getMessage());

                // Intento alternativo de limpieza para resolver el problema de duplicados
                try {
                    usuarioRepository.deleteAll();
                    System.out.println("Se han eliminado todos los usuarios para resolver el problema de duplicados.");
                    System.out.println("Por favor, reinicie la aplicación para cargar los datos de prueba.");
                } catch (Exception ex) {
                    System.err.println("No se pudieron eliminar todos los usuarios: " + ex.getMessage());
                }
            }
        };
    }
}