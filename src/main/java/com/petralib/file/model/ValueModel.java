package com.petralib.file.model;


import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Data
public class ValueModel {

    /**
     * Уникальный идентификатор значения в рамках модели.
     * Может использоваться для ссылок при маппинге, скриптах или UI-состоянии.
     */
    private Long id;

    /**
     * Имя значения, отображаемое в интерфейсе.
     * Например: "timestamp", "userId", "result".
     */
    private String name;

    /**
     * Множественность значения — количество допустимых элементов.
     * Используется при валидации и отображении структуры данных.
     */
    private String multiplicity;


    /**
     * Стратегия получения значения. Возможные значения:
     * <ul>
     *   <li>{@code INPUT} — прямое копирование из источника</li>
     *   <li>{@code SCRIPT} — вычисляется через скрипт ({@link #script})</li>
     *   <li>{@code SOURCE} — загружается из соурса</li>
     *   <li>{@code EMPTY} — пустое значение</li>
     * </ul>
     */
    private String loaderType;


    /**
     * Строка для извлечения значения из сложного источника.
     * Например:
     * <ul>
     *   <li>JSONPath: {@code $.data.temperature}</li>
     * </ul>
     */
    private String extractionString;


    /**
     * ID входного значения, от которого зависит это значение.
     * Используется, когда {@link #loaderType} = {@code INPUT_LOADER}.
     * Используется при маппинге INPUT_LOADER между блоками: указывает, из какого выходного значения берётся данные.
     */
    private Long inputValueId;

    /**
     * Скрипт для вычисления значения.
     * Используется, когда {@link #loaderType} = {@code SCRIPT_LOADER}.
     * Пример: {@code input.value * 2}, {@code toUpperCase(input.str)}, {@code now()}.
     * Язык скрипта зависит от реализации (SpEL, JS, Groovy и т.д.).
     */
    private String script;

    /**
     * Уникальный ID источника данных (например, Kafka-топика, HTTP-эндпоинта, базы данных).
     * Определяет, откуда приходят данные.
     * Используется, когда {@link #loaderType} = {@code SOURCE_LOADER}.
     */
    private Long sourceId;
    /**
     * Версия источника данных.
     * Может использоваться для управления совместимостью схем.
     * Используется, когда {@link #loaderType} = {@code SOURCE_LOADER}.
     * Например: "2.1.0".
     */
    private String sourceVersion;
    /**
     * Имя источника данных (для отображения в UI).
     * Например: "Sensor Kafka Topic", "CRM API".
     * Используется, когда {@link #loaderType} = {@code SOURCE_LOADER}.
     */
    private String sourceName;

    private String sourceServicePath;
    /**
     * Список входных переменных источника.
     * Используется, если источник требует параметров (например, API с фильтрами).
     * Позволяет описать, какие значения нужно передать источнику для получения данных.
     * Используется, когда {@link #loaderType} = {@code SOURCE_LOADER}.
     */
    private Collection<SourceInputVariableModel> sourceInputVariableModels;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ValueModel that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
