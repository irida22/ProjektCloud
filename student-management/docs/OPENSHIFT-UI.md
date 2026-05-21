# OpenShift — UI (pa Web Terminal)

Projekti: **iridalala-dev** · Aplikacioni: **projektcloud**

> Nëse merr **`ghcr.io/... denied`** → përdor **Opsionin A** ose bëje paketën **Public** (shiko më poshtë).

---

## Opsioni A — Import from Git (më i thjeshtë) ⭐

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

### 1. Verifiko që imazhi ekziston

**GitHub** → **Actions** → **Docker Build and Push** → duhet ✅ **Success**.

Pastaj: **GitHub** → foto profili → **Your packages** → duhet **`projektcloud`**.

Nëse Actions dështon ose nuk ka paketë → Opsioni B nuk funksionon ende.

### 2. Bëje paketën **Public** (e detyrueshme për OpenShift pa secret)

1. Hap: https://github.com/users/irida22/packages
2. Kliko **projektcloud**
3. **Package settings** (djathtas)
4. Scroll → **Danger Zone** → **Change package visibility** → **Public**
5. Konfirmo emrin e paketës

### 3. OpenShift UI — Container Image

| Fushë | Vlera |
|--------|--------|
| Image | `ghcr.io/irida22/projektcloud:latest` |
| Application name | `projektcloud` |
| Create a route | ✅ |

**Create**

---

## Gabim: `ghcr.io/... denied`

OpenShift **nuk ka leje** të lexojë imazhin nga GHCR (zakonisht paketa **private**).

### Zgjidhja 1 — Public (më e lehtë)

Bëj hapat e **Opsionit B, pika 2** më sipër. Pastaj:

- Fshi deployment-in e dështuar
- **+ Add** → **Container Image** → përsëri `ghcr.io/irida22/projektcloud:latest`

### Zgjidhja 2 — Pull Secret (nëse e lë private)

1. **GitHub** → **Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
2. **Generate** → scope: **`read:packages`** → kopjo token-in
3. **OpenShift** → projekti `iridalala-dev` → **Workloads** → **Secrets** → **Create** → **Image pull secret**
   - Type: **Image pull secret**
   - Name: `ghcr-pull`
   - Registry: `ghcr.io`
   - Username: `irida22`
   - Password: *(token-i PAT)*
   - Email: `irida22@users.noreply.github.com`
4. Lidh secret-in me service account:
   - **Workloads** → **Service Accounts** → **default** → **Secrets** → **Add secret** → `ghcr-pull` (Image pull)
5. Rikrijo deployment-in (Container Image)

### Zgjidhja 3 — Mos përdor GHCR (rekomandohet)

Përdor **Opsionin A — Import from Git** (OpenShift ndërton vetë, pa `ghcr.io`, pa `denied`).

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
