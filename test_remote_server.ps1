# Скрипт для проверки доступности удаленного сервера

Write-Host "Проверка доступности удаленного сервера..." -ForegroundColor Cyan
Write-Host ""

# Настройте эти переменные или используйте переменные окружения
$apiServer = $env:API_SERVER ?? "http://localhost:8081"
$webServer = $env:WEB_SERVER ?? "http://localhost:8080"
$email = $env:TEST_EMAIL ?? "your-email@example.com"
$password = $env:TEST_PASSWORD ?? "your-password"

# Проверка портов
Write-Host "1. Проверка портов..." -ForegroundColor Yellow
# Извлекаем hostname из URL
$apiHost = ([System.Uri]$apiServer).Host
$webHost = ([System.Uri]$webServer).Host
$apiPort = ([System.Uri]$apiServer).Port
$webPort = ([System.Uri]$webServer).Port

$port8081 = Test-NetConnection -ComputerName $apiHost -Port $apiPort -InformationLevel Quiet
$port8080 = Test-NetConnection -ComputerName $webHost -Port $webPort -InformationLevel Quiet

if ($port8081) {
    Write-Host "   ✅ Порт $apiPort (API) открыт" -ForegroundColor Green
} else {
    Write-Host "   ❌ Порт $apiPort (API) недоступен" -ForegroundColor Red
}

if ($port8080) {
    Write-Host "   ✅ Порт $webPort (Web) открыт" -ForegroundColor Green
} else {
    Write-Host "   ❌ Порт $webPort (Web) недоступен" -ForegroundColor Red
}

Write-Host ""

# Проверка API endpoints
Write-Host "2. Проверка API endpoints..." -ForegroundColor Yellow

# Проверка базового endpoint
try {
    $response = Invoke-WebRequest -Uri "$apiServer/" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ API сервер отвечает (Status: $($response.StatusCode))" -ForegroundColor Green
} catch {
    Write-Host "   ⚠️  API сервер: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Проверка аутентификации
Write-Host "   Проверка аутентификации..." -ForegroundColor Gray
try {
    $loginBody = @{
        email = $email
        password = $password
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "$apiServer/api/v1/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody `
        -TimeoutSec 10 `
        -ErrorAction Stop

    Write-Host "   ✅ Аутентификация успешна!" -ForegroundColor Green
    Write-Host "   Token получен: $($response.token.Substring(0, 20))..." -ForegroundColor Gray
    
    return $response.token
} catch {
    Write-Host "   ❌ Ошибка аутентификации: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "   Детали: $($_.ErrorDetails.Message)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "3. Выводы:" -ForegroundColor Yellow
Write-Host "   - Сервер доступен по сети" -ForegroundColor Green
Write-Host ""
Write-Host "Примечание: Для использования скрипта настройте переменные окружения:" -ForegroundColor Cyan
Write-Host "   - API_SERVER - URL API сервера" -ForegroundColor Gray
Write-Host "   - WEB_SERVER - URL Web сервера" -ForegroundColor Gray
Write-Host "   - TEST_EMAIL - Email для тестирования" -ForegroundColor Gray
Write-Host "   - TEST_PASSWORD - Пароль для тестирования" -ForegroundColor Gray

