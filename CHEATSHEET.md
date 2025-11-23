# 📝 Шпаргалка для разработчика

> **Распечатайте и держите под рукой**

## 🚀 Быстрый старт

```bash
# 1. Проверка работоспособности
./gradlew test --tests SmokeTest

# 2. Запуск приложения
./gradlew bootRun

# 3. Открыть в браузере
http://localhost:8080/login
```

## 🧪 Команды тестирования

```bash
# Smoke тесты (быстро, ~10 сек)
./gradlew test --tests SmokeTest

# Все автотесты
./gradlew testAuto

# Ручные тесты (инструкции)
./gradlew testManual

# Все тесты
./gradlew testAll
```

## 🔑 Учетные данные

```
Email:    r0meo1.ru@gmail.com
Password: 8K3uLnPVGTtcm5a
```

## ⚠️ Важно помнить

- ❌ **НЕ меняйте** `application.yml` - откатили изменение
- ✅ Все API endpoints требуют JWT токен (кроме `/api/v1/auth/login`)
- ✅ Создание возвращает `201 Created` (не `200 OK`)
- ✅ Без токена → `401 Unauthorized`

## 📁 Измененные файлы

1. `SecurityConfig.java` - безопасность
2. `ProjectRestController.java` - проверки на null
3. `BlockRestController.java` - исправлен projectId
4. `TypeRestController.java` - исправлен projectId
5. `ServiceRestController.java` - обработка ошибок
6. `ConstructorApplication.java` - убрана аннотация
7. `application.yml` - откатили изменение

## 📚 Документация

- **`QUICK_START.md`** - быстрый старт (2 мин) ⚡
- **`README_FOR_DEVELOPER.md`** - подробная сводка (3 мин) 📋
- **`CHANGES.md`** - список изменений
- **`SMOKE_TESTS.md`** - про smoke тесты

## 🔍 Проверка работоспособности

```bash
# 1. Smoke тесты
./gradlew test --tests SmokeTest

# 2. Запуск приложения
./gradlew bootRun

# 3. Проверка в браузере
# Откройте http://localhost:8080/login
# Войдите с учетными данными выше
```

## ❓ Частые вопросы

**Q: Почему тесты используют реальные JWT токены?**  
A: Чтобы проверять реальную работу API.

**Q: Что делать, если smoke тесты падают?**  
A: Приложение не готово. Нужно исправить проблему.

**Q: Как запустить только автотесты?**  
A: `./gradlew testAuto`

---

**PR:** https://github.com/Khorm/petralib-constructor/pull/5

