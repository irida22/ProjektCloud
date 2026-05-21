# OpenShift — UI (pa Web Terminal)

Projekti: **iridalala-dev** · Aplikacioni: **projektcloud**

---

## Opsioni A — Import from Git (më i thjeshtë)

OpenShift ndërton imazhin vetë nga `Dockerfile` (Docker, **jo** Java S2I).

### 1. Fshi deployment të vjetër (nëse ka ImagePullBackOff)

**Topology** → zgjidh **projektcloud** → **Delete** (ose Applications → Deployments → Delete).

### 2. + Add → Import from Git

| Fushë | Vlera |
|--------|--------|
| Git repo URL | `https://github.com/irida22/ProjektCloud.git` |
| Branch | `main` |
| Context dir | *(bosh — Dockerfile është në root)* |
| Builder | **Dockerfile** (detektohet automatikisht) |
| Dockerfile path | `Dockerfile` |
| Application name | `projektcloud` |
| Create a route | ✅ |
| Create | Kliko |

### 3. Prit build-in

**Builds** → `projektcloud-1` → **Logs** → duhet **Complete** (5–15 min).

### 4. Verifiko

**Workloads** → Pods → **Running**, **1/1**.

**Networking** → Routes → hap URL-n (p.sh. `projektcloud-iridalala-dev.apps...`).

---

## Opsioni B — GitHub Actions + Container Image

Imazhi ndërtohet në GitHub dhe OpenShift e tërheq.

### 1. Push në GitHub

Çdo push në `main` (në `student-management/`) nis workflow-in:

`.github/workflows/docker-build-push.yml`

Shiko: **GitHub** → **Actions** → **Docker Build and Push** → ✅ Success.

### 2. Bëje paketën publike (herën e parë)

**GitHub** → profili → **Packages** → `projektcloud` → **Package settings** → **Change visibility** → **Public**.

(Ose krijo **Pull Secret** në OpenShift për GHCR private.)

### 3. OpenShift UI

**+ Add** → **Container Image**

| Fushë | Vlera |
|--------|--------|
| Image | `ghcr.io/irida22/projektcloud:latest` |
| Application name | `projektcloud` |
| Create a route | ✅ |

**Create**

---

## Shënime

- `COPY target/*.jar` **nuk funksionon** — projekti është **multi-module**; JAR është `web/target/student-management.jar`.
- Dockerfile në **root** dhe në `student-management/` janë të përshtatura për këtë.
- **Mos** zgjidh **Java** builder (S2I) — jep gabimin “0 JARs in /deployments”.

---

## Screenshot për detyrën

1. OpenShift → Route URL në shfletues (lista studentëve)
2. GitHub Actions → build i gjelbër (Opsioni B)
3. OpenShift → Build Complete (Opsioni A)
