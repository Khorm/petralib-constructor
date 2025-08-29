package com.petralib.scenario.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class ScenarioBlockDto {
    Long id;
    Long blockId;
    String name;
    String blockType;
    Long previousBlock;
    Long nextBlock;
    Long x;
    Long y;
}
