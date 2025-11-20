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

    public static ScenarioVariableEntity createVar(ScenarioVariableDto dto, Long scenarioBlockId) {
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity(scenarioBlockId);
        return switch (ScenarioVariableType.valueOf(dto.getType())) {
            case SIMPLE -> simpleVarFactory(dto, scenarioBlockEntity);
//            case SOURCE_IN -> sourceInVarFactory(dto, scenarioBlockEntity);
            case SOURCE -> sourceVarFactory(dto, scenarioBlockEntity);
            case SCRIPT -> scriptVarFactory(dto, scenarioBlockEntity);
        };
    }

    private static ScenarioVariableEntity simpleVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity) {

//        ScenarioVariableEntity scenarioVariable = ScenarioVariableEntity.builder()
//                .id(dto.getScenarioVariableId())
//                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
//                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
//                .scenarioBlock(scenarioBlockEntity)
//                .type(ScenarioVariableType.SIMPLE)
//                .localId(dto.getLocalId())
//                .parentId(dto.getParentId())
//                .ownerVariable(new VariableEntity(dto.getBlockVariableId()))
//                .build();

//        scenarioVariable.getTypeDependence().forEach(typeDepend -> typeDepend.setScenarioVariable(scenarioVariable));
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity);
        entity.setProducerVariable(new VariableEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SIMPLE);
        return entity;
    }


//    public static ScenarioVariableEntity sourceInVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity){

//        scenarioVariable.getTypeDependence().forEach(typeDepend -> typeDepend.setScenarioVariable(scenarioVariable));
//        return ScenarioVariableEntity.builder()
//                .id(dto.getScenarioVariableId())
//                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
//                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
//                .scenarioBlock(scenarioBlockEntity)
//                .type(ScenarioVariableType.SOURCE_IN)
//                .producerSource(new BlockEntity(dto.getSourceId()))
//                .localId(dto.getLocalId())
//                .parentId(dto.getParentId())
//                .ownerVariable(new VariableEntity(dto.getBlockVariableId()))
//                .build();
//        return createBase(dto, scenarioBlockEntity)
//                .producerVariable()
//    }

    public static ScenarioVariableEntity sourceVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity) {
//        return ScenarioVariableEntity.builder()
//                .id(dto.getScenarioVariableId())
//                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
//                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
//                .scenarioBlock(scenarioBlockEntity)
//                .type(ScenarioVariableType.SOURCE_OUT)
//                .producerSource(new BlockEntity(dto.getSourceId()))
//                .localId(dto.getLocalId())
//                .parentId(dto.getParentId())
//                .ownerVariable(new VariableEntity(dto.getBlockVariableId()))
//                .build();
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity);
        entity.setProducerSource(new BlockEntity(dto.getProducerId()));
        entity.setType(ScenarioVariableType.SOURCE);
        return entity;

    }

    public static ScenarioVariableEntity scriptVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity) {
//        Script script = Script.builder()
//                .script(dto.getScript())
//                .build();
//
//        ScenarioVariableEntity scenarioVariable = ScenarioVariableEntity.builder()
//                .id(dto.getScenarioVariableId())
//                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
//                .scenarioBlock(scenarioBlockEntity)
//                .type(ScenarioVariableType.SCRIPT)
//                .producerScript(script)
//                .localId(dto.getLocalId())
//                .parentId(dto.getParentId())
//                .ownerVariable(new VariableEntity(dto.getBlockVariableId()))
//                .build();
//        script.setScenarioVariable(scenarioVariable);
//        return scenarioVariable;
        ScenarioVariableEntity entity = createBase(dto, scenarioBlockEntity);
        entity.setType(ScenarioVariableType.SCRIPT);
        entity.setProducerScript(dto.getScript());
        return entity;
    }


    private static ScenarioVariableEntity createBase(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity) {
        ScenarioVariableEntity entity = new ScenarioVariableEntity();
        entity.setId(dto.getScenarioVariableId());
        entity.setConsumerVariable(new VariableEntity(dto.getConsumerVariableId()));
        entity.setScenarioBlock(scenarioBlockEntity);
        entity.setLocalId(dto.getLocalId());
        entity.setParentId(dto.getParentId());
        entity.setOwnerVariable(new VariableEntity(dto.getBlockVariableId()));
        return entity;
    }


}
