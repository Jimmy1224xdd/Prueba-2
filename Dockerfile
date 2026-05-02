# Etapa 1: Build con Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Usar caché local de Maven para no descargar todo desde internet
COPY .m2 /root/.m2
RUN mvn clean package -DskipTests

# Etapa 2: Deploy en Tomcat
FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/GR06_1BT23622_26A.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]