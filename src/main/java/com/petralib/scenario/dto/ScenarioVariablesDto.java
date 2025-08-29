package com.petralib.scenario.dto;

import com.petralib.block.dto.VariableDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class ScenarioVariablesDto {
    Collection<VariableDto> inputVariables;
    Collection<VariableDto> currentVariables;
    Collection<ScenarioVariableDto> scenarioVariables;
}
