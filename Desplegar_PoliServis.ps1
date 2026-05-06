# ==========================================================
# Script de Despliegue Automático - PoliServis GR06
# ==========================================================

# 1. Limpiar contenedores y redes previas
Write-Host "Limpiando entorno previo..." -ForegroundColor Cyan
docker rm -f poliservis-app poliservis-jenkins 2>$null
docker network rm poliservis-net 2>$null

# 2. Descargar las imágenes más recientes de Docker Hub
Write-Host "Descargando imágenes desde Docker Hub (jimmynow)..." -ForegroundColor Cyan
docker pull jimmynow/uniservicios:latest
docker pull jimmynow/uniservicios-jenkins:latest

# 3. Crear red para intercomunicación
docker network create poliservis-net

# 4. Desplegar Aplicación (Puerto 8085)
Write-Host "Iniciando Aplicación en puerto 8085..." -ForegroundColor Green
docker run -d `
  --name poliservis-app `
  -p 8085:8080 `
  --network poliservis-net `
  -e MYSQL_HOST=poli-servis-jimmyarias772.g.aivencloud.com `
  -e MYSQL_PORT=13512 `
  -e MYSQL_DATABASE=defaultdb `
  -e MYSQL_USER=avnadmin `
  -e MYSQL_PASSWORD=AVNS_z0fh2LSeHStzFyIugpo `
  --restart unless-stopped `
  jimmynow/uniservicios:latest

# 5. Desplegar Jenkins (Puerto 8082)
Write-Host "Iniciando Jenkins en puerto 8082..." -ForegroundColor Green
docker run -d `
  --name poliservis-jenkins `
  -p 8082:8080 `
  -p 50000:50000 `
  --network poliservis-net `
  -v /var/run/docker.sock:/var/run/docker.sock `
  --restart unless-stopped `
  jimmynow/uniservicios-jenkins:latest

Write-Host "`n==========================================================" -ForegroundColor Yellow
Write-Host "¡DESPLIEGUE COMPLETADO!" -ForegroundColor Yellow
Write-Host "App: http://localhost:8085" -ForegroundColor White
Write-Host "Jenkins: http://localhost:8082" -ForegroundColor White
Write-Host "==========================================================" -ForegroundColor Yellow
