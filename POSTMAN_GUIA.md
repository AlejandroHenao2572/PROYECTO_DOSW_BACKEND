# 🔐 Guía de Pruebas en Postman - Sistema SIRHA

## Tabla de Contenidos
1. [Configuración Inicial](#configuración-inicial)
2. [Registro de Usuarios](#registro-de-usuarios)
3. [Login de Usuarios](#login-de-usuarios)
4. [Uso del Token JWT](#uso-del-token-jwt)
5. [Ejemplos por Rol](#ejemplos-por-rol)

---

## Configuración Inicial

### Requisitos Previos
1. Tener **Postman** instalado
2. El servidor Spring Boot debe estar corriendo en: `http://localhost:8080`
3. MongoDB Atlas debe estar conectado

### URL Base
```
http://localhost:8080
```

---

## Registro de Usuarios

### 📝 Endpoint: Registrar Usuario
**POST** `/api/auth/register`

Este endpoint permite registrar nuevos usuarios sin necesidad de autenticación.

### Pasos en Postman:

#### 1️⃣ **Registrar un ESTUDIANTE**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@estudiantes.edu.co",
  "password": "password123",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA"
}
```

**Response exitosa (201 CREATED)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123...",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

**⚠️ Nota**: Guarda el `token` que recibes, lo necesitarás para hacer peticiones autenticadas.

---

#### 2️⃣ **Registrar un DECANO**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "nombre": "María",
  "apellido": "García",
  "email": "maria.garcia@decanos.edu.co",
  "password": "decanopwd456",
  "rol": "DECANO",
  "facultad": "INGENIERIA"
}
```

**Response exitosa (201 CREATED)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "maria.garcia@decanos.edu.co",
  "rol": "DECANO",
  "nombre": "María García"
}
```

---

#### 3️⃣ **Registrar un ADMINISTRADOR**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/register`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "nombre": "Carlos",
  "apellido": "Rodríguez",
  "email": "carlos.rodriguez@admin.edu.co",
  "password": "adminpwd789",
  "rol": "ADMINISTRADOR"
}
```

**⚠️ Nota**: El ADMINISTRADOR NO requiere facultad, así que se omite ese campo.

**Response exitosa (201 CREATED)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "carlos.rodriguez@admin.edu.co",
  "rol": "ADMINISTRADOR",
  "nombre": "Carlos Rodríguez"
}
```

---

### ❌ Posibles Errores en Registro

#### Error: Email Duplicado (409 CONFLICT)
```json
{
  "error": "El email ya está registrado en el sistema"
}
```
**Solución**: Use un email diferente.

#### Error: Datos Inválidos (400 BAD REQUEST)
```json
{
  "error": "Los datos proporcionados no son válidos"
}
```
**Solución**: Verifique que todos los campos requeridos estén presentes y sean válidos.

#### Error: Facultad Requerida
Si intenta registrar un ESTUDIANTE o DECANO sin facultad:
```json
{
  "error": "La facultad es requerida para estudiantes y decanos"
}
```

---

## Login de Usuarios

### 🔑 Endpoint: Iniciar Sesión
**POST** `/api/auth/login`

Este endpoint permite autenticar usuarios existentes.

### Pasos en Postman:

#### 1️⃣ **Login como ESTUDIANTE**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "email": "juan.perez@estudiantes.edu.co",
  "password": "password123"
}
```

**Response exitosa (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123...",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

---

#### 2️⃣ **Login como DECANO**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "email": "maria.garcia@decanos.edu.co",
  "password": "decanopwd456"
}
```

**Response exitosa (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "maria.garcia@decanos.edu.co",
  "rol": "DECANO",
  "nombre": "María García"
}
```

---

#### 3️⃣ **Login como ADMINISTRADOR**

**Request:**
- **Método**: `POST`
- **URL**: `http://localhost:8080/api/auth/login`
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body (raw - JSON)**:
```json
{
  "email": "carlos.rodriguez@admin.edu.co",
  "password": "adminpwd789"
}
```

**Response exitosa (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "carlos.rodriguez@admin.edu.co",
  "rol": "ADMINISTRADOR",
  "nombre": "Carlos Rodríguez"
}
```

---

### ❌ Posibles Errores en Login

#### Error: Credenciales Inválidas (401 UNAUTHORIZED)
```json
{
  "error": "Credenciales inválidas"
}
```
**Solución**: Verifique que el email y contraseña sean correctos.

#### Error: Usuario No Encontrado (401 UNAUTHORIZED)
```json
{
  "error": "Usuario no encontrado"
}
```
**Solución**: El email no está registrado. Use el endpoint de registro primero.

---

## Uso del Token JWT

### 🎯 ¿Cómo usar el token en Postman?

Una vez que obtienes el token del login o registro, debes incluirlo en todas las peticiones a endpoints protegidos.

#### Pasos:

1. **Copia el token** de la respuesta (el campo `"token"`)

2. En Postman, para cada petición protegida:
   - Ve a la pestaña **"Authorization"**
   - En **"Type"**, selecciona **"Bearer Token"**
   - Pega el token en el campo **"Token"**

   O alternativamente:
   - Ve a la pestaña **"Headers"**
   - Agrega un nuevo header:
     - **Key**: `Authorization`
     - **Value**: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...` (pon "Bearer " seguido de tu token)

### Ejemplo Visual:

```
Headers:
Key: Authorization
Value: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123...
```

---

## Ejemplos por Rol

### 👨‍🎓 ESTUDIANTE - Endpoints Permitidos

#### ✅ Consultar sus propios datos
**GET** `/api/usuarios/{usuarioId}`

**Headers**:
```
Authorization: Bearer {token_estudiante}
```

**Response (200 OK)**:
```json
{
  "id": "20221005001",
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA"
}
```

---

#### ✅ Listar materias
**GET** `/api/materias/`

**Headers**:
```
Authorization: Bearer {token_estudiante}
```

---

#### ✅ Consultar grupos
**GET** `/api/grupos/`

**Headers**:
```
Authorization: Bearer {token_estudiante}
```

---

#### ❌ Intentar acceder a endpoint de DECANO (debe fallar)
**GET** `/api/decano/solicitudes`

**Headers**:
```
Authorization: Bearer {token_estudiante}
```

**Response (403 FORBIDDEN)**:
```json
{
  "error": "Acceso denegado"
}
```

---

### 👔 DECANO - Endpoints Permitidos

#### ✅ Consultar solicitudes pendientes
**GET** `/api/decano/solicitudes`

**Headers**:
```
Authorization: Bearer {token_decano}
```

---

#### ✅ Aprobar/Rechazar solicitudes
**PUT** `/api/decano/solicitudes/{id}/aprobar`

**Headers**:
```
Authorization: Bearer {token_decano}
Content-Type: application/json
```

---

#### ✅ Acceder a recursos compartidos
**GET** `/api/carreras/`

**Headers**:
```
Authorization: Bearer {token_decano}
```

---

#### ❌ Intentar acceder a endpoint de ADMIN (debe fallar)
**DELETE** `/api/admin/usuarios/{id}`

**Headers**:
```
Authorization: Bearer {token_decano}
```

**Response (403 FORBIDDEN)**:
```json
{
  "error": "Acceso denegado"
}
```

---

### 👨‍💼 ADMINISTRADOR - Endpoints Permitidos

#### ✅ Gestionar usuarios (listar todos)
**GET** `/api/usuarios/`

**Headers**:
```
Authorization: Bearer {token_admin}
```

---

#### ✅ Eliminar usuarios
**DELETE** `/api/usuarios/{usuarioId}`

**Headers**:
```
Authorization: Bearer {token_admin}
```

**Response (204 NO CONTENT)**: Sin contenido (exitoso)

---

#### ✅ Actualizar cualquier usuario
**PUT** `/api/usuarios/{usuarioId}`

**Headers**:
```
Authorization: Bearer {token_admin}
Content-Type: application/json
```

**Body**:
```json
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez García",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE"
}
```

---

#### ✅ Acceso a recursos de administración
**GET** `/api/admin/reportes`

**Headers**:
```
Authorization: Bearer {token_admin}
```

---

## 🎬 Flujo Completo de Prueba

### Escenario: Registrar y autenticar cada tipo de usuario

```
1. Registrar Estudiante
   POST /api/auth/register
   → Guardar token_estudiante

2. Registrar Decano
   POST /api/auth/register
   → Guardar token_decano

3. Registrar Administrador
   POST /api/auth/register
   → Guardar token_admin

4. Probar Login Estudiante
   POST /api/auth/login
   → Verificar que devuelve token

5. Con token_estudiante:
   GET /api/materias/
   → Debe funcionar (200 OK)
   
   GET /api/decano/solicitudes
   → Debe fallar (403 FORBIDDEN)

6. Con token_decano:
   GET /api/decano/solicitudes
   → Debe funcionar (200 OK)
   
   DELETE /api/usuarios/123
   → Debe fallar (403 FORBIDDEN)

7. Con token_admin:
   GET /api/usuarios/
   → Debe funcionar (200 OK)
   
   DELETE /api/usuarios/123
   → Debe funcionar (204 NO CONTENT)
```

---

## 📋 Checklist de Validación

### ✅ Registro
- [ ] Registrar ESTUDIANTE con facultad funciona
- [ ] Registrar DECANO con facultad funciona
- [ ] Registrar ADMINISTRADOR sin facultad funciona
- [ ] Email duplicado retorna error 409
- [ ] Registro retorna token JWT válido

### ✅ Login
- [ ] Login con credenciales correctas funciona
- [ ] Login retorna token JWT
- [ ] Login con email inexistente retorna 401
- [ ] Login con contraseña incorrecta retorna 401

### ✅ Autorización
- [ ] ESTUDIANTE puede acceder a endpoints generales
- [ ] ESTUDIANTE NO puede acceder a endpoints de DECANO
- [ ] ESTUDIANTE NO puede acceder a endpoints de ADMIN
- [ ] DECANO puede acceder a sus endpoints específicos
- [ ] DECANO NO puede acceder a endpoints de ADMIN
- [ ] ADMINISTRADOR puede acceder a todos los endpoints
- [ ] Request sin token retorna 401/403

---

## 🔧 Solución de Problemas Comunes

### Problema: "403 Forbidden"
**Causa**: El token no tiene permisos para ese endpoint.  
**Solución**: Verifique que esté usando el token del rol correcto.

### Problema: "401 Unauthorized"
**Causa**: Token inválido, expirado o no enviado.  
**Solución**: 
1. Verifique que el token esté en el header `Authorization: Bearer {token}`
2. Genere un nuevo token haciendo login nuevamente
3. Verifique que no haya espacios extras en el header

### Problema: "Email ya registrado"
**Causa**: El email ya existe en la base de datos.  
**Solución**: Use un email diferente o elimine el usuario existente.

### Problema: Token expirado
**Causa**: Los tokens JWT expiran después de 24 horas.  
**Solución**: Haga login nuevamente para obtener un nuevo token.

---

## 📝 Notas Importantes

1. **Contraseñas**: Todas las contraseñas se encriptan automáticamente con BCrypt antes de guardarse.

2. **Tokens JWT**: Los tokens expiran después de **24 horas**. Después de ese tiempo, necesitarás hacer login nuevamente.

3. **Roles**: Los roles válidos son:
   - `ESTUDIANTE` (requiere facultad)
   - `DECANO` (requiere facultad)
   - `ADMINISTRADOR` (no requiere facultad)

4. **Facultades válidas** (según tu modelo):
   - `INGENIERIA`
   - `CIENCIAS`
   - `ARTES`
   - (etc., según lo que tengas configurado en tu enum `Facultad`)

5. **Endpoints públicos** (no requieren autenticación):
   - `POST /api/auth/login`
   - `POST /api/auth/register`

6. **Endpoints protegidos**: Todos los demás endpoints requieren un token JWT válido.

---

## 🎉 ¡Listo!

Ahora puedes probar todas las funcionalidades de registro y login en Postman. Si tienes problemas, revisa la sección de solución de problemas o verifica los logs del servidor.

**¿Preguntas?** Revisa la documentación de Spring Security y JWT en el código fuente.
