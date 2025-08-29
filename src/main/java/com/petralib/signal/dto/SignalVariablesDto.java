package com.petralib.signal.dto;

import com.petralib.variable.dto.VariableDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SignalVariablesDto {
    Long id;
    @NotEmpty(message = "Signal name is empty")
    @Size(max = 100, message = "Signal name is too long")
    String name;

    @NotNull(message = "Project is null")
    Long projectId;
    String description;

    @NotNull(message = "Workflow is null")
    Long workflowId;

    @Valid
    Collection<VariableDto> variableList;
}
