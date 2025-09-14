package com.petralib.scenario.dto;

import com.petralib.ctype.dto.CTypeShortDto;
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
    Long fieldId;
    String fieldName;
    CTypeShortDto fieldType;
}
