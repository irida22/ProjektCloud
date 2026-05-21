# Jira — Strukturë Agile (shembull për kursin)

Krijo projektin **SMS** (Student Management System) në Jira dhe importo/riprodho këtë strukturë.

## Epics

| Epic | Përshkrim |
|------|-----------|
| SMS-1 | Student CRUD API |
| SMS-2 | Excel Export |
| SMS-3 | CI/CD & OpenShift Deploy |

## User Stories — Sprint 1 (javë 1)

| Key | Story | Tasks |
|-----|-------|-------|
| SMS-10 | Si përdorues, dua të regjistroj student të ri | Entity, DTO, Repository, POST endpoint |
| SMS-11 | Si përdorues, dua të shoh listën e studentëve | GET /api/students, MapStruct |
| SMS-12 | Si përdorues, dua të editoj/fshij student | PUT, DELETE endpoints |

## User Stories — Sprint 2 (javë 2)

| Key | Story | Tasks |
|-----|-------|-------|
| SMS-20 | Si përdorues, dua të eksportoj listën në Excel | Modul excel, Apache POI, GET export |
| SMS-21 | Si dev, dua pipeline Jenkins | Jenkinsfile, mvn clean install |
| SMS-22 | Si dev, dua deploy në OpenShift | Dockerfile, deployment.yaml, Route |

## Definition of Done

- [ ] Kodi në GitHub
- [ ] Testet kalojnë (`mvn test`)
- [ ] Code review (peer)
- [ ] Dokumentuar në README

## Screenshot për dorëzim

1. **Jira**: Sprint Board me stories në Done
2. **Jenkins**: Build #N — SUCCESS (test + artifact)
3. **OpenShift**: Deployment running + Route URL
