# OpenShift: Import from Git (repo root) — Docker strategy, JO Java S2I
# Multi-module: JAR = student-management/web/target/student-management.jar

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY student-management/pom.xml student-management/pom.xml
COPY student-management/model/pom.xml student-management/model/
COPY student-management/dto/pom.xml student-management/dto/
COPY student-management/mapper/pom.xml student-management/mapper/
COPY student-management/repository/pom.xml student-management/repository/
COPY student-management/service/pom.xml student-management/service/
COPY student-management/excel/pom.xml student-management/excel/
COPY student-management/web/pom.xml student-management/web/
COPY student-management/model/src student-management/model/src
COPY student-management/dto/src student-management/dto/src
COPY student-management/mapper/src student-management/mapper/src
COPY student-management/repository/src student-management/repository/src
COPY student-management/service/src student-management/service/src
COPY student-management/excel/src student-management/excel/src
COPY student-management/web/src student-management/web/src
WORKDIR /app/student-management
RUN mvn clean package -DskipTests -pl web -am -B -q

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/student-management/web/target/student-management.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=openshift
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
