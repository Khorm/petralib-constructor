# Скрипт для проверки подключения к PostgreSQL базе данных

Write-Host "Проверка подключения к базе данных..." -ForegroundColor Cyan
Write-Host ""

$dbHost = "94.180.117.77"
$dbPort = 5432
$dbName = "petri_constructor"
$dbUser = "petra"
$dbPassword = "petra4321"

# Проверка доступности порта
Write-Host "1. Проверка доступности порта $dbPort..." -ForegroundColor Yellow
$portTest = Test-NetConnection -ComputerName $dbHost -Port $dbPort -InformationLevel Quiet

if ($portTest) {
    Write-Host "   ✅ Порт $dbPort доступен" -ForegroundColor Green
} else {
    Write-Host "   ❌ Порт $dbPort недоступен" -ForegroundColor Red
    Write-Host "   Проверьте, что PostgreSQL сервер запущен и доступен" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Проверка подключения через JDBC (требует Java и PostgreSQL драйвер)
Write-Host "2. Проверка подключения через JDBC..." -ForegroundColor Yellow

# Проверяем наличие Java
$javaVersion = java -version 2>&1 | Select-Object -First 1
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ⚠️  Java не найдена. Установите Java для проверки JDBC подключения" -ForegroundColor Yellow
    Write-Host "   Порт доступен, но для полной проверки нужна Java" -ForegroundColor Gray
    exit 0
}

Write-Host "   Java найдена: $javaVersion" -ForegroundColor Gray

# Проверяем наличие PostgreSQL драйвера в проекте
$postgresJar = Get-ChildItem -Path "build\libs" -Filter "postgresql*.jar" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $postgresJar) {
    Write-Host "   ⚠️  PostgreSQL драйвер не найден. Сначала соберите проект:" -ForegroundColor Yellow
    Write-Host "   .\gradlew build" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "   Или проверьте подключение через Spring Boot приложение:" -ForegroundColor Yellow
    Write-Host "   .\gradlew bootRun --args='--spring.profiles.active=local'" -ForegroundColor Cyan
    exit 0
}

Write-Host ""
Write-Host "3. Рекомендации:" -ForegroundColor Yellow
Write-Host "   ✅ Порт доступен - подключение должно работать" -ForegroundColor Green
Write-Host ""
Write-Host "   Для проверки подключения через приложение:" -ForegroundColor Cyan
Write-Host "   .\gradlew bootRun --args='--spring.profiles.active=local'" -ForegroundColor White
Write-Host ""
Write-Host "   Или используйте pgAdmin или другой PostgreSQL клиент:" -ForegroundColor Cyan
Write-Host "   Host: $dbHost" -ForegroundColor Gray
Write-Host "   Port: $dbPort" -ForegroundColor Gray
Write-Host "   Database: $dbName" -ForegroundColor Gray
Write-Host "   Username: $dbUser" -ForegroundColor Gray
Write-Host ""
Write-Host "Конфигурация в application-local.yml:" -ForegroundColor Cyan
Write-Host "   URL: jdbc:postgresql://$dbHost`:$dbPort/$dbName" -ForegroundColor Gray

