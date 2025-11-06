# Полный автоматический запуск приложения и тестов TestSprite

$ErrorActionPreference = "Continue"
$ProgressPreference = "SilentlyContinue"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TestSprite - Полный автоматический запуск" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Шаг 1: Запуск приложения
Write-Host "[ШАГ 1/4] Запуск Spring Boot приложения..." -ForegroundColor Yellow
Write-Host "Команда: .\gradlew.bat bootRun --args='--spring.profiles.active=test'" -ForegroundColor Gray

$appProcess = Start-Process -FilePath ".\gradlew.bat" -ArgumentList "bootRun","--args=--spring.profiles.active=test" -PassThru -WindowStyle Minimized -NoNewWindow

Write-Host "Приложение запущено (PID: $($appProcess.Id))" -ForegroundColor Gray
Write-Host "Ожидание запуска приложения (это может занять 30-60 секунд)..." -ForegroundColor Gray
Write-Host ""

# Шаг 2: Ожидание порта 8080
Write-Host "[ШАГ 2/4] Ожидание запуска на порту 8080..." -ForegroundColor Yellow

$maxWait = 90
$waited = 0
$portOpen = $false

while ($waited -lt $maxWait -and -not $portOpen) {
    Start-Sleep -Seconds 3
    $waited += 3
    try {
        $portOpen = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue
        if ($portOpen) {
            Write-Host "✓ Приложение запущено на порту 8080! (ожидание: $waited секунд)" -ForegroundColor Green
            break
        } else {
            Write-Host "  Ожидание... ($waited/$maxWait секунд)" -ForegroundColor Gray
        }
    } catch {
        Write-Host "  Проверка порта... ($waited/$maxWait секунд)" -ForegroundColor Gray
    }
}

if (-not $portOpen) {
    Write-Host ""
    Write-Host "✗ Приложение не запустилось за $maxWait секунд" -ForegroundColor Red
    Write-Host "Проверьте логи приложения вручную." -ForegroundColor Yellow
    Write-Host ""
    Stop-Process -Id $appProcess.Id -Force -ErrorAction SilentlyContinue
    exit 1
}

Write-Host ""

# Шаг 3: Запуск тестов
Write-Host "[ШАГ 3/4] Запуск тестов TestSprite..." -ForegroundColor Yellow

$testScript = "C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js"

if (-not (Test-Path $testScript)) {
    Write-Host "✗ Скрипт TestSprite не найден: $testScript" -ForegroundColor Red
    Write-Host "Проверьте путь или установите TestSprite." -ForegroundColor Yellow
    Stop-Process -Id $appProcess.Id -Force -ErrorAction SilentlyContinue
    exit 1
}

Write-Host "Выполнение: node $testScript generateCodeAndExecute" -ForegroundColor Gray
Write-Host ""

try {
    $testOutput = & node $testScript generateCodeAndExecute 2>&1
    $testResult = $LASTEXITCODE
    
    Write-Host "Вывод тестов:" -ForegroundColor Cyan
    $testOutput | ForEach-Object { Write-Host $_ }
    
    Write-Host ""
    
    if ($testResult -eq 0) {
        Write-Host "[ШАГ 4/4] ✓ Тесты выполнены успешно!" -ForegroundColor Green
    } else {
        Write-Host "[ШАГ 4/4] Тесты завершены с кодом выхода: $testResult" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Ошибка при выполнении тестов: $_" -ForegroundColor Red
    $testResult = 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Результаты тестов:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Проверка файлов результатов
$rawReport = "testsprite_tests\tmp\raw_report.md"
$finalReport = "testsprite_tests\testsprite-mcp-test-report.md"

if (Test-Path $rawReport) {
    Write-Host "✓ Сырой отчет: $rawReport" -ForegroundColor Green
    Write-Host "  Размер: $((Get-Item $rawReport).Length) байт" -ForegroundColor Gray
} else {
    Write-Host "✗ Сырой отчет не найден: $rawReport" -ForegroundColor Yellow
}

if (Test-Path $finalReport) {
    Write-Host "✓ Финальный отчет: $finalReport" -ForegroundColor Green
    Write-Host "  Размер: $((Get-Item $finalReport).Length) байт" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Первые 30 строк отчета:" -ForegroundColor Cyan
    Get-Content $finalReport -Head 30 | ForEach-Object { Write-Host $_ }
} else {
    Write-Host "✗ Финальный отчет не найден: $finalReport" -ForegroundColor Yellow
    Write-Host "  (Может быть создан позже)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Остановка приложения..." -ForegroundColor Gray
Stop-Process -Id $appProcess.Id -Force -ErrorAction SilentlyContinue
Write-Host "✓ Приложение остановлено" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Процесс завершен!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

exit $testResult

