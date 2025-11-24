# Скрипт для настройки GitHub профиля
# Автоматически создает репозиторий и копирует README

Write-Host "`n🚀 Настройка GitHub профиля`n" -ForegroundColor Cyan

$username = "Khorm"
$profileRepoPath = "$env:USERPROFILE\Desktop\Khorm"
$currentRepoPath = Get-Location

Write-Host "📋 План действий:`n" -ForegroundColor Yellow
Write-Host "  1. Создать репозиторий $username на GitHub" -ForegroundColor White
Write-Host "  2. Клонировать репозиторий локально" -ForegroundColor White
Write-Host "  3. Скопировать README.md" -ForegroundColor White
Write-Host "  4. Закоммитьте и отправить`n" -ForegroundColor White

Write-Host "⚠️  ВАЖНО: Сначала создайте репозиторий на GitHub!`n" -ForegroundColor Red
Write-Host "  1. Перейдите: https://github.com/new" -ForegroundColor Yellow
Write-Host "  2. Repository name: $username" -ForegroundColor Yellow
Write-Host "  3. Сделайте Public" -ForegroundColor Yellow
Write-Host "  4. НЕ добавляйте README, .gitignore, license`n" -ForegroundColor Yellow

$continue = Read-Host "Создали репозиторий? (y/n)"

if ($continue -ne "y" -and $continue -ne "Y") {
    Write-Host "`n❌ Отменено. Создайте репозиторий сначала.`n" -ForegroundColor Red
    exit
}

Write-Host "`n📁 Клонирование репозитория...`n" -ForegroundColor Cyan

# Клонируем репозиторий
$parentDir = Split-Path -Parent $currentRepoPath
Set-Location $parentDir

if (Test-Path $profileRepoPath) {
    Write-Host "⚠️  Папка $profileRepoPath уже существует" -ForegroundColor Yellow
    $overwrite = Read-Host "Удалить и пересоздать? (y/n)"
    if ($overwrite -eq "y" -or $overwrite -eq "Y") {
        Remove-Item -Path $profileRepoPath -Recurse -Force
    } else {
        Write-Host "`n❌ Отменено.`n" -ForegroundColor Red
        exit
    }
}

try {
    git clone "https://github.com/$username/$username.git"
    Write-Host "✅ Репозиторий клонирован`n" -ForegroundColor Green
} catch {
    Write-Host "`n❌ Ошибка при клонировании: $_`n" -ForegroundColor Red
    Write-Host "Убедитесь, что репозиторий создан на GitHub.`n" -ForegroundColor Yellow
    exit
}

Write-Host "📄 Копирование README.md...`n" -ForegroundColor Cyan

# Копируем README
$readmeSource = Join-Path $currentRepoPath "README_FOR_GITHUB_PROFILE.md"
$readmeDest = Join-Path $profileRepoPath "README.md"

if (Test-Path $readmeSource) {
    Copy-Item $readmeSource $readmeDest -Force
    Write-Host "✅ README.md скопирован`n" -ForegroundColor Green
} else {
    Write-Host "❌ Файл README_FOR_GITHUB_PROFILE.md не найден!`n" -ForegroundColor Red
    Set-Location $currentRepoPath
    exit
}

Write-Host "📤 Отправка в GitHub...`n" -ForegroundColor Cyan

# Переходим в репозиторий и коммитим
Set-Location $profileRepoPath

git add README.md
git commit -m "Add profile README"
git push

Write-Host "`n✅ Готово!`n" -ForegroundColor Green
Write-Host "🔗 Проверьте ваш профиль: https://github.com/$username`n" -ForegroundColor Cyan

Set-Location $currentRepoPath

