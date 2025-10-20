# 📋 Colección de Peticiones para Postman - SIRHA API

## URL Base
```
http://localhost:8080
```

---

## 🔐 AUTENTICACIÓN

### 1. Registrar Estudiante

**POST** `http://localhost:8080/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
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

---

### 2. Registrar Decano

**POST** `http://localhost:8080/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
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

---

### 3. Registrar Administrador

**POST** `http://localhost:8080/api/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
```json
{
  "nombre": "Carlos",
  "apellido": "Rodríguez",
  "email": "carlos.rodriguez@admin.edu.co",
  "password": "adminpwd789",
  "rol": "ADMINISTRADOR"
}
```

**Nota:** El ADMINISTRADOR no requiere facultad

---

### 4. Login (Estudiante)

**POST** `http://localhost:8080/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
```json
{
  "email": "juan.perez@estudiantes.edu.co",
  "password": "password123"
}
```

---

### 5. Login (Decano)

**POST** `http://localhost:8080/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
```json
{
  "email": "maria.garcia@decanos.edu.co",
  "password": "decanopwd456"
}
```

---

### 6. Login (Administrador)

**POST** `http://localhost:8080/api/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (raw - JSON):**
```json
{
  "email": "carlos.rodriguez@admin.edu.co",
  "password": "adminpwd789"
}
```

---

## 👥 GESTIÓN DE USUARIOS (Requiere autenticación)

### 7. Listar Todos los Usuarios

**GET** `http://localhost:8080/api/usuarios/`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

### 8. Obtener Usuario por ID

**GET** `http://localhost:8080/api/usuarios/{usuarioId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Ejemplo:**
```
GET http://localhost:8080/api/usuarios/20221005001
```

---

### 9. Obtener Usuario por Email

**GET** `http://localhost:8080/api/usuarios/email/{email}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Ejemplo:**
```
GET http://localhost:8080/api/usuarios/email/juan.perez@estudiantes.edu.co
```

---

### 10. Obtener Usuarios por Rol

**GET** `http://localhost:8080/api/usuarios/rol/{rol}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Ejemplos:**
```
GET http://localhost:8080/api/usuarios/rol/ESTUDIANTE
GET http://localhost:8080/api/usuarios/rol/DECANO
GET http://localhost:8080/api/usuarios/rol/ADMINISTRADOR
```

---

### 11. Actualizar Usuario

**PUT** `http://localhost:8080/api/usuarios/{usuarioId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
Content-Type: application/json
```

**Body (raw - JSON):**
```json
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez García",
  "email": "juan.perez@estudiantes.edu.co",
  "password": "newpassword456",
  "rol": "ESTUDIANTE"
}
```

---

### 12. Eliminar Usuario

**DELETE** `http://localhost:8080/api/usuarios/{usuarioId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Ejemplo:**
```
DELETE http://localhost:8080/api/usuarios/20221005001
```

---

## 🎓 CARRERAS (Requiere autenticación)

### 13. Listar Todas las Carreras

**GET** `http://localhost:8080/api/carreras/`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

### 14. Obtener Carrera por ID

**GET** `http://localhost:8080/api/carreras/{carreraId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

## 📚 MATERIAS (Requiere autenticación)

### 15. Listar Todas las Materias

**GET** `http://localhost:8080/api/materias/`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

### 16. Obtener Materia por ID

**GET** `http://localhost:8080/api/materias/{materiaId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

## 👥 GRUPOS (Requiere autenticación)

### 17. Listar Todos los Grupos

**GET** `http://localhost:8080/api/grupos/`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

### 18. Obtener Grupo por ID

**GET** `http://localhost:8080/api/grupos/{grupoId}`

**Headers:**
```
Authorization: Bearer {TU_TOKEN_AQUI}
```

---

## 📊 EJEMPLOS DE RESPUESTAS

### Respuesta de Registro/Login Exitoso (200/201)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123defg456hijklmnop789qrstuvwxyz",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

### Respuesta Error: Credenciales Inválidas (401)
```json
{
  "error": "Credenciales inválidas"
}
```

### Respuesta Error: Usuario No Encontrado (401)
```json
{
  "error": "Usuario no encontrado"
}
```

### Respuesta Error: Email Duplicado (409)
```json
{
  "error": "El email ya está registrado en el sistema"
}
```

### Respuesta Error: Acceso Denegado (403)
```json
{
  "error": "Acceso denegado"
}
```

### Respuesta Error: Token Inválido/No Enviado (401)
```json
{
  "error": "No autorizado"
}
```

---

## 🎯 FLUJO DE PRUEBA COMPLETO

### Paso 1: Registrar usuarios
```
1. Registrar Estudiante    → Guardar token_estudiante
2. Registrar Decano        → Guardar token_decano
3. Registrar Administrador → Guardar token_admin
```

### Paso 2: Probar autenticación con cada rol

#### Con token_estudiante:
```
✅ GET /api/usuarios/       → Debe funcionar (200 OK)
✅ GET /api/materias/       → Debe funcionar (200 OK)
✅ GET /api/grupos/         → Debe funcionar (200 OK)
❌ GET /api/decano/...      → Debe fallar (403 Forbidden)
❌ DELETE /api/usuarios/123 → Debe fallar (403 Forbidden)
```

#### Con token_decano:
```
✅ GET /api/usuarios/       → Debe funcionar (200 OK)
✅ GET /api/decano/...      → Debe funcionar (200 OK)
❌ DELETE /api/usuarios/123 → Debe fallar (403 Forbidden)
❌ GET /api/admin/...       → Debe fallar (403 Forbidden)
```

#### Con token_admin:
```
✅ GET /api/usuarios/       → Debe funcionar (200 OK)
✅ DELETE /api/usuarios/123 → Debe funcionar (204 No Content)
✅ PUT /api/usuarios/123    → Debe funcionar (200 OK)
✅ GET /api/admin/...       → Debe funcionar (200 OK)
```

### Paso 3: Probar sin token
```
❌ GET /api/usuarios/       → Debe fallar (401 Unauthorized)
❌ GET /api/materias/       → Debe fallar (401 Unauthorized)
```

---

## 🔧 CONFIGURACIÓN EN POSTMAN

### Crear una Colección

1. **Crear colección nueva**: "SIRHA API"

2. **Configurar variable de entorno**:
   - Variable: `base_url`
   - Valor: `http://localhost:8080`

3. **Configurar variables para tokens**:
   - `token_estudiante`
   - `token_decano`
   - `token_admin`

### Guardar Token Automáticamente

En la pestaña **Tests** de las peticiones de login/register:

```javascript
// Guardar el token en una variable
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    
    // Guardar según el rol
    if (jsonData.rol === "ESTUDIANTE") {
        pm.environment.set("token_estudiante", jsonData.token);
    } else if (jsonData.rol === "DECANO") {
        pm.environment.set("token_decano", jsonData.token);
    } else if (jsonData.rol === "ADMINISTRADOR") {
        pm.environment.set("token_admin", jsonData.token);
    }
    
    console.log("Token guardado para rol: " + jsonData.rol);
}
```

### Usar Token Automáticamente

En las peticiones autenticadas, en lugar de poner el token manualmente:

**Authorization → Type: Bearer Token**
```
Token: {{token_estudiante}}
```

O en Headers:
```
Authorization: Bearer {{token_estudiante}}
```

---

## 📝 NOTAS IMPORTANTES

1. **Reemplazar {TU_TOKEN_AQUI}**: 
   - Copia el token que recibes del login/register
   - Pégalo en el header Authorization

2. **Formato del Header Authorization**:
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
   - Debe empezar con `Bearer ` (con espacio)
   - Seguido del token completo

3. **Tokens expiran en 24 horas**:
   - Si recibes error 401, haz login nuevamente

4. **IDs de ejemplo**:
   - Los IDs de usuarios/materias/grupos varían según tu BD
   - Usa los endpoints de listar para obtener IDs reales

5. **Facultades válidas**:
   - INGENIERIA
   - CIENCIAS
   - ARTES
   - (Según lo configurado en tu enum Facultad)

---

## ✅ CHECKLIST DE PRUEBAS

### Autenticación
- [ ] Registrar ESTUDIANTE funciona
- [ ] Registrar DECANO funciona
- [ ] Registrar ADMINISTRADOR funciona
- [ ] Login con credenciales correctas funciona
- [ ] Login con credenciales incorrectas falla (401)
- [ ] Email duplicado retorna error (409)

### Autorización
- [ ] ESTUDIANTE accede a endpoints generales
- [ ] ESTUDIANTE no accede a endpoints de DECANO (403)
- [ ] ESTUDIANTE no accede a endpoints de ADMIN (403)
- [ ] DECANO accede a endpoints de decano
- [ ] DECANO no accede a endpoints de ADMIN (403)
- [ ] ADMINISTRADOR accede a todos los endpoints
- [ ] Petición sin token falla (401)
- [ ] Token expirado falla (401)

### Funcionalidad
- [ ] Listar usuarios funciona
- [ ] Obtener usuario por ID funciona
- [ ] Actualizar usuario funciona
- [ ] Eliminar usuario funciona
- [ ] Listar carreras funciona
- [ ] Listar materias funciona
- [ ] Listar grupos funciona

---

## 🎉 ¡Todo Listo!

Ahora puedes copiar y pegar estas peticiones directamente en Postman para probar tu API.

**Recuerda**:
1. El servidor debe estar corriendo en `http://localhost:8080`
2. MongoDB debe estar conectado
3. Guarda los tokens que recibes del login/register
4. Incluye el token en el header Authorization de las peticiones protegidas

**¿Problemas?** Consulta `POSTMAN_GUIA.md` para más detalles.
