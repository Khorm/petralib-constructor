package com.petralib.type.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TypeFullDto {
    Long id;
    @NotEmpty(message = "Type name is empty")
    @Size(max = 100, message = "Type name is too long")
    String name;
    String description;

    @Valid
    Collection<TypeVariableDto> variables;
}
