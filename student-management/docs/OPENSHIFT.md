# Deploy në OpenShift — Student Management System

Repository: https://github.com/irida22/ProjektCloud

## Çfarë përmbush kërkesën e kursit

| Kërkesa | Si realizohet |
|---------|----------------|
| Deploy në OpenShift | `oc apply` + BuildConfig / Deployment |
| Aplikacion i aksesueshëm (URL) | **Route** — `oc get route` |

---

## Parakushtet

1. **OpenShift** (Red Hat OpenShift, CRC/OKD, ose cluster shkolle)
2. **CLI `oc`** i instaluar dhe i loguar:
   ```bash
   oc login <API_URL>
   ```
3. (Opsional) **Docker** për build lokal

---

## Metoda A — Deploy nga GitHub (rekomanduar)

### 1. Krijo projekt

```bash
oc new-project student-mgmt
```

### 2. Build imazh nga GitHub

```bash
cd student-management/openshift
oc apply -f buildconfig.yaml
oc start-build student-management --follow
```

Build merr kodin nga GitHub (`contextDir: student-management`) dhe ndërton Docker image me Maven brenda Dockerfile.

### 3. Deploy + Route (URL publike)

```bash
oc apply -f deployment.yaml
```

### 4. Merr URL-n

```bash
oc get route student-management
```

Shembull output:

```
NAME                 HOST/PORT
student-management   https://student-management-student-mgmt.apps.<cluster>.com
```

### 5. Testo në shfletues

- **UI:** `https://<HOST>/`
- **API:** `https://<HOST>/api/students`
- **Health:** `https://<HOST>/actuator/health`

---

## Metoda B — Build lokal + push (Windows)

### 1. Build JAR

```powershell
cd student-management
.\mvnw.cmd clean package -DskipTests
```

### 2. Docker image

```powershell
docker build -t student-management:latest .
```

### 3. OpenShift

```bash
oc new-project student-mgmt
oc tag --source=docker://student-management:latest student-management:latest
# ose push në internal registry:
# docker login -u $(oc whoami) -p $(oc whoami -t) $(oc registry info)
# docker tag student-management:latest $(oc registry info)/student-mgmt/student-management:latest
# docker push ...
oc apply -f openshift/deployment.yaml
oc get route student-management
```

---

## Verifikimi

```bash
oc get pods
# STATUS: Running

oc get route
# HOST: URL publike

curl -k https://<HOST>/actuator/health
# {"status":"UP"}
```

---

## Screenshot për dorëzim

1. `oc get pods` — pod **Running**
2. `oc get route` — **HOST/URL**
3. Shfletuesi — faqja e aplikacionit ose `/api/students`
4. (Opsional) OpenShift Console — Topology me Deployment + Route

---

## Struktura skedarëve

```
student-management/
├── Dockerfile              # Multi-stage: Maven build + JRE
└── openshift/
    ├── buildconfig.yaml    # Build nga GitHub
    └── deployment.yaml     # Deployment + Service + Route
```

---

## Probleme të zakonshme

| Problem | Zgjidhje |
|---------|----------|
| Pod `CrashLoopBackOff` | `oc logs deployment/student-management` |
| Route pa URL | `oc expose svc student-management` ose ri-apliko `deployment.yaml` |
| Build dështon | Verifiko `oc logs -f bc/student-management` |
| 503 / not ready | Prit 1–2 min (Spring Boot + probes) |

---

## Përshkrim për PDF (kursi)

> Aplikacioni u deploy-ua në OpenShift duke përdorur Docker multi-stage build dhe manifestet Kubernetes (Deployment, Service, Route). Route jep URL publike HTTPS; health check përdor `/actuator/health`. Database: H2 in-memory (profili `openshift`).
