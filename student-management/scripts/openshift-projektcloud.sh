#!/bin/bash
# Hapat 1-7 per deploy projektcloud (OpenShift Web Terminal)
set -e

APP=projektcloud
DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$DIR"

echo "=== Hapi 1-2: Maven package ==="
chmod +x mvnw 2>/dev/null || true
./mvnw clean package -DskipTests
ls -la target/*.jar

echo "=== Hapi 4: OpenShift build ==="
oc new-build --binary=true --name=$APP --strategy=docker 2>/dev/null || true
oc start-build $APP --from-dir=. --follow

echo "=== Hapi 5-6: Deploy + expose ==="
oc new-app $APP --name=$APP 2>/dev/null || oc rollout restart deployment/$APP
oc expose svc/$APP 2>/dev/null || true

echo "=== Hapi 7: URL ==="
oc get route $APP
