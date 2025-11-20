# Скрипт для проверки состояния проекта

Write-Host "=== Проверка проекта petralib-constructor ===" -ForegroundColor Cyan
Write-Host ""

# 1. Проверка Java
Write-Host "1. Проверка Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "   ✅ Java найдена: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Java не найдена" -ForegroundColor Red
}

Write-Host ""

# 2. Проверка Gradle wrapper
Write-Host "2. Проверка Gradle wrapper..." -ForegroundColor Yellow
if (Test-Path "gradlew.bat") {
    Write-Host "   ✅ gradlew.bat найден" -ForegroundColor Green
} else {
    Write-Host "   ❌ gradlew.bat не найден" -ForegroundColor Red
}

if (Test-Path "gradle\wrapper\gradle-wrapper.properties") {
    $gradleVersion = Select-String -Path "gradle\wrapper\gradle-wrapper.properties" -Pattern "gradle-(\d+\.\d+\.\d+)" | ForEach-Object { $_.Matches.Groups[1].Value }
    Write-Host "   ✅ Gradle wrapper настроен: версия $gradleVersion" -ForegroundColor Green
} else {
    Write-Host "   ❌ gradle-wrapper.properties не найден" -ForegroundColor Red
}

Write-Host ""

# 3. Проверка конфигурации
Write-Host "3. Проверка конфигурации..." -ForegroundColor Yellow
if (Test-Path "src\main\resources\application.yml") {
    Write-Host "   ✅ application.yml найден" -ForegroundColor Green
} else {
    Write-Host "   ❌ application.yml не найден" -ForegroundColor Red
}

if (Test-Path "src\test\resources\application-test.yml") {
    Write-Host "   ✅ application-test.yml найден" -ForegroundColor Green
} else {
    Write-Host "   ⚠️  application-test.yml не найден" -ForegroundColor Yellow
}

Write-Host ""

# 4. Проверка тестов
Write-Host "4. Проверка тестов..." -ForegroundColor Yellow
$testFiles = Get-ChildItem -Path "src\test\java" -Recurse -Filter "*Test.java" -ErrorAction SilentlyContinue
$testCount = ($testFiles | Measure-Object).Count
if ($testCount -gt 0) {
    Write-Host "   ✅ Найдено тестовых классов: $testCount" -ForegroundColor Green
    Write-Host "   Файлы:" -ForegroundColor Gray
    $testFiles | ForEach-Object { Write-Host "     - $($_.Name)" -ForegroundColor Gray }
} else {
    Write-Host "   ⚠️  Тесты не найдены" -ForegroundColor Yellow
}

Write-Host ""

# 5. Проверка подключения к БД
Write-Host "5. Проверка подключения к базе данных..." -ForegroundColor Yellow
$dbHost = "94.180.117.77"
$dbPort = 5432
$dbTest = Test-NetConnection -ComputerName $dbHost -Port $dbPort -InformationLevel Quiet -WarningAction SilentlyContinue
if ($dbTest) {
    Write-Host "   База данных доступна ($dbHost`:$dbPort)" -ForegroundColor Green
} else {
    Write-Host "   База данных недоступна ($dbHost`:$dbPort)" -ForegroundColor Red
}

Write-Host ""

# 6. Проверка документации
Write-Host "6. Проверка документации..." -ForegroundColor Yellow
$docs = @("README.md", "TESTING_README.md", "TEST_SUMMARY.md")
foreach ($doc in $docs) {
    if (Test-Path $doc) {
        Write-Host "   ✅ $doc найден" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  $doc не найден" -ForegroundColor Yellow
    }
}

Write-Host ""

# 7. Проверка структуры проекта
Write-Host "7. Проверка структуры проекта..." -ForegroundColor Yellow
$requiredDirs = @(
    "src\main\java",
    "src\main\resources",
    "src\test\java",
    "src\test\resources"
)
foreach ($dir in $requiredDirs) {
    if (Test-Path $dir) {
        Write-Host "   ✅ $dir существует" -ForegroundColor Green
    } else {
        Write-Host "   ❌ $dir не найден" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== Проверка завершена ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "Рекомендации:" -ForegroundColor Yellow
Write-Host "  - Для запуска тестов используйте IDE (IntelliJ IDEA)" -ForegroundColor White
Write-Host "  - Или исправьте проблему с Gradle кешем" -ForegroundColor White
Write-Host "  - Проверьте подключение к БД через pgAdmin" -ForegroundColor White

