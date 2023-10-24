package com.petralib.workflow.dto;

import com.petralib.signal.dto.SignalDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ConstructorWorkflowChildrenDto {
    List<WorkflowDto> childWorkflows;
    List<SignalDto> childSignals;
    WorkflowDto currentDto;
}
