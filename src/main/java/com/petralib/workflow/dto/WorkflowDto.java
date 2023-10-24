package com.petralib.workflow.dto;

import com.petralib.signal.dto.SignalDto;
import com.petralib.variable.dto.VariableDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class WorkflowDto {
    Long id;
    String name;
    String description;
    List<SignalDto> subscribedSignals;
    List<SignalDto> sendSignals;
}
