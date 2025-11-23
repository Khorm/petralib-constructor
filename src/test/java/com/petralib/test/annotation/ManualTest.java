package com.petralib.test.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркер для ручных тестов.
 * Ручные тесты требуют визуальной проверки или сложной настройки окружения.
 * 
 * Примеры ручных тестов:
 * - UI тесты с реальным браузером
 * - Тесты с реальными внешними сервисами
 * - Тесты, требующие визуальной проверки результатов
 * - Тесты производительности
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("manual")
public @interface ManualTest {
}

