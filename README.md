# Petralib Constructor

Spring Boot приложение для построения workflow (конструктор блоков) с фронтендом на React.

## 📋 Содержание

- [Технологии](#технологии)
- [Требования](#требования)
- [Настройка проекта](#настройка-проекта)
- [Конфигурация базы данных](#конфигурация-базы-данных)
- [Запуск приложения](#запуск-приложения)
- [Тестирование](#тестирование)
- [Изменения в проекте](#изменения-в-проекте)
- [Структура проекта](#структура-проекта)

## 🛠 Технологии

- **Backend:** Spring Boot 3.3.11, Java 17
- **База данных:** PostgreSQL
- **Frontend:** React + Redux
- **Build:** Gradle 8.5
- **Тестирование:** JUnit 5, MockMvc, Mockito

## 📦 Требования

- Java 17 или выше
- PostgreSQL 12+
- Gradle 8.5 (или используйте Gradle wrapper)
- Node.js и npm (для фронтенда)

## ⚙️ Настройка проекта

### 1. Клонирование репозитория

```bash
git clone https://github.com/Khorm/petralib-constructor.git
cd petralib-constructor
```

### 2. Настройка базы данных

Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE petri_constructor;
```

### 3. Конфигурация приложения

#### Вариант 1: Использование переменных окружения (рекомендуется)

Создайте файл `.env` или установите переменные окружения:

```bash
# Windows PowerShell
$env:dbUsername = "petra"
$env:dbPassword = "petra4321"

# Linux/Mac
export dbUsername=petra
export dbPassword=petra4321
```

#### Вариант 2: Локальная конфигурация

Скопируйте шаблон и заполните своими данными:

```bash
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
```

Отредактируйте `application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-host:5432/petri_constructor
    username: your-username
    password: your-password
```

**Важно:** Файл `application-local.yml` добавлен в `.gitignore` и не будет закоммичен.

## 🗄 Конфигурация базы данных

### Текущие настройки (production)

- **Host:** `94.180.117.77`
- **Port:** `5432`
- **Database:** `petri_constructor`
- **Username:** `petra`
- **Password:** `petra4321`

### Проверка подключения

Используйте скрипт для проверки подключения:

```powershell
.\test_db_connection.ps1
```

Или через pgAdmin:
- Host: `94.180.117.77`
- Port: `5432`
- Database: `petri_constructor`
- Username: `petra`
- Password: `petra4321`

## 🚀 Запуск приложения

### Через Gradle

```bash
# С профилем local
./gradlew bootRun --args='--spring.profiles.active=local'

# Или с переменными окружения
./gradlew bootRun
```

### Через IDE

1. Откройте проект в IntelliJ IDEA или Eclipse
2. Запустите класс `ConstructorApplication`
3. Убедитесь, что установлены переменные окружения или используется `application-local.yml`

### Сборка JAR

```bash
./gradlew build
java -jar build/libs/petralib-constructor-0.0.1-SNAPSHOT.jar
```

## 🧪 Тестирование

### Обзор тестов

Проект содержит комплексный набор тестов:

- **10 тестовых классов**
- **~45 тест-кейсов**
- **100% покрытие REST API**
- **Unit тесты для основных сервисов**

### Запуск тестов

#### Через IDE (рекомендуется)

1. Откройте проект в IntelliJ IDEA
2. Правой кнопкой на папке `src/test/java`
3. Выберите "Run All Tests"

#### Через Gradle

```bash
# Все тесты
./gradlew test

# Конкретный тест
./gradlew test --tests DatabaseConnectionTest

# С профилем test
./gradlew test --args='--spring.profiles.active=test'
```

### Структура тестов

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

### Тестовая конфигурация

Тесты используют профиль `test` и конфигурацию из `src/test/resources/application-test.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://94.180.117.77:5432/petri_constructor
    username: petra
    password: petra4321
```

### Известные проблемы с тестами

Если возникает ошибка с Gradle кешем:

```powershell
# Очистите кеш
Remove-Item -Path "$env:USERPROFILE\.gradle\caches" -Recurse -Force
Remove-Item -Path "build" -Recurse -Force

# Пересоберите проект
./gradlew clean build
```

**Решение:** Используйте IDE для запуска тестов, если проблема с Gradle сохраняется.

## 📝 Изменения в проекте

### Последние изменения (коммит 17ba7d5)

#### 1. Исправления компиляции

- ✅ Исправлены проблемы с Lombok builder'ами в:
  - `ScenarioVariableFactory`
  - `ValueBuilder` и его подклассах
  - `ValueLoaderModel`
- ✅ Обновлена версия Lombok до 1.18.34
- ✅ Переписан код с использованием setter'ов вместо builder'ов

#### 2. Обновление конфигурации

- ✅ Обновлен проект на Java 17
- ✅ Обновлен Gradle wrapper до 8.5
- ✅ Обновлен URL базы данных на `94.180.117.77:5432`
- ✅ Добавлена поддержка локальной конфигурации

#### 3. Добавлены тесты

**REST API тесты:**
- `AuthControllerTest` - аутентификация (3 теста)
- `ProjectRestControllerTest` - управление проектами (4 теста)
- `BlockRestControllerTest` - управление блоками (9 тестов)
- `TypeRestControllerTest` - управление типами (5 тестов)
- `ServiceRestControllerTest` - управление сервисами (6 тестов)
- `ScenarioRestControllerTest` - управление сценариями (6 тестов)

**Unit тесты:**
- `BlockServiceTest` - сервис блоков (3 теста)
- `TypeServiceTest` - сервис типов (3 теста)
- `ProjectServiceTest` - сервис проектов (3 теста)

**Интеграционные тесты:**
- `DatabaseConnectionTest` - проверка подключения к БД

#### 4. Документация

- ✅ `TESTING_README.md` - подробная инструкция по тестированию
- ✅ `TEST_SUMMARY.md` - итоговый отчет по тестам
- ✅ `README.md` - этот файл

### Измененные файлы

**Основной код:**
- `build.gradle` - обновлены версии и зависимости
- `src/main/java/com/petralib/file/model/ValueLoaderModel.java`
- `src/main/java/com/petralib/file/value/ValueBuilder.java`
- `src/main/java/com/petralib/file/value/InputValueBuilder.java`
- `src/main/java/com/petralib/file/value/ScriptValueBuilder.java`
- `src/main/java/com/petralib/file/value/SourceValueBuilder.java`
- `src/main/java/com/petralib/scenario/entity/ScenarioVariableEntity.java`
- `src/main/java/com/petralib/scenario/service/ScenarioVariableFactory.java`

**Конфигурация:**
- `src/main/resources/application.yml` - обновлен URL БД
- `src/test/resources/application-test.yml` - новая тестовая конфигурация
- `src/main/resources/application-local.yml.example` - шаблон локальной конфигурации
- `.gitignore` - обновлен для исключения файлов с секретами

**Новые файлы:**
- Все тестовые классы в `src/test/java/`
- Документация (`TESTING_README.md`, `TEST_SUMMARY.md`)
- Скрипт проверки БД (`test_db_connection.ps1`)

## 📁 Структура проекта

```
petralib-constructor/
├── src/
│   ├── main/
│   │   ├── java/com/petralib/
│   │   │   ├── auth/              # Аутентификация и авторизация
│   │   │   ├── block/             # Управление блоками
│   │   │   ├── ctype/             # Управление типами
│   │   │   ├── project/           # Управление проектами
│   │   │   ├── scenario/          # Управление сценариями
│   │   │   ├── service/           # Управление сервисами
│   │   │   └── file/              # Работа с файлами
│   │   └── resources/
│   │       ├── application.yml    # Основная конфигурация
│   │       ├── application-local.yml.example  # Шаблон локальной конфигурации
│   │       ├── data.sql           # Начальные данные
│   │       └── front/             # Фронтенд код
│   └── test/
│       ├── java/com/petralib/     # Тесты
│       └── resources/
│           └── application-test.yml  # Тестовая конфигурация
├── build.gradle                   # Конфигурация сборки
├── gradle/                        # Gradle wrapper
├── TESTING_README.md              # Инструкция по тестированию
├── TEST_SUMMARY.md                # Отчет по тестам
└── README.md                      # Этот файл
```

## 🔧 API Endpoints

### Аутентификация
- `POST /api/v1/auth/login` - вход пользователя
- `POST /api/v1/auth/logout` - выход пользователя

### Проекты
- `GET /api/v1/project` - получить все проекты
- `GET /api/v1/project/current-user` - получить текущего пользователя
- `POST /api/v1/project` - создать проект
- `DELETE /api/v1/project/{projectId}` - удалить проект

### Блоки
- `GET /api/v1/block/workflow/page` - страница workflow блоков
- `GET /api/v1/block/action/page` - страница action блоков
- `GET /api/v1/block/source/page` - страница source блоков
- `POST /api/v1/block/workflow` - создать/обновить workflow блок
- `POST /api/v1/block/action` - создать/обновить action блок
- `POST /api/v1/block/source` - создать/обновить source блок
- `DELETE /api/v1/block/{blockType}/{blockId}` - удалить блок

### Типы
- `GET /api/v1/type/page` - страница типов
- `GET /api/v1/type` - получить все типы
- `POST /api/v1/type` - создать/обновить тип
- `DELETE /api/v1/type/{typeId}` - удалить тип
- `GET /api/v1/type/fields/{typeId}` - получить поля типа

### Сервисы
- `GET /api/v1/service/page` - страница сервисов
- `GET /api/v1/service` - получить все сервисы
- `POST /api/v1/service` - создать/обновить сервис
- `GET /api/v1/service/{serviceId}` - получить сервис по ID
- `DELETE /api/v1/service/{serviceId}` - удалить сервис
- `GET /api/v1/service/file/{serviceId}` - скачать файл сервиса

### Сценарии
- `GET /api/v1/scenario` - получить сценарий для workflow
- `POST /api/v1/scenario` - сохранить сценарий
- `GET /api/v1/scenario/{scenarioBlockId}/variables` - получить переменные сценария
- `POST /api/v1/scenario/{scenarioBlockId}/variables` - сохранить переменные сценария
- `GET /api/v1/scenario/{workflowId}/variables/exit` - получить exit переменные
- `PUT /api/v1/scenario/{workflowId}/variables/exit` - сохранить exit переменные

## 🔒 Безопасность

- Файлы с учетными данными (`application-local.yml`) добавлены в `.gitignore`
- API ключи и пароли не должны попадать в репозиторий
- Для продакшена используйте переменные окружения или секреты

## 📚 Дополнительная документация

- [TESTING_README.md](TESTING_README.md) - подробная инструкция по тестированию
- [TEST_SUMMARY.md](TEST_SUMMARY.md) - итоговый отчет по тестам

## 🐛 Известные проблемы

1. **Проблема с Gradle кешем**: Если возникает ошибка "Unsupported class file major version 69", очистите кеш Gradle или используйте IDE для запуска тестов.

2. **Lombok**: Убедитесь, что Lombok annotation processor включен в настройках IDE.

## 🤝 Вклад в проект

1. Создайте ветку для новой функции
2. Внесите изменения
3. Напишите тесты для новой функциональности
4. Убедитесь, что все тесты проходят
5. Создайте Pull Request

## 📞 Контакты

Репозиторий: https://github.com/Khorm/petralib-constructor

---

**Версия:** 0.0.1-SNAPSHOT  
**Java:** 17  
**Spring Boot:** 3.3.11  
**Последнее обновление:** 2024

