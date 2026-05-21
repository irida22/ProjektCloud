# Student Management System

Aplikacion multi-module Maven për menaxhimin e studentëve (CRUD + eksport Excel), i përgatitur për CI/CD me Jenkins dhe deploy në OpenShift.

## Struktura e projektit

```
student-management/
├── model/        # Entity + Lombok
├── dto/          # DTO classes
├── mapper/       # MapStruct (DTO ↔ Entity)
├── repository/   # Spring Data JPA
├── service/      # Business logic
├── excel/        # Apache POI export
└── web/          # REST API + Spring Boot (runnable JAR)
```

## Teknologjitë

| Teknologji | Përdorimi |
|------------|-----------|
| Java 17 | Gjuha kryesore |
| Maven | Multi-module build |
| Spring Boot 3.2 | Web, JPA, DI |
| Lombok | Boilerplate reduction |
| MapStruct | Mapping Entity/DTO |
| Apache POI | Eksport `.xlsx` |
| H2 | Database (dev/deploy demo) |
| JUnit 5 | Unit & integration tests |

## Si të ekzekutosh lokalisht

```powershell
cd student-management
.\mvnw.cmd clean install
java -jar web\target\web-1.0.0-SNAPSHOT.jar
```

> Pa Maven të instaluar global: përdor **`.\mvnw.cmd`** në vend të `mvn`.

Aplikacioni: `http://localhost:8080`

## API Endpoints

| Metoda | URL | Përshkrim |
|--------|-----|-----------|
| POST | `/api/students` | Shto student |
| GET | `/api/students` | Listo të gjithë |
| GET | `/api/students/{id}` | Merr një student |
| PUT | `/api/students/{id}` | Përditëso |
| DELETE | `/api/students/{id}` | Fshi |
| GET | `/api/students/export/excel` | Shkarko Excel |

### Shembull request (POST)

```json
{
  "firstName": "Ana",
  "lastName": "Hoxha",
  "email": "ana@university.edu",
  "program": "Computer Science",
  "enrollmentYear": 2024
}
```

## Jenkins CI/CD

1. Krijo job **Pipeline** në Jenkins
2. Lidh repository Git (GitHub)
3. Përdor `Jenkinsfile` nga root i projektit
4. Pipeline ekzekuton: `mvn clean install` → testet → arkivon JAR

Konfiguro tools në Jenkins: **Maven 3.9**, **JDK 17**.

## Deploy në OpenShift

```bash
# Build JAR
mvn clean package -pl web -am

# Build image (pas login në registry)
docker build -t student-management:1.0.0 .
oc new-project student-mgmt
oc apply -f openshift/deployment.yaml
```

Ndrysho `YOUR_PROJECT` në `deployment.yaml` me emrin e projektit OpenShift.

Route jep URL publik: `oc get route student-management`

## Jira (Agile)

Organizo punën sipas kërkesës së kursit:

- **Epic**: Student Management Core
- **Epic**: Excel Export
- **Epic**: CI/CD & Deployment
- **User Stories**: CRUD për student, eksport Excel, pipeline Jenkins, deploy OpenShift
- **Sprint**: 1–2 javë me Backlog dhe Sprint Board

## Dorëzimi i kursit

1. Kodi në GitHub
2. Screenshot: Jira board, Jenkins (build success), OpenShift deployment
3. PDF dokumentim — shiko `docs/DOKUMENTIM.md` si bazë për PDF

## Testet (JUnit 5)

```bash
mvn test
```

| Test | Modul | Lloji |
|------|-------|-------|
| `StudentMapperImplTest` | mapper | Unit (4) |
| `StudentServiceTest` | service | Unit (9) |
| `StudentExcelExporterTest` | excel | Unit (2) |
| `StudentControllerTest` | web | Unit (4) |

Dokumentim: [docs/TESTIMI.md](docs/TESTIMI.md)
