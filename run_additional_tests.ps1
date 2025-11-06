# Скрипт для запуска дополнительных тестов

Write-Host "🧪 Запуск дополнительных тестов (TC033-TC075)" -ForegroundColor Cyan
Write-Host ""

# Проверка доступности сервера
$apiServer = "http://94.180.117.77:8081"
$email = "r0meo1.ru@gmail.com"
$password = "8K3uLnPVGTtcm5a"

Write-Host "1. Проверка доступности API сервера..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$apiServer/" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ Сервер доступен (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Сервер недоступен: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "2. Получение JWT токена..." -ForegroundColor Yellow
try {
    $loginBody = @{
        email = $email
        password = $password
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$apiServer/api/v1/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody `
        -TimeoutSec 10 `
        -ErrorAction Stop

    $token = $loginResponse.token
    Write-Host "   ✅ Токен получен: $($token.Substring(0, 20))..." -ForegroundColor Green
} catch {
    Write-Host "   ❌ Ошибка аутентификации: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "3. Примеры дополнительных тестов:" -ForegroundColor Yellow
Write-Host ""

# Тест TC033: Login с неверными учетными данными
Write-Host "   TC033: Login с неверными учетными данными..." -ForegroundColor Cyan
try {
    $wrongBody = @{
        email = "wrong@email.com"
        password = "wrongpassword"
    } | ConvertTo-Json

    $wrongResponse = Invoke-WebRequest -Uri "$apiServer/api/v1/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $wrongBody `
        -TimeoutSec 10 `
        -ErrorAction Stop
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "      ✅ Тест пройден: 403 Forbidden (как ожидалось)" -ForegroundColor Green
    } else {
        Write-Host "      ⚠️  Неожиданный статус: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}

# Тест TC038: Get projects без аутентификации
Write-Host "   TC038: Get projects без аутентификации..." -ForegroundColor Cyan
try {
    $projectsResponse = Invoke-WebRequest -Uri "$apiServer/api/v1/project" `
        -Method GET `
        -TimeoutSec 10 `
        -ErrorAction Stop
} catch {
    if ($_.Exception.Response.StatusCode -eq 401) {
        Write-Host "      ✅ Тест пройден: 401 Unauthorized (как ожидалось)" -ForegroundColor Green
    } else {
        Write-Host "      ⚠️  Неожиданный статус: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}

# Тест TC039: Create project с пустым именем
Write-Host "   TC039: Create project с пустым именем..." -ForegroundColor Cyan
try {
    $emptyNameBody = @{
        name = ""
        description = "Test"
    } | ConvertTo-Json

    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }

    $emptyNameResponse = Invoke-WebRequest -Uri "$apiServer/api/v1/project" `
        -Method POST `
        -Headers $headers `
        -Body $emptyNameBody `
        -TimeoutSec 10 `
        -ErrorAction Stop
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "      ✅ Тест пройден: 400 Bad Request (как ожидалось)" -ForegroundColor Green
    } else {
        Write-Host "      ⚠️  Неожиданный статус: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}

# Тест TC040: Create project с именем > 100 символов
Write-Host "   TC040: Create project с именем > 100 символов..." -ForegroundColor Cyan
try {
    $longName = "A" * 101
    $longNameBody = @{
        name = $longName
        description = "Test"
    } | ConvertTo-Json

    $longNameResponse = Invoke-WebRequest -Uri "$apiServer/api/v1/project" `
        -Method POST `
        -Headers $headers `
        -Body $longNameBody `
        -TimeoutSec 10 `
        -ErrorAction Stop
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "      ✅ Тест пройден: 400 Bad Request (как ожидалось)" -ForegroundColor Green
    } else {
        Write-Host "      ⚠️  Неожиданный статус: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Дополнительные тесты выполнены!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Для полного набора тестов используйте TestSprite:" -ForegroundColor Yellow
Write-Host "  1. Добавьте тесты TC033-TC075 в testsprite_backend_test_plan.json" -ForegroundColor Gray
Write-Host "  2. Или используйте testsprite_backend_test_plan_extended.json" -ForegroundColor Gray
Write-Host "  3. Запустите: .\run_testsprite.ps1" -ForegroundColor Gray
Write-Host ""
Write-Host "Подробная документация: ADDITIONAL_TESTS_GUIDE.md" -ForegroundColor Cyan

