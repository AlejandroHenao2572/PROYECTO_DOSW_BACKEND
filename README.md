# PROYECTO_DOSW_BACKEND
## Estructura del Proyecto (Spring Boot MVC)

```
PROYECTO_DOSW_BACKEND/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/proyecto/
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── model/
│       │       └── ProyectoApplication.java
│       └── resources/
│           └── application.properties
└── README.md
```

- **controller/**: Controladores REST.
- **service/**: Lógica de negocio y servicios.
- **repository/**: Acceso a datos y repositorios JPA.
- **model/**: Entidades y modelos de datos.
- **resources/**: Configuración y archivos estáticos.
- **ProyectoApplication.java**: Clase principal de arranque.

## Estrategia de Gitflow

1. **Ramas principales:**
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

> Para dudas sobre la estructura o el flujo de trabajo, consulta este README o contacta al equipo de desarrollo.