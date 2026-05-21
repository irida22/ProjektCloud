# Deploy Student Management në OpenShift (PowerShell)
# Parakusht: oc login, OpenShift cluster aktiv

param(
    [string]$ProjectName = "student-mgmt",
    [switch]$SkipBuild
)

$ErrorActionPreference = "Stop"
$Root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$OpenshiftDir = Join-Path $Root "openshift"

Write-Host "=== OpenShift Deploy ===" -ForegroundColor Cyan

# Projekt
$exists = oc get project $ProjectName 2>$null
if (-not $exists) {
    Write-Host "Krijoj projektin: $ProjectName"
    oc new-project $ProjectName
} else {
    oc project $ProjectName
}

if (-not $SkipBuild) {
    Write-Host "Build nga GitHub..." -ForegroundColor Yellow
    oc apply -f (Join-Path $OpenshiftDir "buildconfig.yaml")
    oc start-build student-management --follow
}

Write-Host "Deploy + Route..." -ForegroundColor Yellow
oc apply -f (Join-Path $OpenshiftDir "deployment.yaml")

Write-Host "`n=== URL e aplikacionit ===" -ForegroundColor Green
oc get route student-management -o jsonpath='https://{..spec.host}{"\n"}'

Write-Host "`nPods:" -ForegroundColor Green
oc get pods -l app=student-management

Write-Host "`nTest health (pas 30-60 sek):" -ForegroundColor Yellow
$hostUrl = oc get route student-management -o jsonpath='{.spec.host}'
if ($hostUrl) {
    Write-Host "https://$hostUrl/"
    Write-Host "https://$hostUrl/api/students"
    Write-Host "https://$hostUrl/actuator/health"
}
