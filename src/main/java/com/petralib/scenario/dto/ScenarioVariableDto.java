package com.petralib.scenario.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ScenarioVariableDto {
    Long scenarioVariableId;
    String type;
    Long consumerVariableId;
    Long producerVariableId;
    Long sourceId;
    List<TypeInheritanceDto> typeInheritance = new ArrayList<>();
    String script;
}
