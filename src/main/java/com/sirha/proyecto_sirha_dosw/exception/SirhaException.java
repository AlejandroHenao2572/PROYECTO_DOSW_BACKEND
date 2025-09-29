package com.sirha.proyecto_sirha_dosw.exception;

public class SirhaException extends Exception{
    //Mensajes de error generales
    public static final String ERROR_FALTAN_DATOS = "Faltan datos obligatorios: ";
    //Carrera
    public static final String CARRERA_NO_ENCONTRADA = "Carrera no encontrada: ";
    public static final String CARRERA_YA_EXISTE = "Ya existe una carrera con el mismo código o nombre.";
    public static final String FACULTAD_ERROR = "Tipo de carrera inválido: ";
    public static final String ERROR_CREACION_CARRERA = "Error al crear la carrera: ";
    public static final String ERROR_ASOCIAR_MATERIA = "Error al asociar la materia a la carrera: ";
    //Grupo
    public static final String ERROR_CREACION_GRUPO = "Error al crear el grupo: ";
    public static final String ERROR_ACTUALIZACION_GRUPO = "Error al actualizar el grupo: ";
    public static final String ERROR_ELIMINACION_GRUPO = "Error al eliminar el grupo: ";
    public static final String ERROR_INSCRIPCION_ESTUDIANTE = "Error al inscribir al estudiante: ";
    public static final String ERROR_DESINSCRIPCION_ESTUDIANTE = "Error al desinscribir al estudiante: ";
    public static final String GRUPO_COMPLETO = "El grupo está lleno.";
    public static final String ESTUDIANTE_YA_INSCRITO = "El estudiante ya está inscrito en el grupo.";
    public static final String ESTUDIANTE_NO_INSCRITO = "El estudiante no está inscrito en el grupo.";
    //Materia
    public static final String ERROR_CREACION_MATERIA = "Error al crear la materia: ";
    public static final String ERROR_ELIMINACION_MATERIA = "Error al eliminar la materia: ";
    public static final String ERROR_ACTUALIZACION_MATERIA = "Error al actualizar la materia: ";
    public static final String MATERIA_YA_EXISTE = "Ya existe una materia con el mismo acrónimo o nombre.";

    //Profesor
    public static final String PROFESOR_NO_ENCONTRADO = "Profesor no encontrado: ";
    //Estudiante
    public static final String ESTUDIANTE_NO_ENCONTRADO = "Estudiante no encontrado: ";
    public static final String SEMESTRE_INVALIDO = "Semestre inválido: ";
    public static final String NO_HORARIO_ENCONTRADO = "No se encontró el horario para el semestre especificado.";
    public static final String MATERIA_NO_ENCONTRADA = "Materia no encontrada: ";
    public static final String GRUPO_NO_ENCONTRADO = "Grupo no encontrado: ";
    public static final String ERROR_CREACION_SOLICITUD = "Error al crear la solicitud: ";
    public static final String SOLICITUD_NO_ENCONTRADA = "Solicitud no encontrada: ";
    //Adminstrador

    //Usuario
    public static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado: ";
    public static final String EMAIL_YA_REGISTRADO = "El email ya está registrado.";
    public static final String ROL_INVALIDO = "Rol no válido. Roles válidos: ESTUDIANTE, PROFESOR, DECANO, ADMINISTRADOR.";
    public static final String ERROR_REGISTRO_USUARIO = "Error al registrar el usuario: ";
    public static final String CREDENCIALES_INVALIDAS = "Credenciales inválidas.";
    public static final String ERROR_ELIMINACION_USUARIO = "Error al eliminar el usuario: ";
    public static final String ERROR_ACTUALIZACION_USUARIO = "Error al actualizar el usuario: ";
    public static final String DECANO_YA_EXISTE = "Ya existe un decano para la facultad: ";
    public static final String FACULTAD_INVALIDA = "Facultad no válida o no autorizada para el decano.";
    public static final String DECANO_NO_AUTORIZADO = "El decano no tiene autorización para consultar esta facultad.";
    public static final String SEMESTRE_FUERA_RANGO = "El semestre debe estar entre 1 y 10.";


    public SirhaException(String message) {
        super(message);
    }
}
