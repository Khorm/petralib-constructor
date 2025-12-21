package com.petralib.scenario.service;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;

public final class ScenarioVariableFactory {
    private ScenarioVariableFactory() {
    }

    public static ScenarioVariableEntity createVar(ScenarioVariableDto dto, Long scenarioBlockId, ScenarioVariableEntity parent) {
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity(scenarioBlockId);
        return switch (ScenarioVariableType.valueOf(dto.getType())) {
            case SIMPLE -> simpleVarFactory(dto, scenarioBlockEntity, parent);
//            case SOURCE_IN -> sourceInVarFactory(dto, scenarioBlockEntity);
            case SOURCE -> sourceVarFactory(dto, scenarioBlockEntity, parent);
            case SCRIPT -> scriptVarFactory(dto, scenarioBlockEntity, parent);
        };
    }

    private static ScenarioVariableEntity simpleVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, ScenarioVariableEntity parent) {
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parent);
        entity.setProducerVariable(new VariableEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SIMPLE);
        return entity;
    }


    public static ScenarioVariableEntity sourceVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, ScenarioVariableEntity parent) {

        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parent);
        entity.setProducerSource(new BlockEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SOURCE);
        return entity;

    }

    public static ScenarioVariableEntity scriptVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, ScenarioVariableEntity parent) {
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parent);
        entity.setType(ScenarioVariableType.SCRIPT);
        entity.setProducerScript(dto.getScript());
        return entity;
    }


    private static ScenarioVariableEntity createBase(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, ScenarioVariableEntity parent) {
        ScenarioVariableEntity entity = new ScenarioVariableEntity();
        entity.setId(dto.getScenarioVariableId());
        entity.setConsumerVariable(new VariableEntity(dto.getConsumerVariableId()));
        entity.setScenarioBlock(scenarioBlockEntity);
        entity.setLocalId(dto.getLocalId());
        entity.setParent(parent);
        entity.setOwnerVariable(new VariableEntity(dto.getBlockVariableId()));
        return entity;
    }


}
