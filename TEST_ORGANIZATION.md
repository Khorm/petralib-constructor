# Организация тестов

Проект использует разделение тестов на **автотесты** и **ручные тесты** для более эффективного процесса тестирования.

## 📋 Типы тестов

### ✅ Автотесты (`@AutoTest`)

Автотесты запускаются автоматически и не требуют ручной проверки. Они идеально подходят для CI/CD.

**Характеристики:**
- Используют моки и заглушки
- Не требуют визуальной проверки
- Быстро выполняются
- Детерминированные результаты

**Примеры:**
- Unit тесты (с Mockito)
- REST API тесты (с MockMvc)
- Тесты подключения к БД
- Тесты валидации данных

**Текущие автотесты:**
- `BlockServiceTest` - unit тесты сервиса блоков
- `TypeServiceTest` - unit тесты сервиса типов
- `ProjectServiceTest` - unit тесты сервиса проектов
- `BlockRestControllerTest` - REST API тесты блоков
- `TypeRestControllerTest` - REST API тесты типов
- `ProjectRestControllerTest` - REST API тесты проектов
- `AuthControllerTest` - REST API тесты аутентификации
- `ScenarioRestControllerTest` - REST API тесты сценариев
- `ServiceRestControllerTest` - REST API тесты сервисов
- `DatabaseConnectionTest` - тест подключения к БД

### 🔧 Ручные тесты (`@ManualTest`)

Ручные тесты требуют визуальной проверки или сложной настройки окружения.

**Характеристики:**
- Требуют визуальной проверки результатов
- Могут использовать реальные внешние сервисы
- Требуют настройки окружения
- Результаты требуют интерпретации

**Примеры:**
- UI тесты с реальным браузером
- Тесты производительности
- Интеграционные тесты с реальными сервисами
- Тесты, требующие визуальной проверки

**Пример:** `ManualTestExample` - демонстрация структуры ручных тестов

## 🚀 Запуск тестов

### По умолчанию (только автотесты)

```bash
./gradlew test
```

Запускает только тесты с аннотацией `@AutoTest`. Это используется в CI/CD.

### Все автотесты

```bash
./gradlew testAuto
```

Явно запускает только автотесты.

### Только ручные тесты

```bash
./gradlew testManual
```

Запускает только тесты с аннотацией `@ManualTest`.

### Все тесты (авто + ручные)

```bash
./gradlew testAll
```

Запускает все тесты, включая ручные.

## 📝 Создание новых тестов

### Создание автотеста

```java
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.Test;

@AutoTest
class MyServiceTest {
    
    @Test
    void testSomething() {
        // Тест, который не требует ручной проверки
    }
}
```

### Создание ручного теста

```java
import com.petralib.test.annotation.ManualTest;
import org.junit.jupiter.api.Test;

@ManualTest
class MyManualTest {
    
    @Test
    void testUIRendering() {
        // Тест, который требует визуальной проверки
        System.out.println("⚠️  РУЧНОЙ ТЕСТ: Проверьте результаты вручную");
    }
}
```

## 🏗️ Структура тестов

```
src/test/java/com/petralib/
├── test/
│   ├── annotation/
│   │   ├── AutoTest.java          # Аннотация для автотестов
│   │   └── ManualTest.java        # Аннотация для ручных тестов
│   └── manual/
│       └── ManualTestExample.java # Пример ручного теста
├── auth/
│   └── rest/
│       └── AuthControllerTest.java (@AutoTest)
├── block/
│   ├── BlockRestControllerTest.java (@AutoTest)
│   └── service/
│       └── BlockServiceTest.java (@AutoTest)
├── ctype/
│   ├── TypeRestControllerTest.java (@AutoTest)
│   └── TypeServiceTest.java (@AutoTest)
├── project/
│   ├── ProjectRestControllerTest.java (@AutoTest)
│   └── service/
│       └── ProjectServiceTest.java (@AutoTest)
├── scenario/
│   └── ScenarioRestControllerTest.java (@AutoTest)
├── service/
│   └── ServiceRestControllerTest.java (@AutoTest)
└── DatabaseConnectionTest.java (@AutoTest)
```

## ⚙️ Настройка Gradle

В `build.gradle` настроены следующие задачи:

- `test` - по умолчанию запускает только автотесты
- `testAuto` - явно запускает только автотесты
- `testManual` - запускает только ручные тесты
- `testAll` - запускает все тесты

## 📊 Статистика тестов

Для просмотра статистики выполнения тестов:

```bash
./gradlew test --info
```

Или в IDE:
- IntelliJ IDEA: Run → Run 'All Tests' (только автотесты)
- Для ручных тестов: запустите конкретный тест или используйте `testManual`

## 🔍 Когда использовать ручные тесты?

Используйте `@ManualTest` для тестов, которые:

1. **Требуют визуальной проверки** - UI тесты, проверка отображения
2. **Используют реальные внешние сервисы** - интеграция с API третьих сторон
3. **Требуют сложной настройки** - специальное окружение, данные
4. **Результаты требуют интерпретации** - тесты производительности, нагрузочные тесты
5. **Используют реальный браузер** - Selenium, Playwright тесты

## ✅ Когда использовать автотесты?

Используйте `@AutoTest` для тестов, которые:

1. **Используют моки** - unit тесты с Mockito
2. **Используют MockMvc** - REST API тесты
3. **Быстро выполняются** - не требуют долгого ожидания
4. **Детерминированные** - всегда дают одинаковый результат
5. **Не требуют визуальной проверки** - результаты проверяются автоматически

## 🎯 Best Practices

1. **Большинство тестов должны быть автотестами** - для быстрой обратной связи
2. **Ручные тесты только когда необходимо** - для сложных сценариев
3. **Документируйте ручные тесты** - опишите, что нужно проверить вручную
4. **Используйте CI/CD для автотестов** - автоматический запуск при коммитах
5. **Ручные тесты запускайте перед релизом** - для финальной проверки

## 📚 Дополнительная информация

- [JUnit 5 Tags](https://junit.org/junit5/docs/current/user-guide/#writing-tests-tagging-and-filtering)
- [Gradle Test Filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering)

