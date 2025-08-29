package com.petralib.block.dto;

import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.enums.Multiplicity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VariableDto {
    Long id;

    @NotEmpty(message = "Variable name is empty")
    @Size(max = 100, message = "Variable name is too long")
    String name;
    String description;

    @NotEmpty(message = "Multiplicity name is empty")
    String multiplicity;

    @NotEmpty(message = "Pin type is empty")
    String pinType;

    @NotNull(message = "Variable type is empty")
    TypeShortDto varType;
}
