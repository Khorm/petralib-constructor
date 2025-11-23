package com.petralib.test.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркер для автоматических тестов.
 * Автотесты могут быть запущены автоматически в CI/CD и не требуют ручной проверки.
 * 
 * Примеры автотестов:
 * - Unit тесты (с моками)
 * - Интеграционные тесты с MockMvc
 * - Тесты подключения к БД
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("auto")
public @interface AutoTest {
}

