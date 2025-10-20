# Guía de Registro y Autenticación de Usuarios - SIRHA

## Descripción General

El sistema SIRHA implementa un flujo de registro simplificado donde solo se requieren los datos básicos del usuario. El **ID** y el **email** se generan automáticamente siguiendo reglas específicas.

## Cambios Implementados

### ¿Qué cambió?

✅ **ID automático**: Se genera un ID único de 10 dígitos numéricos  
✅ **Email automático**: Se genera siguiendo el formato institucional  
✅ **Registro simplificado**: Solo se requieren 5 campos (nombre, apellido, password, rol, facultad*)  
✅ **Validaciones mejoradas**: Contraseña segura, rol válido, facultad según rol  
✅ **DTO existente actualizado**: Se modificó `UsuarioDTO` en lugar de crear uno nuevo

### ¿Qué se modificó en el código?

1. **UsuarioDTO** - Ahora documenta que el email no debe enviarse en el registro
2. **UsuarioService.registrar()** - Genera automáticamente ID y email
3. **AuthService.register()** - Usa el DTO existente con las nuevas validaciones
4. **AuthController.register()** - Endpoint actualizado con nueva documentación

## Flujo de Registro

### Datos Requeridos

Para registrar un nuevo usuario, solo necesitas proporcionar:

1. **nombre**: Nombre del usuario
2. **apellido**: Apellido del usuario
3. **password**: Contraseña (debe cumplir requisitos de seguridad)
4. **rol**: Rol del usuario en el sistema
5. **facultad**: Solo para ESTUDIANTE y DECANO (no para ADMINISTRADOR)

### Generación Automática

El sistema genera automáticamente:

#### 1. **ID de Usuario**
- Cadena de 10 dígitos numéricos
- Rango: `0000000000` a `9999999999`
- Se verifica que sea único en el sistema
- Ejemplo: `1234567890`

#### 2. **Email Institucional**
- Formato: `{nombre}.{apellido}-{primera_letra_apellido}@mail.escuelaing.edu.co`
- Normalización automática:
  - Convierte a minúsculas
  - Elimina espacios
  - Remueve acentos
  
**Ejemplos:**
- Juan Pérez → `juan.perez-p@mail.escuelaing.edu.co`
- María García → `maria.garcia-g@mail.escuelaing.edu.co`
- Carlos López Martínez → `carlos.lopezmartinez-l@mail.escuelaing.edu.co`

## Validaciones

### Contraseña
La contraseña debe cumplir con los siguientes requisitos:

- ✅ Mínimo **8 caracteres**
- ✅ Al menos **1 letra mayúscula** (A-Z)
- ✅ Al menos **1 letra minúscula** (a-z)
- ✅ Al menos **1 dígito** (0-9)
- ✅ Al menos **1 carácter especial** (@$!%*?&.#-_)
- ✅ Debe ser **alfanumérica** (solo letras, números y caracteres especiales permitidos)

**Ejemplos válidos:**
- `Password123!`
- `Sirha2024@`
- `Secure#Pass1`

**Ejemplos inválidos:**
- `pass123` (falta mayúscula y carácter especial)
- `PASSWORD!` (falta minúscula y número)
- `Password` (falta número y carácter especial)

### Rol
Los roles válidos en el sistema son:

- `ESTUDIANTE`
- `DECANO`
- `ADMINISTRADOR`
- `PROFESOR`

### Facultad
Las facultades válidas son:

- `INGENIERIA_SISTEMAS`
- `INGENIERIA_CIVIL`
- `ADMINISTRACION`

**Reglas:**
- ✅ **ESTUDIANTE**: Facultad **obligatoria**
- ✅ **DECANO**: Facultad **obligatoria** (solo puede haber un decano por facultad)
- ❌ **ADMINISTRADOR**: **NO** debe tener facultad
- ✅ **PROFESOR**: Facultad **opcional**

## Ejemplos de Uso

### Registrar un Estudiante

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "password": "Password123!",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA_SISTEMAS"
}
```

**Response (201 CREATED):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

**Usuario Generado:**
```json
{
  "id": "1234567890",
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "password": "$2a$10$... (encriptada)",
  "rol": "ESTUDIANTE",
  "carrera": "INGENIERIA_SISTEMAS"
}
```

### Registrar un Decano

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "García López",
  "password": "Secure#Pass1",
  "rol": "DECANO",
  "facultad": "INGENIERIA_CIVIL"
}
```

**Response (201 CREATED):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "maria.garcialopez-g@mail.escuelaing.edu.co",
  "rol": "DECANO",
  "nombre": "María García López"
}
```

### Registrar un Administrador

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Carlos",
  "apellido": "Rodríguez",
  "password": "Admin2024@",
  "rol": "ADMINISTRADOR"
}
```

**Nota:** ⚠️ NO incluir `facultad` para ADMINISTRADOR

**Response (201 CREATED):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "carlos.rodriguez-r@mail.escuelaing.edu.co",
  "rol": "ADMINISTRADOR",
  "nombre": "Carlos Rodríguez"
}
```

## Login (Autenticación)

Una vez registrado, el usuario puede iniciar sesión usando el email generado:

**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "password": "Password123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

## Manejo de Errores

### Email Duplicado (409 CONFLICT)
```json
{
  "error": "El email ya está registrado en el sistema"
}
```

### Rol Inválido (409 CONFLICT)
```json
{
  "error": "Rol inválido. Roles válidos: ESTUDIANTE, DECANO, ADMINISTRADOR, PROFESOR"
}
```

### Facultad Inválida (409 CONFLICT)
```json
{
  "error": "Facultad inválida. Facultades válidas: INGENIERIA_SISTEMAS, INGENIERIA_CIVIL, ADMINISTRACION"
}
```

### Facultad No Encontrada (409 CONFLICT)
```json
{
  "error": "Carrera no encontrada: INGENIERIA_SISTEMAS"
}
```

### Decano Ya Existe (409 CONFLICT)
```json
{
  "error": "Ya existe un decano para la facultad INGENIERIA_CIVIL"
}
```

### Facultad Obligatoria (409 CONFLICT)
```json
{
  "error": "La facultad es obligatoria para el rol ESTUDIANTE"
}
```

### Administrador No Debe Tener Facultad (409 CONFLICT)
```json
{
  "error": "El rol ADMINISTRADOR no debe tener facultad asignada"
}
```

### Contraseña Inválida (400 BAD REQUEST)
```json
{
  "error": "La contraseña no cumple con los requisitos: debe tener mínimo 8 caracteres, debe contener al menos una mayúscula, debe contener al menos un número, debe contener al menos un carácter especial (@$!%*?&.#-_)"
}
```

### Credenciales Inválidas - Login (401 UNAUTHORIZED)
```json
{
  "error": "Credenciales inválidas"
}
```

## Uso del Token JWT

Una vez obtenido el token (ya sea en registro o login), debe incluirse en las siguientes peticiones:

```http
GET /api/estudiantes/me
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Cambios Técnicos Implementados

### Nuevos Componentes

1. **RegistroUsuarioDTO**: Nuevo DTO simplificado para el registro
2. **Generación de ID**: Método `generarIdUnico()` en `UsuarioService`
3. **Validaciones mejoradas**: Validación de rol, facultad y restricciones

### Métodos Actualizados

- `UsuarioService.registrar(RegistroUsuarioDTO)`: Nueva versión con generación de ID
- `AuthService.register(RegistroUsuarioDTO)`: Nueva versión para registro
- `AuthController.register()`: Actualizado para usar `RegistroUsuarioDTO`

### Validaciones Implementadas

- ✅ Validación de rol válido
- ✅ Validación de facultad válida
- ✅ Facultad obligatoria para ESTUDIANTE y DECANO
- ✅ Facultad prohibida para ADMINISTRADOR
- ✅ Solo un decano por facultad
- ✅ ID único de 10 dígitos
- ✅ Email único y generado automáticamente
- ✅ Contraseña segura con requisitos estrictos

## Notas Importantes

- 🔒 La contraseña se encripta automáticamente con BCrypt antes de guardarla
- 🔑 El token JWT se genera automáticamente después del registro
- 📧 El email es único por usuario y se genera automáticamente
- 🆔 El ID es único y se genera automáticamente
- ⚠️ No es posible cambiar el email una vez generado (debe ser único)
