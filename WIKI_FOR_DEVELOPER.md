# 📚 Wiki для разработчика petralib-constructor

## 🎯 Обзор проекта

**Petralib Constructor** - платформа для управления проектами, workflow, типами данных, сервисами и сценариями с использованием Spring Boot, JWT аутентификации и React UI.

### Технологический стек
- **Backend:** Java 15/17, Spring Boot 3.3.11, Spring Security, JWT
- **Database:** PostgreSQL (production), H2 (testing)
- **ORM:** JPA/Hibernate
- **Mapping:** MapStruct
- **Frontend:** React, Thymeleaf
- **Build:** Gradle 8.5

---

## 🧪 Тестирование

### Доступные тесты

#### TestSprite Tests (32 тест-кейса)
Полное покрытие всех API endpoints:
- **Authentication API:** 2 теста
- **Project Management API:** 4 теста
- **Block Management API:** 9 тестов
- **Type Management API:** 5 тестов
- **Scenario Management API:** 6 тестов
- **Service Management API:** 6 тестов

**Файл:** `testsprite_tests/testsprite_backend_test_plan.json`

#### JUnit Tests (5 тестовых классов)
- `ConstructorApplicationTest` - проверка загрузки Spring контекста
- `AuthControllerTest` - тесты API аутентификации
- `BlockRestControllerTest` - тесты API блоков
- `BlockServiceTest` - unit тесты сервиса блоков
- `TypeRestControllerTest` - тесты API типов

**Расположение:** `src/test/java/com/petralib/`

### Запуск тестов

#### Локальное тестирование (H2 in-memory)

```powershell
# Вариант 1: Автоматический запуск
.\auto_run_all.ps1

# Вариант 2: Пошагово
# Терминал 1: Запуск приложения
.\gradlew.bat bootRun --args='--spring.profiles.active=test'

# Терминал 2: Запуск тестов
.\run_testsprite.ps1
```

#### Удаленное тестирование

**Сервер:** `http://94.180.117.77:8081`

**Учетные данные:**
- API Email: `r0meo1.ru@gmail.com`
- API Password: `8K3uLnPVGTtcm5a`
- Web Username: `dm`
- Web Password: `dm`

**Конфигурация:** `testsprite_tests/tmp/config.json`

---

## 🔐 Учетные данные и конфигурация

### Тестовая конфигурация

**H2 Database (для локального тестирования):**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    username: sa
    password: (пусто)
```

**JWT для тестов:**
- Secret: `test-secret-key-for-testing-purposes-only`
- Expiration: 3600000 (1 час)

### Production конфигурация

**Переменные окружения:**
- `dbUsername` - имя пользователя PostgreSQL
- `dbPassword` - пароль PostgreSQL

**Файл:** `src/main/resources/application.yml`

---

## 📋 API Endpoints

### Authentication API

#### POST /api/v1/auth/login
Вход пользователя
```json
Request:
{
  "email": "user@example.com",
  "password": "password123"
}

Response:
{
  "email": "user@example.com",
  "token": "jwt_token_here"
}
```

#### POST /api/v1/auth/logout
Выход пользователя
- **Auth:** Bearer Token required

---

### Project Management API

#### GET /api/v1/project
Получить все проекты пользователя
- **Auth:** Bearer Token required
- **Response:** `List<ProjectDto>`

#### GET /api/v1/project/current-user
Получить текущего аутентифицированного пользователя
- **Auth:** Bearer Token required
- **Response:** `UserDto`

#### POST /api/v1/project
Создать новый проект
- **Auth:** Bearer Token required
- **Request Body:** `ProjectDto`
  - `name` (required, max 100 chars)
  - `description` (optional)

#### DELETE /api/v1/project/{projectId}
Удалить проект
- **Auth:** Bearer Token required

---

### Block Management API

#### GET /api/v1/block/workflow/page
Получить страницу workflow блоков
- **Query Parameters:**
  - `projectId` (required)
  - `pageNumber` (required)
  - `name` (required) - фильтр по имени
  - `pageElementsCount` (required)

#### GET /api/v1/block/action/page
Получить страницу action блоков
- **Query Parameters:** (аналогично workflow)

#### GET /api/v1/block/source/page
Получить страницу source блоков
- **Query Parameters:** (аналогично workflow)

#### POST /api/v1/block/workflow
Создать/обновить workflow блок
- **Query Parameters:** `projectId` (required)
- **Request Body:** `BlockDto`
  - `name` (required, max 100 chars)
  - `description` (optional)
  - `type`: "WORKFLOW"
  - `variables` (optional)

#### POST /api/v1/block/action
Создать/обновить action блок
- Аналогично workflow, `type`: "ACTION"

#### POST /api/v1/block/source
Создать/обновить source блок
- Аналогично workflow, `type`: "SOURCE"

#### GET /api/v1/block/source/acceptedSources
Получить принятые источники для проекта
- **Query Parameters:** `projectId` (required)

#### GET /api/v1/block/source/{sourceId}
Получить источник по ID
- **Response:** `BlockDto` с переменными

#### DELETE /api/v1/block/{blockType}/{blockId}
Удалить блок
- **Path Parameters:**
  - `blockType`: "workflow" | "action" | "source"
  - `blockId` (required)

---

### Type Management API

#### GET /api/v1/type/page
Получить страницу типов
- **Query Parameters:**
  - `projectId` (required)
  - `pageNumber` (required)
  - `name` (required)
  - `pageElementsCount` (required)

#### GET /api/v1/type
Получить все типы для проекта
- **Query Parameters:** `projectId` (required)

#### POST /api/v1/type
Создать/обновить тип
- **Query Parameters:** `projectId` (required)
- **Request Body:** `TypeFullDto`
  - `name` (required, max 100 chars)
  - `description` (optional)
  - `variables` (optional array)

#### DELETE /api/v1/type/{typeId}
Удалить тип

#### GET /api/v1/type/fields/{typeId}
Получить поля типа
- **Response:** `List<CTypeFieldDto>`

---

### Scenario Management API

#### GET /api/v1/scenario
Получить сценарий для workflow
- **Query Parameters:** `workflowId` (required)
- **Response:** `ScenarioDto`
  - `beginEndDtoList`
  - `scenarioBlocks`

#### POST /api/v1/scenario
Сохранить сценарий
- **Query Parameters:** `workflowId` (required)
- **Request Body:** `ScenarioDto`

#### GET /api/v1/scenario/{scenarioBlockId}/variables
Получить переменные сценария
- **Response:** `ScenarioVariablesDto`

#### POST /api/v1/scenario/{scenarioBlockId}/variables
Сохранить переменные сценария
- **Request Body:** `Collection<ScenarioVariableDto>`

#### GET /api/v1/scenario/{workflowId}/variables/exit
Получить exit переменные workflow
- **Response:** `ScenarioVariablesDto`

#### PUT /api/v1/scenario/{workflowId}/variables/exit
Сохранить exit переменные workflow
- **Request Body:** `Collection<ScenarioVariableDto>`

---

### Service Management API

#### GET /api/v1/service/page
Получить страницу сервисов
- **Query Parameters:**
  - `projectId` (required)
  - `pageNumber` (required)
  - `name` (required)
  - `pageElementsCount` (required)

#### GET /api/v1/service
Получить все сервисы для проекта
- **Query Parameters:** `projectId` (required)

#### POST /api/v1/service
Создать/обновить сервис
- **Request Body:** `ServiceDto`
  - `name` (required, max 100 chars)
  - `projectId` (required)
  - `path` (required, max 100 chars)
  - `description` (optional)

#### GET /api/v1/service/{serviceId}
Получить сервис по ID
- **Response:** `ServiceDto`

#### DELETE /api/v1/service/{serviceId}
Удалить сервис

#### GET /api/v1/service/file/{serviceId}
Скачать конфигурационный файл сервиса
- **Response:** JSON file download
- **Headers:** `Content-Disposition: attachment; filename=data_export.json`

---

## 🗄️ Структура базы данных

### Основные таблицы

1. **constructor_users** - пользователи системы
   - `user_id`, `email`, `user_name`, `user_password`

2. **projects** - проекты
   - `project_id`, `project_name`, `description`

3. **services** - сервисы
   - `service_id`, `project_id`, `service_name`, `service_path`, `description`

4. **ctypes** - пользовательские типы
   - `type_id`, `type_name`, `project_id`, `description`

5. **ctype_fields** - поля типов
   - `field_id`, `owner_ctype_id`, `field_name`, `field_ctype_id`, `multiplicity`

6. **blocks** - блоки (workflow, action, source)
   - `block_id`, `project_id`, `service_id`, `block_name`, `block_type`, `description`

7. **variables** - переменные блоков
   - `variable_id`, `block_id`, `var_type_id`, `variable_name`, `var_pin_type`, `multiplicity`

8. **scenario_blocks** - блоки сценариев
   - `scenario_block_id`, `block_id`, `parent_workflow_id`, `next_scenario_block`, `previous_scenario_block`, `x`, `y`

9. **scenario_variables** - переменные сценариев
   - `scenario_variable_id`, `scenario_block_id`, `variable_name`, `variable_type`

10. **begin_end** - начало/конец workflow
    - `begin_end_id`, `workflow_id`, `begin_end_type`, `scenario_block_id`

11. **project_user_roles** - роли пользователей в проектах
    - `project_user_roles_id`, `project_id`, `user_id`, `role`

**Схема БД:** `src/main/resources/data.sql`

---

## 🚀 Быстрый старт

### Требования
- Java 15/17
- Gradle (включен в проект)
- PostgreSQL (для production) или H2 (для тестов)

### Установка и запуск

```powershell
# 1. Клонировать репозиторий
git clone https://github.com/Khorm/petralib-constructor.git
cd petralib-constructor

# 2. Настроить PostgreSQL (опционально)
$env:dbUsername="your_username"
$env:dbPassword="your_password"

# 3. Запустить приложение
.\gradlew.bat bootRun

# 4. Или с тестовым профилем (H2)
.\gradlew.bat bootRun --args='--spring.profiles.active=test'
```

Приложение будет доступно на `http://localhost:8080`

---

## 📁 Структура проекта

```
petralib-constructor/
├── src/
│   ├── main/
│   │   ├── java/com/petralib/
│   │   │   ├── auth/          # Аутентификация и авторизация
│   │   │   ├── block/         # Управление блоками
│   │   │   ├── ctype/         # Управление типами
│   │   │   ├── project/       # Управление проектами
│   │   │   ├── scenario/      # Управление сценариями
│   │   │   ├── service/       # Управление сервисами
│   │   │   └── utils/         # Утилиты
│   │   └── resources/
│   │       ├── application.yml  # Основная конфигурация
│   │       ├── application-test.yml  # Тестовая конфигурация
│   │       ├── data.sql       # Схема БД
│   │       └── front/         # Frontend (React)
│   └── test/
│       └── java/com/petralib/ # JUnit тесты
├── testsprite_tests/          # TestSprite тесты
│   ├── testsprite_backend_test_plan.json  # План тестирования (32 теста)
│   ├── standard_prd.json      # PRD документация
│   └── tmp/
│       ├── config.json        # Конфигурация
│       └── code_summary.json  # Описание API
├── build.gradle               # Зависимости проекта
└── README files               # Документация
```

---

## 🔧 Разработка

### Добавление нового API endpoint

1. Создать Controller в соответствующем пакете
2. Создать DTO в `dto/` подпапке
3. Создать Entity в `entity/` подпапке
4. Создать Repository в `repo/` подпапке
5. Создать Service в `service/` подпапке
6. Добавить Mapper (MapStruct)
7. Добавить тест в `testsprite_backend_test_plan.json`
8. Добавить JUnit тест в `src/test/java/`

### Тестирование

**JUnit тесты:**
```powershell
.\gradlew.bat test
```

**TestSprite тесты:**
```powershell
# После запуска приложения
.\run_testsprite.ps1
```

---

## 📊 Статистика тестового покрытия

- **TestSprite Tests:** 32 тест-кейса
- **JUnit Tests:** 5 тестовых классов
- **API Endpoints покрыто:** 32/32 (100%)
- **Модули покрыто:**
  - Authentication: ✅
  - Projects: ✅
  - Blocks: ✅
  - Types: ✅
  - Scenarios: ✅
  - Services: ✅

---

## 🐛 Известные проблемы и ограничения

1. **TestSprite требует локальный сервер** для создания туннеля
   - Для удаленного тестирования используйте прямые HTTP запросы
   
2. **H2 vs PostgreSQL**
   - Тесты используют H2 in-memory БД
   - Production использует PostgreSQL
   - Некоторые функции могут отличаться

3. **JWT токены**
   - Тестовый secret: `test-secret-key-for-testing-purposes-only`
   - Production secret должен быть в переменных окружения

---

## 📞 Контакты и поддержка

- **Репозиторий:** https://github.com/Khorm/petralib-constructor
- **Pull Request:** https://github.com/Khorm/petralib-constructor/pull/3

---

## 📝 Changelog

### Добавлено в PR #3
- ✅ 32 TestSprite тест-кейса для всех API endpoints
- ✅ 5 JUnit тестовых классов
- ✅ Конфигурация H2 для тестирования
- ✅ Полная документация API
- ✅ Скрипты автоматизации тестирования
- ✅ Документация по настройке и запуску

---

**Последнее обновление:** $(Get-Date -Format "yyyy-MM-dd")

