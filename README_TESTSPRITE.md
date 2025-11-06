# TestSprite - Настройка и запуск тестов

## ✅ Что уже настроено:

1. **code_summary.json** - Полное описание всех API endpoints (Authentication, Project, Block, Type, Scenario, Service)
2. **standard_prd.json** - Стандартизированный PRD для проекта
3. **testsprite_backend_test_plan.json** - План тестирования с 10 тест-кейсами:
   - TC001: Authentication API - User Login
   - TC002: Authentication API - User Logout  
   - TC003: Project Management API - Get All Projects
   - TC004: Project Management API - Create New Project
   - TC005: Project Management API - Delete Project
   - TC006: Block Management API - Get Workflow Blocks Page
   - TC007: Block Management API - Create/Update Action Block
   - TC008: Type Management API - Create/Update Type
   - TC009: Scenario Management API - Save Scenario
   - TC010: Service Management API - Download Service File

4. **Тестовый профиль** - application-test.yml с H2 in-memory базой данных
5. **Зависимости** - H2 database добавлена в build.gradle

## 🚀 Как запустить тесты:

### Вариант 1: Автоматический запуск (рекомендуется)

```powershell
.\start_and_test.ps1
```

Этот скрипт:
- Запустит приложение с тестовым профилем
- Дождется запуска на порту 8080
- Запустит тесты TestSprite
- Покажет результаты

### Вариант 2: Ручной запуск

**Шаг 1:** Запустите приложение в отдельном терминале:
```powershell
.\gradlew.bat bootRun --args='--spring.profiles.active=test'
```

Дождитесь сообщения: `Started ConstructorApplication`

**Шаг 2:** Проверьте, что приложение работает:
```powershell
Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet
```
Должно вернуть `True`

**Шаг 3:** Запустите тесты в другом терминале:
```powershell
.\run_tests.bat
```

Или напрямую:
```powershell
node C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js generateCodeAndExecute
```

## 📊 Где найти результаты:

После выполнения тестов результаты будут в:
- `testsprite_tests/tmp/raw_report.md` - сырой отчет о тестах
- `testsprite_tests/testsprite-mcp-test-report.md` - финальный обработанный отчет

## 🔧 Настройки:

- **Порт приложения:** 8080
- **Тестовая БД:** H2 in-memory (не требует внешней PostgreSQL)
- **Профиль:** test (использует application-test.yml)

## 📝 Файлы проекта:

- `start_and_test.ps1` - PowerShell скрипт для автоматического запуска
- `run_tests.bat` - Batch файл для запуска тестов
- `run_testsprite.ps1` - Альтернативный PowerShell скрипт
- `ЗАПУСК_ТЕСТОВ.md` - Детальная инструкция на русском

## ⚠️ Важно:

1. Убедитесь, что Java 17 установлена и доступна
2. Приложение должно быть запущено перед выполнением тестов
3. Если используется PostgreSQL, установите переменные окружения:
   ```powershell
   $env:dbUsername="your_username"
   $env:dbPassword="your_password"
   ```

## 🐛 Решение проблем:

**Приложение не запускается:**
- Проверьте версию Java: `java -version`
- Убедитесь, что Gradle wrapper доступен
- Проверьте логи в консоли

**Тесты не запускаются:**
- Убедитесь, что приложение работает на порту 8080
- Проверьте путь к TestSprite: `C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js`
- Проверьте, что Node.js установлен: `node --version`

