# Инструкция для владельца репозитория

## 🎯 Настройка TestSprite тестов

После принятия Pull Request, для запуска тестов на удаленном сервере необходимо настроить учетные данные локально.

## 📋 Шаг 1: Получите учетные данные

Свяжитесь с автором PR для получения учетных данных:
- API Server (порт 8081): email и password
- Web Server (порт 8080): username и password

## 📝 Шаг 2: Создайте файл с учетными данными

Создайте файл `testsprite_tests/tmp/credentials.json`:

```json
{
  "api_server": {
    "url": "http://94.180.117.77:8081",
    "email": "your-api-email@example.com",
    "password": "your-api-password"
  },
  "web_server": {
    "url": "http://94.180.117.77:8080",
    "login_page": "http://94.180.117.77:8080/login",
    "username": "your-username",
    "password": "your-password"
  }
}
```

⚠️ **ВАЖНО:** Этот файл добавлен в `.gitignore` и не будет закоммичен в репозиторий.

## 🚀 Шаг 3: Запустите тесты

```powershell
.\run_tests_all_credentials.ps1
```

Или используйте любой из скриптов:
- `run_tests_all_credentials.ps1` - полная настройка и запуск
- `run_tests_remote.ps1` - только удаленный сервер
- `run_tests_with_credentials.ps1` - с учетными данными API

## 📚 Документация

Полная документация доступна в:
- `ALL_CREDENTIALS_SETUP.md` - полная настройка учетных данных
- `REMOTE_SERVER_SETUP.md` - настройка удаленного сервера
- `README_TESTSPRITE.md` - общая документация TestSprite

## ✅ Проверка

После настройки проверьте:
1. Доступность серверов:
   - API: http://94.180.117.77:8081
   - Web: http://94.180.117.77:8080/login

2. Запустите тесты и проверьте отчеты:
   - `testsprite_tests/tmp/raw_report.md`
   - `testsprite_tests/testsprite-mcp-test-report.md`

## 🔒 Безопасность

- Учетные данные хранятся только локально
- Файл `credentials.json` в `.gitignore`
- Не коммитьте учетные данные в репозиторий

