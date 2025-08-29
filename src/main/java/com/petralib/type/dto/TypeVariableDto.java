package com.petralib.type.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeVariableDto {
    Long id;

    Long ownerId;

    @NotEmpty(message = "Variable name is empty")
    @Size(max = 100, message = "Variable name is too long")
    String name;
    String description;

    @NotEmpty(message = "Multiplicity name is empty")
    String multiplicity;

    @NotNull(message = "Variable type is empty")
    TypeShortDto varType;
}
