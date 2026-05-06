# PoliServis — UniServicios

Plataforma de servicios universitarios entre pares.  
Stack: Java 17 · Servlet/JSP · Hibernate · MySQL · Docker · Jenkins CI/CD


## Estructura del proyecto

```
.
├── src/                    # Código fuente Java
├── Dockerfile              # Imagen de la app (Tomcat + WAR)
├── Dockerfile.jenkins      # Jenkins preconfigurado
├── docker-compose.yml      # Stack completo
├── Jenkinsfile             # Pipeline CI/CD
├── jenkins-config/         # Configuración precargada de Jenkins
│   ├── jenkins.yaml        # JCasC: usuario, Maven, seguridad
│   ├── config.xml          # Config global de Jenkins
│   └── jobs/               # Jobs precreados
├── jenkins-plugins.txt     # Plugins que se instalan en el build
├── .gitattributes          # Fix CRLF — no tocar
├── .gitignore
└── init.sql                # Schema inicial de la DB
```
