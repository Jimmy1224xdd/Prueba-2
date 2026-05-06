# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilar el proyecto y empaquetar en un WAR (se salta pruebas en build para evitar fallos por DB no lista, las pruebas se hacen en CI/CD)
RUN mvn clean package -DskipTests

# Etapa 2: Deploy en Tomcat
FROM tomcat:10.1-jdk17
# Eliminar apps por defecto para limpieza
RUN rm -rf /usr/local/tomcat/webapps/*
# Copiar el WAR generado en la etapa anterior y renombrarlo a ROOT.war para que responda en /
COPY --from=build /app/target/GR06_1BT23622_26A.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
