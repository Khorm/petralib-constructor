# Изменения в проекте

> **Для быстрого понимания:** См. `QUICK_START.md` (2 мин) или `README_FOR_DEVELOPER.md` (3 мин)

## Что исправлено

### Безопасность
- `GET /api/v1/project` теперь требует авторизацию
- Все `/api/v1/**` endpoints требуют JWT токен

### Ошибки 500
- `/api/v1/project/current-user` - добавлены проверки на null
- `/api/v1/block/workflow` - исправлен projectId (query parameter)
- `/api/v1/type` - исправлен projectId
- `/api/v1/service` - добавлена обработка ошибок

### API
- Методы создания возвращают `201 Created` с данными сущности
- Улучшена обработка ошибок во всех контроллерах

## Измененные файлы

```
src/main/java/com/petralib/auth/security/config/SecurityConfig.java
src/main/java/com/petralib/project/ProjectRestController.java
src/main/java/com/petralib/block/BlockRestController.java
src/main/java/com/petralib/ctype/TypeRestController.java
src/main/java/com/petralib/service/ServiceRestController.java
src/main/java/com/petralib/ConstructorApplication.java
src/main/resources/application.yml (откатили изменение)
```

## Тестирование

✅ Фронтенд: 20/20 тестов успешно  
✅ Smoke тесты: 10 тестов созданы  
✅ Автотесты: улучшены (реальные JWT токены)  
✅ Ручные тесты: 24 теста созданы  
✅ Приложение работает: http://localhost:8080/login

## Быстрый старт

```bash
# 1. Проверьте работоспособность
./gradlew test --tests SmokeTest

# 2. Запустите приложение
./gradlew bootRun
```

## Документация

- **`QUICK_START.md`** - быстрый старт (2 мин) ⚡
- **`README_FOR_DEVELOPER.md`** - подробная сводка (3 мин) 📋
- **`SMOKE_TESTS.md`** - документация по smoke тестам
- **`MANUAL_TESTS_GUIDE.md`** - руководство по ручным тестам

## PR

https://github.com/Khorm/petralib-constructor/pull/5

