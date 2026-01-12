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
    //входящие в блок сценария переменные
    Collection<VariableDto> inputVariables;

    //текущие переменные блока сценария
    Collection<CurrentVariableDto> currentVariables;
}
