FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY web/target/web-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=openshift

ENTRYPOINT ["java", "-jar", "app.jar"]
