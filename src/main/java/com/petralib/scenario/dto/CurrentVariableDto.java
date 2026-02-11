package com.petralib.scenario.dto;

import com.petralib.block.dto.VariableDto;
import com.petralib.scenario.enums.CurrentVariableType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurrentVariableDto {
    //переменная блока
    VariableDto variable;

    //максимальный локальный id
    Long maxLocalId;

    //все переменные сценария связанные с этой переменной блока
    Collection<ScenarioVariableDto> scenarioVariables;

    CurrentVariableType variableType;

}
