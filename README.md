# PROYECTO_DOSW_BACKEND
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Maven]((https://img.shields.io/badge/apachemaven-C71A36.svg?style=for-the-badge&logo=apachemaven&logoColor=white))
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-black?style=for-the-badge&logo=sonarqube&logoColor=4E9BCD)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)

---

## Miembros del Equipo:
- Anderson Fabian Garcia Nieto  
- David Alejandro Patacon Henao   
- Felipe Eduardo Calviche Gallego  
- Jared Sebastian Farfan Guevara   
- Kevin Arturo Cuitiva Pardo  
- Maria Paula Rodriguez Muñoz  

---

## Estructura del Proyecto (Spring Boot MVC):
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

## Estrategia de Gitflow:

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

## Artefactos de analisis y diseño:
### Diagrama de contexto  
<img width="2273" height="1820" alt="DiagramadeContexto (1)" src="https://github.com/user-attachments/assets/fd7ea4a2-b28f-48f4-b61e-c9c2be929d0a" />

### Diagrama de casos de uso  
<img width="867" height="940" alt="image" src="https://github.com/user-attachments/assets/e63ea5b3-a5c7-4043-ac8e-a013d6e8d04c" />

### Diagrama de componentes general
<img width="793" height="577" alt="image" src="https://github.com/user-attachments/assets/83e9783a-36a1-4161-a2f3-e1e13f92e2d0" />

### Diagrama de componentes especifico
<img width="826" height="935" alt="image" src="https://github.com/user-attachments/assets/36f0dc30-1ebc-44fd-9f5a-e249298e27ee" />

### Diagrama de clases  
<img width="1407" height="843" alt="image" src="https://github.com/user-attachments/assets/5b1cc964-99bf-4222-8dea-2797facfcdfe" />

### Diagrama base de datos  
<img width="1312" height="662" alt="image" src="https://github.com/user-attachments/assets/d7438a5b-dfec-45ed-b73e-9827abedf262" />

---

## Dependencias:  

```xml
 <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Spring Boot con MongoDB -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <!-- Swagger / OpenAPI con Springdoc -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
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

            <!-- SonarQube plugin -->
            <plugin>
                <groupId>org.sonarsource.scanner.maven</groupId>
                <artifactId>sonar-maven-plugin</artifactId>
                <version>3.10.0.2594</version>
            </plugin>
        </plugins>
```

