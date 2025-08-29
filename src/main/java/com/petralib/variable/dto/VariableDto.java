package com.petralib.variable.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Deprecated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VariableDto {
    Long id;

    @NotEmpty(message = "Variable name is empty")
    @Size(max = 100, message = "Variable name is too long")
    String name;

    @NotEmpty(message = "Variable type is empty")
    @Size(max = 100, message = "Variable type is too long")
    String type;

    @NotEmpty(message = "Variable multiplicity is incorrect")
    String multiplicity;
    String description;


}
