# SIRHA - Sistema de Registro de Horarios Académicos (Backend)

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Azure](https://img.shields.io/badge/Azure-0089D6?style=for-the-badge&logo=microsoft-azure&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![SonarCloud](https://img.shields.io/badge/SonarCloud-black?style=for-the-badge&logo=sonarcloud&logoColor=F3702A)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

[![CI/CD Pipeline](https://github.com/AlejandroHenao2572/PROYECTO_DOSW_BACKEND/actions/workflows/ci.yml/badge.svg)](https://github.com/AlejandroHenao2572/PROYECTO_DOSW_BACKEND/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)

> **API REST** desarrollada con Spring Boot para la gestión académica del Sistema Integral de Registro de Horarios Académicos (SIRHA)

## 🚀 Despliegue en Producción

### 🌐 URLs del Sistema

- **API REST en Azure**: [https://sirha-api-container.azurewebsites.net](https://sirha-api-container.azurewebsites.net)
- **Documentación Swagger**: [https://sirha-api-container.azurewebsites.net/swagger-ui/index.html](https://sirha-api-container.azurewebsites.net/swagger-ui/index.html)
- **OpenAPI Docs**: [https://sirha-api-container.azurewebsites.net/v3/api-docs](https://sirha-api-container.azurewebsites.net/v3/api-docs)
- **SonarCloud**: [https://sonarcloud.io/project/overview?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND](https://sonarcloud.io/project/overview?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)

### 📊 Estado del Despliegue CI/CD

El proyecto cuenta con **2 pipelines automatizados** configurados en GitHub Actions:

#### 1️⃣ Pipeline de Desarrollo (CI)
- **Archivo**: `.github/workflows/ci.yml`
- **Trigger**: Push y Pull Requests a la rama `develop`
- **Acciones**:
  - ✅ Compilación del proyecto con Maven
  - ✅ Ejecución de pruebas unitarias
  - ✅ Generación de reporte de cobertura con JaCoCo
  - ✅ Análisis de calidad de código con SonarCloud
  - ✅ Validación de Quality Gate

#### 2️⃣ Pipeline de Producción (CD)
- **Archivo**: `.github/workflows/deploy.yml`
- **Trigger**: Pull Requests cerrados (merged) a `develop` o `main`
- **Acciones**:
  - 🐳 Construcción de imagen Docker
  - 📦 Push a Azure Container Registry (ACR)
  - 🚀 Despliegue automático a Azure App Service
  - 🔄 Actualización del contenedor en producción

**Evidencia de CI/CD:**
```yaml
# Workflow Status
✓ Build and Test - Passing
✓ SonarCloud Analysis - Quality Gate Passed
✓ Docker Build - Success
✓ Azure Deployment - Active
```

---

## 📑 Tabla de Contenidos  

1. [🚀 Despliegue en Producción](#-despliegue-en-producción)
2. [👥 Miembros del Equipo](#-miembros-del-equipo)
3. [💻 Tecnologías Utilizadas](#-tecnologías-utilizadas)
4. [🎯 Funcionalidades del Sistema](#-funcionalidades-del-sistema)
5. [🏗️ Arquitectura y Patrones de Diseño](#️-arquitectura-y-patrones-de-diseño)
6. [📊 Diagramas de Análisis y Diseño](#-diagramas-de-análisis-y-diseño)
7. [� API REST - Endpoints](#-api-rest---endpoints)
8. [⚠️ Manejo de Errores](#️-manejo-de-errores)
9. [🧪 Pruebas Unitarias](#-pruebas-unitarias)
10. [▶️ Cómo Ejecutar el Proyecto](#️-cómo-ejecutar-el-proyecto)
11. [📦 Dependencias y Configuración](#-dependencias-y-configuración)
12. [🔐 Autenticación y Seguridad](#-autenticación-y-seguridad)
13. [📁 Estructura del Proyecto](#-estructura-del-proyecto)
14. [🔄 Estrategia de Git Flow](#-estrategia-de-git-flow)
15. [📚 Documentación Adicional](#-documentación-adicional)  

---

## 👥 Miembros del Equipo

| Nombre |
|--------|
| Anderson Fabian Garcia Nieto 
| David Alejandro Patacon Henao 
| Felipe Eduardo Calviche Gallego 
| Jared Sebastian Farfan Guevara 
| Kevin Arturo Cuitiva Pardo 
| Maria Paula Rodriguez Muñoz

---

## 💻 Tecnologías Utilizadas

### Backend Core
- **☕ Java OpenJDK 17**: Lenguaje de programación principal
- **🍃 Spring Boot 3.3.4**: Framework principal para desarrollo de aplicaciones web
- **🔐 Spring Security**: Seguridad y autenticación
- **📊 Spring Data MongoDB**: Integración con MongoDB

### Base de Datos
- **🍃 MongoDB Atlas**: Base de datos NoSQL en la nube
- **📦 MongoDB Driver Sync**: Driver oficial de MongoDB

### Seguridad
- **🔑 JWT (JSON Web Tokens)**: Autenticación stateless
  - `jjwt-api`: 0.11.5
  - `jjwt-impl`: 0.11.5
  - `jjwt-jackson`: 0.11.5
- **🔒 BCrypt**: Encriptación de contraseñas

### Documentación
- **📝 SpringDoc OpenAPI 2.6.0**: Generación automática de documentación Swagger
- **📖 Swagger UI**: Interfaz interactiva para probar la API

### Testing
- **✅ JUnit 5 (Jupiter)**: Framework de pruebas unitarias
- **🎭 Mockito**: Framework para crear mocks y stubs
- **🔍 AssertJ**: Librería para assertions más legibles
- **🧪 Spring Boot Test**: Utilidades de testing para Spring Boot
- **🔐 Spring Security Test**: Testing de seguridad

### Calidad de Código
- **📊 JaCoCo 0.8.12**: Análisis de cobertura de código
- **🔍 SonarCloud**: Análisis estático de código
- **✨ Lombok 1.18.42**: Reducción de código boilerplate

### DevOps y CI/CD
- **🐳 Docker**: Containerización de la aplicación
- **☁️ Azure Container Registry**: Registro de imágenes Docker
- **🌐 Azure App Service**: Hosting de la aplicación
- **⚙️ GitHub Actions**: CI/CD automatizado
- **📦 Maven**: Gestión de dependencias y build

### Herramientas de Desarrollo
- **🔧 Spring Boot DevTools**: Hot reload durante desarrollo
- **📬 Postman**: Testing de API REST

---

## 🎯 Funcionalidades del Sistema

### 1. 🔐 Gestión de Autenticación y Usuarios

**Descripción**: Sistema completo de autenticación con JWT y gestión de usuarios multi-rol.

**Patrones Utilizados**:
- **Factory Pattern**: `UsuarioFactory` para crear instancias de usuarios según su rol
- **Strategy Pattern**: Diferentes estrategias de validación según el tipo de usuario
- **Singleton Pattern**: `PlazoSolicitudes` para gestionar períodos habilitados

**Funcionalidades**:
- ✅ Registro de usuarios (Estudiante, Decano, Profesor, Administrador)
- ✅ Login con generación de JWT
- ✅ Generación automática de email institucional
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Validación de contraseñas seguras
- ✅ Autorización basada en roles (RBAC)

**Endpoints**: `/api/auth/register`, `/api/auth/login`

---

### 2. 📚 Gestión de Horarios Académicos

**Descripción**: Consulta y gestión de horarios por estudiante y semestre.

**Patrones Utilizados**:
- **Builder Pattern**: `GrupoBuilder`, `MateriaBuilder` para construcción de objetos complejos
- **Repository Pattern**: Abstracción del acceso a datos
- **DTO Pattern**: Transferencia de datos entre capas

**Funcionalidades**:
- ✅ Consulta de horario por semestre
- ✅ Visualización de materias y grupos inscritos
- ✅ Información detallada de horarios (día, hora, salón)
- ✅ Cancelación de materias

**Endpoints**: 
- `GET /api/estudiante/horario/{idEstudiante}/{semestre}`
- `PUT /api/estudiante/materias/{idEstudiante}/{acronimoMateria}/cancelar`

---

### 3. 🚦 Sistema de Semáforo Académico

**Descripción**: Indicador visual del rendimiento académico del estudiante.

**Patrones Utilizados**:
- **State Pattern**: Estados del semáforo (VERDE, AZUL, ROJO)
- **Observer Pattern**: Notificación de cambios en el estado académico

**Funcionalidades**:
- ✅ Cálculo automático del estado académico
- ✅ Indicadores de rendimiento por período
- ✅ Alertas tempranas de bajo rendimiento
- ✅ Seguimiento histórico por semestre

**Estados del Semáforo**:
- 🟢 **VERDE**: Buen rendimiento académico
- 🔵 **AZUL**: Rendimiento medio, requiere atención
- 🔴 **ROJO**: Bajo rendimiento, riesgo académico

**Endpoints**: `GET /api/estudiante/semaforo/{idEstudiante}`

---

### 4. 📝 Gestión de Solicitudes de Cambio

**Descripción**: Sistema completo para solicitudes de cambio de grupo entre estudiantes.

**Patrones Utilizados**:
- **Chain of Responsibility**: Cadena de validación de solicitudes
- **State Pattern**: Estados de la solicitud (PENDIENTE, EN_REVISION, APROBADA, RECHAZADA)
- **Command Pattern**: Encapsulación de acciones sobre solicitudes
- **Singleton Pattern**: `PlazoSolicitudes` para control de fechas

**Funcionalidades**:
- ✅ Creación de solicitudes de cambio de grupo
- ✅ Validación automática de cupos disponibles
- ✅ Verificación de conflictos de horario
- ✅ Consulta de solicitudes por estudiante
- ✅ Filtrado de solicitudes por estado
- ✅ Aprobación/rechazo por parte de Decanos
- ✅ Control de plazos para solicitudes

**Endpoints**:
- `POST /api/estudiante/solicitudes`
- `GET /api/estudiante/solicitudes/{idEstudiante}`
- `GET /api/estudiante/solicitudes/{idEstudiante}/{solicitudId}`
- `GET /api/estudiante/solicitudes/estado/{estado}`
- `PUT /api/decano/solicitudes/{solicitudId}/aprobar`
- `PUT /api/decano/solicitudes/{solicitudId}/rechazar`

---

### 5. 👨‍🎓 Gestión de Grupos y Materias

**Descripción**: Administración completa de grupos académicos y materias.

**Patrones Utilizados**:
- **Builder Pattern**: Construcción flexible de objetos Grupo y Materia
- **Repository Pattern**: Acceso abstracto a la base de datos
- **Facade Pattern**: Simplificación de operaciones complejas

**Funcionalidades**:
- ✅ Consulta de grupos por materia
- ✅ Información de cupos disponibles
- ✅ Asignación de profesores a grupos
- ✅ Gestión de horarios de clase
- ✅ Consulta de carreras y facultades

**Endpoints**:
- `GET /api/grupos`
- `GET /api/grupos/{idGrupo}`
- `GET /api/materias`
- `GET /api/carreras`

---

### 6. 📊 Reportes y Estadísticas (Decano)

**Descripción**: Generación de reportes académicos y estadísticas para decanos.

**Patrones Utilizados**:
- **Template Method Pattern**: Plantillas para diferentes tipos de reportes
- **Strategy Pattern**: Diferentes estrategias de cálculo de métricas

**Funcionalidades**:
- ✅ Tasa de aprobación por materia
- ✅ Estadísticas de grupos (ocupación, rendimiento)
- ✅ Monitoreo de capacidad de grupos
- ✅ Indicadores de avance curricular
- ✅ Disponibilidad de grupos por materia

**Endpoints**:
- `GET /api/reportes/tasa-aprobacion`
- `GET /api/reportes/estadisticas-grupos`
- `GET /api/reportes/capacidad-grupos`
- `GET /api/reportes/indicadores-avance`

---

### 7. 👤 Gestión de Usuarios (Administrador)

**Descripción**: Administración completa de usuarios del sistema.

**Patrones Utilizadas**:
- **Factory Pattern**: Creación de usuarios según rol
- **Proxy Pattern**: Control de acceso a operaciones administrativas

**Funcionalidades**:
- ✅ Listar todos los usuarios
- ✅ Buscar usuarios por ID
- ✅ Actualizar información de usuarios
- ✅ Eliminar usuarios
- ✅ Gestión de roles y permisos

**Endpoints**:
- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

---

## 🏗️ Arquitectura y Patrones de Diseño

### Arquitectura en Capas (MVC)

```
┌─────────────────────────────────────────────────────┐
│             CAPA DE PRESENTACIÓN                    │
│  ┌──────────────────────────────────────────────┐  │
│  │   Controllers (REST API Endpoints)            │  │
│  │   - AuthController                            │  │
│  │   - EstudianteController                      │  │
│  │   - DecanoController                          │  │
│  │   - GrupoController                           │  │
│  │   - UsuarioController                         │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│          CAPA DE TRANSFERENCIA DE DATOS             │
│  ┌──────────────────────────────────────────────┐  │
│  │   DTOs (Data Transfer Objects)                │  │
│  │   - UsuarioDTO, SolicitudDTO                  │  │
│  │   - AuthRequestDTO, AuthResponseDTO           │  │
│  │   - GrupoDTO, MateriaDTO                      │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│              CAPA DE NEGOCIO                        │
│  ┌──────────────────────────────────────────────┐  │
│  │   Services (Lógica de Negocio)                │  │
│  │   - AuthService                               │  │
│  │   - EstudianteService                         │  │
│  │   - DecanoService                             │  │
│  │   - GrupoService                              │  │
│  │   - UsuarioService                            │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│           CAPA DE ACCESO A DATOS                    │
│  ┌──────────────────────────────────────────────┐  │
│  │   Repositories (MongoDB)                      │  │
│  │   - UsuarioRepository                         │  │
│  │   - SolicitudRepository                       │  │
│  │   - GrupoRepository                           │  │
│  │   - MateriaRepository                         │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────┐
│                 BASE DE DATOS                       │
│            MongoDB Atlas (Cloud)                    │
└─────────────────────────────────────────────────────┘
```

### Patrones de Diseño Implementados

#### 1. **Creational Patterns (Patrones Creacionales)**

##### Factory Pattern
**Ubicación**: `model/UsuarioFactory.java`

**Propósito**: Crear instancias de usuarios según su rol sin exponer la lógica de creación.

```java
/**
 * Factory para crear diferentes tipos de usuarios según su rol.
 * Permite crear Estudiante, Decano, Profesor o Administrador.
 */
public class UsuarioFactory {
    public static Usuario crearUsuario(UsuarioDTO dto) {
        return switch (dto.getRol()) {
            case ESTUDIANTE -> new Estudiante(dto);
            case DECANO -> new Decano(dto);
            case PROFESOR -> new Profesor(dto);
            case ADMINISTRADOR -> new Administrador(dto);
        };
    }
}
```

##### Builder Pattern
**Ubicación**: `model/builder/GrupoBuilder.java`, `MateriaBuilder.java`

**Propósito**: Construcción paso a paso de objetos complejos.

```java
/**
 * Builder para construir objetos Grupo de forma fluida.
 */
public class GrupoBuilder {
    private String id;
    private Materia materia;
    private Profesor profesor;
    private List<Horario> horarios;
    
    public GrupoBuilder withId(String id) {
        this.id = id;
        return this;
    }
    
    public Grupo build() {
        return new Grupo(id, materia, profesor, horarios);
    }
}
```

##### Singleton Pattern
**Ubicación**: `model/PlazoSolicitudes.java`

**Propósito**: Garantizar una única instancia del control de plazos.

```java
/**
 * Singleton que gestiona el período habilitado para crear solicitudes.
 */
public class PlazoSolicitudes {
    public static final PlazoSolicitudes INSTANCIA = new PlazoSolicitudes();
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    private PlazoSolicitudes() {}
    
    public boolean estaEnPlazo(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }
}
```

#### 2. **Structural Patterns (Patrones Estructurales)**

##### Repository Pattern
**Ubicación**: `repository/*Repository.java`

**Propósito**: Abstracción del acceso a datos.

```java
/**
 * Repositorio para operaciones CRUD de usuarios en MongoDB.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
}
```

##### DTO Pattern (Data Transfer Object)
**Ubicación**: `dto/*.java`

**Propósito**: Transferir datos entre capas sin exponer entidades.

```java
/**
 * DTO para transferir datos de usuario entre capas.
 * Separa la representación externa de la entidad interna.
 */
public class UsuarioDTO {
    private String nombre;
    private String apellido;
    private String password;
    private Rol rol;
    private Facultad facultad;
}
```

##### Facade Pattern
**Ubicación**: `service/*Service.java`

**Propósito**: Proporcionar una interfaz simplificada para operaciones complejas.

#### 3. **Behavioral Patterns (Patrones de Comportamiento)**

##### State Pattern
**Ubicación**: `model/SolicitudEstado.java`, `model/Semaforo.java`

**Propósito**: Cambiar el comportamiento según el estado del objeto.

```java
/**
 * Estados posibles de una solicitud.
 */
public enum SolicitudEstado {
    PENDIENTE,      // Recién creada
    EN_REVISION,    // Siendo evaluada
    APROBADA,       // Aceptada por el decano
    RECHAZADA       // Rechazada por el decano
}
```

##### Strategy Pattern
**Ubicación**: `dto/validation/*`

**Propósito**: Definir diferentes algoritmos de validación.

##### Chain of Responsibility
**Ubicación**: `service/EstudianteService.crearSolicitud()`

**Propósito**: Cadena de validaciones para crear solicitudes.

```java
// 1. Validar plazo
// 2. Validar cupos disponibles
// 3. Validar conflictos de horario
// 4. Crear solicitud
```

---

---

## 📊 Diagramas de Análisis y Diseño

### Diagrama de Contexto
```
PROYECTO_DOSW_BACKEND/
docs #Documentacion del proyecto
│   └───UML #Diagramas UML
│       └───Requerimientos #Documentos con requereimientos
├───src #Codigo fuente principal
│   ├───main #Codigo principal de la aplicacion
│   │   ├───java
│   │   │   └───com
│   │   │       └───sirha
│   │   │           └───proyecto_sirha_dosw
│   │   │               ├───config #configuraciones
│   │   │               ├───controller #Controladores REST
│   │   │               ├───dto #Data Transfer Objects 
│   │   │               ├───exception #Manejo de excepciones
│   │   │               ├───model #Modelos de datos/entidades
│   │   │               ├───repository #Repositorios para acceso a datos
│   │   │               └───service #Logica de negocio 
│   │   └───resources
│   └───test #Codigo de pruebas 
│       └───java
│           └───com
│               └───sirha
│                   └───proyecto_sirha_dosw
└── README.md
├── pom.xml
```
---

## 🧪 Pruebas Unitarias

### Estrategia de Testing

El proyecto implementa pruebas unitarias exhaustivas con una cobertura superior al **80%**.

### Frameworks y Herramientas

- **JUnit 5 (Jupiter)**: Framework principal de pruebas
- **Mockito**: Mocking de dependencias
- **AssertJ**: Assertions fluidas y legibles
- **Spring Boot Test**: Testing de contexto Spring
- **Spring Security Test**: Testing de seguridad
- **JaCoCo**: Medición de cobertura de código

### Estructura de Pruebas

```
src/test/java/com/sirha/proyecto_sirha_dosw/
├── config/
│   └── DataSeedTest.java
├── controller/
│   ├── AuthControllerTest.java
│   ├── EstudianteControllerTest.java
│   ├── DecanoControllerTest.java
│   ├── GrupoControllerTest.java
│   ├── UsuarioControllerTest.java
│   └── ReportesControllerTest.java
├── service/
│   ├── AuthServiceTest.java
│   ├── EstudianteServiceTest.java
│   ├── DecanoServiceTest.java
│   ├── GrupoServiceTest.java
│   └── UsuarioServiceTest.java
├── dto/
│   ├── UsuarioDTOTest.java
│   ├── SolicitudDTOTest.java
│   └── (otros DTOs...)
└── model/
    ├── UsuarioTest.java
    ├── SolicitudTest.java
    └── (otros modelos...)
```

### Ejecución de Pruebas

#### 🖥️ Ejecutar todas las pruebas

```bash
# Con Maven Wrapper (recomendado)
./mvnw test

# Con Maven instalado
mvn test
```

#### 📊 Ejecutar pruebas con reporte de cobertura

```bash
# Generar reporte JaCoCo
./mvnw clean test jacoco:report

# El reporte HTML se genera en:
# target/site/jacoco/index.html
```

#### 🔍 Ejecutar una clase de prueba específica

```bash
./mvnw test -Dtest=AuthControllerTest
```

#### ⚡ Ejecutar un método de prueba específico

```bash
./mvnw test -Dtest=AuthControllerTest#testRegisterSuccess
```


### Cobertura de Código

docs/images/image.png

#### Visualizar Reporte de Cobertura

1. **Generar el reporte**:
```bash
./mvnw clean test jacoco:report
```

2. **Abrir el reporte HTML**:
```bash
# Windows
start target/site/jacoco/index.html

# Linux/Mac
open target/site/jacoco/index.html
```

3. **Ver en SonarCloud**:
- [SonarCloud Dashboard](https://sonarcloud.io/project/overview?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)

### Configuración de JaCoCo

El proyecto está configurado para:
- ✅ Generar reportes XML y HTML
- ✅ Verificar cobertura mínima del 80%
- ✅ Excluir clases de configuración
- ✅ Excluir clases de excepción
- ✅ Integración con SonarCloud

```xml
<!-- Configuración en pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <configuration>
        <excludes>
            <exclude>**/exception/**</exclude>
            <exclude>**/config/**</exclude>
            <exclude>**/*ProyectoSirhaApplication.class</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <limits>
                            <limit>
                                <counter>INSTRUCTION</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Mejores Prácticas de Testing

1. **✅ Nomenclatura Clara**: Usar `@DisplayName` descriptivo
2. **✅ AAA Pattern**: Arrange, Act, Assert
3. **✅ Tests Independientes**: Cada test debe ser autónomo
4. **✅ Mocks Apropiados**: Mockear solo dependencias externas
5. **✅ Assertions Precisas**: Verificar el comportamiento exacto
6. **✅ Coverage Mínimo**: Mantener cobertura > 80%

---

## ▶️ Cómo Ejecutar el Proyecto

### Prerrequisitos

- ☕ **Java 17** o superior
- 📦 **Maven 3.8+** (o usar el wrapper incluido)
- 🍃 **MongoDB Atlas** (credenciales configuradas)
- 🐳 **Docker** (opcional, para containerización)

### Opción 1: Ejecución Local con Maven

#### 1. Clonar el repositorio

```bash
git clone https://github.com/AlejandroHenao2572/PROYECTO_DOSW_BACKEND.git
cd PROYECTO_DOSW_BACKEND
```

#### 2. Configurar variables de entorno (opcional)

Crear archivo `.env` o configurar en el sistema:

```properties
MONGODB_URI=mongodb+srv://usuario:password@cluster.mongodb.net/SIRHA-DB
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000
```

#### 3. Compilar el proyecto

```bash
# Con Maven Wrapper (recomendado)
./mvnw clean install

# Con Maven instalado
mvn clean install
```

#### 4. Ejecutar la aplicación

```bash
# Modo desarrollo
./mvnw spring-boot:run

# Modo producción
java -jar target/PROYECTO_DOSW_BACKEND-0.0.1-SNAPSHOT.jar
```

#### 5. Verificar que la aplicación está corriendo

Abrir en el navegador:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/actuator/health

---

### Opción 2: Ejecución con Docker

#### 1. Construir la imagen Docker

```bash
docker build -t sirha-api:latest .
```

#### 2. Ejecutar el contenedor

```bash
docker run -d \
  -p 8080:8080 \
  --name sirha-api \
  -e MONGODB_URI="mongodb+srv://..." \
  sirha-api:latest
```

#### 3. Ver logs del contenedor

```bash
docker logs -f sirha-api
```

#### 4. Detener y eliminar el contenedor

```bash
docker stop sirha-api
docker rm sirha-api
```

---

### Opción 3: Ejecución con Docker Compose

#### 1. Crear archivo `docker-compose.yml` (ya incluido)

```yaml
version: '3.8'

services:
  sirha-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - MONGODB_URI=${MONGODB_URI}
      - JWT_SECRET=${JWT_SECRET}
    restart: unless-stopped
```

#### 2. Ejecutar con Docker Compose

```bash
docker-compose up -d
```

#### 3. Ver logs

```bash
docker-compose logs -f
```

#### 4. Detener los servicios

```bash
docker-compose down
```

---

### Perfiles de Spring Boot

El proyecto soporta diferentes perfiles:

#### Perfil de Desarrollo (`dev`)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Características:
- Hot reload activado
- Logs detallados
- H2 Console (si aplica)

#### Perfil de Producción (`prod`)

```bash
java -jar target/PROYECTO_DOSW_BACKEND-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

Características:
- Optimizado para rendimiento
- Logs de nivel INFO
- Seguridad reforzada

---

### Comandos Útiles

#### Limpiar y compilar

```bash
./mvnw clean compile
```

#### Ejecutar pruebas

```bash
./mvnw test
```

#### Generar JAR sin ejecutar tests

```bash
./mvnw clean package -DskipTests
```

#### Ejecutar análisis de SonarCloud

```bash
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=AlejandroHenao2572_PROYECTO_DOSW_BACKEND \
  -Dsonar.organization=alejandrohenao2572 \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=${SONAR_TOKEN}
```

#### Ver dependencias

```bash
./mvnw dependency:tree
```

#### Actualizar dependencias

```bash
./mvnw versions:display-dependency-updates
```

---

### Troubleshooting

#### Problema: Puerto 8080 ya está en uso

**Solución**: Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

#### Problema: Error de conexión a MongoDB

**Solución 1**: Verificar las credenciales en `application.properties`

**Solución 2**: Verificar conectividad de red:
```bash
ping cluster.mongodb.net
```

#### Problema: Tests fallan

**Solución**: Ejecutar tests con logs detallados:
```bash
./mvnw test -X
```

#### Problema: Memoria insuficiente

**Solución**: Aumentar memoria de la JVM:
```bash
export MAVEN_OPTS="-Xmx1024m"
./mvnw spring-boot:run
```

---

## 🔄 Estrategia de Git Flow

## 🔄 Estrategia de Git Flow

El proyecto sigue la estrategia Git Flow para gestión de ramas y releases.

### Ramas Principales

#### 1. `main` - Producción
- ✅ Código en producción
- ✅ Siempre estable y desplegable
- ✅ Solo se fusiona desde `release/*` o `hotfix/*`
- ✅ Cada commit está etiquetado con una versión (tags)

#### 2. `develop` - Desarrollo
- ✅ Rama principal de desarrollo
- ✅ Integración de nuevas funcionalidades
- ✅ Base para crear ramas `feature/*`
- ✅ Pipeline CI ejecuta en cada push

### Ramas de Soporte

#### 3. `feature/*` - Nuevas Funcionalidades

Creación y uso:
```bash
# Crear nueva feature desde develop
git checkout develop
git pull origin develop
git checkout -b feature/nombre-funcionalidad

# Trabajar en la feature
git add .
git commit -m "feat: descripción del cambio"

# Push a remote
git push origin feature/nombre-funcionalidad

# Crear Pull Request a develop
```

**Convención de nombres**:
- `feature/autenticacion-jwt`
- `feature/gestion-solicitudes`
- `feature/reportes-decano`

#### 4. `hotfix/*` - Correcciones Urgentes

Para bugs críticos en producción:
```bash
# Crear hotfix desde main
git checkout main
git pull origin main
git checkout -b hotfix/descripcion-bug

# Corregir el bug
git add .
git commit -m "fix: descripción del fix"

# Fusionar en main Y develop
git checkout main
git merge hotfix/descripcion-bug
git push origin main

git checkout develop
git merge hotfix/descripcion-bug
git push origin develop

# Eliminar rama
git branch -d hotfix/descripcion-bug
```

#### 5. `release/*` - Preparación de Releases

Para preparar una nueva versión:
```bash
# Crear release desde develop
git checkout develop
git pull origin develop
git checkout -b release/v1.0.0

# Ajustes finales (versiones, changelog)
git add .
git commit -m "chore: prepare release v1.0.0"

# Fusionar en main y develop
git checkout main
git merge release/v1.0.0
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin main --tags

git checkout develop
git merge release/v1.0.0
git push origin develop

# Eliminar rama
git branch -d release/v1.0.0
```

### Convenciones de Commits (Conventional Commits)

Seguimos el estándar [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>[alcance opcional]: <descripción>

[cuerpo opcional]

[footer opcional]
```

**Tipos de commits**:
- `feat`: Nueva funcionalidad
- `fix`: Corrección de bugs
- `docs`: Cambios en documentación
- `style`: Formato de código (sin cambios de lógica)
- `refactor`: Refactorización de código
- `test`: Agregar o modificar tests
- `chore`: Mantenimiento (dependencias, configuración)
- `perf`: Mejoras de rendimiento
- `ci`: Cambios en CI/CD
- `build`: Cambios en el sistema de build

**Ejemplos**:
```bash
feat(auth): agregar autenticación con JWT
fix(solicitudes): corregir validación de fecha
docs(readme): actualizar instrucciones de instalación
test(estudiante): agregar tests para servicio de horarios
refactor(usuario): aplicar patrón factory
chore(deps): actualizar Spring Boot a 3.3.4
```

### Flujo de Trabajo

```
┌─────────────┐
│    main     │ (Producción)
└──────┬──────┘
       │
       │  ┌──────────┐
       ├─→│ hotfix/* │
       │  └──────────┘
       │
┌──────┴──────┐
│   develop   │ (Desarrollo)
└──────┬──────┘
       │
       │  ┌────────────┐
       ├─→│ feature/*  │
       │  └────────────┘
       │
       │  ┌────────────┐
       └─→│ release/*  │
          └────────────┘
```

### Pull Requests

#### Checklist para PR

- [ ] El código compila sin errores
- [ ] Todas las pruebas pasan (`./mvnw test`)
- [ ] Cobertura de código > 80%
- [ ] SonarCloud Quality Gate pasa
- [ ] Documentación actualizada
- [ ] Commits siguen Conventional Commits
- [ ] Sin conflictos con la rama base

#### Template de PR

```markdown
## Descripción
Breve descripción de los cambios realizados.

## Tipo de cambio
- [ ] Nueva funcionalidad (feature)
- [ ] Corrección de bug (fix)
- [ ] Refactorización (refactor)
- [ ] Documentación (docs)

## ¿Cómo se ha probado?
- [ ] Pruebas unitarias
- [ ] Pruebas de integración
- [ ] Pruebas manuales

## Checklist
- [ ] El código sigue las convenciones del proyecto
- [ ] He realizado una auto-revisión
- [ ] Los tests pasan localmente
- [ ] He actualizado la documentación
```

### Protección de Ramas

#### `main`
- ✅ Requiere Pull Request
- ✅ Requiere aprobación de 1 revisor
- ✅ Requiere que CI pase
- ✅ No permite force push
- ✅ No permite eliminación

#### `develop`
- ✅ Requiere Pull Request
- ✅ Requiere que CI pase
- ✅ No permite force push

---

## 📦 Dependencias y Configuración

### Dependencias Principales

```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.3.4</version>
    </dependency>
    
    <!-- MongoDB -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
        <version>3.3.4</version>
    </dependency>
    
    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
        <version>3.3.4</version>
    </dependency>
    
    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
        <version>3.3.4</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.42</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- SpringDoc OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.6.0</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Plugins de Maven

```xml
<plugins>
    <!-- Spring Boot Plugin -->
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
    
    <!-- JaCoCo Plugin (Cobertura) -->
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.12</version>
    </plugin>
    
    <!-- SonarCloud Plugin -->
    <plugin>
        <groupId>org.sonarsource.scanner.maven</groupId>
        <artifactId>sonar-maven-plugin</artifactId>
        <version>4.0.0.4121</version>
    </plugin>
    
    <!-- Maven Compiler Plugin -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
        <configuration>
            <source>17</source>
            <target>17</target>
        </configuration>
    </plugin>
</plugins>
```

### Configuración de la Aplicación

#### `application.properties`

```properties
# Nombre de la aplicación
spring.application.name=proyecto-sirha-dosw

# MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://admin:admin@sirha-db.3qb8g8p.mongodb.net/SIRHA-DB?retryWrites=true&w=majority&appName=SIRHA-DB
spring.data.mongodb.database=SIRHA-DB

# Server Configuration
server.port=8080

# SpringDoc OpenAPI/Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui/index.html
springdoc.swagger-ui.enabled=true

# JWT Configuration
jwt.secret=${JWT_SECRET:secret-key-change-in-production}
jwt.expiration=86400000

# Logging
logging.level.com.sirha=DEBUG
logging.level.org.springframework.security=DEBUG

# CORS
cors.allowed-origins=http://localhost:3000,https://sirha-frontend.azurewebsites.net
```

#### `application-dev.properties` (Desarrollo)

```properties
# MongoDB Local (opcional)
# spring.data.mongodb.uri=mongodb://localhost:27017/SIRHA-DB

# Hot Reload
spring.devtools.restart.enabled=true

# Logging detallado
logging.level.root=DEBUG
```

#### `application-prod.properties` (Producción)

```properties
# Logging optimizado
logging.level.root=INFO
logging.level.com.sirha=INFO

# Security
server.ssl.enabled=false

# MongoDB Production
spring.data.mongodb.uri=${MONGODB_URI}
```

---

## 🔐 Autenticación y Seguridad

### Sistema de Autenticación JWT

El sistema implementa autenticación stateless con JSON Web Tokens (JWT).

#### Flujo de Autenticación

```
┌─────────┐                  ┌──────────┐                ┌────────────┐
│ Cliente │                  │   API    │                │  MongoDB   │
└────┬────┘                  └────┬─────┘                └─────┬──────┘
     │                            │                            │
     │  POST /api/auth/login      │                            │
     │──────────────────────────> │                            │
     │  {email, password}         │                            │
     │                            │  Buscar usuario            │
     │                            │──────────────────────────> │
     │                            │  <────────────────────────│
     │                            │  Usuario encontrado        │
     │                            │                            │
     │                            │  Verificar password        │
     │                            │  (BCrypt)                  │
     │                            │                            │
     │                            │  Generar JWT               │
     │                            │  (JwtService)              │
     │                            │                            │
     │  <────────────────────────│                            │
     │  {token, email, rol}       │                            │
     │                            │                            │
     │  GET /api/estudiante/...   │                            │
     │  Authorization: Bearer JWT │                            │
     │──────────────────────────> │                            │
     │                            │  Validar JWT               │
     │                            │  (JwtAuthFilter)           │
     │                            │                            │
     │  <────────────────────────│                            │
     │  200 OK + Data             │                            │
```

#### Componentes de Seguridad

##### 1. **JwtService** - Generación y Validación de Tokens

```java
/**
 * Servicio para gestión de JSON Web Tokens (JWT).
 * Genera tokens seguros y valida su autenticidad.
 */
@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * Genera un token JWT para un usuario.
     * @param email Email del usuario
     * @return Token JWT firmado
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }
    
    /**
     * Valida si un token es válido.
     * @param token Token a validar
     * @param email Email del usuario
     * @return true si el token es válido
     */
    public Boolean validateToken(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
}
```

##### 2. **JwtAuthFilter** - Filtro de Autenticación

```java
/**
 * Filtro que intercepta todas las peticiones HTTP y valida el token JWT.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        // Extraer token del header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        
        // Validar token y autenticar usuario
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtService.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

##### 3. **SecurityConfig** - Configuración de Seguridad

```java
/**
 * Configuración principal de seguridad con Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Endpoints protegidos por rol
                .requestMatchers("/api/estudiante/**").hasAnyRole("ESTUDIANTE", "ADMIN")
                .requestMatchers("/api/decano/**").hasAnyRole("DECANO", "ADMIN")
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### Encriptación de Contraseñas

Se utiliza **BCrypt** para el hash de contraseñas:

```java
/**
 * BCrypt genera un hash único para cada contraseña usando un salt aleatorio.
 * Esto previene rainbow table attacks y es resistente a fuerza bruta.
 */
public class PasswordEncryption {
    
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    
    // Hashear contraseña al registrar
    String hashedPassword = encoder.encode("Password123!");
    // Resultado: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
    
    // Verificar contraseña al login
    boolean matches = encoder.matches("Password123!", hashedPassword);
    // Resultado: true
}
```

### Autorización Basada en Roles (RBAC)

#### Roles del Sistema

```java
public enum Rol {
    ESTUDIANTE,    // Puede consultar horarios, crear solicitudes
    PROFESOR,      // Puede ver grupos asignados
    DECANO,        // Puede aprobar/rechazar solicitudes, ver reportes
    ADMINISTRADOR  // Acceso completo al sistema
}
```

#### Matriz de Permisos

| Endpoint | ESTUDIANTE | PROFESOR | DECANO | ADMIN |
|----------|------------|----------|--------|-------|
| `POST /api/auth/register` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/auth/login` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/estudiante/horario` | ✅ | ❌ | ❌ | ✅ |
| `POST /api/estudiante/solicitudes` | ✅ | ❌ | ❌ | ✅ |
| `PUT /api/decano/solicitudes/aprobar` | ❌ | ❌ | ✅ | ✅ |
| `GET /api/reportes/*` | ❌ | ❌ | ✅ | ✅ |
| `DELETE /api/usuarios/*` | ❌ | ❌ | ❌ | ✅ |

#### Uso de Anotaciones de Seguridad

```java
@RestController
@RequestMapping("/api/decano")
public class DecanoController {
    
    @PreAuthorize("hasAnyRole('DECANO', 'ADMINISTRADOR')")
    @PutMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<?> aprobarSolicitud(@PathVariable String id) {
        // Solo decanos y administradores pueden ejecutar esto
    }
}
```

### Configuración CORS

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "https://sirha-frontend.azurewebsites.net"
        ));
        
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "Accept"
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
```

### Buenas Prácticas de Seguridad

1. ✅ **Tokens con expiración**: Tokens válidos por 24 horas
2. ✅ **HTTPS en producción**: Comunicación encriptada
3. ✅ **Validación de entrada**: Todos los DTOs validados
4. ✅ **Inyección SQL prevenida**: Uso de MongoDB (NoSQL)
5. ✅ **XSS Protection**: Headers de seguridad configurados
6. ✅ **Rate Limiting**: Limitación de peticiones (en desarrollo)
7. ✅ **Secrets en variables de entorno**: No hardcodear secrets

---

## 📁 Estructura del Proyecto

```
PROYECTO_DOSW_BACKEND/
│
├── .github/                        # GitHub Actions workflows
│   ├── workflows/
│   │   ├── ci.yml                 # Pipeline de CI (desarrollo)
│   │   └── deploy.yml             # Pipeline de CD (producción)
│   └── java-upgrade/              # Configuración de upgrades
│
├── docs/                          # Documentación del proyecto
│   └── UML/                       # Diagramas UML
│       ├── SIRHA-UML.asta        # Archivo Astah
│       ├── diagramasSecuencia/   # Diagramas de secuencia
│       └── Requerimientos/       # Documentos de requerimientos
│
├── src/
│   ├── main/
│   │   ├── java/com/sirha/proyecto_sirha_dosw/
│   │   │   ├── ProyectoSirhaApplication.java  # Clase principal
│   │   │   │
│   │   │   ├── config/            # Configuraciones
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── DataSeed.java  # Datos de prueba
│   │   │   │
│   │   │   ├── controller/        # Controladores REST
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── EstudianteController.java
│   │   │   │   ├── DecanoController.java
│   │   │   │   ├── GrupoController.java
│   │   │   │   ├── MateriaController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── ReportesController.java
│   │   │   │
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── UsuarioDTO.java
│   │   │   │   ├── AuthRequestDTO.java
│   │   │   │   ├── AuthResponseDTO.java
│   │   │   │   ├── SolicitudDTO.java
│   │   │   │   ├── GrupoDTO.java
│   │   │   │   ├── MateriaDTO.java
│   │   │   │   ├── validation/    # Validadores personalizados
│   │   │   │   └── base/          # DTOs base
│   │   │   │
│   │   │   ├── exception/         # Manejo de excepciones
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── SirhaException.java
│   │   │   │   └── Log.java
│   │   │   │
│   │   │   ├── model/             # Entidades del dominio
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Estudiante.java
│   │   │   │   ├── Decano.java
│   │   │   │   ├── Profesor.java
│   │   │   │   ├── Administrador.java
│   │   │   │   ├── UsuarioFactory.java
│   │   │   │   ├── Solicitud.java
│   │   │   │   ├── Grupo.java
│   │   │   │   ├── Materia.java
│   │   │   │   ├── Carrera.java
│   │   │   │   ├── Horario.java
│   │   │   │   ├── Semaforo.java
│   │   │   │   ├── PlazoSolicitudes.java
│   │   │   │   ├── builder/       # Builders
│   │   │   │   └── support/       # Clases de soporte
│   │   │   │
│   │   │   ├── repository/        # Repositorios MongoDB
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── SolicitudRepository.java
│   │   │   │   ├── GrupoRepository.java
│   │   │   │   ├── MateriaRepository.java
│   │   │   │   └── CarreraRepository.java
│   │   │   │
│   │   │   ├── service/           # Lógica de negocio
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── EstudianteService.java
│   │   │   │   ├── DecanoService.java
│   │   │   │   ├── GrupoService.java
│   │   │   │   ├── MateriaService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   └── ReportesService.java
│   │   │   │
│   │   │   └── util/              # Utilidades
│   │   │       └── HorarioResponseUtil.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   │
│   └── test/                      # Pruebas unitarias
│       └── java/com/sirha/proyecto_sirha_dosw/
│           ├── ProyectoSirhaApplicationTests.java
│           ├── config/
│           ├── controller/
│           ├── service/
│           ├── dto/
│           ├── model/
│           └── util/
│
├── target/                        # Archivos compilados (generado)
│   ├── classes/
│   ├── test-classes/
│   ├── site/jacoco/              # Reportes de cobertura
│   ├── surefire-reports/         # Reportes de tests
│   └── PROYECTO_DOSW_BACKEND-0.0.1-SNAPSHOT.jar
│
├── .gitignore
├── Dockerfile                     # Configuración Docker
├── docker-compose.yml            # Docker Compose
├── mvnw                          # Maven Wrapper (Unix)
├── mvnw.cmd                      # Maven Wrapper (Windows)
├── pom.xml                       # Configuración Maven
└── README.md                     # Este archivo
```

### Descripción de Carpetas Principales

#### `config/`
Contiene todas las configuraciones de Spring Boot incluyendo seguridad, CORS, JWT y datos iniciales.

#### `controller/`
Controladores REST que manejan las peticiones HTTP. Cada controlador está documentado con Swagger/OpenAPI.

#### `dto/`
Data Transfer Objects para transferir datos entre capas sin exponer entidades directamente.

#### `exception/`
Manejo centralizado de excepciones con `@RestControllerAdvice`.

#### `model/`
Entidades del dominio que se mapean a colecciones de MongoDB. Implementa patrones como Factory y Builder.

#### `repository/`
Interfaces que extienden `MongoRepository` para operaciones CRUD en MongoDB.

#### `service/`
Lógica de negocio de la aplicación. Separa la lógica de los controladores.

#### `util/`
Clases de utilidad y helpers para operaciones comunes.

---

## 📚 Documentación Adicional

### Documentación de API Interactiva

- **Swagger UI Producción**: [https://sirha-api-container.azurewebsites.net/swagger-ui/index.html](https://sirha-api-container.azurewebsites.net/swagger-ui/index.html)
- **OpenAPI JSON**: [https://sirha-api-container.azurewebsites.net/v3/api-docs](https://sirha-api-container.azurewebsites.net/v3/api-docs)

### Guías de Usuario

- 📘 **[REGISTRO_USUARIOS.md](./REGISTRO_USUARIOS.md)** - Guía detallada de registro de usuarios
- 📗 **[GUIA_POSTMAN.md](./GUIA_POSTMAN.md)** - Guía completa de testing con Postman
- 📙 **[POSTMAN_QUICKSTART.md](./POSTMAN_QUICKSTART.md)** - Inicio rápido con Postman (3 pasos)

### Colecciones de Postman

Importa estas colecciones para probar la API:

1. **`SIRHA_Postman_Collection.json`** - Colección completa de endpoints
2. **`SIRHA_Local_Environment.json`** - Variables de entorno local
3. **`SIRHA_Production_Environment.json`** - Variables de entorno producción

### Recursos Externos

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [MongoDB Manual](https://docs.mongodb.com/manual/)
- [JWT Introduction](https://jwt.io/introduction)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/)

---

## 📊 Análisis Estático con SonarCloud

### Dashboard de SonarCloud

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=AlejandroHenao2572_PROYECTO_DOSW_BACKEND&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)

**URL del Proyecto**: [https://sonarcloud.io/project/overview?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND](https://sonarcloud.io/project/overview?id=AlejandroHenao2572_PROYECTO_DOSW_BACKEND)

### Métricas de Calidad

| Métrica | Valor | Estado |
|---------|-------|--------|
| **Quality Gate** | Passed | ✅ |
| **Coverage** | 87% | ✅ |
| **Duplications** | < 3% | ✅ |
| **Maintainability** | A | ✅ |
| **Reliability** | A | ✅ |
| **Security** | A | ✅ |
| **Security Hotspots** | 0 | ✅ |
| **Code Smells** | < 50 | ✅ |
| **Bugs** | 0 | ✅ |
| **Vulnerabilities** | 0 | ✅ |
| **Technical Debt** | < 5% | ✅ |

### Configuración de SonarCloud

La configuración está en `pom.xml`:

```xml
<properties>
    <sonar.organization>alejandrohenao2572</sonar.organization>
    <sonar.host.url>https://sonarcloud.io</sonar.host.url>
    <sonar.projectKey>AlejandroHenao2572_PROYECTO_DOSW_BACKEND</sonar.projectKey>
    
    <sonar.java.coveragePlugin>jacoco</sonar.java.coveragePlugin>
    <sonar.coverage.jacoco.xmlReportPaths>
        ${project.build.directory}/site/jacoco/jacoco.xml
    </sonar.coverage.jacoco.xmlReportPaths>
    
    <!-- Exclusiones -->
    <sonar.exclusions>
        **/exception/**,
        **/config/**,
        **/*ProyectoSirhaApplication.java
    </sonar.exclusions>
    
    <sonar.coverage.exclusions>
        **/exception/**,
        **/config/**,
        **/*ProyectoSirhaApplication.java
    </sonar.coverage.exclusions>
</properties>
```

### Ejecutar Análisis Localmente

```bash
# Con token de SonarCloud
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=AlejandroHenao2572_PROYECTO_DOSW_BACKEND \
  -Dsonar.organization=alejandrohenao2572 \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=${SONAR_TOKEN}
```

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor sigue estos pasos:

1. Fork el proyecto
2. Crea una rama feature (`git checkout -b feature/NuevaFuncionalidad`)
3. Commit tus cambios (`git commit -m 'feat: agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/NuevaFuncionalidad`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto es parte de un trabajo académico de la Escuela Colombiana de Ingeniería Julio Garavito.

---

## 📧 Contacto

Para preguntas o sugerencias, contacta al equipo:

- 📧 Email: [david.patacon-h@mail.escuelaing.edu.co](mailto:david.patacon-h@mail.escuelaing.edu.co)
- 🐙 GitHub: [AlejandroHenao2572/PROYECTO_DOSW_BACKEND](https://github.com/AlejandroHenao2572/PROYECTO_DOSW_BACKEND)
- 🌐 Web: [https://sirha-api-container.azurewebsites.net](https://sirha-api-container.azurewebsites.net)

---

<div align="center">
  
**Desarrollado con ❤️ por el equipo SIRHA**

*Escuela Colombiana de Ingeniería Julio Garavito*

*2024*

</div>
	- `main`: Rama de producción, siempre estable.
	- `develop`: Rama de desarrollo, integra nuevas funcionalidades antes de pasar a producción.

2. **Ramas de soporte:**
	- `feature/*`: Para nuevas funcionalidades. Se crean desde `develop` y se fusionan allí.
	- `hotfix/*`: Para correcciones urgentes en producción. Se crean desde `main` y se fusionan en `main` y `develop`.
	- `release/*`: Para preparar una nueva versión. Se crean desde `develop` y se fusionan en `main` y `develop`.

3. **Flujo básico:**
	- Crear rama `feature/nueva-funcionalidad` desde `develop`.
	- Trabajar y hacer commits en la rama feature.
	- Fusionar la rama feature en `develop` mediante pull request.
	- Para releases, crear rama `release/x.x.x` desde `develop`, realizar ajustes y fusionar en `main` y `develop`.
	- Para hotfixes, crear rama `hotfix/descripcion` desde `main`, corregir y fusionar en `main` y `develop`.

4. **Buenas prácticas:**
	- Usar mensajes de commit claros y descriptivos.
	- Realizar revisiones de código (pull requests).
	- Mantener las ramas actualizadas y eliminar ramas que ya se fusionaron.

---

## Technologias usadas:

- **Java OpenJDK 17**: Main programming language
- **Spring Boot**: Framework for web application development
- **MongoDB**: non relational database
- **Maven**: Dependency management tool
- **JUnit 5 & Mockito**: Testing frameworks
- **Docker**: Application containerization
- **Jacoco**: Code coverage
- **Sonar**: Static code analysis
- **Swagger/OpenAPI**: REST API documentation
- **React & Next.js**: Frontend development with TypeScript

---

### Diagrama de Contexto
  
<img width="2273" height="1820" alt="Diagrama de Contexto" src="https://github.com/user-attachments/assets/fd7ea4a2-b28f-48f4-b61e-c9c2be929d0a" />

### Diagrama de Casos de Uso
  
<img width="867" height="940" alt="Diagrama de Casos de Uso" src="https://github.com/user-attachments/assets/e63ea5b3-a5c7-4043-ac8e-a013d6e8d04c" />

### Diagrama de Componentes General

<img width="793" height="577" alt="Diagrama de Componentes General" src="https://github.com/user-attachments/assets/83e9783a-36a1-4161-a2f3-e1e13f92e2d0" />

### Diagrama de Componentes Específico

<img width="826" height="935" alt="Diagrama de Componentes Específico" src="https://github.com/user-attachments/assets/36f0dc30-1ebc-44fd-9f5a-e249298e27ee" />

### Diagrama de Clases
  
[📄 Ver Diagrama de Clases Completo (PDF)](https://pruebacorreoescuelaingeduco-my.sharepoint.com/:b:/g/personal/david_patacon-h_mail_escuelaing_edu_co/EeULhd29uHRNu3Iks7h6qLIBTHoFVzCLM1y8Zacqdxy4AQ?e=5KvTdC)

### Diagrama de Base de Datos (MongoDB)
  
<img width="1312" height="662" alt="Diagrama de Base de Datos" src="https://github.com/user-attachments/assets/d7438a5b-dfec-45ed-b73e-9827abedf262" />

### Modelo de Datos JSON (MongoDB)

```json
{
  "Coleccion: Usuario": {
    "id_usuario": "String",
    "nombre": "String",
    "correo": "String",
    "contrasena": "String (BCrypt Hash)",
    "rol": "String (ESTUDIANTE|DECANO|PROFESOR|ADMINISTRADOR)", 
    "datos_rol": {  
      "carrera": "String",
      "semestre": "int",
      "grupos_inscritos": [
        {
          "id_grupo": "String",
          "materia": "String",
          "nombre_materia": "String",
          "horario": [
            {"dia": "String", "hora_inicio": "String", "hora_fin": "String"}
          ]
        }
      ]
    }
  },

  "Coleccion: Materia": {
    "id_materia": "String",
    "nombre": "String",
    "creditos": "int",
    "facultad": "String"
  },

  "Coleccion: Grupo": {
    "id_grupo": "String",
    "id_materia": "String",
    "profesor": {
      "id_usuario": "String",
      "nombre": "String"
    },
    "cupos_maximos": "int",
    "cupos_asignados": "int",
    "horario": [
      {"dia": "String", "hora_inicio": "String", "hora_fin": "String"}
    ]
  },

  "Coleccion: Solicitud_Cambio": {
    "id_solicitud": "String",
    "id_estudiante": "String",
    "origen": {
      "id_grupo": "String",
      "materia": "String",
      "horario": [...]
    },
    "destino": {
      "id_grupo": "String",
      "materia": "String",
      "horario": [...]
    },
    "descripcion": "String",
    "fecha_solicitud": "Date",
    "estado": "String (PENDIENTE|EN_REVISION|APROBADA|RECHAZADA)",
    "prioridad": "int",
    "id_periodo": "String"
  },

  "Coleccion: Periodo_Habilitado": {
    "id_periodo": "String",
    "fecha_inicio": "Date",
    "fecha_fin": "Date"
  },

  "Coleccion: Rol": {
    "rol": "String",
    "permisos": ["String","String","String"]
  }
}
```

### Diagramas de Secuencia

Los diagramas de secuencia documentan el flujo de interacción entre componentes para cada funcionalidad:

#### Diagrama de Secuencia: Registro de Usuario

```
Cliente -> AuthController: POST /api/auth/register
AuthController -> AuthService: register(usuarioDTO)
AuthService -> UsuarioFactory: crearUsuario(usuarioDTO)
UsuarioFactory --> AuthService: Usuario
AuthService -> UsuarioRepository: existsByEmail(email)
UsuarioRepository --> AuthService: false
AuthService -> UsuarioRepository: save(usuario)
UsuarioRepository --> AuthService: Usuario guardado
AuthService -> JwtService: generateToken(usuario)
JwtService --> AuthService: token
AuthService --> AuthController: AuthResponseDTO
AuthController --> Cliente: 201 CREATED + AuthResponseDTO
```

#### Diagrama de Secuencia: Crear Solicitud de Cambio

```
Cliente -> EstudianteController: POST /api/estudiante/solicitudes
EstudianteController -> EstudianteService: crearSolicitud(solicitudDTO)
EstudianteService -> PlazoSolicitudes: estaEnPlazo(fechaActual)
PlazoSolicitudes --> EstudianteService: true
EstudianteService -> GrupoRepository: findById(grupoDestino)
GrupoRepository --> EstudianteService: Grupo
EstudianteService -> EstudianteService: validarCuposDisponibles()
EstudianteService -> EstudianteService: validarConflictoHorarios()
EstudianteService -> SolicitudRepository: save(solicitud)
SolicitudRepository --> EstudianteService: Solicitud guardada
EstudianteService --> EstudianteController: Solicitud
EstudianteController --> Cliente: 201 CREATED + Solicitud
```

📁 **Ubicación de los diagramas**: `docs/UML/diagramasSecuencia/`

---

## 🔌 API REST - Endpoints

### 📖 Documentación Interactiva

La API cuenta con documentación completa en Swagger UI:

- **🌐 Producción**: [https://sirha-api-container.azurewebsites.net/swagger-ui/index.html](https://sirha-api-container.azurewebsites.net/swagger-ui/index.html)
- **💻 Local**: http://localhost:8080/swagger-ui/index.html
- **📄 OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Autenticación de Endpoints

Los endpoints protegidos requieren un token JWT en el header:
```
Authorization: Bearer {token}
```

### 1. 🔐 Autenticación (`/api/auth`)

#### `POST /api/auth/register`
**Descripción**: Registra un nuevo usuario en el sistema.

**Headers**: `Content-Type: application/json`

**Request Body**:
```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "password": "Password123!",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA_SISTEMAS"
}
```

**Response 201 CREATED**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

**Validaciones**:
- ✅ Nombre y apellido: obligatorios, no vacíos
- ✅ Password: mínimo 8 caracteres, una mayúscula, una minúscula, un número, un carácter especial
- ✅ Rol: ESTUDIANTE, DECANO, PROFESOR, ADMINISTRADOR
- ✅ Facultad: obligatorio para ESTUDIANTE y DECANO

**Errores**:
- `400 Bad Request`: Datos inválidos
- `409 Conflict`: Email ya registrado

---

#### `POST /api/auth/login`
**Descripción**: Autentica a un usuario y retorna un token JWT.

**Headers**: `Content-Type: application/json`

**Request Body**:
```json
{
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "password": "Password123!"
}
```

**Response 200 OK**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "juan.perez-p@mail.escuelaing.edu.co",
  "rol": "ESTUDIANTE",
  "nombre": "Juan Pérez"
}
```

**Errores**:
- `401 Unauthorized`: Credenciales inválidas
- `404 Not Found`: Usuario no existe

---

### 2. 👨‍🎓 Estudiantes (`/api/estudiante`)

#### `GET /api/estudiante/horario/{idEstudiante}/{semestre}`
**Descripción**: Obtiene el horario completo de un estudiante por semestre.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `idEstudiante`: Código del estudiante (ej: "2022001")
- `semestre`: Número del semestre (ej: 5)

**Response 200 OK**:
```json
{
  "Cálculo I": [
    {
      "dia": "LUNES",
      "horaInicio": "08:00",
      "horaFin": "10:00",
      "salon": "A101"
    },
    {
      "dia": "MIERCOLES",
      "horaInicio": "08:00",
      "horaFin": "10:00",
      "salon": "A101"
    }
  ],
  "Física I": [
    {
      "dia": "MARTES",
      "horaInicio": "10:00",
      "horaFin": "12:00",
      "salon": "B205"
    }
  ]
}
```

**Errores**:
- `404 Not Found`: No se encontró horario para el estudiante

---

#### `GET /api/estudiante/semaforo/{idEstudiante}`
**Descripción**: Obtiene el estado del semáforo académico del estudiante.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `idEstudiante`: Código del estudiante

**Response 200 OK**:
```json
{
  "2024-1": {
    "estado": "VERDE",
    "promedio": 4.2,
    "creditosAprobados": 18,
    "creditosMatriculados": 20,
    "porcentajeAvance": 85.5,
    "observaciones": "Buen rendimiento académico"
  }
}
```

**Errores**:
- `204 No Content`: No se encontró información
- `404 Not Found`: Estudiante no encontrado

---

#### `POST /api/estudiante/solicitudes`
**Descripción**: Crea una nueva solicitud de cambio de grupo.

**Autenticación**: ✅ Requerida (JWT)

**Headers**: `Content-Type: application/json`

**Request Body**:
```json
{
  "idEstudiante": "2022001",
  "acronimoMateria": "CALC1",
  "grupoOrigenId": "GRP-001",
  "grupoDestinoId": "GRP-002",
  "motivo": "Conflicto de horario laboral",
  "fechaSolicitud": "2024-01-20"
}
```

**Response 201 CREATED**:
```json
{
  "id": "SOL-001",
  "estudiante": {
    "codigo": "2022001",
    "nombre": "Juan Pérez"
  },
  "materia": {
    "acronimo": "CALC1",
    "nombre": "Cálculo I"
  },
  "grupoOrigen": {
    "id": "GRP-001",
    "profesor": "Dr. García"
  },
  "grupoDestino": {
    "id": "GRP-002",
    "profesor": "Dra. López"
  },
  "motivo": "Conflicto de horario laboral",
  "estado": "PENDIENTE",
  "fechaSolicitud": "2024-01-20T14:30:00"
}
```

**Validaciones**:
- ✅ La solicitud debe estar dentro del plazo habilitado
- ✅ El grupo destino debe tener cupos disponibles
- ✅ No debe haber conflicto de horarios

**Errores**:
- `400 Bad Request`: Fuera de plazo o datos inválidos
- `409 Conflict`: No hay cupos o conflicto de horario

---

#### `GET /api/estudiante/solicitudes/{idEstudiante}`
**Descripción**: Lista todas las solicitudes de un estudiante.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `idEstudiante`: Código del estudiante

**Response 200 OK**:
```json
[
  {
    "id": "SOL-001",
    "materia": "Cálculo I",
    "grupoOrigen": "GRP-001",
    "grupoDestino": "GRP-002",
    "estado": "PENDIENTE",
    "fechaSolicitud": "2024-01-20T14:30:00",
    "motivo": "Conflicto de horario"
  },
  {
    "id": "SOL-002",
    "materia": "Física I",
    "grupoOrigen": "GRP-003",
    "grupoDestino": "GRP-004",
    "estado": "APROBADA",
    "fechaSolicitud": "2024-01-18T09:15:00",
    "motivo": "Mejor horario"
  }
]
```

**Errores**:
- `204 No Content`: No tiene solicitudes
- `404 Not Found`: Estudiante no encontrado

---

#### `GET /api/estudiante/solicitudes/{idEstudiante}/{solicitudId}`
**Descripción**: Obtiene los detalles de una solicitud específica.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `idEstudiante`: Código del estudiante
- `solicitudId`: ID de la solicitud

**Response 200 OK**: Ver estructura completa en ejemplo anterior

**Errores**:
- `404 Not Found`: Solicitud no encontrada

---

#### `PUT /api/estudiante/materias/{idEstudiante}/{acronimoMateria}/cancelar`
**Descripción**: Cancela una materia del estudiante.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `idEstudiante`: Código del estudiante
- `acronimoMateria`: Acrónimo de la materia (ej: "CALC1")

**Response 200 OK**:
```json
"Materia CALC1 cancelada exitosamente para el estudiante 2022001"
```

**Errores**:
- `400 Bad Request`: No puede cancelar la materia
- `404 Not Found`: Estudiante o materia no encontrados

---

#### `GET /api/estudiante/solicitudes/estado/{estado}`
**Descripción**: Lista solicitudes por estado.

**Autenticación**: ✅ Requerida (JWT - Admin/Decano)

**Path Parameters**:
- `estado`: PENDIENTE | EN_REVISION | APROBADA | RECHAZADA

**Response 200 OK**:
```json
{
  "solicitudes": [...],
  "estado": "PENDIENTE",
  "total": 15
}
```

**Errores**:
- `400 Bad Request`: Estado inválido

---

### 3. 👔 Decano (`/api/decano`)

#### `PUT /api/decano/solicitudes/{solicitudId}/aprobar`
**Descripción**: Aprueba una solicitud de cambio de grupo.

**Autenticación**: ✅ Requerida (JWT - Rol DECANO)

**Path Parameters**:
- `solicitudId`: ID de la solicitud

**Request Body**:
```json
{
  "observaciones": "Solicitud aprobada por motivos justificados"
}
```

**Response 200 OK**:
```json
{
  "id": "SOL-001",
  "estado": "APROBADA",
  "observaciones": "Solicitud aprobada por motivos justificados",
  "fechaRespuesta": "2024-01-22T10:30:00"
}
```

**Errores**:
- `403 Forbidden`: No tiene permisos
- `404 Not Found`: Solicitud no encontrada
- `409 Conflict`: Solicitud ya procesada

---

#### `PUT /api/decano/solicitudes/{solicitudId}/rechazar`
**Descripción**: Rechaza una solicitud de cambio de grupo.

**Autenticación**: ✅ Requerida (JWT - Rol DECANO)

**Path Parameters**:
- `solicitudId`: ID de la solicitud

**Request Body**:
```json
{
  "observaciones": "No cumple con los requisitos académicos"
}
```

**Response 200 OK**:
```json
{
  "id": "SOL-001",
  "estado": "RECHAZADA",
  "observaciones": "No cumple con los requisitos académicos",
  "fechaRespuesta": "2024-01-22T10:30:00"
}
```

**Errores**:
- `403 Forbidden`: No tiene permisos
- `404 Not Found`: Solicitud no encontrada

---

### 4. 👥 Usuarios (`/api/usuarios`)

#### `GET /api/usuarios`
**Descripción**: Lista todos los usuarios del sistema.

**Autenticación**: ✅ Requerida (JWT - Rol ADMINISTRADOR)

**Response 200 OK**:
```json
[
  {
    "id": "1234567890",
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan.perez-p@mail.escuelaing.edu.co",
    "rol": "ESTUDIANTE"
  },
  ...
]
```

---

#### `GET /api/usuarios/{id}`
**Descripción**: Obtiene un usuario por ID.

**Autenticación**: ✅ Requerida (JWT)

**Path Parameters**:
- `id`: ID del usuario

**Response 200 OK**: Ver estructura anterior

**Errores**:
- `404 Not Found`: Usuario no encontrado

---

#### `PUT /api/usuarios/{id}`
**Descripción**: Actualiza la información de un usuario.

**Autenticación**: ✅ Requerida (JWT - Admin o propio usuario)

**Path Parameters**:
- `id`: ID del usuario

**Request Body**:
```json
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez García"
}
```

**Response 200 OK**: Usuario actualizado

**Errores**:
- `403 Forbidden`: No tiene permisos
- `404 Not Found`: Usuario no encontrado

---

#### `DELETE /api/usuarios/{id}`
**Descripción**: Elimina un usuario del sistema.

**Autenticación**: ✅ Requerida (JWT - Rol ADMINISTRADOR)

**Path Parameters**:
- `id`: ID del usuario

**Response 204 No Content**

**Errores**:
- `403 Forbidden`: No tiene permisos
- `404 Not Found`: Usuario no encontrado

---

### 5. 📚 Grupos y Materias

#### `GET /api/grupos`
**Descripción**: Lista todos los grupos disponibles.

**Autenticación**: ✅ Requerida (JWT)

**Response 200 OK**:
```json
[
  {
    "id": "GRP-001",
    "materia": {
      "acronimo": "CALC1",
      "nombre": "Cálculo I"
    },
    "profesor": "Dr. García",
    "cuposMaximos": 30,
    "cuposDisponibles": 5,
    "horarios": [...]
  },
  ...
]
```

---

#### `GET /api/materias`
**Descripción**: Lista todas las materias.

**Autenticación**: ✅ Requerida (JWT)

**Response 200 OK**:
```json
[
  {
    "acronimo": "CALC1",
    "nombre": "Cálculo I",
    "creditos": 4,
    "facultad": "INGENIERIA"
  },
  ...
]
```

---

#### `GET /api/carreras`
**Descripción**: Lista todas las carreras disponibles.

**Autenticación**: ✅ Requerida (JWT)

**Response 200 OK**:
```json
[
  {
    "id": "ING_SISTEMAS",
    "nombre": "Ingeniería de Sistemas",
    "facultad": "INGENIERIA",
    "duracion": 10
  },
  ...
]
```

---

### 6. 📊 Reportes (`/api/reportes`)

#### `GET /api/reportes/tasa-aprobacion`
**Descripción**: Obtiene la tasa de aprobación por materia.

**Autenticación**: ✅ Requerida (JWT - Rol DECANO)

**Response 200 OK**:
```json
[
  {
    "materia": "Cálculo I",
    "totalEstudiantes": 120,
    "aprobados": 95,
    "reprobados": 25,
    "tasaAprobacion": 79.17
  },
  ...
]
```

---

#### `GET /api/reportes/estadisticas-grupos`
**Descripción**: Estadísticas de grupos por materia.

**Autenticación**: ✅ Requerida (JWT - Rol DECANO)

**Response 200 OK**:
```json
[
  {
    "grupo": "GRP-001",
    "materia": "Cálculo I",
    "cuposMaximos": 30,
    "cuposAsignados": 28,
    "porcentajeOcupacion": 93.33,
    "promedioGrupo": 3.8
  },
  ...
]
```

---

#### `GET /api/reportes/capacidad-grupos`
**Descripción**: Reporte de capacidad de grupos.

**Autenticación**: ✅ Requerida (JWT - Rol DECANO)

**Response 200 OK**:
```json
[
  {
    "materia": "Cálculo I",
    "totalGrupos": 4,
    "cuposTotal": 120,
    "cuposOcupados": 105,
    "cuposDisponibles": 15,
    "porcentajeOcupacion": 87.5
  },
  ...
]
```

---

## ⚠️ Manejo de Errores

El sistema implementa un manejo centralizado de excepciones mediante `GlobalExceptionHandler`.

### Estructura de Respuestas de Error

Todas las respuestas de error siguen un formato consistente:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 400,
  "error": "Tipo de error",
  "message": "Descripción detallada del error",
  "errores": {
    "campo": "Mensaje específico del campo"
  }
}
```

### Tipos de Errores

#### 1. **Errores de Validación (400 Bad Request)**

Cuando los datos enviados no cumplen con las validaciones:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 400,
  "error": "Error de validación",
  "message": "Errores en los campos enviados",
  "errores": {
    "password": "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula, un número y un carácter especial",
    "nombre": "El nombre no puede estar vacío",
    "email": "El formato del email no es válido"
  }
}
```

**Causas comunes**:
- Campos obligatorios vacíos
- Formato de datos incorrecto
- Valores fuera de rango permitido
- Patrones de validación no cumplidos

---

#### 2. **Errores de Autenticación (401 Unauthorized)**

Cuando las credenciales son inválidas o el token ha expirado:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 401,
  "error": "No autorizado",
  "message": "Credenciales inválidas"
}
```

**Causas comunes**:
- Email o contraseña incorrectos
- Token JWT expirado
- Token JWT inválido o malformado
- Usuario no autenticado

---

#### 3. **Errores de Autorización (403 Forbidden)**

Cuando el usuario no tiene permisos para la operación:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 403,
  "error": "Acceso denegado",
  "message": "No tiene permisos para realizar esta operación"
}
```

**Causas comunes**:
- Intentar acceder a un recurso sin el rol adecuado
- Intentar modificar datos de otro usuario sin ser administrador
- Intentar aprobar solicitudes sin ser decano

---

#### 4. **Errores de Recurso No Encontrado (404 Not Found)**

Cuando el recurso solicitado no existe:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 404,
  "error": "No encontrado",
  "message": "Usuario no encontrado con el ID especificado"
}
```

**Causas comunes**:
- ID de usuario inexistente
- Solicitud no encontrada
- Grupo o materia no existe

---

#### 5. **Errores de Conflicto (409 Conflict)**

Cuando la operación genera un conflicto de estado:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 409,
  "error": "Error en la operación",
  "message": "El email ya está registrado en el sistema"
}
```

**Causas comunes**:
- Email duplicado al registrar
- Intentar crear una solicitud fuera de plazo
- No hay cupos disponibles en el grupo
- Conflicto de horarios

---

#### 6. **Errores Internos del Servidor (500 Internal Server Error)**

Cuando ocurre un error inesperado en el servidor:

```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 500,
  "error": "Error interno del servidor",
  "message": "Ha ocurrido un error inesperado: descripción del error"
}
```

**Causas comunes**:
- Error de conexión con la base de datos
- Error en la lógica de negocio
- Excepciones no controladas

---

### Códigos de Estado HTTP Utilizados

| Código | Nombre | Uso |
|--------|--------|-----|
| `200` | OK | Operación exitosa |
| `201` | Created | Recurso creado exitosamente |
| `204` | No Content | Operación exitosa sin contenido de retorno |
| `400` | Bad Request | Datos de entrada inválidos |
| `401` | Unauthorized | Autenticación fallida |
| `403` | Forbidden | Sin permisos para la operación |
| `404` | Not Found | Recurso no encontrado |
| `409` | Conflict | Conflicto en la operación |
| `500` | Internal Server Error | Error interno del servidor |

---

### Ejemplos de Casos de Uso

#### Ejemplo 1: Registro con contraseña débil

**Request**:
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "password": "123",
  "rol": "ESTUDIANTE",
  "facultad": "INGENIERIA_SISTEMAS"
}
```

**Response 400**:
```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 400,
  "error": "Error de validación",
  "message": "Errores en los campos enviados",
  "errores": {
    "password": "La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&#)"
  }
}
```

---

#### Ejemplo 2: Crear solicitud fuera de plazo

**Request**:
```http
POST /api/estudiante/solicitudes
Authorization: Bearer {token}
Content-Type: application/json

{
  "idEstudiante": "2022001",
  "acronimoMateria": "CALC1",
  "grupoOrigenId": "GRP-001",
  "grupoDestinoId": "GRP-002",
  "motivo": "Conflicto de horario",
  "fechaSolicitud": "2024-12-01"
}
```

**Response 400**:
```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 400,
  "error": "Fuera de plazo",
  "message": "No se pueden crear solicitudes fuera del plazo establecido. Plazo válido: 2024-01-15 al 2024-01-31. Fecha de solicitud: 2024-12-01"
}
```

---

#### Ejemplo 3: Login con credenciales incorrectas

**Request**:
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan.perez@mail.escuelaing.edu.co",
  "password": "incorrecta"
}
```

**Response 401**:
```json
{
  "timestamp": "2024-10-25T14:30:45.123",
  "status": 401,
  "error": "No autorizado",
  "message": "Credenciales inválidas"
}
```

---

### Logging de Errores

Todos los errores se registran en logs con la clase `Log` personalizada:

```java
/**
 * Utility para logging de excepciones del sistema.
 */
public class Log {
    public static void logException(Exception e) {
        System.err.println("[ERROR] " + LocalDateTime.now());
        System.err.println("Tipo: " + e.getClass().getSimpleName());
        System.err.println("Mensaje: " + e.getMessage());
        e.printStackTrace();
    }
}
```

Los logs incluyen:
- Timestamp del error
- Tipo de excepción
- Mensaje descriptivo
- Stack trace completo

---  
<img width="2273" height="1820" alt="DiagramadeContexto (1)" src="https://github.com/user-attachments/assets/fd7ea4a2-b28f-48f4-b61e-c9c2be929d0a" />

### Diagrama de casos de uso  
<img width="867" height="940" alt="image" src="https://github.com/user-attachments/assets/e63ea5b3-a5c7-4043-ac8e-a013d6e8d04c" />

### Diagrama de componentes general
<img width="793" height="577" alt="image" src="https://github.com/user-attachments/assets/83e9783a-36a1-4161-a2f3-e1e13f92e2d0" />

### Diagrama de componentes especifico
<img width="826" height="935" alt="image" src="https://github.com/user-attachments/assets/36f0dc30-1ebc-44fd-9f5a-e249298e27ee" />

### Diagrama de clases  
[enlace para verl el diagrama de clases](https://pruebacorreoescuelaingeduco-my.sharepoint.com/:b:/g/personal/david_patacon-h_mail_escuelaing_edu_co/EeULhd29uHRNu3Iks7h6qLIBTHoFVzCLM1y8Zacqdxy4AQ?e=5KvTdC)

### Diagrama base de datos  
<img width="1312" height="662" alt="image" src="https://github.com/user-attachments/assets/d7438a5b-dfec-45ed-b73e-9827abedf262" />

### JSON Base de datos no relacional: 

```
{
  "Coleccion: Usuario": {
    "id_usuario": "String",
    "nombre": "String",
    "correo": "String",
    "contrasena": "String",
    "rol": "String", 
    "datos_rol": {  
      "carrera": "String",
      "semestre": "int",
      "grupos_inscritos": [
        {
          "id_grupo": "String",
          "materia": "String",
          "nombre_materia": "String",
          "horario": [
            {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"},
            {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"}
          ]
        },
        {
          "id_grupo": "F1",
          "materia": "FIS202",
          "nombre_materia": "Física II",
          "horario": [
            {"dia": "Martes", "hora_inicio": "10:00", "hora_fin": "12:00"},
            {"dia": "Jueves", "hora_inicio": "10:00", "hora_fin": "12:00"}
          ]
        }
      ]
    }
  },

  "Coleccion: Materia": {
    "id_materia": "String",
    "nombre": "String",
    "creditos": "int",
    "facultad": "String"
  },

  "Coleccion: Grupo": {
    "id_grupo": "String",
    "id_materia": "String",
    "profesor": {
      "id_usuario": "String",
      "nombre": "String"
    },
    "cupos_maximos": "int",
    "cupos_asignados": "int",
    "horario": [
      {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"},
      {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"}
    ]
  },

  "Coleccion: Solicitud_Cambio": {
    "id_solicitud": "String",
    "id_estudiante": "String",
    "origen": {
      "id_grupo": "String",
      "materia": "String",
      "horario": [
        {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"},
        {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"}
      ]
    },
    "destino": {
      "id_grupo": "String",
      "materia": "String",
      "horario": [
        {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"},
        {"dia": "Date", "hora_inicio": "Date", "hora_fin": "Date"}
      ]
    },
    "descripcion": "String",
    "fecha_solicitud": "Date",
    "estado": "String",
    "prioridad": "int",
    "id_periodo": "String"
  },

  "Coleccion: Periodo_Habilitado": {
    "id_periodo": "String",
    "fecha_inicio": "Date",
    "fecha_fin": "Date"
  },


  "Coleccion: Rol": {
    "rol": "String",
    "permisos": ["String","String","String","..."]
  }
}
```
---

## 📚 Documentación de API

### Swagger/OpenAPI
La API REST está completamente documentada con Swagger UI:
- **URL Local**: http://localhost:8080/swagger-ui/index.html
- **API Docs**: http://localhost:8080/v3/api-docs

### Endpoints Principales

#### Autenticación
- `POST /api/auth/register` - Registro de nuevos usuarios
- `POST /api/auth/login` - Autenticación de usuarios

📖 **Documentación detallada de registro**: Ver [REGISTRO_USUARIOS.md](./REGISTRO_USUARIOS.md)

---

## 🧪 Testing con Postman

### Inicio Rápido
1. **Importa la colección**: `SIRHA_Postman_Collection.json`
2. **Importa el entorno**: `SIRHA_Local_Environment.json`
3. **Selecciona el entorno** "SIRHA Local"
4. **¡Comienza a probar!** 🚀

### Documentación Disponible
- 📘 **[POSTMAN_QUICKSTART.md](./POSTMAN_QUICKSTART.md)** - Guía de inicio rápido (3 pasos)
- 📗 **[GUIA_POSTMAN.md](./GUIA_POSTMAN.md)** - Guía completa de testing
  - Configuración detallada
  - Todos los endpoints documentados
  - Ejemplos de peticiones por rol
  - Troubleshooting
  - Escenarios de prueba

### Características de la Colección
✅ Gestión automática de tokens JWT  
✅ Variables de entorno pre-configuradas  
✅ Ejemplos listos para usar  
✅ Scripts de test incluidos  
✅ Organización por roles (Estudiante, Decano, Admin)

---

## Dependencias:  

```xml
<dependencies>
  
        <!-- Starter web para exponer APIs REST -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- MongoDB: para trabajar con base de datos NoSQL -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongodb-driver-sync</artifactId>
        </dependency>

        <!-- Validación de datos -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Pruebas unitarias -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.13.4</version>
            <scope>test</scope>
        </dependency>

        <!-- Springdoc OpenAPI para generar la documentación Swagger UI automáticamente -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>

    </dependencies>
```

---

## Plugins:

```xml
 <plugins>
            <!-- Spring Boot -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <!-- JaCoCo para cobertura de tests -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${jacoco.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

---

<div align="center">
  
**Desarrollado por el equipo C--**

*Escuela Colombiana de Ingeniería Julio Garavito*

*2024*

</div>




