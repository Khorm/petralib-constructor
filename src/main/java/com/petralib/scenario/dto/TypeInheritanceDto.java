package com.petralib.scenario.dto;

import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.dto.TypeVariableDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TypeInheritanceDto {
    Long id;
    Long ownerId;
    String name;
    TypeShortDto varType;
}
