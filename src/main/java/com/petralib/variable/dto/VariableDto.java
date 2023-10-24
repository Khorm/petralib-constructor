package com.petralib.variable.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VariableDto {
    Long id;
    String name;
    String type;
}
