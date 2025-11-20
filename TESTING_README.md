# Инструкция по тестированию проекта

## Подготовка

### 1. Конфигурация базы данных

Создан файл `src/test/resources/application-test.yml` с настройками для тестовой БД:
- Host: `94.180.117.77:5432`
- Database: `petri_constructor`
- Username: `petra`
- Password: `petra4321`

### 2. Локальная конфигурация

Для локальной разработки создайте файл `src/main/resources/application-local.yml` (шаблон: `application-local.yml.example`)

## Созданные тесты

### Тесты подключения к БД
- `DatabaseConnectionTest` - проверка подключения к базе данных

### Тесты REST контроллеров
- `AuthControllerTest` - тесты аутентификации (login, logout)
- `ProjectRestControllerTest` - тесты управления проектами
- `BlockRestControllerTest` - тесты управления блоками
- `TypeRestControllerTest` - тесты управления типами
- `ServiceRestControllerTest` - тесты управления сервисами
- `ScenarioRestControllerTest` - тесты управления сценариями

### Unit тесты сервисов
- `BlockServiceTest` - unit тесты сервиса блоков
- `TypeServiceTest` - unit тесты сервиса типов
- `ProjectServiceTest` - unit тесты сервиса проектов

## Запуск тестов

### Если Gradle работает:

```bash
# Запустить все тесты
./gradlew test

# Запустить конкретный тест
./gradlew test --tests DatabaseConnectionTest

# Запустить тесты с профилем test
./gradlew test --args='--spring.profiles.active=test'
```

### Если есть проблема с Gradle:

1. Очистите кеш Gradle:
```powershell
Remove-Item -Path "$env:USERPROFILE\.gradle\caches" -Recurse -Force
Remove-Item -Path "build" -Recurse -Force
```

2. Пересоздайте Gradle wrapper:
```powershell
# Удалите старый wrapper
Remove-Item -Path "gradle\wrapper\gradle-wrapper.jar" -Force

# Скачайте новый
$url = "https://github.com/gradle/gradle/raw/v8.5.0/gradle/wrapper/gradle-wrapper.jar"
Invoke-WebRequest -Uri $url -OutFile "gradle\wrapper\gradle-wrapper.jar"
```

3. Попробуйте запустить снова

### Альтернативный способ (через IDE):

1. Откройте проект в IntelliJ IDEA или Eclipse
2. Запустите тесты через интерфейс IDE
3. Убедитесь, что используется профиль `test`

## Проверка подключения к БД

Если тесты не запускаются, можно проверить подключение к БД через pgAdmin:

- **Host:** `94.180.117.77`
- **Port:** `5432`
- **Database:** `petri_constructor`
- **Username:** `petra`
- **Password:** `petra4321`

## Структура тестов

```
src/test/java/com/petralib/
├── DatabaseConnectionTest.java          # Тест подключения к БД
├── auth/rest/
│   └── AuthControllerTest.java          # Тесты аутентификации
├── project/
│   ├── ProjectRestControllerTest.java   # Тесты REST API проектов
│   └── service/
│       └── ProjectServiceTest.java      # Unit тесты сервиса
├── block/
│   ├── BlockRestControllerTest.java     # Тесты REST API блоков
│   └── service/
│       └── BlockServiceTest.java        # Unit тесты сервиса
├── ctype/
│   ├── TypeRestControllerTest.java      # Тесты REST API типов
│   └── TypeServiceTest.java             # Unit тесты сервиса
├── service/
│   └── ServiceRestControllerTest.java   # Тесты REST API сервисов
└── scenario/
    └── ScenarioRestControllerTest.java  # Тесты REST API сценариев
```

## Известные проблемы

1. **Проблема с Gradle кешем**: Если возникает ошибка "Unsupported class file major version 69", очистите кеш Gradle (см. выше)

2. **Проблема с Lombok**: Убедитесь, что Lombok annotation processor включен в настройках IDE

3. **Проблема с подключением к БД**: Проверьте доступность сервера БД и правильность учетных данных

## Следующие шаги

1. Исправить проблему с Gradle кешем
2. Запустить все тесты и проверить результаты
3. Добавить больше unit тестов для покрытия edge cases
4. Настроить CI/CD для автоматического запуска тестов

