package com.petralib.test.manual;

import com.petralib.test.annotation.ManualTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Ручные тесты для проверки процесса аутентификации.
 * 
 * Эти тесты требуют ручного выполнения и визуальной проверки.
 * 
 * Для запуска:
 * ./gradlew testManual
 */
@ManualTest
@DisplayName("Ручные тесты: Аутентификация")
class ManualLoginTest {

    @Test
    @DisplayName("MT001: Проверка входа в систему через UI")
    void testLoginViaUI() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT001: Вход в систему через UI");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Запустите приложение: ./gradlew bootRun");
        System.out.println("2. Откройте браузер: http://localhost:8080/login");
        System.out.println("3. Введите валидные учетные данные:");
        System.out.println("   Email: r0meo1.ru@gmail.com");
        System.out.println("   Password: 8K3uLnPVGTtcm5a");
        System.out.println("4. Нажмите кнопку 'Войти'");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Форма входа отображается корректно");
        System.out.println("   ✓ Поля ввода работают (можно ввести текст)");
        System.out.println("   ✓ Кнопка 'Войти' активна");
        System.out.println("   ✓ После ввода данных происходит редирект");
        System.out.println("   ✓ После успешного входа отображается главная страница");
        System.out.println("   ✓ В консоли браузера нет ошибок JavaScript");
        System.out.println("\n❌ ЕСЛИ ОШИБКА:");
        System.out.println("   - Проверьте логи приложения");
        System.out.println("   - Проверьте консоль браузера (F12)");
        System.out.println("   - Проверьте Network tab в DevTools");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT002: Проверка входа с неверными учетными данными")
    void testLoginWithInvalidCredentials() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT002: Вход с неверными данными");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте: http://localhost:8080/login");
        System.out.println("2. Введите неверные учетные данные:");
        System.out.println("   Email: wrong@example.com");
        System.out.println("   Password: wrongpassword");
        System.out.println("3. Нажмите кнопку 'Войти'");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Отображается сообщение об ошибке");
        System.out.println("   ✓ Сообщение об ошибке понятное и информативное");
        System.out.println("   ✓ Пользователь остается на странице входа");
        System.out.println("   ✓ Поля ввода не очищаются (или очищаются - зависит от UX)");
        System.out.println("   ✓ Можно попробовать войти снова");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT003: Проверка выхода из системы")
    void testLogout() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT003: Выход из системы");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Войдите в систему (см. MT001)");
        System.out.println("2. Найдите кнопку/ссылку 'Выход' или 'Logout'");
        System.out.println("3. Нажмите на неё");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Происходит выход из системы");
        System.out.println("   ✓ Пользователь перенаправляется на страницу входа");
        System.out.println("   ✓ JWT токен удаляется из localStorage/sessionStorage");
        System.out.println("   ✓ При попытке доступа к защищенным страницам требуется авторизация");
        System.out.println("   ✓ После выхода нельзя выполнять API запросы");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT004: Проверка сохранения сессии (запоминание пользователя)")
    void testSessionPersistence() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT004: Сохранение сессии");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Войдите в систему");
        System.out.println("2. Закройте вкладку браузера (не закрывайте браузер)");
        System.out.println("3. Откройте новую вкладку и перейдите на http://localhost:8080");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Пользователь остается авторизованным (или нет - зависит от реализации)");
        System.out.println("   ✓ JWT токен сохраняется в localStorage/sessionStorage");
        System.out.println("   ✓ При обновлении страницы (F5) сессия сохраняется");
        System.out.println("\n");
    }
}

