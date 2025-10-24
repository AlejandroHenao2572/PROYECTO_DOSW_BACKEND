package com.sirha.proyecto_sirha_dosw.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sirha.proyecto_sirha_dosw.dto.CalendarioAcademicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.DisponibilidadGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.EstudianteBasicoDTO;
import com.sirha.proyecto_sirha_dosw.dto.MonitoreoGrupoDTO;
import com.sirha.proyecto_sirha_dosw.dto.PlazoSolicitudesDTO;
import com.sirha.proyecto_sirha_dosw.dto.RespuestaSolicitudDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.Estudiante;
import com.sirha.proyecto_sirha_dosw.model.Facultad;
import com.sirha.proyecto_sirha_dosw.model.Grupo;
import com.sirha.proyecto_sirha_dosw.model.Horario;
import com.sirha.proyecto_sirha_dosw.model.Materia;
import com.sirha.proyecto_sirha_dosw.model.RegistroMaterias;
import com.sirha.proyecto_sirha_dosw.model.Semaforo;
import com.sirha.proyecto_sirha_dosw.model.Solicitud;
import com.sirha.proyecto_sirha_dosw.model.SolicitudEstado;
import com.sirha.proyecto_sirha_dosw.model.Usuario;
import com.sirha.proyecto_sirha_dosw.service.DecanoService;

class DecanoControllerTest {

    @Mock
    private DecanoService decanoService;

    @InjectMocks
    private DecanoController decanoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // obtenerPorNombreYApellido
    @Test
    void testObtenerPorNombreYApellido_OK() {
        String facutal = "ingenieria_sistemas";
        String nombre = "Kevin";
        String apellido = "Cuitiva";
        Estudiante estudiante = new Estudiante();
        when(decanoService.findEstudiantesByNombreApellidoAndFacultad(nombre, apellido, facutal))
                .thenReturn(Arrays.asList(estudiante));

        ResponseEntity<List<Usuario>> response = decanoController.obtenerPorNombreYApellido(facutal, nombre, apellido);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(estudiante));
    }

    // consultarSolicitudesPorFacultad
    @Test
    void testConsultarSolicitudesPorFacultad_OK() {
        String facultad = "INGENIERIA_SISTEMAS";
        Solicitud solicitud = new Solicitud();
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(Arrays.asList(solicitud));

        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(solicitud));
    }

    @Test
    void testConsultarSolicitudesPorFacultad_BadRequest() {
        String facultad = "NO_EXISTE";
        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // consultarSolicitudesPendientesPorFacultad
    @Test
    void testConsultarSolicitudesPendientesPorFacultad_OK() {
        String facultad = "INGENIERIA_SISTEMAS";
        Solicitud solicitud = new Solicitud();
        when(decanoService.consultarSolicitudesPendientesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(Arrays.asList(solicitud));

        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPendientesPorFacultad(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(solicitud));
    }

    @Test
    void testConsultarSolicitudesPendientesPorFacultad_BadRequest() {
        String facultad = "NO_EXISTE";
        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPendientesPorFacultad(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // consultarSolicitudesPorFacultadYEstado
    @Test
    void testConsultarSolicitudesPorFacultadYEstado_OK() {
        String facultad = "INGENIERIA_SISTEMAS";
        String estado = "PENDIENTE";
        Solicitud solicitud = new Solicitud();
        when(decanoService.consultarSolicitudesPorFacultadYEstado(Facultad.INGENIERIA_SISTEMAS, SolicitudEstado.PENDIENTE))
                .thenReturn(Arrays.asList(solicitud));

        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPorFacultadYEstado(facultad, estado);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(solicitud));
    }

    @Test
    void testConsultarSolicitudesPorFacultadYEstado_BadRequest() {
        String facultad = "NO_EXISTE";
        String estado = "NO_EXISTE";
        ResponseEntity<List<Solicitud>> response = decanoController.consultarSolicitudesPorFacultadYEstado(facultad, estado);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // consultarHorarioEstudiante
    @Test
    void testConsultarHorarioEstudiante_OK() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        int semestre = 1;
        Grupo grupo = mock(Grupo.class);
        Materia materia = mock(Materia.class);
        Horario horario = mock(Horario.class);

        RegistroMaterias reg = mock(RegistroMaterias.class);
        when(reg.getGrupo()).thenReturn(grupo);
        when(grupo.getMateria()).thenReturn(materia);
        when(materia.getNombre()).thenReturn("Matematicas");
        when(grupo.getHorarios()).thenReturn(Arrays.asList(horario));

        List<RegistroMaterias> registros = Arrays.asList(reg);

        doNothing().when(decanoService).validarFacultad(facultad);
        doNothing().when(decanoService).validarSemestre(semestre);
        doNothing().when(decanoService).validarEstudianteFacultad(idEstudiante, facultad);
        when(decanoService.consultarHorarioEstudiante(idEstudiante, semestre)).thenReturn(registros);

        ResponseEntity<Object> response = decanoController.consultarHorarioEstudiante(facultad, idEstudiante, semestre);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, List<Horario>> horariosPorMateria = (Map<String, List<Horario>>)response.getBody();
        assertTrue(horariosPorMateria.containsKey("Matematicas"));
    }

    @Test
    void testConsultarHorarioEstudiante_NoHorario() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        int semestre = 1;

        doNothing().when(decanoService).validarFacultad(facultad);
        doNothing().when(decanoService).validarSemestre(semestre);
        doNothing().when(decanoService).validarEstudianteFacultad(idEstudiante, facultad);
        when(decanoService.consultarHorarioEstudiante(idEstudiante, semestre)).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = decanoController.consultarHorarioEstudiante(facultad, idEstudiante, semestre);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(SirhaException.NO_HORARIO_ENCONTRADO, response.getBody());
    }

    @Test
    void testConsultarHorarioEstudiante_BadRequest() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        int semestre = 1;
        doThrow(new SirhaException("Error")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.consultarHorarioEstudiante(facultad, idEstudiante, semestre);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testConsultarHorarioEstudiante_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        int semestre = 1;
        doNothing().when(decanoService).validarFacultad(facultad);
        doNothing().when(decanoService).validarSemestre(semestre);
        doNothing().when(decanoService).validarEstudianteFacultad(idEstudiante, facultad);
        when(decanoService.consultarHorarioEstudiante(idEstudiante, semestre)).thenThrow(new RuntimeException("Fallo interno"));

        ResponseEntity<Object> response = decanoController.consultarHorarioEstudiante(facultad, idEstudiante, semestre);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // consultarSemaforoAcademicoEstudiante
    @Test
    void testConsultarSemaforoAcademicoEstudiante_OK() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        Map<String, Semaforo> semaforo = new HashMap<>();
        semaforo.put("MAT1", Semaforo.VERDE);

        doNothing().when(decanoService).validarFacultad(facultad);
        doNothing().when(decanoService).validarEstudianteFacultad(idEstudiante, facultad);
        when(decanoService.consultarSemaforoAcademicoEstudiante(idEstudiante)).thenReturn(semaforo);

        ResponseEntity<Object> response = decanoController.consultarSemaforoAcademicoEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(semaforo, response.getBody());
    }

    @Test
    void testConsultarSemaforoAcademicoEstudiante_BadRequest() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        doThrow(new SirhaException("Error")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.consultarSemaforoAcademicoEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testConsultarSemaforoAcademicoEstudiante_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        doNothing().when(decanoService).validarFacultad(facultad);
        doNothing().when(decanoService).validarEstudianteFacultad(idEstudiante, facultad);
        when(decanoService.consultarSemaforoAcademicoEstudiante(idEstudiante)).thenThrow(new RuntimeException());

        ResponseEntity<Object> response = decanoController.consultarSemaforoAcademicoEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // consultarInformacionBasicaEstudiante
    @Test
    void testConsultarInformacionBasicaEstudiante_OK() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        EstudianteBasicoDTO dto = new EstudianteBasicoDTO();

        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.obtenerInformacionBasicaEstudiante(idEstudiante, facultad)).thenReturn(dto);

        ResponseEntity<Object> response = decanoController.consultarInformacionBasicaEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testConsultarInformacionBasicaEstudiante_BadRequest() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        doThrow(new SirhaException("Error")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.consultarInformacionBasicaEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testConsultarInformacionBasicaEstudiante_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String idEstudiante = "123";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.obtenerInformacionBasicaEstudiante(idEstudiante, facultad)).thenThrow(new RuntimeException());

        ResponseEntity<Object> response = decanoController.consultarInformacionBasicaEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // consultarDisponibilidadGrupos
    @Test
    void testConsultarDisponibilidadGrupos_OK() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String materiaAcronimo = "MAT1";
        List<DisponibilidadGrupoDTO> disponibilidad = Arrays.asList(new DisponibilidadGrupoDTO());

        when(decanoService.consultarDisponibilidadGrupos(materiaAcronimo, facultad)).thenReturn(disponibilidad);

        ResponseEntity<Object> response = decanoController.consultarDisponibilidadGrupos(facultad, materiaAcronimo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(disponibilidad, response.getBody());
    }

    @Test
    void testConsultarDisponibilidadGrupos_BadRequest() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String materiaAcronimo = "MAT1";
        when(decanoService.consultarDisponibilidadGrupos(materiaAcronimo, facultad))
                .thenThrow(new SirhaException("Error"));

        ResponseEntity<Object> response = decanoController.consultarDisponibilidadGrupos(facultad, materiaAcronimo);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testConsultarDisponibilidadGrupos_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String materiaAcronimo = "MAT1";
        when(decanoService.consultarDisponibilidadGrupos(materiaAcronimo, facultad))
                .thenThrow(new RuntimeException("Fallo"));

        ResponseEntity<Object> response = decanoController.consultarDisponibilidadGrupos(facultad, materiaAcronimo);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // responderSolicitud
    @Test
    void testResponderSolicitud_OK() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String solicitudId = "sol1";
        RespuestaSolicitudDTO respuesta = mock(RespuestaSolicitudDTO.class);
        when(respuesta.getNuevoEstado()).thenReturn(SolicitudEstado.APROBADA);

        doNothing().when(decanoService).responderSolicitud(respuesta, facultad);

        ResponseEntity<Object> response = decanoController.responderSolicitud(facultad, solicitudId, respuesta);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>)response.getBody();
        assertEquals("Solicitud respondida", body.get("mensaje"));
        assertEquals(solicitudId, body.get("solicitudId"));
        assertEquals("APROBADA", body.get("estado"));
    }

    @Test
    void testResponderSolicitud_BadRequest() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String solicitudId = "sol1";
        RespuestaSolicitudDTO respuesta = mock(RespuestaSolicitudDTO.class);
        doThrow(new SirhaException("Error")).when(decanoService).responderSolicitud(respuesta, facultad);

        ResponseEntity<Object> response = decanoController.responderSolicitud(facultad, solicitudId, respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void testResponderSolicitud_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String solicitudId = "sol1";
        RespuestaSolicitudDTO respuesta = mock(RespuestaSolicitudDTO.class);
        doThrow(new RuntimeException("Fallo")).when(decanoService).responderSolicitud(respuesta, facultad);

        ResponseEntity<Object> response = decanoController.responderSolicitud(facultad, solicitudId, respuesta);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Tests para métodos con 0% cobertura
    @Test
    void testListarUsuarios_OK() {
        String facultad = "INGENIERIA_SISTEMAS";
        Usuario usuario = new Estudiante();
        when(decanoService.findEstudiantesByFacultad(facultad))
                .thenReturn(Arrays.asList(usuario));

        ResponseEntity<Object> response = decanoController.listarUsuarios(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(((List<?>) response.getBody()).contains(usuario));
    }

    @Test
    void testListarUsuarios_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        doThrow(new SirhaException("Facultad inválida")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.listarUsuarios(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerPorId_OK() {
        String facultad = "INGENIERIA_SISTEMAS";
        String id = "EST123";
        Usuario usuario = new Estudiante();
        when(decanoService.findEstudianteByIdAndFacultad(id, facultad))
                .thenReturn(usuario);

        ResponseEntity<Object> response = decanoController.obtenerPorId(facultad, id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testObtenerPorId_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        String id = "EST123";
        doThrow(new SirhaException("Facultad inválida")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.obtenerPorId(facultad, id);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerPorEmail_OK() {
        String facultad = "ingenieria_sistemas";
        String email = "test@test.com";
        Usuario usuario = new Estudiante();
        when(decanoService.findEstudianteByEmailAndFacultad(email, facultad))
                .thenReturn(usuario);

        ResponseEntity<Object> response = decanoController.obtenerPorEmail(facultad, email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void testObtenerPorEmail_BadRequest() throws SirhaException {
        String facultad = "invalida";
        String email = "test@test.com";
        doThrow(new SirhaException("Facultad inválida")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.obtenerPorEmail(facultad, email);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerPorNombre_OK() {
        String facultad = "ingenieria_sistemas";
        String nombre = "Juan";
        Usuario usuario = new Estudiante();
        when(decanoService.findEstudiantesByNombreAndFacultad(nombre, facultad))
                .thenReturn(Arrays.asList(usuario));

        ResponseEntity<List<Usuario>> response = decanoController.obtenerPorNombre(facultad, nombre);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains(usuario));
    }

    @Test
    void testObtenerPorApellido_OK() {
        String facultad = "ingenieria_sistemas";
        String apellido = "Pérez";
        Usuario usuario = new Estudiante();
        when(decanoService.findEstudiantesByApellidoAndFacultad(apellido, facultad))
                .thenReturn(Arrays.asList(usuario));

        ResponseEntity<List<Usuario>> response = decanoController.obtenerPorApellido(facultad, apellido);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains(usuario));
    }

    @Test
    void testObtenerInformacionBasicaEstudiante_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String idEstudiante = "EST123";
        EstudianteBasicoDTO estudianteDTO = new EstudianteBasicoDTO();
        when(decanoService.obtenerInformacionBasicaEstudiante(idEstudiante, facultad))
                .thenReturn(estudianteDTO);

        ResponseEntity<Object> response = decanoController.consultarInformacionBasicaEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(estudianteDTO, response.getBody());
    }

    @Test
    void testObtenerInformacionBasicaEstudiante_BadRequest() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String idEstudiante = "INVALID";
        when(decanoService.obtenerInformacionBasicaEstudiante(idEstudiante, facultad))
                .thenThrow(new SirhaException("Estudiante no encontrado"));

        ResponseEntity<Object> response = decanoController.consultarInformacionBasicaEstudiante(facultad, idEstudiante);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Estudiante no encontrado", response.getBody());
    }

    @Test
    void testConfigurarCalendarioAcademico_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        CalendarioAcademicoDTO calendario = new CalendarioAcademicoDTO();
        doNothing().when(decanoService).configurarCalendarioAcademico(calendario, facultad);

        ResponseEntity<Object> response = decanoController.configurarCalendarioAcademico(facultad, calendario);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // La respuesta del controlador incluye más información que solo el mensaje
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testConfigurarCalendarioAcademico_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        CalendarioAcademicoDTO calendario = new CalendarioAcademicoDTO();
        doThrow(new SirhaException("Error de configuración")).when(decanoService)
                .configurarCalendarioAcademico(calendario, facultad);

        ResponseEntity<Object> response = decanoController.configurarCalendarioAcademico(facultad, calendario);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de configuración", response.getBody());
    }

    @Test
    void testConfigurarPlazoSolicitudes_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        PlazoSolicitudesDTO plazo = new PlazoSolicitudesDTO();
        doNothing().when(decanoService).configurarPlazoSolicitudes(plazo, facultad);

        ResponseEntity<Object> response = decanoController.configurarPlazoSolicitudes(facultad, plazo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // La respuesta del controlador incluye más información que solo el mensaje
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testConfigurarPlazoSolicitudes_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        PlazoSolicitudesDTO plazo = new PlazoSolicitudesDTO();
        doThrow(new SirhaException("Error de configuración")).when(decanoService)
                .configurarPlazoSolicitudes(plazo, facultad);

        ResponseEntity<Object> response = decanoController.configurarPlazoSolicitudes(facultad, plazo);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de configuración", response.getBody());
    }

    // Tests para métodos faltantes con 0% cobertura
    @Test
    void testObtenerEstadisticasGrupos_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("NORMAL", 5L);
        estadisticas.put("ADVERTENCIA", 2L);
        estadisticas.put("CRITICO", 1L);
        
        List<MonitoreoGrupoDTO> grupos = Arrays.asList(
            new MonitoreoGrupoDTO(), new MonitoreoGrupoDTO(), new MonitoreoGrupoDTO()
        );
        
        when(decanoService.obtenerEstadisticasAlertas(facultad)).thenReturn(estadisticas);
        when(decanoService.monitorearGruposPorFacultad(facultad)).thenReturn(grupos);

        ResponseEntity<Object> response = decanoController.obtenerEstadisticasGrupos(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testObtenerEstadisticasGrupos_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.obtenerEstadisticasAlertas(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.obtenerEstadisticasGrupos(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testConsultarResumenSolicitudesPorFacultad_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        List<Solicitud> solicitudes = Arrays.asList(
            new Solicitud(), new Solicitud(), new Solicitud()
        );
        
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(solicitudes);

        ResponseEntity<Object> response = decanoController.consultarResumenSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testConsultarResumenSolicitudesPorFacultad_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        doThrow(new SirhaException("Facultad inválida")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.consultarResumenSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testObtenerResumenConfiguraciones_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        CalendarioAcademicoDTO calendario = new CalendarioAcademicoDTO();
        calendario.setFechaInicio(LocalDate.of(2024, 1, 1));
        calendario.setFechaFin(LocalDate.of(2024, 12, 31));
        
        PlazoSolicitudesDTO plazo = new PlazoSolicitudesDTO();
        plazo.setFechaInicio(LocalDate.of(2024, 1, 1));
        plazo.setFechaFin(LocalDate.of(2024, 3, 31));
        
        when(decanoService.obtenerCalendarioAcademico(facultad)).thenReturn(calendario);
        when(decanoService.obtenerPlazoSolicitudes(facultad)).thenReturn(plazo);
        when(decanoService.esPlazoSolicitudesActivo(facultad)).thenReturn(true);

        ResponseEntity<Object> response = decanoController.obtenerResumenConfiguraciones(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testObtenerResumenConfiguraciones_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.obtenerCalendarioAcademico(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.obtenerResumenConfiguraciones(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testObtenerGruposConAlerta_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        List<MonitoreoGrupoDTO> grupos = Arrays.asList(
            new MonitoreoGrupoDTO(), new MonitoreoGrupoDTO()
        );
        
        when(decanoService.obtenerGruposConAlerta(facultad)).thenReturn(grupos);

        ResponseEntity<Object> response = decanoController.obtenerGruposConAlerta(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testObtenerGruposConAlerta_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.obtenerGruposConAlerta(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.obtenerGruposConAlerta(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testConsultarDetalleSolicitud_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String solicitudId = "SOL123";
        Solicitud solicitud = new Solicitud();
        solicitud.setId(solicitudId);
        
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(Arrays.asList(solicitud));

        ResponseEntity<Object> response = decanoController.consultarDetalleSolicitud(facultad, solicitudId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(solicitud, response.getBody());
    }

    @Test
    void testConsultarDetalleSolicitud_NotFound() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String solicitudId = "SOL999";
        
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(Arrays.asList());

        ResponseEntity<Object> response = decanoController.consultarDetalleSolicitud(facultad, solicitudId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Object body = response.getBody();
        assertTrue(body != null && body.toString().contains("Solicitud no encontrada"));
    }

    @Test
    void testMonitorearGrupos_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        List<MonitoreoGrupoDTO> grupos = Arrays.asList(
            new MonitoreoGrupoDTO(), new MonitoreoGrupoDTO()
        );
        
        when(decanoService.monitorearGruposPorFacultad(facultad)).thenReturn(grupos);

        ResponseEntity<Object> response = decanoController.monitorearGrupos(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testMonitorearGrupos_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.monitorearGruposPorFacultad(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.monitorearGrupos(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testObtenerCalendarioAcademico_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        CalendarioAcademicoDTO calendario = new CalendarioAcademicoDTO();
        
        when(decanoService.obtenerCalendarioAcademico(facultad)).thenReturn(calendario);

        ResponseEntity<Object> response = decanoController.obtenerCalendarioAcademico(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testObtenerCalendarioAcademico_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.obtenerCalendarioAcademico(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.obtenerCalendarioAcademico(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    @Test
    void testObtenerPlazoSolicitudes_OK() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        PlazoSolicitudesDTO plazo = new PlazoSolicitudesDTO();
        
        when(decanoService.obtenerPlazoSolicitudes(facultad)).thenReturn(plazo);

        ResponseEntity<Object> response = decanoController.obtenerPlazoSolicitudes(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
    }

    @Test
    void testObtenerPlazoSolicitudes_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        when(decanoService.obtenerPlazoSolicitudes(facultad))
                .thenThrow(new SirhaException("Facultad inválida"));

        ResponseEntity<Object> response = decanoController.obtenerPlazoSolicitudes(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Facultad inválida", response.getBody());
    }

    // Tests adicionales para mejorar cobertura

    @Test
    void testListarUsuarios_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.findEstudiantesByFacultad(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.listarUsuarios(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerPorId_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String id = "EST123";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.findEstudianteByIdAndFacultad(id, facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerPorId(facultad, id);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerPorEmail_EmptyEmail() {
        String facultad = "ingenieria_sistemas";
        String email = "";

        ResponseEntity<Object> response = decanoController.obtenerPorEmail(facultad, email);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email no puede estar vacío", response.getBody());
    }

    @Test
    void testObtenerPorEmail_NotFound() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String email = "noexiste@test.com";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.findEstudianteByEmailAndFacultad(email, facultad))
                .thenThrow(new SirhaException(SirhaException.ESTUDIANTE_NO_ENCONTRADO));

        ResponseEntity<Object> response = decanoController.obtenerPorEmail(facultad, email);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerPorEmail_InternalServerError() throws SirhaException {
        String facultad = "ingenieria_sistemas";
        String email = "test@test.com";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.findEstudianteByEmailAndFacultad(email, facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerPorEmail(facultad, email);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConfigurarCalendarioAcademico_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        CalendarioAcademicoDTO calendario = new CalendarioAcademicoDTO();
        doThrow(new RuntimeException("Error interno")).when(decanoService)
                .configurarCalendarioAcademico(calendario, facultad);

        ResponseEntity<Object> response = decanoController.configurarCalendarioAcademico(facultad, calendario);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConfigurarPlazoSolicitudes_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        PlazoSolicitudesDTO plazo = new PlazoSolicitudesDTO();
        doThrow(new RuntimeException("Error interno")).when(decanoService)
                .configurarPlazoSolicitudes(plazo, facultad);

        ResponseEntity<Object> response = decanoController.configurarPlazoSolicitudes(facultad, plazo);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerCalendarioAcademico_NotFound() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerCalendarioAcademico(facultad))
                .thenThrow(new SirhaException("Calendario no configurado"));

        ResponseEntity<Object> response = decanoController.obtenerCalendarioAcademico(facultad);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerCalendarioAcademico_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerCalendarioAcademico(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerCalendarioAcademico(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerPlazoSolicitudes_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerPlazoSolicitudes(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerPlazoSolicitudes(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerResumenConfiguraciones_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerCalendarioAcademico(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerResumenConfiguraciones(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testMonitorearGrupos_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.monitorearGruposPorFacultad(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.monitorearGrupos(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerGruposConAlerta_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerGruposConAlerta(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerGruposConAlerta(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerEstadisticasGrupos_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerEstadisticasAlertas(facultad))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.obtenerEstadisticasGrupos(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConsultarResumenSolicitudesPorFacultad_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.consultarResumenSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConsultarDetalleSolicitud_BadRequest() throws SirhaException {
        String facultad = "INVALIDA";
        String solicitudId = "SOL123";
        doThrow(new SirhaException("Facultad inválida")).when(decanoService).validarFacultad(facultad);

        ResponseEntity<Object> response = decanoController.consultarDetalleSolicitud(facultad, solicitudId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testConsultarDetalleSolicitud_InternalServerError() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        String solicitudId = "SOL123";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenThrow(new RuntimeException("Error interno"));

        ResponseEntity<Object> response = decanoController.consultarDetalleSolicitud(facultad, solicitudId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testConsultarResumenSolicitudesPorFacultad_EmptyList() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = decanoController.consultarResumenSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(0, body.get("totalSolicitudes"));
    }

    @Test
    void testConsultarResumenSolicitudesPorFacultad_WithMixedStates() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        
        Solicitud sol1 = new Solicitud();
        sol1.setEstado(SolicitudEstado.PENDIENTE);
        
        Solicitud sol2 = new Solicitud();
        sol2.setEstado(SolicitudEstado.APROBADA);
        
        Solicitud sol3 = new Solicitud();
        sol3.setEstado(SolicitudEstado.RECHAZADA);
        
        List<Solicitud> solicitudes = Arrays.asList(sol1, sol2, sol3);
        
        doNothing().when(decanoService).validarFacultad(facultad);
        when(decanoService.consultarSolicitudesPorFacultad(Facultad.INGENIERIA_SISTEMAS))
                .thenReturn(solicitudes);

        ResponseEntity<Object> response = decanoController.consultarResumenSolicitudesPorFacultad(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(3, body.get("totalSolicitudes"));
        
        Map<String, Long> resumen = (Map<String, Long>) body.get("resumen");
        assertEquals(1L, resumen.get("pendientes"));
        assertEquals(1L, resumen.get("aprobadas"));
        assertEquals(1L, resumen.get("rechazadas"));
    }

    @Test
    void testMonitorearGrupos_EmptyList() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.monitorearGruposPorFacultad(facultad))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = decanoController.monitorearGrupos(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(0, body.get("totalGrupos"));
    }

    @Test
    void testObtenerGruposConAlerta_EmptyList() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        when(decanoService.obtenerGruposConAlerta(facultad))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = decanoController.obtenerGruposConAlerta(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(0, body.get("totalGruposConAlerta"));
        assertEquals(0, ((List<?>) body.get("gruposConAlerta")).size());
    }

    @Test
    void testObtenerEstadisticasGrupos_EmptyStats() throws SirhaException {
        String facultad = "INGENIERIA_SISTEMAS";
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("NORMAL", 0L);
        estadisticas.put("ADVERTENCIA", 0L);
        estadisticas.put("CRITICO", 0L);
        
        when(decanoService.obtenerEstadisticasAlertas(facultad)).thenReturn(estadisticas);
        when(decanoService.monitorearGruposPorFacultad(facultad))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = decanoController.obtenerEstadisticasGrupos(facultad);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}