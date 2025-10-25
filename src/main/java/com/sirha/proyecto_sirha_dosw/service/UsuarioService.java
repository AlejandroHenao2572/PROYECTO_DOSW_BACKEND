package com.sirha.proyecto_sirha_dosw.service;

import com.sirha.proyecto_sirha_dosw.dto.UsuarioDTO;
import com.sirha.proyecto_sirha_dosw.exception.SirhaException;
import com.sirha.proyecto_sirha_dosw.model.*;
import com.sirha.proyecto_sirha_dosw.repository.CarreraRepository;
import com.sirha.proyecto_sirha_dosw.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import com.sirha.proyecto_sirha_dosw.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import java.util.Optional;
import java.security.SecureRandom;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios.
 *
 * <p>
 * Se encarga de acceder al repositorio {@code UsuarioRepository}
 * y proveer métodos para consultar usuarios por diferentes criterios
 * como ID, correo, rol, nombre y apellido.
 * </p>
 */

@Service
public class UsuarioService {
    // Métodos privados para validaciones
    private void validarNombre(Usuario usuario, String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            usuario.setNombre(nombre);
        }
    }

    private void validarApellido(Usuario usuario, String apellido) {
        if (apellido != null && !apellido.trim().isEmpty()) {
            usuario.setApellido(apellido);
        }
    }

    private void validarEmail(Usuario usuario, String email, String usuarioId) throws SirhaException {
        if (email != null && !email.trim().isEmpty()) {
            Optional<Usuario> existente = usuarioRepository.findByEmail(email);
            if (existente.isPresent() && !existente.get().getId().equals(usuarioId)) {
                throw new SirhaException(SirhaException.EMAIL_YA_REGISTRADO);
            }
            usuario.setEmail(email);
        }
    }

    private void validarPassword(Usuario usuario, String password) {
        if (password != null && !password.trim().isEmpty()) {
            usuario.setPassword(password);
        }
    }

    private void validarRol(Usuario usuario, String rol) throws SirhaException {
        if (rol != null && !rol.trim().isEmpty()) {
            try {
                usuario.setRol(Rol.valueOf(rol.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new SirhaException(SirhaException.ROL_INVALIDO);
            }
        }
    }

    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final SolicitudRepository solicitudRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyectar los repositorios necesarios.
     *
     * @param usuarioRepository repositorio que gestiona la persistencia de
     *                          {@link Usuario}
     * @param carreraRepository repositorio que gestiona la persistencia de
     *                          {@link Carrera}
     * @param solicitudRepository repositorio que gestiona la persistencia de
     *                          {@link Solicitud}
     * @param passwordEncoder codificador de contraseñas
     */

    public UsuarioService(UsuarioRepository usuarioRepository, CarreraRepository carreraRepository, SolicitudRepository solicitudRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.carreraRepository = carreraRepository;
        this.solicitudRepository = solicitudRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>
     * Este método genera automáticamente:
     * <ul>
     *   <li>ID: Cadena de 10 dígitos numéricos entre 0000000000 y 9999999999</li>
     *   <li>Email: Formato {nombre}.{apellido}-{primera letra del apellido}@mail.escuelaing.edu.co</li>
     * </ul>
     * </p>
     *
     * @param dto objeto {@link UsuarioDTO} con los datos del nuevo usuario (nombre, apellido, password, rol, facultad)
     * @return el {@link Usuario} recién creado y persistido
     * @throws SirhaException si el email ya existe, el rol es inválido, o hay errores de validación
     */
    public Usuario registrar(UsuarioDTO dto) throws SirhaException {
        Rol rol = validarYObtenerRol(dto.getRol());
        Facultad facultad = validarYObtenerFacultad(dto.getFacultad());
        
        validarFacultadPorRol(rol, facultad);
        validarCarreraExistente(facultad);
        validarDecanoUnico(rol, facultad);
        
        String emailGenerado = generarYValidarEmail(dto.getNombre(), dto.getApellido());
        dto.setEmail(emailGenerado);
        
        Usuario usuario = crearYConfigurarUsuario(dto, rol, facultad, emailGenerado);
        
        return usuarioRepository.insert(usuario);
    }

    /**
     * Valida y obtiene el rol desde el DTO.
     *
     * @param rolStr el rol en formato String
     * @return el enum {@link Rol} validado
     * @throws SirhaException si el rol no es válido
     */
    private Rol validarYObtenerRol(String rolStr) throws SirhaException {
        try {
            return Rol.valueOf(rolStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SirhaException(SirhaException.ROL_INVALIDO + ". Roles válidos: ESTUDIANTE, DECANO, ADMINISTRADOR, PROFESOR");
        }
    }

    /**
     * Valida y obtiene la facultad desde el DTO.
     *
     * @param facultadStr la facultad en formato String
     * @return el enum {@link Facultad} validado o null si no se proporciona
     * @throws SirhaException si la facultad no es válida
     */
    private Facultad validarYObtenerFacultad(String facultadStr) throws SirhaException {
        if (facultadStr == null || facultadStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Facultad.valueOf(facultadStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SirhaException(SirhaException.FACULTAD_ERROR + ". Facultades válidas: INGENIERIA_SISTEMAS, INGENIERIA_CIVIL, ADMINISTRACION");
        }
    }

    /**
     * Valida que la facultad sea apropiada según el rol del usuario.
     *
     * @param rol el rol del usuario
     * @param facultad la facultad asignada
     * @throws SirhaException si la validación falla
     */
    private void validarFacultadPorRol(Rol rol, Facultad facultad) throws SirhaException {
        if (requiereFacultad(rol) && facultad == null) {
            throw new SirhaException("La facultad es obligatoria para el rol " + rol.name());
        }
        
        if (rol == Rol.ADMINISTRADOR && facultad != null) {
            throw new SirhaException("El rol ADMINISTRADOR no debe tener facultad asignada");
        }
    }

    /**
     * Determina si un rol requiere facultad asignada.
     *
     * @param rol el rol a verificar
     * @return true si el rol requiere facultad, false en caso contrario
     */
    private boolean requiereFacultad(Rol rol) {
        return rol == Rol.ESTUDIANTE || rol == Rol.DECANO;
    }

    /**
     * Valida que la carrera/facultad exista en el sistema.
     *
     * @param facultad la facultad a validar
     * @throws SirhaException si la facultad no existe en el sistema
     */
    private void validarCarreraExistente(Facultad facultad) throws SirhaException {
        if (facultad != null && carreraRepository.findByNombre(facultad).isEmpty()) {
            throw new SirhaException(SirhaException.CARRERA_NO_ENCONTRADA + facultad.name());
        }
    }

    /**
     * Valida que no exista ya un decano para la facultad especificada.
     *
     * @param rol el rol del usuario
     * @param facultad la facultad a verificar
     * @throws SirhaException si ya existe un decano para esa facultad
     */
    private void validarDecanoUnico(Rol rol, Facultad facultad) throws SirhaException {
        if (rol == Rol.DECANO && facultad != null) {
            List<Usuario> decanos = usuarioRepository.findByRol(Rol.DECANO);
            for (Usuario decano : decanos) {
                if (decano instanceof Decano decanoObj && decanoObj.getFacultad().equals(facultad)) {
                    throw new SirhaException(SirhaException.DECANO_YA_EXISTE + " para la facultad " + facultad.name());
                }
            }
        }
    }

    /**
     * Genera un email y valida que no exista en el sistema.
     *
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @return el email generado
     * @throws SirhaException si el email ya está registrado
     */
    private String generarYValidarEmail(String nombre, String apellido) throws SirhaException {
        String emailGenerado = generarEmail(nombre, apellido);
        
        if (usuarioRepository.findByEmail(emailGenerado).isPresent()) {
            throw new SirhaException(SirhaException.EMAIL_YA_REGISTRADO);
        }
        
        return emailGenerado;
    }

    /**
     * Crea y configura un nuevo usuario con todos sus atributos.
     *
     * @param dto el DTO con los datos del usuario
     * @param rol el rol validado
     * @param facultad la facultad validada
     * @param email el email generado
     * @return el usuario configurado listo para persistir
     */
    private Usuario crearYConfigurarUsuario(UsuarioDTO dto, Rol rol, Facultad facultad, String email) {
        Usuario usuario = UsuarioFactory.crearUsuario(
                rol,
                dto.getNombre(),
                dto.getApellido(),
                email,
                dto.getPassword(),
                facultad);

        usuario.setId(generarIdUnico());
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        return usuario;
    }

    /**
     * Genera un ID único de 10 dígitos numéricos para un usuario.
     * 
     * <p>El ID se genera de forma criptográficamente segura usando {@link SecureRandom}
     * en el rango de 0000000000 a 9999999999.
     * Si el ID ya existe en la base de datos, se genera uno nuevo hasta encontrar uno único.</p>
     *
     * @return ID único de 10 dígitos en formato String
     */
    private String generarIdUnico() {
        SecureRandom secureRandom = new SecureRandom();
        String id;
        do {
            // Generar un número aleatorio de 10 dígitos de forma criptográficamente segura
            long numeroAleatorio = secureRandom.nextLong(10000000000L);
            // Formatear con ceros a la izquierda si es necesario
            id = String.format("%010d", numeroAleatorio);
        } while (usuarioRepository.existsById(id)); // Verificar que no exista
        
        return id;
    }
    
    /**
     * Genera un correo electrónico automáticamente basado en el nombre y apellido.
     * Formato: {nombre}.{apellido}-{primera letra del apellido}@mail.escuelaing.edu.co
     * 
     * <p>Ejemplos:</p>
     * <ul>
     *   <li>Juan Pérez -> juan.perez-p@mail.escuelaing.edu.co</li>
     *   <li>María García -> maria.garcia-g@mail.escuelaing.edu.co</li>
     *   <li>Carlos López Martínez -> carlos.lopezmartinez-l@mail.escuelaing.edu.co</li>
     * </ul>
     *
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @return el correo electrónico generado
     */
    private String generarEmail(String nombre, String apellido) {
        // Normalizar: remover espacios extras, convertir a minúsculas, remover acentos
        String nombreNormalizado = normalizarTexto(nombre);
        String apellidoNormalizado = normalizarTexto(apellido);
        
        // Obtener la primera letra del apellido
        char primeraLetraApellido = apellidoNormalizado.charAt(0);
        
        // Formato: nombre.apellido-primeraLetraApellido@mail.escuelaing.edu.co
        return String.format("%s.%s-%c@mail.escuelaing.edu.co", 
                nombreNormalizado, 
                apellidoNormalizado, 
                primeraLetraApellido);
    }
    
    /**
     * Normaliza un texto removiendo acentos, espacios extras y convirtiéndolo a minúsculas.
     *
     * @param texto el texto a normalizar
     * @return el texto normalizado
     */
    private String normalizarTexto(String texto) {
        return texto.trim()
                .toLowerCase()
                .replace(" ", "")  // Remover todos los espacios
                .replace("á", "a")
                .replace("à", "a")
                .replace("ä", "a")
                .replace("â", "a")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ë", "e")
                .replace("ê", "e")
                .replace("í", "i")
                .replace("ì", "i")
                .replace("ï", "i")
                .replace("î", "i")
                .replace("ó", "o")
                .replace("ò", "o")
                .replace("ö", "o")
                .replace("ô", "o")
                .replace("ú", "u")
                .replace("ù", "u")
                .replace("ü", "u")
                .replace("û", "u")
                .replace("ñ", "n");
    }

    /**
     * Actualiza a un usuario.
     * @param usuarioId ID del usuario.
     * @param dto datos a modificar.
     * @return usuario actualizado.
     */
    public Usuario actualizarUsuario(String usuarioId, UsuarioDTO dto) throws  SirhaException{
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new SirhaException(SirhaException.USUARIO_NO_ENCONTRADO + usuarioId);
        }
        Usuario usuario = usuarioOpt.get();
        validarNombre(usuario, dto.getNombre());
        validarApellido(usuario, dto.getApellido());
        validarEmail(usuario, dto.getEmail(), usuarioId);
        validarPassword(usuario, dto.getPassword());
        validarRol(usuario, dto.getRol());
        return usuarioRepository.save(usuario);
    }

    /**
     * Para eliminar un usuario.
     * @param usuarioId ID del usuario.
     */
    public void eliminarUsuario(String usuarioId) throws SirhaException{
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new SirhaException(SirhaException.USUARIO_NO_ENCONTRADO + usuarioId);
        }
        usuarioRepository.deleteById(usuarioId);
    }

    /**
     * Autentica a un usuario en el sistema.
     *
     * <p>
     * Busca un usuario por su correo electrónico y compara la contraseña
     * proporcionada con la almacenada en la base de datos.
     * </p>
     *
     * <p>
     * <b>Nota:</b> actualmente la contraseña se compara en texto plano.
     * Para producción se recomienda usar un mecanismo seguro como
     * {@code BCryptPasswordEncoder}.
     * </p>
     *
     * @param email       correo electrónico del usuario
     * @param rawPassword contraseña en texto plano proporcionada por el usuario
     * @return {@code true} si las credenciales son correctas, {@code false} en caso
     *         contrario
     */

    public boolean autenticar(String email, String rawPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return false;
        return passwordEncoder.matches(rawPassword, usuario.getPassword());
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link Usuario}
     */

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador único del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public Optional<Usuario> obtenerPorId(String id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Busca todos los usuarios que tengan el rol especificado.
     *
     * @param rol rol a filtrar
     * @return lista de {@link Usuario} con el rol indicado
     */

    public List<Usuario> obtenerPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Busca un usuario por su nombre.
     *
     * @param nombre nombre del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    /**
     * Busca un usuario por su apellido.
     *
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorApellido(String apellido) {
        return usuarioRepository.findByApellido(apellido);
    }

    /**
     * Busca un usuario por su nombre y apellido.
     *
     * @param nombre   nombre del usuario
     * @param apellido apellido del usuario
     * @return un {@link Optional} que contiene el {@link Usuario} si existe, o
     *         vacío si no
     */

    public List<Usuario> obtenerPorNombreYApellido(String nombre, String apellido) {
        return usuarioRepository.findByNombreAndApellido(nombre, apellido);
    }

    /**
     * Consulta todas las solicitudes que se encuentran en un estado específico.
     * 
     * @param estado el estado de las solicitudes a consultar (PENDIENTE, EN_REVISION, APROBADA, RECHAZADA)
     * @return lista de {@link Solicitud} en el estado indicado
     * @throws SirhaException si el estado no es válido
     */
    public List<Solicitud> consultarSolicitudesPorEstado(SolicitudEstado estado) throws SirhaException {
        if (estado == null) {
            throw new SirhaException("El estado de la solicitud no puede ser nulo");
        }
        return solicitudRepository.findByEstado(estado);
    }

    /**
     * Consulta todas las solicitudes existentes en el sistema.
     * 
     * @return lista completa de {@link Solicitud}
     */
    public List<Solicitud> consultarTodasLasSolicitudes() {
        return solicitudRepository.findAll();
    }

}
