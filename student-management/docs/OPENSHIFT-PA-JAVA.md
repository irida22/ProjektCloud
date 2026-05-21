# OpenShift — PA Maven lokalisht (JAVA_HOME problem)

| | Emri |
|---|---|
| **OpenShift Project** (namespace) | `iridalala-dev` |
| **Aplikacioni** (deployment, route) | `projektcloud` |

Nëse merr:

```
The JAVA_HOME environment variable is not defined correctly
```

**Mos përdor `mvnw` në Web Terminal.** Përdor build nga **GitHub** — Maven ekzekutohet brenda Docker.

---

## Komanda (kopjo një nga një)

```bash
oc login
oc project iridalala-dev
# duhet: "Now using project \"iridalala-dev\" on server ..."

cd ~/ProjektCloud
git pull origin main
# VERIFIKO qe skedari ekziston:
ls -la student-management/openshift/buildconfig-projektcloud-git.yaml

cd student-management/openshift
pwd
# duhet: .../ProjektCloud/student-management/openshift

# Build nga GitHub (5-15 min — Maven brenda Docker)
oc apply -f buildconfig-projektcloud-git.yaml
oc start-build projektcloud-git
# Nese --follow jep "timed out", shiko logun manualisht:
# oc logs -f build/projektcloud-git-7
```

**MOS vazhdo** derisa build te jete **Complete**:

```bash
oc get builds | grep projektcloud-git
# STATUS duhet: Complete (JO Running, JO Failed)
```

Kur eshte **Complete**:

```bash
oc get istag projektcloud:latest
IMG=$(oc get istag projektcloud:latest -o jsonpath='{.image.dockerImageReference}{"\n"}')
echo "$IMG"
oc set image deployment/projektcloud projektcloud="$IMG"
oc rollout status deployment/projektcloud --timeout=5m
```

```bash
# Deploy + URL
oc get deployment projektcloud
oc get route projektcloud

# Nëse mungon route:
oc expose svc/projektcloud
oc get route projektcloud
```

---

## Testo

```bash
oc get pods -l app=projektcloud
oc logs deployment/projektcloud --tail=30
```

Duhet: `Started StudentManagementApplication`

Hap URL në shfletues.

---

## Opsional — rregullo JAVA_HOME (nëse do mvn lokalisht)

```bash
which java
ls /usr/lib/jvm/
export JAVA_HOME=/usr/lib/jvm/java-17
export PATH=$JAVA_HOME/bin:$PATH
java -version
cd ~/ProjektCloud/student-management
chmod +x mvnw
./mvnw clean package -DskipTests
```

(Nuk është e nevojshme për Plan B.)

---

## Gabime të zakonshme

| Gabim | Shkaku | Zgjidhja |
|--------|--------|----------|
| `ProjektCloud already exists` | `git clone` kur folderi ekziston | `cd ~/ProjektCloud && git pull` (mos `rm` nëse ke ndryshime lokale) |
| `buildconfig-projektcloud-git.yaml does not exist` | Repo i vjetër, je në folder të gabuar | `git pull` pastaj `cd student-management/openshift` |
| `projektcloud-git not found` | `oc apply` dështoi | Rregullo `apply` së pari, pastaj `start-build` |
| `timed out waiting` (--follow) | Web Terminal, build ende vazhdon | `oc get builds` + `oc logs build/projektcloud-git-N` |
| `istag projektcloud:latest not found` | Build nuk ka mbaruar | Prit **Complete**, mos `oc set image` para |
| `image: Required value` | `oc set image` me IMG bosh | `oc rollout undo deployment/projektcloud` pastaj provo perseri |
