# Maven JAR — Opsioni 1 (OpenShift / Java S2I)

## Kërkesa e kursit vs multi-module

| Kërkesa | Ku është |
|---------|----------|
| `<packaging>jar</packaging>` | **`web/pom.xml`** (moduli që nis aplikacionin) |
| `spring-boot-maven-plugin` | **`web/pom.xml`** |
| `target/*.jar` | **`student-management/target/student-management.jar`** pas `mvn package` |

**Root `pom.xml` mbetet `packaging pom`** — është projekt me 7 module (Maven standard).

---

## Build

```bash
cd student-management
./mvnw clean package -DskipTests
```

## Rezultati

```
web/target/student-management.jar      ← JAR Spring Boot (repackage)
target/student-management.jar          ← kopje për OpenShift S2I
```

Verifiko:

```bash
ls -la target/*.jar
ls -la web/target/*.jar
```

---

## web/pom.xml (përmbledhje)

```xml
<packaging>jar</packaging>

<build>
  <finalName>student-management</finalName>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
        <mainClass>com.student.management.StudentManagementApplication</mainClass>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>repackage</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```
