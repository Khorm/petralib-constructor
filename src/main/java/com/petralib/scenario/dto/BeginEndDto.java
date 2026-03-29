package com.petralib.scenario.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class BeginEndDto {
    Long id;
    Long x;
    Long y;
    String pointType;
    ScenarioBlockDto connectedBlock;
}
