# OpenShift — Hapat e thjeshtë (projektcloud)

## Para fillimit

```bash
oc login
oc project   # shiko projektin aktiv
```

---

## Hapi 1–2 — Build Maven (krijo JAR)

```bash
cd student-management
./mvnw clean package -DskipTests
# ose: mvn clean package -DskipTests

ls target/*.jar
# duhet: target/student-management.jar
```

---

## Hapi 3 — Dockerfile (tashmë në repo)

```
student-management/Dockerfile
```

```dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/student-management.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

---

## Hapi 4 — Build imazh në OpenShift

```bash
# Në folderin student-management (ku është Dockerfile + target/*.jar)
cd student-management

# Krijo BuildConfig binary (herën e parë)
oc new-build --binary=true --name=projektcloud --strategy=docker

# Build nga folderi aktual
oc start-build projektcloud --from-dir=. --follow
```

---

## Hapi 5 — Deploy

```bash
oc new-app projektcloud --name=projektcloud
```

Nëse thotë që ekziston, përdor:

```bash
oc delete all -l app=projektcloud
oc new-app projektcloud
```

---

## Hapi 6 — Expose URL

```bash
oc expose svc/projektcloud
```

---

## Hapi 7 — Merr link

```bash
oc get route projektcloud
```

Hap në shfletues URL-n (HOST/PORT).

Test:

```bash
curl -k https://<HOST>/actuator/health
```

---

## Verifiko logs

```bash
oc get pods
oc logs deployment/projektcloud --tail=50
```

Duhet: `Started StudentManagementApplication` (JO gabim JAR).

---

## Nëse build binary dështon

Sigurohu:

1. `mvn package` u ekzekutua **para** `oc start-build`
2. Ekziston `target/student-management.jar`
3. Je në folderin `student-management` kur bën `--from-dir=.`
