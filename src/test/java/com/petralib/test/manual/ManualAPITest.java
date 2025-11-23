package com.petralib.test.manual;

import com.petralib.test.annotation.ManualTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Ручные тесты для проверки API через браузер или инструменты разработчика.
 */
@ManualTest
@DisplayName("Ручные тесты: REST API")
class ManualAPITest {

    @Test
    @DisplayName("MT021: Проверка безопасности API (требование JWT токена)")
    void testAPISecurity() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT021: Безопасность API");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте браузер");
        System.out.println("2. Откройте DevTools (F12) -> вкладка 'Network'");
        System.out.println("3. БЕЗ авторизации попробуйте выполнить запрос:");
        System.out.println("   GET http://localhost:8080/api/v1/project");
        System.out.println("   (можно через адресную строку или curl)");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Запрос возвращает 401 Unauthorized");
        System.out.println("   ✓ НЕ возвращает данные проектов");
        System.out.println("   ✓ Сообщение об ошибке понятное");
        System.out.println("   ✓ Только /api/v1/auth/login доступен без токена");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT022: Проверка работы API с валидным JWT токеном")
    void testAPIWithValidToken() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT022: API с валидным токеном");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Войдите в систему через UI");
        System.out.println("2. Откройте DevTools (F12) -> вкладка 'Application' -> 'Local Storage'");
        System.out.println("3. Найдите JWT токен (обычно в ключе 'token' или 'jwt')");
        System.out.println("4. Скопируйте токен");
        System.out.println("5. Выполните запрос с токеном:");
        System.out.println("   curl -H \"Authorization: Bearer <TOKEN>\" http://localhost:8080/api/v1/project");
        System.out.println("   ИЛИ через Postman/Insomnia");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Запрос возвращает 200 OK");
        System.out.println("   ✓ Возвращаются данные проектов");
        System.out.println("   ✓ JSON формат корректный");
        System.out.println("   ✓ Возвращаются только проекты текущего пользователя");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT023: Проверка создания проекта через API")
    void testCreateProjectViaAPI() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT023: Создание проекта через API");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Получите JWT токен (см. MT022)");
        System.out.println("2. Выполните POST запрос:");
        System.out.println("   POST http://localhost:8080/api/v1/project");
        System.out.println("   Headers: Authorization: Bearer <TOKEN>");
        System.out.println("   Body (JSON):");
        System.out.println("   {");
        System.out.println("     \"name\": \"API Test Project\"");
        System.out.println("   }");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Запрос возвращает 201 Created");
        System.out.println("   ✓ В ответе возвращается созданный проект");
        System.out.println("   ✓ Проект появляется в списке (проверьте через UI)");
        System.out.println("   ✓ Валидация работает (пустое название -> 400)");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT024: Проверка обработки ошибок API")
    void testAPIErrorHandling() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT024: Обработка ошибок API");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Выполните несколько запросов с ошибками:");
        System.out.println("   - POST /api/v1/project с пустым телом -> должен вернуть 400");
        System.out.println("   - GET /api/v1/project/999999 -> должен вернуть 404 (если проект не существует)");
        System.out.println("   - POST /api/v1/project с невалидными данными -> должен вернуть 400");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Все ошибки возвращают правильные HTTP статусы");
        System.out.println("   ✓ Сообщения об ошибках понятные и информативные");
        System.out.println("   ✓ JSON формат ошибок структурированный");
        System.out.println("   ✓ В логах приложения записываются ошибки");
        System.out.println("\n");
    }
}

