# 📖 API Документация - Petralib Constructor

## 🔐 Аутентификация

Все API endpoints (кроме `/api/v1/auth/login`) требуют JWT токен в заголовке:
```
Authorization: Bearer <jwt_token>
```

Получить токен можно через `/api/v1/auth/login`.

---

## 📋 Полный список API Endpoints

### Authentication API

#### `POST /api/v1/auth/login`
Вход пользователя в систему.

**Request:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response 200:**
```json
{
  "email": "user@example.com",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response 403:**
Invalid credentials

---

#### `POST /api/v1/auth/logout`
Выход пользователя из системы.

**Headers:**
- `Authorization: Bearer <token>` (required)

**Response 200:**
Success

---

### Project Management API

#### `GET /api/v1/project`
Получить все проекты текущего пользователя.

**Headers:**
- `Authorization: Bearer <token>` (required)

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "My Project",
    "description": "Project description"
  }
]
```

---

#### `GET /api/v1/project/current-user`
Получить информацию о текущем аутентифицированном пользователе.

**Headers:**
- `Authorization: Bearer <token>` (required)

**Response 200:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "user"
}
```

---

#### `POST /api/v1/project`
Создать новый проект.

**Headers:**
- `Authorization: Bearer <token>` (required)

**Request:**
```json
{
  "id": null,  // для нового проекта
  "name": "New Project",  // required, max 100 chars
  "description": "Project description"  // optional
}
```

**Response 200:**
```json
{
  "id": 1,
  "name": "New Project",
  "description": "Project description"
}
```

**Response 400:**
Validation errors array

---

#### `DELETE /api/v1/project/{projectId}`
Удалить проект по ID.

**Headers:**
- `Authorization: Bearer <token>` (required)

**Path Parameters:**
- `projectId` (required) - ID проекта

**Response 200:**
```json
1
```

---

### Block Management API

#### `GET /api/v1/block/workflow/page`
Получить страницу workflow блоков с пагинацией.

**Query Parameters:**
- `projectId` (required) - ID проекта
- `pageNumber` (required) - номер страницы
- `name` (required) - фильтр по имени
- `pageElementsCount` (required) - количество элементов на странице

**Response 200:**
```json
{
  "blocks": [...],
  "totalPages": 5,
  "currentPage": 1,
  "totalElements": 50
}
```

---

#### `GET /api/v1/block/action/page`
Аналогично workflow/page, но для action блоков.

---

#### `GET /api/v1/block/source/page`
Аналогично workflow/page, но для source блоков.

---

#### `POST /api/v1/block/workflow`
Создать или обновить workflow блок.

**Query Parameters:**
- `projectId` (required)

**Request:**
```json
{
  "id": null,  // для нового блока
  "name": "Workflow Block",  // required, max 100 chars
  "description": "Description",
  "type": "WORKFLOW",
  "variables": [
    {
      "name": "var1",
      "varPinType": "IN",
      "multiplicity": "SINGLE",
      "varTypeId": 1
    }
  ]
}
```

**Response 200:**
```json
"ok"
```

**Response 400:**
Validation errors

---

#### `POST /api/v1/block/action`
Аналогично workflow, но `type: "ACTION"`

---

#### `POST /api/v1/block/source`
Аналогично workflow, но `type: "SOURCE"`

---

#### `GET /api/v1/block/source/acceptedSources`
Получить список принятых источников для проекта.

**Query Parameters:**
- `projectId` (required)

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "Source Block",
    "description": "Description",
    "type": "SOURCE",
    "variables": [...]
  }
]
```

---

#### `GET /api/v1/block/source/{sourceId}`
Получить источник по ID с полной информацией о переменных.

**Path Parameters:**
- `sourceId` (required)

**Response 200:**
```json
{
  "id": 1,
  "name": "Source Block",
  "description": "Description",
  "type": "SOURCE",
  "variables": [...]
}
```

---

#### `DELETE /api/v1/block/{blockType}/{blockId}`
Удалить блок.

**Path Parameters:**
- `blockType` (required) - "workflow" | "action" | "source"
- `blockId` (required)

**Response 200:**
Success

---

### Type Management API

#### `GET /api/v1/type/page`
Получить страницу типов с пагинацией.

**Query Parameters:**
- `projectId` (required)
- `pageNumber` (required)
- `name` (required)
- `pageElementsCount` (required)

**Response 200:**
```json
{
  "types": [...],
  "totalPages": 3,
  "currentPage": 1
}
```

---

#### `GET /api/v1/type`
Получить все типы для проекта.

**Query Parameters:**
- `projectId` (required)

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "CustomType",
    "description": "Description",
    "variables": [...]
  }
]
```

---

#### `POST /api/v1/type`
Создать или обновить тип.

**Query Parameters:**
- `projectId` (required)

**Request:**
```json
{
  "id": null,
  "name": "CustomType",  // required, max 100 chars
  "description": "Description",
  "variables": [
    {
      "fieldName": "field1",
      "fieldCtypeId": 2,
      "multiplicity": "SINGLE"
    }
  ]
}
```

**Response 200:**
```json
{
  "id": 1,
  "name": "CustomType",
  ...
}
```

**Response 400:**
Validation errors

**Response 406:**
Not acceptable (конфликт типов)

---

#### `DELETE /api/v1/type/{typeId}`
Удалить тип.

**Path Parameters:**
- `typeId` (required)

**Response 200:**
Success

---

#### `GET /api/v1/type/fields/{typeId}`
Получить поля типа.

**Path Parameters:**
- `typeId` (required)

**Response 200:**
```json
[
  {
    "fieldId": 1,
    "fieldName": "field1",
    "fieldCtypeId": 2,
    "multiplicity": "SINGLE",
    "description": "Description"
  }
]
```

---

### Scenario Management API

#### `GET /api/v1/scenario`
Получить сценарий для workflow.

**Query Parameters:**
- `workflowId` (required)

**Response 200:**
```json
{
  "beginEndDtoList": [
    {
      "beginEndId": 1,
      "beginEndType": "BEGIN",
      "scenarioBlockId": 1
    }
  ],
  "scenarioBlocks": [
    {
      "scenarioBlockId": 1,
      "blockId": 1,
      "x": 100,
      "y": 200,
      "nextScenarioBlock": 2
    }
  ]
}
```

---

#### `POST /api/v1/scenario`
Сохранить сценарий.

**Query Parameters:**
- `workflowId` (required)

**Request:**
```json
{
  "beginEndDtoList": [...],
  "scenarioBlocks": [...]
}
```

**Response 200:**
```json
"ok"
```

---

#### `GET /api/v1/scenario/{scenarioBlockId}/variables`
Получить переменные сценария.

**Path Parameters:**
- `scenarioBlockId` (required)

**Response 200:**
```json
{
  "scenarioVariables": [
    {
      "scenarioVariableId": 1,
      "variableName": "var1",
      "variableType": "INPUT"
    }
  ]
}
```

---

#### `POST /api/v1/scenario/{scenarioBlockId}/variables`
Сохранить переменные сценария.

**Path Parameters:**
- `scenarioBlockId` (required)

**Request:**
```json
[
  {
    "scenarioVariableId": null,
    "variableName": "var1",
    "variableType": "INPUT"
  }
]
```

**Response 200:**
```json
"ok"
```

---

#### `GET /api/v1/scenario/{workflowId}/variables/exit`
Получить exit переменные workflow.

**Path Parameters:**
- `workflowId` (required)

**Response 200:**
```json
{
  "scenarioVariables": [...]
}
```

---

#### `PUT /api/v1/scenario/{workflowId}/variables/exit`
Сохранить exit переменные workflow.

**Path Parameters:**
- `workflowId` (required)

**Request:**
```json
[
  {
    "variableName": "exitVar",
    "variableType": "OUTPUT"
  }
]
```

**Response 200:**
```json
"ok"
```

---

### Service Management API

#### `GET /api/v1/service/page`
Получить страницу сервисов с пагинацией.

**Query Parameters:**
- `projectId` (required)
- `pageNumber` (required)
- `name` (required)
- `pageElementsCount` (required)

**Response 200:**
```json
{
  "services": [...],
  "totalPages": 2,
  "currentPage": 1
}
```

---

#### `GET /api/v1/service`
Получить все сервисы для проекта.

**Query Parameters:**
- `projectId` (required)

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "My Service",
    "path": "/api/service",
    "description": "Description",
    "projectId": 1
  }
]
```

---

#### `POST /api/v1/service`
Создать или обновить сервис.

**Request:**
```json
{
  "id": null,
  "name": "My Service",  // required, max 100 chars
  "path": "/api/service",  // required, max 100 chars
  "projectId": 1,  // required
  "description": "Description"  // optional
}
```

**Response 200:**
```json
"ok"
```

**Response 400:**
Validation errors

---

#### `GET /api/v1/service/{serviceId}`
Получить сервис по ID.

**Path Parameters:**
- `serviceId` (required)

**Response 200:**
```json
{
  "id": 1,
  "name": "My Service",
  "path": "/api/service",
  "description": "Description",
  "projectId": 1
}
```

---

#### `DELETE /api/v1/service/{serviceId}`
Удалить сервис.

**Path Parameters:**
- `serviceId` (required)

**Response 200:**
```json
1
```

---

#### `GET /api/v1/service/file/{serviceId}`
Скачать конфигурационный файл сервиса в формате JSON.

**Path Parameters:**
- `serviceId` (required)

**Response 200:**
- Content-Type: `application/json`
- Content-Disposition: `attachment; filename=data_export.json`
- Body: JSON конфигурация сервиса

---

## 🔒 Коды ответов

- **200 OK** - успешный запрос
- **400 Bad Request** - ошибки валидации
- **403 Forbidden** - неверные учетные данные
- **404 Not Found** - ресурс не найден
- **406 Not Acceptable** - конфликт данных (для типов)

---

## 📝 Примечания

1. Все даты и временные метки в формате ISO 8601
2. Максимальная длина строковых полей указана в описании
3. Все ID - числовые (Long)
4. Пагинация начинается с 1
5. JWT токены имеют срок действия (по умолчанию 1 час для тестов)

---

**Версия API:** 1.0.0  
**Базовый URL:** `http://localhost:8080/api/v1`  
**Последнее обновление:** 2025-01-27

