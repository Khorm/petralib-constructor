package com.petralib.file.model;


import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * Модель представления значения в конструкторе сценариев.
 * Используется для описания структуры данных, источников, преобразований и иерархии полей
 * при построении графов обработки (например, в workflow, mapping, scripting).
 *
 * <p>Поддерживает:
 * <ul>
 *   <li>Иерархию вложенных полей (через {@link #children} и {@link #parents})</li>
 *   <li>Разные способы получения значения: копирование, скрипт, наследование</li>
 *   <li>Связь с источниками данных (источник, версия, входные параметры)</li>
 * </ul>
 */
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
     * Список ID родительских значений в иерархии.
     * Позволяет восстановить путь к полю, например:
     * <pre>
     * parents: [100, 101] → 100.name = "user", 101.name = "address" → полный путь: user.address.{this.name}
     * </pre>
     */
    private List<Long> parents;

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
     * Дочерние значения — вложенные поля (например, поля объекта или JSON).
     * Используется для представления структурированных типов.
     * Позволяет строить древовидные структуры данных.
     */
    private Collection<ValueModel> children;

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
    /**
     * Список входных переменных источника.
     * Используется, если источник требует параметров (например, API с фильтрами).
     * Позволяет описать, какие значения нужно передать источнику для получения данных.
     * Используется, когда {@link #loaderType} = {@code SOURCE_LOADER}.
     */
    private Collection<SourceInputVariableModel> sourceInputVariableModels;


}
