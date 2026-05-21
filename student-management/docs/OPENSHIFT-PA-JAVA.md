# OpenShift — PA Maven lokalisht (JAVA_HOME problem)

Nëse merr:

```
The JAVA_HOME environment variable is not defined correctly
```

**Mos përdor `mvnw` në Web Terminal.** Përdor build nga **GitHub** — Maven ekzekutohet brenda Docker.

---

## Komanda (kopjo një nga një)

```bash
cd ~
rm -rf ProjektCloud
git clone https://github.com/irida22/ProjektCloud.git
cd ProjektCloud/student-management/openshift

# Build nga GitHub (5-15 min — Maven brenda Docker)
oc apply -f buildconfig-projektcloud-git.yaml
oc start-build projektcloud-git --follow
```

Prit derisa shfaqet **Push successful**.

```bash
# Deploy + URL (nëse deployment nuk u krijua nga yaml)
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
