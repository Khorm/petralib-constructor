package com.petralib.scenario.service;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.Script;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import com.petralib.type.entity.TypeEntity;
import com.petralib.type.entity.TypeVariableEntity;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public final class ScenarioVariableFactory {
    private ScenarioVariableFactory(){}

    public static ScenarioVariableEntity createVar(ScenarioVariableDto dto, Long scenarioBlockId){
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity(scenarioBlockId);
        return switch (ScenarioVariableType.valueOf(dto.getType())) {
            case SIMPLE -> simpleVarFactory(dto, scenarioBlockEntity);
            case SOURCE_IN -> sourceInVarFactory(dto, scenarioBlockEntity);
            case SOURCE_OUT -> sourceOutVarFactory(dto, scenarioBlockEntity);
            case SCRIPT -> scriptVarFactory(dto, scenarioBlockEntity);
        };
    }

    public static ScenarioVariableEntity simpleVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity){
        AtomicInteger i = new AtomicInteger(1);
        Collection<TypeDependenceEntity> typeDependence = dto.getTypeInheritance().stream().map(typeInheritanceDto -> TypeDependenceEntity.builder()
                .ownerType(new TypeEntity(typeInheritanceDto.getOwnerId()))
                .currentTypeVariable(new TypeVariableEntity(typeInheritanceDto.getId()))
                .count(i.getAndIncrement())
                .build()).toList();

        ScenarioVariableEntity scenarioVariable = ScenarioVariableEntity.builder()
                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
                .scenarioBlock(scenarioBlockEntity)
                .type(ScenarioVariableType.SIMPLE)
                .typeDependence(typeDependence)
                .build();

        scenarioVariable.getTypeDependence().forEach(typeDepend -> typeDepend.setScenarioVariable(scenarioVariable));
        return scenarioVariable;
    }


    public static ScenarioVariableEntity sourceInVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity){
        AtomicInteger i = new AtomicInteger(1);
        Collection<TypeDependenceEntity> typeDependence = dto.getTypeInheritance().stream().map(typeInheritanceDto -> TypeDependenceEntity.builder()
                .ownerType(new TypeEntity(typeInheritanceDto.getOwnerId()))
                .currentTypeVariable(new TypeVariableEntity(typeInheritanceDto.getId()))
                .count(i.getAndIncrement())
                .build()).toList();


        ScenarioVariableEntity scenarioVariable = ScenarioVariableEntity.builder()
                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
                .scenarioBlock(scenarioBlockEntity)
                .type(ScenarioVariableType.SOURCE_IN)
                .typeDependence(typeDependence)
                .source(new BlockEntity(dto.getSourceId()))
                .build();

        scenarioVariable.getTypeDependence().forEach(typeDepend -> typeDepend.setScenarioVariable(scenarioVariable));
        return scenarioVariable;
    }

    public static ScenarioVariableEntity sourceOutVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity){
        return ScenarioVariableEntity.builder()
                .producerVariable(new VariableEntity(dto.getProducerVariableId()))
                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
                .scenarioBlock(scenarioBlockEntity)
                .type(ScenarioVariableType.SOURCE_OUT)
                .source(new BlockEntity(dto.getSourceId()))
                .build();

    }

    public static ScenarioVariableEntity scriptVarFactory(ScenarioVariableDto dto, ScenarioBlockEntity scenarioBlockEntity){
        Script script = Script.builder()
                .script(dto.getScript())
                .build();

        ScenarioVariableEntity scenarioVariable = ScenarioVariableEntity.builder()
                .consumerVariable(new VariableEntity(dto.getConsumerVariableId()))
                .scenarioBlock(scenarioBlockEntity)
                .type(ScenarioVariableType.SCRIPT)
                .script(script)
                .build();
        script.setScenarioVariable(scenarioVariable);
        return scenarioVariable;

    }

}
