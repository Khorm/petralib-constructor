# 🧪 Дополнительные тесты с текущими доступами

## 📊 Обзор

С текущими доступами (API Server + Web Server) можно выполнить **43 дополнительных теста**, которые расширяют покрытие:

### Категории дополнительных тестов:

1. **Негативные тесты (Negative Tests)** - 15 тестов
2. **Валидация (Validation Tests)** - 12 тестов
3. **Безопасность (Security Tests)** - 3 теста
4. **Граничные случаи (Boundary Tests)** - 5 тестов
5. **Интеграционные тесты (Integration Tests)** - 3 теста
6. **Производительность (Performance Tests)** - 1 тест
7. **Целостность данных (Data Integrity Tests)** - 2 теста
8. **Специальные символы (Special Characters Tests)** - 1 тест

---

## 🔴 Негативные тесты (15 тестов)

### Authentication API (5 тестов)

#### TC033: Login с неверными учетными данными
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "wrong@email.com",
  "password": "wrongpassword"
}
```
**Ожидаемый результат:** 403 Forbidden

#### TC034: Login с отсутствующими полями
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "r0meo1.ru@gmail.com"
  // password отсутствует
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC035: Login с пустыми полями
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "",
  "password": ""
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC036: Logout без токена
```http
POST /api/v1/auth/logout
```
**Ожидаемый результат:** 401 Unauthorized

#### TC037: Logout с невалидным токеном
```http
POST /api/v1/auth/logout
Authorization: Bearer invalid_token_here
```
**Ожидаемый результат:** 401 Unauthorized

### Project API (4 теста)

#### TC038: Get projects без аутентификации
```http
GET /api/v1/project
```
**Ожидаемый результат:** 401 Unauthorized

#### TC039: Create project с пустым именем
```http
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "",
  "description": "Test"
}
```
**Ожидаемый результат:** 400 Bad Request с ошибкой валидации

#### TC040: Create project с именем > 100 символов
```http
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A".repeat(101),  // 101 символ
  "description": "Test"
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC041: Create project без аутентификации
```http
POST /api/v1/project
Content-Type: application/json

{
  "name": "Test Project"
}
```
**Ожидаемый результат:** 401 Unauthorized

#### TC042: Delete несуществующего проекта
```http
DELETE /api/v1/project/99999
Authorization: Bearer <token>
```
**Ожидаемый результат:** Ошибка или 404 Not Found

### Block API (3 теста)

#### TC044: Create block с пустым именем
```http
POST /api/v1/block/action?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "",
  "type": "ACTION"
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC045: Create block с именем > 100 символов
```http
POST /api/v1/block/action?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A".repeat(101),
  "type": "ACTION"
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC048: Get source с несуществующим ID
```http
GET /api/v1/block/source/99999
Authorization: Bearer <token>
```
**Ожидаемый результат:** 404 Not Found

### Type API (3 теста)

#### TC051: Create type с пустым именем
```http
POST /api/v1/type?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "",
  "variables": []
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC052: Create type с именем > 100 символов
```http
POST /api/v1/type?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A".repeat(101),
  "variables": []
}
```
**Ожидаемый результат:** 400 Bad Request

#### TC055: Get type fields для несуществующего типа
```http
GET /api/v1/type/fields/99999
Authorization: Bearer <token>
```
**Ожидаемый результат:** 404 Not Found

---

## ✅ Валидация (12 тестов)

### TC043: Пагинация с невалидными параметрами
```http
GET /api/v1/block/workflow/page?projectId=1&pageNumber=-1&name=test&pageElementsCount=0
Authorization: Bearer <token>
```
**Ожидаемый результат:** Ошибка валидации

### TC046: Create block с невалидным типом
```http
POST /api/v1/block/action?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Test Block",
  "type": "INVALID_TYPE"
}
```
**Ожидаемый результат:** 400 Bad Request

### TC047: Create block с дублирующимися именами переменных
```http
POST /api/v1/block/action?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Test Block",
  "type": "ACTION",
  "variables": [
    {"name": "var1", ...},
    {"name": "var1", ...}  // дубликат
  ]
}
```
**Ожидаемый результат:** 400 Bad Request

### TC053: Create type с невалидной multiplicity
```http
POST /api/v1/type?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Test Type",
  "variables": [
    {
      "fieldName": "field1",
      "multiplicity": "INVALID"  // должно быть SINGLE или COLLECTION
    }
  ]
}
```
**Ожидаемый результат:** 400 Bad Request

### TC054: Create type с циклической зависимостью
```http
POST /api/v1/type?projectId=1
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "TypeA",
  "variables": [
    {
      "fieldName": "fieldB",
      "fieldCtypeId": <TypeB_ID>
    }
  ]
}
// Затем создать TypeB, который ссылается на TypeA
```
**Ожидаемый результат:** 406 Not Acceptable

### TC060: Create service с пустыми обязательными полями
```http
POST /api/v1/service
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "",
  "path": "",
  "projectId": null
}
```
**Ожидаемый результат:** 400 Bad Request с ошибками валидации

### TC061: Create service с полями > 100 символов
```http
POST /api/v1/service
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A".repeat(101),
  "path": "/api/service",
  "projectId": 1
}
```
**Ожидаемый результат:** 400 Bad Request

### TC066: Пагинация - граничные случаи
```http
# Тест 1: pageNumber = 0
GET /api/v1/block/workflow/page?projectId=1&pageNumber=0&name=test&pageElementsCount=10

# Тест 2: pageNumber = -1
GET /api/v1/block/workflow/page?projectId=1&pageNumber=-1&name=test&pageElementsCount=10

# Тест 3: pageElementsCount = 0
GET /api/v1/block/workflow/page?projectId=1&pageNumber=1&name=test&pageElementsCount=0

# Тест 4: pageElementsCount = -1
GET /api/v1/block/workflow/page?projectId=1&pageNumber=1&name=test&pageElementsCount=-1
```
**Ожидаемый результат:** Ошибки валидации

### TC072: Граничные значения длины полей
```http
# Тест 1: Ровно 100 символов (должно пройти)
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A".repeat(100),  // ровно 100
  "description": "Test"
}

# Тест 2: Ровно 1 символ (должно пройти)
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "A",  // 1 символ
  "description": "Test"
}

# Тест 3: Пустая строка (должно не пройти)
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "",
  "description": "Test"
}
```

---

## 🔒 Безопасность (3 теста)

### TC064: Неавторизованный доступ ко всем защищенным endpoints
```http
# Тестировать ВСЕ защищенные endpoints без токена:
GET /api/v1/project
GET /api/v1/project/current-user
POST /api/v1/project
DELETE /api/v1/project/1
GET /api/v1/block/workflow/page?...
POST /api/v1/block/action?...
# ... и все остальные
```
**Ожидаемый результат:** 401 Unauthorized для всех

### TC065: Невалидный формат JWT токена
```http
GET /api/v1/project
Authorization: Bearer not_a_valid_jwt_token_at_all
```
**Ожидаемый результат:** 401 Unauthorized

### TC069: SQL Injection попытки
```http
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Test'; DROP TABLE projects; --",
  "description": "'; SELECT * FROM users; --"
}
```
**Ожидаемый результат:** Валидация должна отклонить или экранировать

### TC070: XSS попытки
```http
POST /api/v1/project
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "<script>alert('XSS')</script>",
  "description": "<img src=x onerror=alert('XSS')>"
}
```
**Ожидаемый результат:** Данные должны быть экранированы или отклонены

---

## 🔗 Интеграционные тесты (3 теста)

### TC074: Полный workflow
```javascript
// 1. Создать проект
POST /api/v1/project -> получить projectId

// 2. Создать блоки
POST /api/v1/block/workflow?projectId={projectId} -> получить workflowId
POST /api/v1/block/action?projectId={projectId} -> получить actionId
POST /api/v1/block/source?projectId={projectId} -> получить sourceId

// 3. Создать типы
POST /api/v1/type?projectId={projectId} -> получить typeId

// 4. Создать сценарий
POST /api/v1/scenario?workflowId={workflowId}

// 5. Создать сервис
POST /api/v1/service -> получить serviceId

// 6. Проверить все связи
GET /api/v1/project -> проверить наличие проекта
GET /api/v1/block/workflow/page?projectId={projectId} -> проверить блоки
GET /api/v1/type?projectId={projectId} -> проверить типы
GET /api/v1/scenario?workflowId={workflowId} -> проверить сценарий
GET /api/v1/service?projectId={projectId} -> проверить сервисы
```

### TC075: Операции обновления
```javascript
// 1. Создать сущность
POST /api/v1/project -> projectId = 1, name = "Project 1"

// 2. Обновить (POST с существующим ID)
POST /api/v1/project
{
  "id": 1,
  "name": "Updated Project 1"
}

// 3. Проверить изменения
GET /api/v1/project -> проверить, что name = "Updated Project 1"

// 4. Обновить еще раз
POST /api/v1/project
{
  "id": 1,
  "name": "Final Project 1"
}

// 5. Проверить финальное состояние
GET /api/v1/project -> проверить, что name = "Final Project 1"
```

### TC067: Каскадное удаление
```javascript
// 1. Создать проект с блоками, типами, сервисами
// 2. Удалить проект
DELETE /api/v1/project/{projectId}

// 3. Проверить, что все связанные сущности удалены:
GET /api/v1/block/workflow/page?projectId={projectId} -> должно быть пусто
GET /api/v1/type?projectId={projectId} -> должно быть пусто
GET /api/v1/service?projectId={projectId} -> должно быть пусто
```

---

## 🎯 Целостность данных (2 теста)

### TC068: Проверка orphaned references
```javascript
// 1. Создать проект -> projectId = 1
// 2. Создать блок с projectId = 1 -> blockId = 1
// 3. Создать переменную для блока 1
// 4. Удалить блок
DELETE /api/v1/block/workflow/1

// 5. Проверить, что переменные также удалены
// (нужен доступ к БД или проверка через API)
```

---

## 🚀 Производительность (1 тест)

### TC073: Конкурентные запросы
```javascript
// Отправить 10-20 одновременных запросов:
for (let i = 0; i < 20; i++) {
  Promise.all([
    GET /api/v1/project,
    GET /api/v1/block/workflow/page?projectId=1&pageNumber=1&name=test&pageElementsCount=10,
    GET /api/v1/type?projectId=1
  ])
}

// Проверить:
// - Все запросы завершились успешно
// - Время ответа приемлемое (< 2 секунды)
// - Нет ошибок 500
```

---

## 📋 Итого дополнительных тестов

| Категория | Количество | Тесты |
|-----------|-----------|-------|
| Негативные тесты | 15 | TC033-TC049, TC056, TC062, TC063 |
| Валидация | 12 | TC043, TC046, TC047, TC050-TC053, TC060, TC061, TC066, TC072 |
| Безопасность | 3 | TC064, TC065, TC069, TC070 |
| Интеграционные | 3 | TC074, TC075, TC067 |
| Целостность данных | 2 | TC068 |
| Производительность | 1 | TC073 |
| **ВСЕГО** | **43** | **TC033-TC075** |

---

## 🚀 Как запустить дополнительные тесты

### Вариант 1: Добавить в TestSprite план
Добавить все 43 теста в `testsprite_backend_test_plan.json`

### Вариант 2: Создать отдельный файл
Использовать `additional_test_plan.json` и запустить:
```powershell
# Обновить config.json с testIds для дополнительных тестов
.\run_testsprite.ps1
```

### Вариант 3: Ручное тестирование через HTTP
Использовать Postman, curl, или PowerShell скрипты для каждого теста

---

## 📊 Итоговое покрытие

- **Базовые тесты:** 32 (TC001-TC032)
- **Дополнительные тесты:** 43 (TC033-TC075)
- **ОБЩЕЕ ПОКРЫТИЕ:** **75 тест-кейсов**

---

**Все эти тесты можно выполнить с текущими доступами к API серверу!**

