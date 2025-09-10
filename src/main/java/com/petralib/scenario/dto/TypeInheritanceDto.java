package com.petralib.scenario.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TypeInheritanceDto {
    Long id;
    Long ownerId;
    Integer count;
    String fieldName;
    Long fieldId;
    Long fieldTypeId;
//    TypeShortDto varType;
}
