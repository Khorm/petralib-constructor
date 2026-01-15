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

    public static ScenarioVariableEntity createVar(ScenarioVariableDto dto, Long scenarioBlockId, Long parentId) {
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity(scenarioBlockId);
        return switch (ScenarioVariableType.valueOf(dto.getType())) {
            case SIMPLE -> simpleVarFactory(dto, scenarioBlockEntity, parentId);
//            case SOURCE_IN -> sourceInVarFactory(dto, scenarioBlockEntity);
            case SOURCE -> sourceVarFactory(dto, scenarioBlockEntity, parentId);
            case SCRIPT -> scriptVarFactory(dto, scenarioBlockEntity, parentId);
        };
    }

    private static ScenarioVariableEntity simpleVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, Long parentId) {
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parentId);
        entity.setProducerVariable(new VariableEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SIMPLE);
        return entity;
    }


    public static ScenarioVariableEntity sourceVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, Long parentId) {

        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parentId);
        entity.setProducerSource(new BlockEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SOURCE);
        return entity;

    }

    public static ScenarioVariableEntity scriptVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, Long parentId) {
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity, parentId);
        entity.setType(ScenarioVariableType.SCRIPT);
        entity.setProducerScript(dto.getScript());
        return entity;
    }


    private static ScenarioVariableEntity createBase(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity, Long parentId) {
        ScenarioVariableEntity entity = new ScenarioVariableEntity();
        entity.setId(dto.getScenarioVariableId());
        entity.setConsumerVariable(new VariableEntity(dto.getConsumerVariableId()));
        entity.setScenarioBlock(scenarioBlockEntity);
        entity.setLocalId(dto.getLocalId());
        entity.setParentId(parentId);
        entity.setOwnerVariable(new VariableEntity(dto.getBlockVariableId()));
        return entity;
    }


}
