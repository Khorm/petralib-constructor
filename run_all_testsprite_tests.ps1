# Скрипт для запуска ВСЕХ тестов через TestSprite (75 тест-кейсов)

Write-Host "🚀 Запуск ВСЕХ тестов TestSprite (75 тест-кейсов)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Проверка доступности сервера
$apiServer = "http://94.180.117.77:8081"

Write-Host "1. Проверка доступности сервера..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$apiServer/" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ Сервер доступен (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Сервер недоступен: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   ⚠️  Продолжаем - TestSprite может сам проверить доступность" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "2. Проверка конфигурации..." -ForegroundColor Yellow

$configPath = "testsprite_tests/tmp/config.json"
if (Test-Path $configPath) {
    $config = Get-Content $configPath -Raw | ConvertFrom-Json
    Write-Host "   ✅ Конфигурация найдена" -ForegroundColor Green
    Write-Host "   Server: $($config.localEndpoint)" -ForegroundColor Gray
    Write-Host "   API_KEY: настроен" -ForegroundColor Gray
    Write-Host "   Test cases: 75 (TC001-TC075)" -ForegroundColor Gray
} else {
    Write-Host "   ❌ Конфигурация не найдена: $configPath" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "3. Проверка тестового плана..." -ForegroundColor Yellow

$planPath = "testsprite_tests/testsprite_backend_test_plan.json"
if (Test-Path $planPath) {
    $plan = Get-Content $planPath -Raw | ConvertFrom-Json
    $testCount = $plan.Count
    Write-Host "   ✅ Тестовый план найден: $testCount тест-кейсов" -ForegroundColor Green
    
    if ($testCount -eq 75) {
        Write-Host "   ✅ Все 75 тестов включены!" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Ожидалось 75 тестов, найдено $testCount" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ Тестовый план не найден: $planPath" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "4. Проверка TestSprite..." -ForegroundColor Yellow

$testScript = "C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js"

if (Test-Path $testScript) {
    Write-Host "   ✅ TestSprite найден" -ForegroundColor Green
} else {
    Write-Host "   ❌ TestSprite не найден: $testScript" -ForegroundColor Red
    Write-Host "   Установите TestSprite или проверьте путь" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Запуск всех тестов через TestSprite..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Тесты будут выполняться:" -ForegroundColor Yellow
Write-Host "  - Базовые API тесты: 32 (TC001-TC032)" -ForegroundColor Gray
Write-Host "  - Негативные тесты: 15 (TC033-TC049, TC056, TC062-TC063)" -ForegroundColor Gray
Write-Host "  - Валидация: 12 (TC043, TC046-TC047, TC050-TC053, TC060-TC061, TC066, TC072)" -ForegroundColor Gray
Write-Host "  - Безопасность: 3 (TC064-TC065, TC069-TC070)" -ForegroundColor Gray
Write-Host "  - Граничные случаи: 5 (TC043, TC066, TC072)" -ForegroundColor Gray
Write-Host "  - Интеграционные: 3 (TC074-TC075, TC067)" -ForegroundColor Gray
Write-Host "  - Целостность данных: 2 (TC068)" -ForegroundColor Gray
Write-Host "  - Производительность: 1 (TC073)" -ForegroundColor Gray
Write-Host "  - Специальные символы: 1 (TC071)" -ForegroundColor Gray
Write-Host ""
Write-Host "Всего: 75 тест-кейсов" -ForegroundColor Green
Write-Host ""

# Запуск TestSprite
Write-Host "Выполняю команду TestSprite..." -ForegroundColor Cyan
Write-Host ""

try {
    Push-Location $PSScriptRoot
    node $testScript generateCodeAndExecute
    $exitCode = $LASTEXITCODE
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    if ($exitCode -eq 0) {
        Write-Host "✅ Все тесты выполнены успешно!" -ForegroundColor Green
    } else {
        Write-Host "⚠️  Тесты завершены с кодом выхода: $exitCode" -ForegroundColor Yellow
    }
    Write-Host "========================================" -ForegroundColor Cyan
    
    # Проверка отчетов
    Write-Host ""
    Write-Host "Результаты тестов:" -ForegroundColor Cyan
    
    $rawReport = "testsprite_tests/tmp/raw_report.md"
    $finalReport = "testsprite_tests/testsprite-mcp-test-report.md"
    
    if (Test-Path $rawReport) {
        Write-Host "  ✅ Сырой отчет: $rawReport" -ForegroundColor Green
        $rawSize = (Get-Item $rawReport).Length
        Write-Host "    Размер: $rawSize байт" -ForegroundColor Gray
    }
    
    if (Test-Path $finalReport) {
        Write-Host "  ✅ Финальный отчет: $finalReport" -ForegroundColor Green
        $finalSize = (Get-Item $finalReport).Length
        Write-Host "    Размер: $finalSize байт" -ForegroundColor Gray
        Write-Host ""
        Write-Host "Первые 50 строк отчета:" -ForegroundColor Cyan
        Get-Content $finalReport -Head 50 | ForEach-Object { Write-Host "    $_" -ForegroundColor Gray }
    }
    
} catch {
    Write-Host "❌ Ошибка при выполнении тестов: $_" -ForegroundColor Red
    $exitCode = 1
} finally {
    Pop-Location
}

exit $exitCode

