# ImagePullBackOff — rregullim i shpejtë

## Shkaku

`image: projektcloud:latest` në Kubernetes do të thotë **Docker Hub**, jo imazhi i build-it në OpenShift.

---

## Hapi 1 — A ka imazh build-i?

```bash
oc project iridalala-dev
oc get builds | tail -5
oc describe is/projektcloud
```

Duhet të shohësh tag `latest` me `Docker Image Reference`.

Nëse **nuk ka** imazh ose build **Failed**:

```bash
cd ~/ProjektCloud/student-management/openshift
git pull
oc start-build projektcloud-git --follow
```

Prit **Push successful**.

---

## Hapi 2 — Lidh deployment me ImageStream

```bash
oc project iridalala-dev

# Merr emrin e saktë të imazhit nga registry
IMG=$(oc get istag projektcloud:latest -o jsonpath='{.image.dockerImageReference}{"\n"}')
echo "$IMG"

oc set image deployment/projektcloud projektcloud="$IMG"
oc rollout status deployment/projektcloud --timeout=5m
```

---

## Hapi 3 — Verifiko

```bash
oc get pods -l app=projektcloud
oc logs deployment/projektcloud --tail=30
oc get route projektcloud
```

`STATUS` duhet të bëhet **Running** / **1/1 Ready**.

Hap: `https://projektcloud-iridalala-dev.apps.rm1.0a51.p1.openshiftapps.com`

---

## Nëse prap ImagePullBackOff

```bash
oc describe pod -l app=projektcloud | tail -20
```

Dërgo output-in (rreshti `Failed to pull image`).
