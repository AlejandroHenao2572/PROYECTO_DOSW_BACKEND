# 🔧 Solución al Error 403 Forbidden en Postman

## ❌ Problema
Swagger funciona correctamente, pero Postman devuelve **403 Forbidden** al intentar acceder a endpoints protegidos.

---

## ✅ Causas Comunes y Soluciones

### 1. ⚠️ Token JWT no se está enviando correctamente

#### Problema:
El token JWT no está en el header `Authorization` o tiene un formato incorrecto.

#### ✅ Solución:

En Postman, para **CADA petición protegida**:

**Opción A - Usar la pestaña Authorization (Recomendado)**:
1. Ve a la pestaña **"Authorization"**
2. En **"Type"**, selecciona **"Bearer Token"**
3. En el campo **"Token"**, pega **SOLO el token** (sin "Bearer ")
   ```
   eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.abc123...
   ```

**Opción B - Usar Headers manualmente**:
1. Ve a la pestaña **"Headers"**
2. Agrega un nuevo header:
   - **Key**: `Authorization`
   - **Value**: `Bearer eyJhbGc...` (DEBE incluir "Bearer " con espacio)

---

### 2. ⚠️ El token ha expirado

#### Problema:
Los tokens JWT expiran después de 24 horas.

#### ✅ Solución:

1. Haz **login nuevamente** para obtener un token fresco:
   ```http
   POST http://localhost:8080/api/auth/login
   Content-Type: application/json

   {
     "email": "tu_email@ejemplo.com",
     "password": "tu_password"
   }
   ```

2. Copia el **nuevo token** de la respuesta y úsalo en tus peticiones.

---

### 3. ⚠️ Problema de CORS en Postman

#### Problema:
Postman puede tener configuraciones de CORS que interfieren.

#### ✅ Solución:

1. En Postman, ve a **Settings** (⚙️ en la esquina superior derecha)
2. Desactiva **"SSL certificate verification"** temporalmente
3. En la pestaña **"General"**, desactiva **"Automatically follow redirects"** si está causando problemas

---

### 4. ⚠️ Rol del usuario no tiene permisos

#### Problema:
El usuario autenticado no tiene el rol necesario para acceder al endpoint.

#### ✅ Solución:

Verifica que el rol del usuario coincida con los permisos del endpoint:

| Endpoint | Rol Requerido |
|----------|---------------|
| `/api/auth/login` | Público (sin token) |
| `/api/auth/register` | Público (sin token) |
| `/api/usuarios/**` | Cualquier rol autenticado |
| `/api/carreras/**` | Cualquier rol autenticado |
| `/api/materias/**` | Cualquier rol autenticado |
| `/api/grupos/**` | Cualquier rol autenticado |
| `/api/estudiante/**` | Solo ESTUDIANTE |
| `/api/decano/**` | Solo DECANO |
| `/api/admin/**` | Solo ADMINISTRADOR |

**Ejemplo**:
- Si intentas acceder a `/api/decano/solicitudes` con un token de ESTUDIANTE → **403 Forbidden** ✅ (correcto)
- Debes usar un token de DECANO para ese endpoint

---

### 5. ⚠️ Formato incorrecto del header Authorization

#### Problema:
El header tiene espacios extras, falta "Bearer ", o tiene caracteres incorrectos.

#### ✅ Solución:

**Formato CORRECTO**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Formatos INCORRECTOS**:
```
❌ Authorization: eyJhbGc... (falta "Bearer ")
❌ Authorization: Bearer  eyJhbGc... (doble espacio)
❌ Authorization: bearer eyJhbGc... (minúscula)
❌ Authorization: BearereyJhbGc... (sin espacio después de Bearer)
```

---

### 6. ⚠️ Endpoint no existe o tiene typo

#### Problema:
La URL del endpoint tiene un error tipográfico.

#### ✅ Solución:

Verifica que la URL sea exactamente correcta:

**CORRECTO**:
```
http://localhost:8080/api/usuarios/
```

**INCORRECTO**:
```
http://localhost:8080/usuarios/      (falta /api/)
http://localhost:8080/api/usuario/   (falta la 's')
http://localhost:8080/api/Usuarios/  (mayúscula incorrecta)
```

---

## 🔍 Diagnóstico Paso a Paso

### Paso 1: Verificar que el login funciona

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "juan.perez@estudiantes.edu.co",
  "password": "password123"
}
```

**Respuesta esperada (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

Si esto **NO funciona** → El usuario no existe o las credenciales son incorrectas.

---

### Paso 2: Copiar el token completo

Copia **TODO** el valor del campo `token` de la respuesta:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

⚠️ **NO copies las comillas** `"`, solo el token.

---

### Paso 3: Configurar Authorization en Postman

**Para CADA petición protegida**:

1. Abre la petición en Postman
2. Ve a la pestaña **"Authorization"**
3. En **"Type"**, selecciona **"Bearer Token"**
4. Pega el token en el campo **"Token"** (sin "Bearer ")
5. Haz clic en **"Save"**

**Captura de ejemplo**:
```
Authorization
Type: Bearer Token
Token: [Aquí pega tu token completo]
```

---

### Paso 4: Probar con un endpoint simple

Prueba primero con un endpoint que permita cualquier rol autenticado:

```http
GET http://localhost:8080/api/usuarios/
Authorization: Bearer {tu_token}
```

**Respuesta esperada (200 OK)**:
```json
[
  {
    "id": "...",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@estudiantes.edu.co",
    "rol": "ESTUDIANTE"
  }
]
```

Si esto **funciona** → El problema es de permisos de rol.
Si esto **NO funciona** → El token no se está enviando correctamente.

---

### Paso 5: Verificar el rol del usuario

Si obtienes 403, verifica que tu token tenga el rol correcto:

1. Ve a [jwt.io](https://jwt.io)
2. Pega tu token en el campo "Encoded"
3. En la sección "Payload", verás algo como:
   ```json
   {
     "sub": "juan.perez@estudiantes.edu.co",
     "iat": 1700000000,
     "exp": 1700086400
   }
   ```

4. El usuario tiene el rol que definiste al registrarte/hacer login

---

## 🎯 Ejemplo Completo Paso a Paso

### 1️⃣ Registrar un usuario

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan.perez@estudiantes.edu.co",
  "password": "password123",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA"
}
```

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcyOTM2MDgwMCwiZXhwIjoxNzI5NDQ3MjAwfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "email": "juan.perez@estudiantes.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

---

### 2️⃣ Copiar el token

Token copiado:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcyOTM2MDgwMCwiZXhwIjoxNzI5NDQ3MjAwfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

---

### 3️⃣ Crear petición en Postman

**Petición**: Listar usuarios

1. **Método**: `GET`
2. **URL**: `http://localhost:8080/api/usuarios/`
3. **Authorization**:
   - Type: `Bearer Token`
   - Token: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuLnBlcmV6QGVzdHVkaWFudGVzLmVkdS5jbyIsImlhdCI6MTcyOTM2MDgwMCwiZXhwIjoxNzI5NDQ3MjAwfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`

---

### 4️⃣ Enviar petición

**Resultado esperado (200 OK)**:
```json
[
  {
    "id": "67123456789abcdef",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez@estudiantes.edu.co",
    "rol": "ESTUDIANTE"
  }
]
```

---

## 🚨 Si Aún Tienes Error 403

### Opción 1: Verificar headers en Postman

1. En Postman, ve a la pestaña **"Headers"**
2. Verifica que aparezca:
   ```
   Key: Authorization
   Value: Bearer eyJhbGc...
   ```
3. Asegúrate de que el header esté **marcado** (checkbox activado)

---

### Opción 2: Ver qué está enviando Postman

1. En Postman, haz clic en **"Code"** (</> icono en la derecha)
2. Selecciona **"HTTP"** en el dropdown
3. Verifica que aparezca:
   ```http
   GET /api/usuarios/ HTTP/1.1
   Host: localhost:8080
   Authorization: Bearer eyJhbGc...
   ```

---

### Opción 3: Probar con cURL

Si Postman sigue fallando, prueba con cURL en la terminal:

```bash
curl -X GET "http://localhost:8080/api/usuarios/" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Si cURL funciona pero Postman no, es un problema de configuración de Postman.

---

### Opción 4: Limpiar caché de Postman

1. En Postman, ve a **Settings** (⚙️)
2. Ve a **"Data"**
3. Haz clic en **"Clear"** al lado de "Cache"
4. Reinicia Postman

---

### Opción 5: Deshabilitar interceptores

En Postman:
1. Ve a la parte inferior izquierda
2. Si ves **"Capturing requests"** o **"Interceptor"** activo, desactívalo
3. Intenta la petición nuevamente

---

## 📋 Checklist de Verificación

Marca cada item que hayas verificado:

- [ ] El servidor Spring Boot está corriendo (`mvn spring-boot:run`)
- [ ] MongoDB está conectado
- [ ] El login/register funciona sin token (200/201)
- [ ] Copiaste el token completo sin comillas
- [ ] El token se pega en Authorization → Bearer Token
- [ ] El header Authorization tiene formato: `Bearer {token}`
- [ ] No hay espacios dobles ni caracteres extraños
- [ ] El token no ha expirado (máximo 24 horas)
- [ ] El rol del usuario coincide con el endpoint
- [ ] La URL del endpoint es correcta
- [ ] No hay typos en la URL

---

## 🎬 Video Tutorial Paso a Paso (Texto)

### Paso 1: Login
```
1. Abre Postman
2. Nueva petición: POST
3. URL: http://localhost:8080/api/auth/login
4. Body → raw → JSON
5. Pega:
   {
     "email": "juan.perez@estudiantes.edu.co",
     "password": "password123"
   }
6. Send
7. Copia el valor del campo "token" (sin comillas)
```

### Paso 2: Petición Protegida
```
1. Nueva petición: GET
2. URL: http://localhost:8080/api/usuarios/
3. Authorization → Type: Bearer Token
4. Token: [Pega el token copiado]
5. Send
6. ✅ Debes ver 200 OK con lista de usuarios
```

---

## 💡 Diferencia entre Swagger y Postman

**¿Por qué funciona en Swagger pero no en Postman?**

Swagger UI tiene integración automática con Spring Security y maneja el token JWT automáticamente cuando haces "Authorize". 

En Postman, **DEBES configurar manualmente** el header Authorization en cada petición.

---

## 🆘 Última Solución: Agregar Logs

Si nada funciona, agrega logs para ver qué está pasando:

En `JwtAuthFilter.java`, después de la línea 58, agrega:

```java
System.out.println("=== JWT Auth Filter ===");
System.out.println("Auth Header: " + authHeader);
System.out.println("Token: " + (jwt != null ? jwt.substring(0, 20) + "..." : "null"));
System.out.println("User Email: " + userEmail);
```

Ejecuta la petición desde Postman y revisa la consola del servidor para ver qué está recibiendo.

---

## ✅ Solución Confirmada

Si sigues estos pasos exactamente, debería funcionar. El problema más común es:

1. ❌ **Token copiado con comillas**: `"eyJhbGc..."` 
   ✅ **Correcto**: `eyJhbGc...`

2. ❌ **Falta "Bearer " en headers**: `eyJhbGc...`
   ✅ **Correcto**: `Bearer eyJhbGc...`

3. ❌ **Token expirado** (más de 24 horas)
   ✅ **Solución**: Haz login nuevamente

---

## 📞 Contacto

Si después de seguir todos estos pasos aún tienes problemas, verifica:
1. Los logs del servidor Spring Boot
2. La consola de Postman (View → Show Postman Console)
3. Que no haya un firewall bloqueando las peticiones

---

**¡Éxito!** 🎉 Si seguiste estos pasos, tu Postman debería funcionar correctamente ahora.
