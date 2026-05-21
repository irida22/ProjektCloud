#!/bin/bash
# Deploy nga OpenShift Web Terminal — rregullon gabimin JAR / deployments
set -e

APP_NAME="student-management"
REPO="https://github.com/irida22/ProjektCloud.git"
CONTEXT="student-management"

echo "=== Fshij deployment te vjeter (projektcloud / student-management) ==="
oc delete deployment projektcloud student-management --ignore-not-found 2>/dev/null || true
oc delete svc projektcloud student-management --ignore-not-found 2>/dev/null || true
oc delete route projektcloud student-management --ignore-not-found 2>/dev/null || true
oc delete bc projektcloud student-management --ignore-not-found 2>/dev/null || true

echo "=== Klon repo ==="
cd /tmp
rm -rf ProjektCloud
git clone "$REPO"
cd ProjektCloud/$CONTEXT/openshift

echo "=== Build Docker image (Maven + JAR brenda imazhit) ==="
oc apply -f buildconfig.yaml
oc start-build $APP_NAME --follow

echo "=== Deploy + Route ==="
oc apply -f deployment.yaml

echo ""
echo "=== URL ==="
oc get route $APP_NAME

HOST=$(oc get route $APP_NAME -o jsonpath='{.spec.host}' 2>/dev/null || echo "")
if [ -n "$HOST" ]; then
  echo "Hap ne shfletues: https://$HOST/"
fi

echo ""
echo "=== Pods ==="
oc get pods -l app=$APP_NAME
