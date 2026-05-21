# Build + run (OpenShift / GitHub Actions) — multi-module Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY model/pom.xml model/
COPY dto/pom.xml dto/
COPY mapper/pom.xml mapper/
COPY repository/pom.xml repository/
COPY service/pom.xml service/
COPY excel/pom.xml excel/
COPY web/pom.xml web/
COPY model/src model/src
COPY dto/src dto/src
COPY mapper/src mapper/src
COPY repository/src repository/src
COPY service/src service/src
COPY excel/src excel/src
COPY web/src web/src
RUN mvn clean package -DskipTests -pl web -am -B -q

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/web/target/student-management.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=openshift
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
