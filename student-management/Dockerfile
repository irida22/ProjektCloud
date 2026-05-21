# Hapi 3 — Dockerfile (pas: mvn clean package)
# Kerkon: target/student-management.jar (ose target/*.jar)

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/student-management.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=openshift

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
