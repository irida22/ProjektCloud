# OpenShift — Hapat e thjeshtë (projektcloud)

## Para fillimit

```bash
oc login
oc project   # shiko projektin aktiv
```

---

## Hapi 1–2 — Build Maven (krijo JAR)

```bash
# SIGUROHU qe je ne folderin e sakte (JO dy here ProjektCloud/...)
cd ~/ProjektCloud/student-management
pwd
# duhet: .../ProjektCloud/student-management

# Leje ekzekutimi per mvnw (nese: Permission denied)
chmod +x mvnw

./mvnw clean package -DskipTests
# OSE nese prap merr gabim:
# sh mvnw clean package -DskipTests
# OSE: mvn clean package -DskipTests

ls -la target/student-management.jar
# DUHET te ekzistoje para oc start-build!
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

**Vetem PAS build SUCCESS!**

```bash
oc new-app image-registry.openshift-image-registry.svc:5000/$(oc project -q)/projektcloud:latest --name=projektcloud
```

Ose:

```bash
oc new-app projektcloud:latest --name=projektcloud
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
