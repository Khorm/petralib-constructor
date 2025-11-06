# Автоматическое создание Pull Request

## 🚀 Быстрый способ

### Вариант 1: Если у вас уже есть GitHub Personal Access Token

1. **Установите токен в переменную окружения:**
   ```powershell
   $env:GITHUB_TOKEN = "ваш_github_token"
   ```

2. **Запустите скрипт:**
   ```powershell
   .\create_pr.ps1
   ```

### Вариант 2: Передать токен напрямую

```powershell
.\create_pr.ps1 -GitHubToken "ваш_github_token"
```

## 📝 Как получить GitHub Personal Access Token

1. Перейдите на: **https://github.com/settings/tokens**
2. Нажмите **"Generate new token (classic)"**
3. Задайте название токена (например: "PR Creation")
4. Выберите scope: **`repo`** (полный доступ к репозиториям)
5. Нажмите **"Generate token"**
6. Скопируйте токен (он показывается только один раз!)

## ⚙️ Настройка скрипта

Вы можете изменить параметры при запуске:

```powershell
.\create_pr.ps1 `
    -GitHubToken "ваш_токен" `
    -Title "Мой заголовок PR" `
    -BaseBranch "develop" `
    -HeadBranch "feature/add-testsprite-tests"
```

## ✅ После запуска

Скрипт автоматически:
- ✅ Создаст Pull Request
- ✅ Покажет номер PR и ссылку
- ✅ Владелец репозитория увидит ваш PR!

## 🔒 Безопасность

⚠️ **Важно:** Никогда не коммитьте токен в репозиторий!

Используйте переменную окружения или передавайте токен как параметр.

