# Testimi me JUnit

Projekti përdor **JUnit 5** (Jupiter) dhe **Spring Boot Test** (`spring-boot-starter-test`).

## Si të ekzekutosh testet

```powershell
cd student-management
.\mvnw.cmd test
```

Ose build i plotë me teste:

```powershell
.\mvnw.cmd clean install
```

> Në Windows, nëse `mvn` nuk njihet, përdor **`mvnw.cmd`** (Maven Wrapper në projekt).

## Testet sipas modulit

| Modul | Klasa test | Lloji | Çfarë teston |
|-------|------------|-------|--------------|
| **mapper** | `StudentMapperImplTest` | Unit | Mapim Entity ↔ DTO |
| **service** | `StudentServiceTest` | Unit | CRUD, email unik, përjashtime |
| **excel** | `StudentExcelExporterTest` | Unit | Gjenerim `.xlsx` me Apache POI |
| **web** | `StudentControllerTest` | Unit | REST endpoints (MockMvc + mock service) |

## Teknologjitë e testimit

- **JUnit 5** — `@Test`, `@BeforeEach`
- **Mockito** — `@Mock`, `@InjectMocks`, `@ExtendWith(MockitoExtension.class)`
- **AssertJ** — `assertThat(...)`
- **MockMvc** — test i controller-it HTTP
- **MockMvc** (standalone) — unit test i controller-it HTTP

## Shembull: unit test (service)

```java
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock StudentRepository studentRepository;
    @Mock StudentMapper studentMapper;
    @InjectMocks StudentService studentService;

    @Test
    void createStudent_shouldReturnSavedStudent() { ... }
}
```

## Rezultati i pritur

```
Tests run: 19, Failures: 0, Errors: 0
BUILD SUCCESS
```

Raportet XML: `*/target/surefire-reports/`
