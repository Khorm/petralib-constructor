package com.petralib.test.manual;

import com.petralib.test.annotation.ManualTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Ручные тесты для проверки управления проектами.
 */
//@ManualTest
//@DisplayName("Ручные тесты: Управление проектами")
class ManualProjectTest {

//    @Test
//    @DisplayName("MT005: Создание нового проекта")
//    void testCreateProject() {
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("РУЧНОЙ ТЕСТ MT005: Создание нового проекта");
//        System.out.println("=".repeat(60));
//        System.out.println("\n📋 ИНСТРУКЦИЯ:");
//        System.out.println("1. Войдите в систему");
//        System.out.println("2. Найдите кнопку 'Создать проект' или 'New Project'");
//        System.out.println("3. Заполните форму:");
//        System.out.println("   - Название: 'Тестовый проект' (макс. 100 символов)");
//        System.out.println("   - Описание (если есть): 'Описание тестового проекта'");
//        System.out.println("4. Нажмите 'Создать' или 'Save'");
//        System.out.println("\n✅ ПРОВЕРЬТЕ:");
//        System.out.println("   ✓ Проект создается успешно");
//        System.out.println("   ✓ Проект появляется в списке проектов");
//        System.out.println("   ✓ Можно открыть созданный проект");
//        System.out.println("   ✓ Валидация работает (пустое название не принимается)");
//        System.out.println("   ✓ Название длиннее 100 символов не принимается");
//        System.out.println("   ✓ Сообщения об ошибках понятные");
//        System.out.println("\n");
//    }
//
//    @Test
//    @DisplayName("MT006: Просмотр списка проектов")
//    void testViewProjectsList() {
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("РУЧНОЙ ТЕСТ MT006: Просмотр списка проектов");
//        System.out.println("=".repeat(60));
//        System.out.println("\n📋 ИНСТРУКЦИЯ:");
//        System.out.println("1. Войдите в систему");
//        System.out.println("2. Перейдите на страницу со списком проектов");
//        System.out.println("3. Просмотрите список");
//        System.out.println("\n✅ ПРОВЕРЬТЕ:");
//        System.out.println("   ✓ Отображаются все проекты текущего пользователя");
//        System.out.println("   ✓ Не отображаются проекты других пользователей");
//        System.out.println("   ✓ Для каждого проекта отображается название");
//        System.out.println("   ✓ Список отображается корректно (нет визуальных багов)");
//        System.out.println("   ✓ Можно кликнуть на проект для открытия");
//        System.out.println("   ✓ Если проектов много, работает пагинация");
//        System.out.println("\n");
//    }
//
//    @Test
//    @DisplayName("MT007: Редактирование проекта")
//    void testEditProject() {
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("РУЧНОЙ ТЕСТ MT007: Редактирование проекта");
//        System.out.println("=".repeat(60));
//        System.out.println("\n📋 ИНСТРУКЦИЯ:");
//        System.out.println("1. Откройте существующий проект");
//        System.out.println("2. Найдите кнопку 'Редактировать' или 'Edit'");
//        System.out.println("3. Измените название проекта");
//        System.out.println("4. Сохраните изменения");
//        System.out.println("\n✅ ПРОВЕРЬТЕ:");
//        System.out.println("   ✓ Изменения сохраняются");
//        System.out.println("   ✓ Новое название отображается в списке проектов");
//        System.out.println("   ✓ Валидация работает (пустое название не принимается)");
//        System.out.println("   ✓ Можно отменить изменения (если есть кнопка 'Отмена')");
//        System.out.println("\n");
//    }
//
//    @Test
//    @DisplayName("MT008: Удаление проекта")
//    void testDeleteProject() {
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("РУЧНОЙ ТЕСТ MT008: Удаление проекта");
//        System.out.println("=".repeat(60));
//        System.out.println("\n📋 ИНСТРУКЦИЯ:");
//        System.out.println("1. Создайте тестовый проект (см. MT005)");
//        System.out.println("2. Найдите кнопку 'Удалить' или 'Delete'");
//        System.out.println("3. Подтвердите удаление (если есть диалог подтверждения)");
//        System.out.println("\n✅ ПРОВЕРЬТЕ:");
//        System.out.println("   ✓ Проект удаляется");
//        System.out.println("   ✓ Проект исчезает из списка");
//        System.out.println("   ✓ Есть диалог подтверждения (чтобы избежать случайного удаления)");
//        System.out.println("   ✓ После удаления нельзя открыть проект");
//        System.out.println("   ✓ Удаление необратимо (или есть корзина - зависит от реализации)");
//        System.out.println("\n");
//    }
//
//    @Test
//    @DisplayName("MT009: Проверка уникальности названий проектов")
//    void testProjectNameUniqueness() {
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("РУЧНОЙ ТЕСТ MT009: Уникальность названий проектов");
//        System.out.println("=".repeat(60));
//        System.out.println("\n📋 ИНСТРУКЦИЯ:");
//        System.out.println("1. Создайте проект с названием 'Проект 1'");
//        System.out.println("2. Попробуйте создать еще один проект с тем же названием 'Проект 1'");
//        System.out.println("\n✅ ПРОВЕРЬТЕ:");
//        System.out.println("   ✓ Можно создать проект с тем же названием (если уникальность не требуется)");
//        System.out.println("   ИЛИ");
//        System.out.println("   ✓ Отображается ошибка о дублировании названия (если уникальность требуется)");
//        System.out.println("   ✓ Сообщение об ошибке понятное");
//        System.out.println("\n");
//    }
}

