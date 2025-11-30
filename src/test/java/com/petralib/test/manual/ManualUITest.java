package com.petralib.test.manual;

import com.petralib.test.annotation.ManualTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Ручные тесты для проверки пользовательского интерфейса.
 */
@ManualTest
@DisplayName("Ручные тесты: Пользовательский интерфейс")
class ManualUITest {

    @Test
    @DisplayName("MT015: Проверка отображения страницы входа")
    void testLoginPageDisplay() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT015: Отображение страницы входа");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте: http://localhost:8080/login");
        System.out.println("2. Проверьте визуальное отображение");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Страница загружается полностью");
        System.out.println("   ✓ Все элементы отображаются корректно");
        System.out.println("   ✓ Стили применяются правильно (CSS загружен)");
        System.out.println("   ✓ Форма входа видна и доступна");
        System.out.println("   ✓ Поля ввода имеют правильный размер");
        System.out.println("   ✓ Кнопка 'Войти' видна и активна");
        System.out.println("   ✓ Нет визуальных багов (смещенные элементы, обрезанный текст)");
        System.out.println("   ✓ Страница адаптивна (проверьте на разных размерах экрана)");
        System.out.println("   ✓ В консоли браузера нет ошибок (F12)");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT016: Проверка отображения главной страницы")
    void testMainPageDisplay() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT016: Отображение главной страницы");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Войдите в систему");
        System.out.println("2. Проверьте главную страницу");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Страница загружается полностью");
        System.out.println("   ✓ Навигационное меню отображается");
        System.out.println("   ✓ Все разделы доступны");
        System.out.println("   ✓ Стили применяются правильно");
        System.out.println("   ✓ Нет визуальных багов");
        System.out.println("   ✓ Страница адаптивна");
        System.out.println("   ✓ В консоли браузера нет ошибок");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT017: Проверка отображения страницы проектов")
    void testProjectsPageDisplay() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT017: Отображение страницы проектов");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Войдите в систему");
        System.out.println("2. Перейдите на страницу проектов");
        System.out.println("3. Проверьте отображение");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Список проектов отображается");
        System.out.println("   ✓ Кнопка 'Создать проект' видна");
        System.out.println("   ✓ Каждый проект отображается корректно");
        System.out.println("   ✓ Можно кликнуть на проект");
        System.out.println("   ✓ Стили применяются правильно");
        System.out.println("   ✓ Нет визуальных багов");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT018: Проверка отображения конструктора")
    void testConstructorPageDisplay() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT018: Отображение конструктора");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте проект");
        System.out.println("2. Перейдите в конструктор (если есть)");
        System.out.println("3. Проверьте отображение");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ Конструктор загружается");
        System.out.println("   ✓ Все элементы интерфейса видны");
        System.out.println("   ✓ Можно взаимодействовать с элементами");
        System.out.println("   ✓ Стили применяются правильно");
        System.out.println("   ✓ Нет визуальных багов");
        System.out.println("   ✓ В консоли браузера нет ошибок");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT019: Проверка адаптивности (responsive design)")
    void testResponsiveDesign() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT019: Адаптивность интерфейса");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте приложение в браузере");
        System.out.println("2. Откройте DevTools (F12)");
        System.out.println("3. Включите режим адаптивного дизайна (Ctrl+Shift+M)");
        System.out.println("4. Проверьте на разных размерах:");
        System.out.println("   - Desktop (1920x1080)");
        System.out.println("   - Tablet (768x1024)");
        System.out.println("   - Mobile (375x667)");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ На всех размерах экрана интерфейс читаемый");
        System.out.println("   ✓ Элементы не обрезаются");
        System.out.println("   ✓ Можно взаимодействовать со всеми элементами");
        System.out.println("   ✓ Меню адаптируется (горизонтальное/вертикальное)");
        System.out.println("   ✓ Текст читаемый на всех размерах");
        System.out.println("\n");
    }

    @Test
    @DisplayName("MT020: Проверка загрузки статических ресурсов")
    void testStaticResourcesLoading() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РУЧНОЙ ТЕСТ MT020: Загрузка статических ресурсов");
        System.out.println("=".repeat(60));
        System.out.println("\n📋 ИНСТРУКЦИЯ:");
        System.out.println("1. Откройте приложение");
        System.out.println("2. Откройте DevTools (F12)");
        System.out.println("3. Перейдите на вкладку 'Network'");
        System.out.println("4. Обновите страницу (F5)");
        System.out.println("5. Проверьте загрузку ресурсов");
        System.out.println("\n✅ ПРОВЕРЬТЕ:");
        System.out.println("   ✓ CSS файлы загружаются (status 200)");
        System.out.println("   ✓ JavaScript файлы загружаются (status 200)");
        System.out.println("   ✓ Изображения загружаются (если есть)");
        System.out.println("   ✓ Нет ошибок 404 (файл не найден)");
        System.out.println("   ✓ Нет ошибок 403 (доступ запрещен)");
        System.out.println("   ✓ Время загрузки разумное (< 2 секунд)");
        System.out.println("\n");
    }
}

