# 🚀 Быстрый гайд по тестированию

## Запуск тестов

### По умолчанию (только автотесты)
```bash
./gradlew test
```
✅ Запускает только автотесты - используется в CI/CD

### Все автотесты
```bash
./gradlew testAuto
```
✅ Явно запускает только автотесты

### Только ручные тесты
```bash
./gradlew testManual
```
🔧 Запускает только ручные тесты (требуют визуальной проверки)

### Все тесты
```bash
./gradlew testAll
```
📋 Запускает все тесты (авто + ручные)

## Типы тестов

### ✅ Автотесты (`@AutoTest`)
- Unit тесты (Mockito)
- REST API тесты (MockMvc)
- Тесты подключения к БД
- **Запускаются автоматически в CI/CD**

### 🔧 Ручные тесты (`@ManualTest`)
- UI тесты (требуют визуальной проверки)
- Тесты производительности
- Интеграция с реальными сервисами
- **Требуют ручной проверки результатов**

## Создание нового теста

### Автотест
```java
import com.petralib.test.annotation.AutoTest;

@AutoTest
class MyServiceTest {
    @Test
    void testSomething() { }
}
```

### Ручной тест
```java
import com.petralib.test.annotation.ManualTest;

@ManualTest
class MyManualTest {
    @Test
    void testUIRendering() {
        // Требует визуальной проверки
    }
}
```

## 📚 Подробная документация

См. [TEST_ORGANIZATION.md](TEST_ORGANIZATION.md) для полной документации.

