package com.petralib.scenario.dto;

import com.petralib.ctype.dto.CTypeShortDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO для описания структуры наследования или вложенности полей типа при передаче данных
 * между блоками в сценарии.
 *
 * <p>Используется для представления иерархии полей при настройке переменных в сценарии,
 * особенно в контексте сложных типов ({@link CTypeShortDto}), где важно отразить
 * вложенную структуру и типы полей.</p>
 *
 * <p>Пример: если переменная ссылается на тип с полями, это DTO описывает одно из таких полей
 * и его тип, возможно, в составе дерева наследования.</p>
 */
@Deprecated
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@AllArgsConstructor
//@Getter
//@NoArgsConstructor
class TypeInheritanceDto {
//    /**
//     * Уникальный идентификатор записи наследования поля.
//     * Может использоваться для отслеживания изменений или ссылок в БД.
//     */
//    Long id;
//
//
//    Long ownerId;
//
//    /**
//     * Идентификатор владельца — ссылка на сценарную переменную или поле,
//     * которому принадлежит это наследуемое поле.
//     */
//    Long fieldId;
//
//    /**
//     * ID поля в исходном типе (в сущности CTypeFieldEntity).
//     * Используется для точной идентификации поля при маппинге.
//     */
//    String fieldName;
//
//    /**
//     * Тип данного поля. Поддерживает вложенность — может быть примитивом
//     * или ссылаться на другой сложный тип.
//     */
//    CTypeShortDto fieldType;
}
