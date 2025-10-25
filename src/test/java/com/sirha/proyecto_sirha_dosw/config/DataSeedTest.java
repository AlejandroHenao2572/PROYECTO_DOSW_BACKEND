package com.sirha.proyecto_sirha_dosw.config;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sirha.proyecto_sirha_dosw.model.Carrera;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.Rol;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.TipoSolicitud;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.GrupoRepository;
import com.sirha.proyecto_sirha_dosw.repository.MateriaRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;

/**
 * Clase de pruebas unitarias para DataSeed.
 * Verifica que la clase inicialice correctamente los datos de prueba.
 */
@ExtendWith(MockitoExtension.class)
class DataSeedTest {

    @Mock
    private MateriaRepository materiaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private DataSeed dataSeed;

    private Carrera carreraMock;
    private Materia materiaMock;

    @BeforeEach
    void setUp() {
        carreraMock = new Carrera(Facultad.INGENIERIA_SISTEMAS, "ISIS", 8, 145);
        
        materiaMock = new Materia("Test Materia", "TM", 3, Facultad.INGENIERIA_SISTEMAS);
        
        // Configurar mocks básicos para todos los tests
        setupBasicMocks();
    }

    private void setupBasicMocks() {
        when(carreraRepository.save(any(Carrera.class))).thenReturn(carreraMock);
        when(materiaRepository.saveAll(anyList())).thenReturn(Arrays.asList(materiaMock));
        when(usuarioRepository.saveAll(anyList())).thenReturn(Arrays.asList());
        when(grupoRepository.saveAll(anyList())).thenReturn(Arrays.asList());
        when(solicitudRepository.saveAll(anyList())).thenReturn(Arrays.asList());
    }

    @Test
    @DisplayName("Debe ejecutar el proceso de seeding exitosamente")
    void testRunSuccess() throws Exception {
        // When
        dataSeed.run();

        // Then
        // Verificar que se llamen los métodos de limpieza
        verify(solicitudRepository).deleteAll();
        verify(grupoRepository).deleteAll();
        verify(materiaRepository).deleteAll();
        verify(usuarioRepository).deleteAll();
        verify(carreraRepository).deleteAll();

        // Verificar que se llamen los métodos de guardado
        verify(carreraRepository, atLeastOnce()).save(any(Carrera.class));
        verify(materiaRepository).saveAll(anyList());
        verify(usuarioRepository).saveAll(anyList());
        verify(grupoRepository).saveAll(anyList());
        verify(solicitudRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Debe limpiar todos los datos en el orden correcto")
    void testLimpiarDatos() throws Exception {
        // When
        dataSeed.run();

        // Then
        // Verificar el orden de limpieza (dependencias primero)
        var inOrder = inOrder(solicitudRepository, grupoRepository, materiaRepository, usuarioRepository, carreraRepository);
        inOrder.verify(solicitudRepository).deleteAll();
        inOrder.verify(grupoRepository).deleteAll();
        inOrder.verify(materiaRepository).deleteAll();
        inOrder.verify(usuarioRepository).deleteAll();
        inOrder.verify(carreraRepository).deleteAll();
    }

    @Test
    @DisplayName("Debe crear una carrera de Ingeniería de Sistemas")
    void testCrearCarrera() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(carreraRepository, atLeastOnce()).save(argThat(carrera -> 
            carrera.getNombre() == Facultad.INGENIERIA_SISTEMAS &&
            carrera.getCodigo().equals("ISIS") &&
            carrera.getDuracionSemestres() == 8 &&
            carrera.getCreditosTotales() == 145
        ));
    }

    @Test
    @DisplayName("Debe crear materias con los datos correctos")
    void testCrearMaterias() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(materiaRepository).saveAll(argThat(materias -> {
            List<Materia> listaMaterias = (List<Materia>) materias;
            return listaMaterias.size() == 2 &&
                   listaMaterias.stream().anyMatch(m -> m.getNombre().equals("Desarrollo de Software") && 
                                                        m.getAcronimo().equals("DOSW") && 
                                                        m.getCreditos() == 4) &&
                   listaMaterias.stream().anyMatch(m -> m.getNombre().equals("Sistemas Operativos") && 
                                                        m.getAcronimo().equals("ODSC") && 
                                                        m.getCreditos() == 3);
        }));
    }

    @Test
    @DisplayName("Debe crear usuarios con diferentes roles")
    void testCrearUsuarios() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(usuarioRepository).saveAll(argThat(usuarios -> {
            List<Usuario> listaUsuarios = (List<Usuario>) usuarios;
            long profesores = listaUsuarios.stream().filter(u -> u.getRol() == Rol.PROFESOR).count();
            long estudiantes = listaUsuarios.stream().filter(u -> u.getRol() == Rol.ESTUDIANTE).count();
            long decanos = listaUsuarios.stream().filter(u -> u.getRol() == Rol.DECANO).count();
            
            return profesores == 2 && estudiantes == 2 && decanos == 1;
        }));
    }

    @Test
    @DisplayName("Debe crear grupos con horarios y profesores asignados")
    void testCrearGrupos() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(grupoRepository).saveAll(argThat(grupos -> {
            List<Grupo> listaGrupos = (List<Grupo>) grupos;
            return listaGrupos.size() == 4 &&
                   listaGrupos.stream().allMatch(g -> g.getHorarios() != null && g.getHorarios().size() == 2) &&
                   listaGrupos.stream().allMatch(g -> g.getProfesor() != null) &&
                   listaGrupos.stream().allMatch(g -> g.getCapacidad() > 0);
        }));
    }

    @Test
    @DisplayName("Debe crear solicitudes de prueba")
    void testCrearSolicitudes() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(solicitudRepository).saveAll(argThat(solicitudes -> {
            List<Solicitud> listaSolicitudes = (List<Solicitud>) solicitudes;
            return listaSolicitudes.size() == 2 &&
                   listaSolicitudes.stream().allMatch(s -> s.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO) &&
                   listaSolicitudes.stream().allMatch(s -> s.getEstado() == SolicitudEstado.PENDIENTE) &&
                   listaSolicitudes.stream().allMatch(s -> s.getFacultad() == Facultad.INGENIERIA_SISTEMAS) &&
                   listaSolicitudes.stream().allMatch(s -> s.getNumeroRadicado() != null);
        }));
    }

    @Test
    @DisplayName("Debe manejar excepciones durante la ejecución")
    void testRunWithException() {
        // Given
        // Reset individual mocks to avoid unnecessary stubbing warnings
        reset(carreraRepository);
        reset(materiaRepository);
        reset(usuarioRepository);
        reset(grupoRepository);
        reset(solicitudRepository);
        
        doThrow(new RuntimeException("Error de base de datos")).when(carreraRepository).deleteAll();

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> dataSeed.run());
        assertNotNull(exception, "Debe lanzar una excepción");
        
        // Verificar que se intentó limpiar los datos
        verify(carreraRepository).deleteAll();
    }

    @Test
    @DisplayName("Debe verificar que los estudiantes se inscriban en grupos")
    void testInscripcionEstudiantes() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(grupoRepository).saveAll(argThat(grupos -> {
            List<Grupo> listaGrupos = (List<Grupo>) grupos;
            // Verificar que al menos algunos grupos tienen estudiantes inscritos
            return listaGrupos.stream().anyMatch(g -> g.getEstudiantesId() != null && !g.getEstudiantesId().isEmpty());
        }));
    }

    @Test
    @DisplayName("Debe crear horarios válidos para los grupos")
    void testCrearHorarios() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(grupoRepository).saveAll(argThat(grupos -> {
            List<Grupo> listaGrupos = (List<Grupo>) grupos;
            return listaGrupos.stream().allMatch(grupo -> {
                List<Horario> horarios = grupo.getHorarios();
                return horarios != null && 
                       horarios.size() == 2 &&
                       horarios.stream().allMatch(h -> h.getDia() != null) &&
                       horarios.stream().allMatch(h -> h.getHoraInicio() != null) &&
                       horarios.stream().allMatch(h -> h.getHoraFin() != null) &&
                       horarios.stream().allMatch(h -> h.getHoraInicio().isBefore(h.getHoraFin()));
            });
        }));
    }

    @Test
    @DisplayName("Debe crear estudiantes con semestres y registros de materias")
    void testCrearSemestresParaEstudiantes() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(usuarioRepository).saveAll(argThat(usuarios -> {
            List<Usuario> listaUsuarios = (List<Usuario>) usuarios;
            return listaUsuarios.stream()
                    .filter(Estudiante.class::isInstance)
                    .map(u -> (Estudiante) u)
                    .allMatch(e -> e.getSemestres() != null && 
                                  !e.getSemestres().isEmpty() &&
                                  e.getSemestres().get(0).getRegistros() != null &&
                                  e.getSemestres().get(0).getRegistros().size() == 2);
        }));
    }

    @Test
    @DisplayName("Debe validar que las materias se agreguen a la carrera")
    void testAgregarMateriasACarrera() throws Exception {
        // When
        dataSeed.run();

        // Then
        verify(carreraRepository, atLeast(2)).save(argThat(carrera -> {
            // La carrera debería tener materias después de la primera llamada save
            return carrera.getMaterias() != null;
        }));
    }
}