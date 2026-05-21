# Rregullimi: "0 JARs found in /deployments"

## Shkaku i gabimit

```
ERROR Neither $JAVA_MAIN_CLASS nor $JAVA_APP_JAR is set and 0 JARs found in /deployments
```

OpenShift përdori **Java S2I** (`oc new-app` me builder Java), jo **Dockerfile** tonë.
Multi-module Maven nuk vendos JAR-in në `/deployments` → pod-i nuk nis.

## Zgjidhja: fshi deployment-in e gabuar dhe ri-deploy me Docker

Ekzekuto në **OpenShift Web Terminal** (copy-paste një nga një):

```bash
# 1. Shiko projektin aktiv
oc project

# 2. Fshi aplikacionin e prishur (emri: projektcloud)
oc delete deployment projektcloud --ignore-not-found
oc delete svc projektcloud --ignore-not-found
oc delete route projektcloud --ignore-not-found
oc delete bc projektcloud --ignore-not-found
oc delete istag projektcloud:latest --ignore-not-found

# 3. Klon repo (nëse nuk e ke)
cd ~
rm -rf ProjektCloud 2>/dev/null
git clone https://github.com/irida22/ProjektCloud.git
cd ProjektCloud/student-management/openshift

# 4. Build me DOCKER (Maven brenda Dockerfile → krijon JAR)
oc apply -f buildconfig.yaml
oc start-build student-management --follow

# 5. Deploy + URL
oc apply -f deployment.yaml

# 6. URL e aplikacionit
oc get route student-management
```

## Testo URL

```bash
HOST=$(oc get route student-management -o jsonpath='{.spec.host}')
echo "https://$HOST/"
echo "https://$HOST/api/students"
echo "https://$HOST/actuator/health"
```

## Verifiko që pod-i punon

```bash
oc get pods
oc logs deployment/student-management --tail=50
```

Duhet të shohësh: `Started StudentManagementApplication` (jo gabim JAR).

## Nëse build dështon

```bash
oc logs -f bc/student-management
```

Kontrollo që `contextDir` është `student-management` dhe Dockerfile ekziston në GitHub.
