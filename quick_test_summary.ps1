# Быстрая сводка по тестам проекта

Write-Host "=== Сводка по тестам проекта ===" -ForegroundColor Cyan
Write-Host ""

# Подсчет тестовых классов
$testFiles = Get-ChildItem -Path "src\test\java" -Recurse -Filter "*Test.java" -ErrorAction SilentlyContinue
$testCount = ($testFiles | Measure-Object).Count

Write-Host "Всего тестовых классов: $testCount" -ForegroundColor Green
Write-Host ""

# Группировка по категориям
Write-Host "По категориям:" -ForegroundColor Yellow

$categories = @{
    "REST API тесты" = @("Controller", "RestController")
    "Unit тесты" = @("Service", "Repository")
    "Интеграционные тесты" = @("Database", "Integration")
}

foreach ($category in $categories.Keys) {
    $patterns = $categories[$category]
    $categoryTests = $testFiles | Where-Object {
        $name = $_.Name
        $patterns | ForEach-Object { if ($name -match $_) { return $true } }
        return $false
    }
    $count = ($categoryTests | Measure-Object).Count
    if ($count -gt 0) {
        Write-Host "  $category`: $count" -ForegroundColor White
        $categoryTests | ForEach-Object { Write-Host "    - $($_.Name)" -ForegroundColor Gray }
    }
}

Write-Host ""

# Список всех тестов
Write-Host "Все тестовые классы:" -ForegroundColor Yellow
$testFiles | ForEach-Object {
    $relativePath = $_.FullName.Replace((Get-Location).Path + "\", "").Replace("\", "/")
    Write-Host "  - $relativePath" -ForegroundColor White
}

Write-Host ""
Write-Host "Для запуска тестов:" -ForegroundColor Cyan
Write-Host "  1. Откройте проект в IntelliJ IDEA" -ForegroundColor White
Write-Host "  2. Правой кнопкой на src/test/java -> Run All Tests" -ForegroundColor White
Write-Host "  Или: ./gradlew test (после исправления Gradle)" -ForegroundColor White

