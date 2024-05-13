package com.petralib.workflow.dto;

import com.petralib.signal.dto.SignalDto;
import com.petralib.variable.dto.VariableDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class WorkflowDto {
    Long id;
    @NotEmpty(message = "Workflow name is empty")
    @Size(max = 100, message = "Workflow name is too long")
    String name;

    @NotNull(message = "Project id is null")
    Long projectId;
    String description;

    @Valid
    Collection<VariableDto> variableList;
}
