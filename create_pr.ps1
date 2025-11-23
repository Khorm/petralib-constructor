# Скрипт для создания Pull Request через GitHub API
# Требуется GitHub Personal Access Token с правами repo

param(
    [string]$Title = "Организация тестов: разделение на автотесты и ручные тесты",
    [string]$Body = @"
## Описание

Организация тестового покрытия с разделением на автотесты и ручные тесты.

## Изменения

- ✅ Добавлены аннотации `@AutoTest` и `@ManualTest` для разделения тестов
- ✅ Настроен Gradle для запуска только автотестов по умолчанию
- ✅ Добавлены задачи: `testAuto`, `testManual`, `testAll`
- ✅ Все существующие тесты помечены как `@AutoTest`
- ✅ Исправлены ошибки компиляции:
  * Добавлена зависимость `spring-security-test`
  * Исправлен импорт `DataSource` в `DatabaseConnectionTest`
  * Исправлен `ProjectServiceTest` (заменен `findByUserId` на `findAll`)
- ✅ Создан пример ручного теста `ManualTestExample`
- ✅ Добавлена документация: `TEST_ORGANIZATION.md` и `QUICK_TEST_GUIDE.md`
- ✅ Создан `gradle.properties` для настройки JVM

## Команды для тестирования

\`\`\`bash
# Только автотесты (по умолчанию)
./gradlew test

# Все автотесты
./gradlew testAuto

# Только ручные тесты
./gradlew testManual

# Все тесты
./gradlew testAll
\`\`\`

## Документация

- [TEST_ORGANIZATION.md](TEST_ORGANIZATION.md) - полная документация
- [QUICK_TEST_GUIDE.md](QUICK_TEST_GUIDE.md) - быстрый гайд
"@,
    [string]$BaseBranch = "develop",
    [string]$HeadBranch = "feature/test-organization-improvements",
    [string]$Repo = "Khorm/petralib-constructor"
)

# Проверка наличия токена
$token = $env:GITHUB_TOKEN
if (-not $token) {
    Write-Host "❌ Ошибка: Не найден GitHub токен!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Установите переменную окружения GITHUB_TOKEN:" -ForegroundColor Yellow
    Write-Host "  `$env:GITHUB_TOKEN = 'your-token-here'" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Или создайте PR вручную по ссылке:" -ForegroundColor Yellow
    Write-Host "  https://github.com/$Repo/compare/$BaseBranch...$HeadBranch" -ForegroundColor Cyan
    exit 1
}

# Создание PR через API
$headers = @{
    "Authorization" = "token $token"
    "Accept" = "application/vnd.github.v3+json"
}

$bodyJson = @{
    title = $Title
    body = $Body
    head = $HeadBranch
    base = $BaseBranch
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "https://api.github.com/repos/$Repo/pulls" `
        -Method Post `
        -Headers $headers `
        -Body $bodyJson `
        -ContentType "application/json"
    
    Write-Host "✅ Pull Request успешно создан!" -ForegroundColor Green
    Write-Host ""
    Write-Host "PR #$($response.number): $($response.title)" -ForegroundColor Cyan
    Write-Host "URL: $($response.html_url)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Ошибка при создании PR:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Ответ сервера: $responseBody" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "Создайте PR вручную по ссылке:" -ForegroundColor Yellow
    Write-Host "  https://github.com/$Repo/compare/$BaseBranch...$HeadBranch" -ForegroundColor Cyan
}

