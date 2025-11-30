# 📚 API Документация

> **Полное описание REST API проекта petralib-constructor**

**Base URL:** `http://localhost:8080/api/v1`

**Авторизация:** Все endpoints (кроме `/auth/login`) требуют JWT токен в заголовке:
```
Authorization: Bearer <token>
```

---

## 🔐 Аутентификация

### POST `/api/v1/auth/login`
Вход пользователя и получение JWT токена.

**Авторизация:** Не требуется

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response 200 OK:**
```json
{
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response 403 Forbidden:**
```
Invalid email/password combination
```

**Пример:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"r0meo1.ru@gmail.com","password":"8K3uLnPVGTtcm5a"}'
```

---

### POST `/api/v1/auth/logout`
Выход пользователя из системы.

**Авторизация:** Требуется

**Response 200 OK:**
```
(пустое тело)
```

**Пример:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer <token>"
```

---

## 📁 Проекты

### GET `/api/v1/project`
Получить все проекты текущего пользователя.

**Авторизация:** Требуется

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "My Project",
    "description": "Project description"
  }
]
```

**Response 401 Unauthorized:**
```
Authentication required
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/project \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/project/current-user`
Получить информацию о текущем пользователе.

**Авторизация:** Требуется

**Response 200 OK:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "username"
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/project/current-user \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/project`
Создать новый проект.

**Авторизация:** Требуется

**Request Body:**
```json
{
  "name": "New Project",
  "description": "Project description"
}
```

**Валидация:**
- `name` - обязательное, максимум 100 символов
- `description` - опциональное

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "New Project",
  "description": "Project description"
}
```

**Response 400 Bad Request:**
```json
[
  "Block name is empty",
  "Block name is too long"
]
```

**Пример:**
```bash
curl -X POST http://localhost:8080/api/v1/project \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"New Project","description":"Description"}'
```

---

### DELETE `/api/v1/project/{projectId}`
Удалить проект.

**Авторизация:** Требуется

**Path Parameters:**
- `projectId` (Long) - ID проекта

**Response 200 OK:**
```
1
```

**Пример:**
```bash
curl -X DELETE http://localhost:8080/api/v1/project/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🧩 Блоки

### GET `/api/v1/block/workflow/page`
Получить страницу workflow блоков с пагинацией.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта
- `pageNumber` (Integer, required) - номер страницы (начиная с 0)
- `name` (String, required) - фильтр по названию (может быть пустым)
- `pageElementsCount` (Integer, required) - количество элементов на странице

**Response 200 OK:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Workflow Block",
      "type": "WORKFLOW"
    }
  ],
  "totalElements": 10,
  "totalPages": 2,
  "currentPage": 0
}
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/block/workflow/page?projectId=1&pageNumber=0&name=&pageElementsCount=10" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/block/action/page`
Получить страницу action блоков с пагинацией.

**Авторизация:** Требуется

**Query Parameters:** (аналогично workflow/page)

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/block/action/page?projectId=1&pageNumber=0&name=&pageElementsCount=10" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/block/source/page`
Получить страницу source блоков с пагинацией.

**Авторизация:** Требуется

**Query Parameters:** (аналогично workflow/page)

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/block/source/page?projectId=1&pageNumber=0&name=&pageElementsCount=10" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/block/source/acceptedSources`
Получить список доступных source блоков для проекта.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Source Block",
    "type": "SOURCE"
  }
]
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/block/source/acceptedSources?projectId=1" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/block/source/{sourceId}`
Получить source блок по ID с переменными.

**Авторизация:** Требуется

**Path Parameters:**
- `sourceId` (Long) - ID source блока

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Source Block",
  "type": "SOURCE",
  "variables": [...]
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/block/source/1 \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/block/workflow`
Создать или обновить workflow блок.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Request Body:**
```json
{
  "name": "Workflow Block",
  "variables": [...]
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "name": "Workflow Block",
  "type": "WORKFLOW"
}
```

**Response 400 Bad Request:**
```json
[
  "Block name is empty"
]
```

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/v1/block/workflow?projectId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Workflow Block"}'
```

---

### POST `/api/v1/block/action`
Создать или обновить action блок.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Request Body:** (аналогично workflow)

**Response 201 Created:** (аналогично workflow)

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/v1/block/action?projectId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Action Block"}'
```

---

### POST `/api/v1/block/source`
Создать или обновить source блок.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Request Body:** (аналогично workflow)

**Response 201 Created:** (аналогично workflow)

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/v1/block/source?projectId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Source Block"}'
```

---

### DELETE `/api/v1/block/{blockType}/{blockId}`
Удалить блок.

**Авторизация:** Требуется

**Path Parameters:**
- `blockType` (String) - тип блока (workflow, action, source)
- `blockId` (Long) - ID блока

**Response 200 OK:**
```
(пустое тело)
```

**Пример:**
```bash
curl -X DELETE http://localhost:8080/api/v1/block/workflow/1 \
  -H "Authorization: Bearer <token>"
```

---

## 📝 Типы данных

### GET `/api/v1/type/page`
Получить страницу типов данных с пагинацией.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта
- `pageNumber` (Integer, required) - номер страницы
- `name` (String, required) - фильтр по названию
- `pageElementsCount` (Integer, required) - количество элементов

**Response 200 OK:**
```json
{
  "content": [...],
  "totalElements": 10,
  "totalPages": 2
}
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/type/page?projectId=1&pageNumber=0&name=&pageElementsCount=10" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/type`
Получить все типы данных для проекта.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "CustomType",
    "fields": [...]
  }
]
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/type?projectId=1" \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/type`
Создать или обновить тип данных.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Request Body:**
```json
{
  "name": "CustomType",
  "fields": [...],
  "parentTypeId": null
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "name": "CustomType",
  "fields": [...]
}
```

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/v1/type?projectId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"CustomType","fields":[]}'
```

---

### DELETE `/api/v1/type/{typeId}`
Удалить тип данных.

**Авторизация:** Требуется

**Path Parameters:**
- `typeId` (Long) - ID типа

**Response 200 OK:**
```
(пустое тело)
```

**Пример:**
```bash
curl -X DELETE http://localhost:8080/api/v1/type/1 \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/type/fields/{typeId}`
Получить поля типа данных.

**Авторизация:** Требуется

**Path Parameters:**
- `typeId` (Long) - ID типа

**Response 200 OK:**
```json
[
  {
    "name": "fieldName",
    "type": "String"
  }
]
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/type/fields/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🔧 Сервисы

### GET `/api/v1/service/page`
Получить страницу сервисов с пагинацией.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта
- `pageNumber` (Integer, required) - номер страницы
- `name` (String, required) - фильтр по названию
- `pageElementsCount` (Integer, required) - количество элементов

**Response 200 OK:**
```json
{
  "content": [...],
  "totalElements": 10,
  "totalPages": 2
}
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/service/page?projectId=1&pageNumber=0&name=&pageElementsCount=10" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/service`
Получить все сервисы для проекта.

**Авторизация:** Требуется

**Query Parameters:**
- `projectId` (Long, required) - ID проекта

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Service Name",
    "configuration": {...}
  }
]
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/service?projectId=1" \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/service/{serviceId}`
Получить сервис по ID.

**Авторизация:** Требуется

**Path Parameters:**
- `serviceId` (Long) - ID сервиса

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Service Name",
  "configuration": {...}
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/service/1 \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/service`
Создать или обновить сервис.

**Авторизация:** Требуется

**Request Body:**
```json
{
  "name": "Service Name",
  "configuration": {...}
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "name": "Service Name",
  "configuration": {...}
}
```

**Пример:**
```bash
curl -X POST http://localhost:8080/api/v1/service \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Service Name","configuration":{}}'
```

---

### DELETE `/api/v1/service/{serviceId}`
Удалить сервис.

**Авторизация:** Требуется

**Path Parameters:**
- `serviceId` (Long) - ID сервиса

**Response 200 OK:**
```
(пустое тело)
```

**Пример:**
```bash
curl -X DELETE http://localhost:8080/api/v1/service/1 \
  -H "Authorization: Bearer <token>"
```

---

### GET `/api/v1/service/file/{serviceId}`
Скачать файл конфигурации сервиса в формате JSON.

**Авторизация:** Требуется

**Path Parameters:**
- `serviceId` (Long) - ID сервиса

**Response 200 OK:**
```
Content-Type: application/json
Content-Disposition: attachment; filename="service-1.json"

{
  "name": "Service Name",
  "configuration": {...}
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/service/file/1 \
  -H "Authorization: Bearer <token>" \
  -o service.json
```

---

## 🎬 Сценарии

### GET `/api/v1/scenario`
Получить сценарий для workflow.

**Авторизация:** Требуется

**Query Parameters:**
- `workflowId` (Long, required) - ID workflow блока

**Response 200 OK:**
```json
{
  "blocks": [...],
  "connections": [...]
}
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/v1/scenario?workflowId=1" \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/scenario`
Сохранить сценарий для workflow.

**Авторизация:** Требуется

**Query Parameters:**
- `workflowId` (Long, required) - ID workflow блока

**Request Body:**
```json
{
  "blocks": [...],
  "connections": [...]
}
```

**Response 200 OK:**
```
ok
```

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/v1/scenario?workflowId=1" \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"blocks":[],"connections":[]}'
```

---

### GET `/api/v1/scenario/{scenarioBlockId}/variables`
Получить переменные сценария для блока.

**Авторизация:** Требуется

**Path Parameters:**
- `scenarioBlockId` (Long) - ID блока сценария

**Response 200 OK:**
```json
{
  "variables": [...]
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/scenario/1/variables \
  -H "Authorization: Bearer <token>"
```

---

### POST `/api/v1/scenario/{scenarioBlockId}/variables`
Сохранить переменные сценария для блока.

**Авторизация:** Требуется

**Path Parameters:**
- `scenarioBlockId` (Long) - ID блока сценария

**Request Body:**
```json
[
  {
    "name": "variableName",
    "value": "value"
  }
]
```

**Response 200 OK:**
```
ok
```

**Пример:**
```bash
curl -X POST http://localhost:8080/api/v1/scenario/1/variables \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '[{"name":"var1","value":"value1"}]'
```

---

### GET `/api/v1/scenario/{workflowId}/variables/exit`
Получить выходные переменные workflow.

**Авторизация:** Требуется

**Path Parameters:**
- `workflowId` (Long) - ID workflow блока

**Response 200 OK:**
```json
{
  "variables": [
    {
      "name": "exitVariable",
      "type": "String"
    }
  ]
}
```

**Пример:**
```bash
curl -X GET http://localhost:8080/api/v1/scenario/1/variables/exit \
  -H "Authorization: Bearer <token>"
```

---

### PUT `/api/v1/scenario/{workflowId}/variables/exit`
Сохранить выходные переменные workflow.

**Авторизация:** Требуется

**Path Parameters:**
- `workflowId` (Long) - ID workflow блока

**Request Body:**
```json
[
  {
    "name": "exitVariable",
    "type": "String"
  }
]
```

**Response 200 OK:**
```
ok
```

**Пример:**
```bash
curl -X PUT http://localhost:8080/api/v1/scenario/1/variables/exit \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '[{"name":"exitVar","type":"String"}]'
```

---

## 📊 Коды ответов

| Код | Описание |
|-----|----------|
| 200 | OK - успешный запрос |
| 201 | Created - ресурс создан |
| 400 | Bad Request - ошибка валидации |
| 401 | Unauthorized - требуется авторизация |
| 403 | Forbidden - доступ запрещен |
| 404 | Not Found - ресурс не найден |
| 500 | Internal Server Error - внутренняя ошибка сервера |

---

## 🔒 Безопасность

### Получение токена

1. Выполните POST запрос на `/api/v1/auth/login` с email и password
2. Получите JWT токен из ответа
3. Используйте токен в заголовке `Authorization: Bearer <token>` для всех последующих запросов

### Пример использования токена

```bash
# 1. Получить токен
TOKEN=$(curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}' \
  | jq -r '.token')

# 2. Использовать токен
curl -X GET http://localhost:8080/api/v1/project \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📝 Примечания

- Все даты и время в формате ISO 8601
- Все ID - числовые (Long)
- Все строки в UTF-8
- Максимальная длина названия проекта/блока - 100 символов
- Пагинация начинается с 0

---

## 🔗 Полезные ссылки

- **Быстрый старт:** `QUICK_START.md`
- **Документация для разработчика:** `README_FOR_DEVELOPER.md`
- **Шпаргалка:** `CHEATSHEET.md`

