# ⚡ Быстрый старт для разработчика

> **Время: 2 минуты**

## 1️⃣ Запустите smoke тесты

```bash
./gradlew test --tests SmokeTest
```

✅ Если все проходят → приложение работает  
❌ Если падают → нужно исправить

---

## 2️⃣ Запустите приложение

```bash
./gradlew bootRun
```

Откройте: http://localhost:8080/login

---

## 3️⃣ Что изменилось (кратко)

### Безопасность
- Все API endpoints требуют JWT токен
- Без токена → 401

### Исправления
- Исправлены ошибки 500 на 6 endpoints
- Добавлена обработка ошибок
- Правильные HTTP статусы (201 для создания)

### Тесты
- **Smoke тесты** - быстрая проверка (10 тестов)
- **Автотесты** - используют реальные JWT токены
- **Ручные тесты** - инструкции для ручного тестирования

---

## 4️⃣ Важно знать

### ❌ НЕ меняйте `application.yml`
Откатили изменение: `spring.sql.init.mode: always` (data.sql выполняется)

### ✅ Измененные файлы
- `SecurityConfig.java` - безопасность
- `ProjectRestController.java` - проверки на null
- `BlockRestController.java` - исправлен projectId
- `TypeRestController.java` - исправлен projectId
- `ServiceRestController.java` - обработка ошибок
- `ConstructorApplication.java` - убрана лишняя аннотация

---

## 5️⃣ Запуск тестов

```bash
# Smoke (быстро, ~10 сек)
./gradlew test --tests SmokeTest

# Автотесты
./gradlew testAuto

# Ручные тесты
./gradlew testManual

# Все
./gradlew testAll
```

---

## 📚 Документация

- **`README_FOR_DEVELOPER.md`** - подробная сводка (3 мин)
- **`CHANGES.md`** - список изменений
- **`SMOKE_TESTS.md`** - про smoke тесты

---

**Готово!** 🚀

Если нужно больше деталей → читайте `README_FOR_DEVELOPER.md`

