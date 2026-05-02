# Integración y Despliegue Continuo (CI/CD)

## Configuración con Docker
El proyecto incluye un `docker-compose.yml` que levanta tres servicios esenciales:
1. **db (MySQL 8):** Base de datos de la aplicación.
2. **app (Tomcat 10):** El servidor que aloja la aplicación empaquetada. Se construye a partir de un `Dockerfile` multistage que compila el código con Maven y luego copia el `.war` a Tomcat.
3. **jenkins (Jenkins LTS):** Servidor de CI/CD para automatizar pruebas y builds.

## Pipeline Explicado (`Jenkinsfile`)
El pipeline declarativo de Jenkins consta de las siguientes etapas:

- **Checkout:** Clona el repositorio desde el SCM (Git).
- **Build:** Ejecuta `mvn clean compile` para asegurar que el código compila sin errores sintácticos.
- **Test:** Ejecuta `mvn test`. Si las pruebas TDD o unitarias fallan, el pipeline se detiene, previniendo despliegues rotos.
- **Package:** Ejecuta `mvn package` empaquetando el `.war`.
- **Docker Build & Deploy:** (Opcional/Script) Ejecuta comandos de Docker para reconstruir y redesplegar el contenedor de Tomcat con el nuevo `.war`.

## Configuración en Jenkins
Para habilitar esto en Jenkins:
1. Crear un proyecto tipo "Pipeline".
2. Apuntar la definición del Pipeline a "Pipeline script from SCM".
3. Proveer el repositorio de Git. Jenkins detectará automáticamente el archivo `Jenkinsfile` y ejecutará los pasos definidos.
