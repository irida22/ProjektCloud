# Jenkins CI/CD — Student Management System

Repository GitHub: https://github.com/irida22/ProjektCloud

## Kërkesat e kursit (plotësohen nga pipeline)

| # | Kërkesa | Faza në Jenkinsfile |
|---|---------|----------------------|
| 1 | Pull nga Git | `1. Checkout (Pull from Git)` |
| 2 | `mvn clean install` | `2. Build (mvn clean install)` |
| 3 | Ekzekuton testet | përfshihet në `clean install` + `3. Test Results` |
| 4 | Gjeneron artifact `.jar` | `4. Archive Artifact (.jar)` |

---

## Hapi 1 — Plugin-et në Jenkins

**Manage Jenkins → Plugins → Available**

Instalo (nëse mungojnë):

- Git plugin
- Pipeline (workflow-aggregator)
- JUnit Plugin
- GitHub plugin (e ke lidhur tashmë)

Restart Jenkins nëse kërkohet.

---

## Hapi 2 — Konfiguro Tools

**Manage Jenkins → Tools**

| Tool | Emri në Jenkins | Vlera |
|------|-----------------|-------|
| JDK | `JDK-17` | JDK 17 (ose 21) |
| Maven | opsional | Pipeline përdor **mvnw** nga projekti |

> Emri `JDK-17` duhet të përputhet me `Jenkinsfile` (tools { jdk 'JDK-17' }).

---

## Hapi 3 — Krijo Pipeline Job

1. **New Item** → emër: `student-management-pipeline`
2. Lloji: **Pipeline** → OK
3. **Pipeline** → Definition: **Pipeline script from SCM**
4. **SCM:** Git
5. **Repository URL:** `https://github.com/irida22/ProjektCloud.git`
6. **Credentials:** shto token GitHub (nëse repo private) ose lëre bosh (public)
7. **Branch:** `*/main`
8. **Script Path:** `Jenkinsfile`  ← **në root të repo, jo në student-management/**
9. Save

---

## Hapi 4 — Lidhja me GitHub (e ke bërë)

- GitHub webhook → Jenkins (push trigger), ose
- **Build Now** manual për testim

Pas push në `main`, Jenkins bën pull automatikisht.

---

## Hapi 5 — Ekzekuto build

1. Hap job-in `student-management-pipeline`
2. Kliko **Build Now**
3. Kliko numrin e build (#1, #2…)
4. **Console Output** — duhet të shohësh:
   - `Checkout`
   - `mvnw clean install`
   - `Tests run: 19, Failures: 0`
   - `BUILD SUCCESS`
5. **Test Result** — 19 teste JUnit
6. **Build Artifacts** — `web-1.0.0-SNAPSHOT.jar`

---

## Screenshot për dorëzim

Kap ekranin me:

1. Jenkins dashboard — job me **✓ blu** (success)
2. Pipeline Stages — të gjitha fazat jeshile
3. Console Output — `BUILD SUCCESS` dhe `Tests run: 19`
4. Build Artifacts — JAR i arkivuar

---

## Probleme të zakonshme

| Problem | Zgjidhje |
|---------|----------|
| `JDK-17 not found` | Krijo JDK tool me emrin saktë `JDK-17` |
| `mvn not found` | Pipeline përdor `mvnw.cmd` — nuk duhet Maven global |
| `Script Path` gabim | Duhet `Jenkinsfile` (root), jo `student-management/Jenkinsfile` |
| Testet dështojnë | Verifiko lokalisht: `.\mvnw.cmd test` |
| `clean` dështon (JAR locked) | Mos e ekzekuto aplikacionin gjatë build-it |

---

## Struktura e repo (për Jenkins)

```
ProjektCloud/                 ← root (checkout)
├── Jenkinsfile               ← Script Path këtu
└── student-management/
    ├── mvnw.cmd
    ├── pom.xml
    └── web/target/*.jar      ← artifact
```
