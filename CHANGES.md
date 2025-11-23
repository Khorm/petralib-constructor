# Изменения в проекте

## Что исправлено

### Безопасность
- `GET /api/v1/project` теперь требует авторизацию

### Ошибки 500
- `/api/v1/project/current-user` - добавлены проверки на null
- `/api/v1/block/workflow` - исправлен projectId (query parameter)
- `/api/v1/type` - исправлен projectId
- `/api/v1/service` - добавлена обработка ошибок

### API
- Методы создания возвращают `201 Created` с данными сущности

## Измененные файлы

```
src/main/java/com/petralib/auth/security/config/SecurityConfig.java
src/main/java/com/petralib/project/ProjectRestController.java
src/main/java/com/petralib/block/BlockRestController.java
src/main/java/com/petralib/ctype/TypeRestController.java
src/main/java/com/petralib/service/ServiceRestController.java
src/main/java/com/petralib/ConstructorApplication.java
src/main/resources/application.yml
```

## Тестирование

✅ Фронтенд: 20/20 тестов успешно
✅ Приложение работает: http://localhost:8080/login

## PR

https://github.com/Khorm/petralib-constructor/pull/5

