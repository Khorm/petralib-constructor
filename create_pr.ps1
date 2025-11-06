# Скрипт для автоматического создания Pull Request через GitHub API

param(
    [string]$GitHubToken = $env:GITHUB_TOKEN,
    [string]$RepoOwner = "Khorm",
    [string]$RepoName = "petralib-constructor",
    [string]$BaseBranch = "develop",
    [string]$HeadBranch = "feature/add-testsprite-tests",
    [string]$Title = "Add TestSprite and JUnit tests (32 test cases)",
    [string]$Body = ""
)

if (-not $GitHubToken) {
    Write-Host "❌ GitHub token не найден!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Создайте Personal Access Token на GitHub:" -ForegroundColor Yellow
    Write-Host "1. Перейдите: https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host "2. Нажмите 'Generate new token (classic)'" -ForegroundColor Cyan
    Write-Host "3. Выберите scope: 'repo' (для создания PR)" -ForegroundColor Cyan
    Write-Host "4. Скопируйте токен" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Затем установите переменную окружения:" -ForegroundColor Yellow
    Write-Host '  $env:GITHUB_TOKEN = "ваш_токен"' -ForegroundColor Green
    Write-Host "  .\create_pr.ps1" -ForegroundColor Green
    Write-Host ""
    Write-Host "Или передайте токен напрямую:" -ForegroundColor Yellow
    Write-Host '  .\create_pr.ps1 -GitHubToken "ваш_токен"' -ForegroundColor Green
    exit 1
}

if ([string]::IsNullOrEmpty($Body)) {
    $Body = @"
## Описание

Этот PR добавляет комплексное тестирование для petralib-constructor.

### TestSprite Tests (32 тест-кейса)
- **Authentication API** (2 теста)
  - User login
  - User logout

- **Project Management API** (3 теста)
  - Get all projects
  - Create new project
  - Delete project
  - Get current user

- **Block Management API** (9 тестов)
  - Get workflow/action/source blocks page
  - Create/update workflow/action/source blocks
  - Get accepted sources
  - Get source by ID
  - Delete block

- **Type Management API** (5 тестов)
  - Get types page
  - Get all types
  - Create/update type
  - Delete type
  - Get type fields

- **Scenario Management API** (6 тестов)
  - Get scenario blocks for workflow
  - Save scenario
  - Get/save scenario variables
  - Get/save workflow exit variables

- **Service Management API** (6 тестов)
  - Get services page
  - Get all services
  - Create/update service
  - Get service by ID
  - Delete service
  - Download service file

### JUnit Tests (5 тестовых классов)
- ConstructorApplicationTest - проверка загрузки контекста
- AuthControllerTest - тесты API аутентификации
- BlockRestControllerTest - тесты API блоков
- BlockServiceTest - unit тесты сервиса
- TypeRestControllerTest - тесты API типов

### Конфигурация
- H2 тестовая база данных
- TestSprite конфигурация
- Spring Security Test для тестирования безопасности

### Документация
- README_TESTSPRITE.md
- README-TESTING.md
- TEST_SETUP.md
- ЗАПУСК_ТЕСТОВ.md

Все тесты готовы к выполнению.
"@
}

$headers = @{
    "Authorization" = "Bearer $GitHubToken"
    "Accept" = "application/vnd.github.v3+json"
    "Content-Type" = "application/json"
}

$bodyJson = @{
    title = $Title
    body = $Body
    head = $HeadBranch
    base = $BaseBranch
} | ConvertTo-Json -Depth 10

Write-Host "Создание Pull Request..." -ForegroundColor Cyan
Write-Host "  Репозиторий: $RepoOwner/$RepoName" -ForegroundColor Gray
Write-Host "  Base branch: $BaseBranch" -ForegroundColor Gray
Write-Host "  Head branch: $HeadBranch" -ForegroundColor Gray
Write-Host ""

try {
    $response = Invoke-RestMethod -Uri "https://api.github.com/repos/$RepoOwner/$RepoName/pulls" `
        -Method Post `
        -Headers $headers `
        -Body $bodyJson `
        -ErrorAction Stop

    Write-Host "✅ Pull Request успешно создан!" -ForegroundColor Green
    Write-Host ""
    Write-Host "PR #$($response.number): $($response.title)" -ForegroundColor Cyan
    Write-Host "URL: $($response.html_url)" -ForegroundColor Green
    Write-Host ""
    Write-Host "Статус: $($response.state)" -ForegroundColor Gray
    Write-Host "Владелец репозитория теперь видит ваш PR!" -ForegroundColor Green

} catch {
    Write-Host "❌ Ошибка при создании PR:" -ForegroundColor Red
    $errorMessage = $_.ErrorDetails.Message
    if ($errorMessage) {
        $errorObj = $errorMessage | ConvertFrom-Json -ErrorAction SilentlyContinue
        if ($errorObj -and $errorObj.message) {
            Write-Host "  $($errorObj.message)" -ForegroundColor Yellow
            if ($errorObj.errors) {
                foreach ($err in $errorObj.errors) {
                    Write-Host "    - $($err.message)" -ForegroundColor Yellow
                }
            }
        } else {
            Write-Host "  $errorMessage" -ForegroundColor Yellow
        }
    } else {
        Write-Host "  $_" -ForegroundColor Yellow
    }
    
    Write-Host ""
    Write-Host "Возможные причины:" -ForegroundColor Yellow
    Write-Host "  - PR с таким названием уже существует" -ForegroundColor Gray
    Write-Host "  - Недостаточно прав для создания PR" -ForegroundColor Gray
    Write-Host "  - Неверный токен или он не имеет прав 'repo'" -ForegroundColor Gray
    exit 1
}

