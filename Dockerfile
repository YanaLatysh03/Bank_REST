FROM eclipse-temurin:17-jdk-alpine

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app.jar"]

EXPOSE 8090
