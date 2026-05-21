# Stage 1: Build JAR me Maven (funksionon direkt nga Git në OpenShift)
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build

COPY mvnw mvnw.cmd .mvn ./
COPY pom.xml ./
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

RUN chmod +x mvnw && ./mvnw clean package -DskipTests -pl web -am -q

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /build/web/target/web-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=openshift
ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
