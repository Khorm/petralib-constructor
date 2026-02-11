package com.petralib.scenario.dto;

import com.petralib.block.dto.VariableDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collection;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
public class ScenarioVariablesInputDto {
    Collection<ScenarioVariableDto> dtos;
    Collection<VariableDto> localVariables = new ArrayList<>();
    Long version;
}
