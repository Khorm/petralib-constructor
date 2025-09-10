package com.petralib.file.value;

import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class ValueBuilder {
    final ScenarioBlockEntity scenarioBlock;
    final ScenarioVariableEntity scenarioVariable;
    final Map<Long, ValueLoaderModel> loaderModelMap;

    ScenarioBlockEntity getScenarioBlock() {
        return scenarioBlock;
    }

//    BlockEntity getBlock() {
//        return scenarioBlock.getBlock();
//    }

    ScenarioVariableEntity getScenarioVariable() {
        return scenarioVariable;
    }

    public ValueLoaderModel build() {
//        if (loaderModelMap.containsKey(scenarioVariable.getConsumerVariable().getId())) {
//            return loaderModelMap.get(scenarioVariable.getConsumerVariable().getId());
//        }
//        ValueLoaderModel.ValueLoaderModelBuilder builder = ValueLoaderModel.builder();
//        builder.id(scenarioVariable.getConsumerVariable().getId())
//                .name(scenarioVariable.getConsumerVariable().getName())
//                .multiplicity(scenarioVariable.getConsumerVariable().getMultiplicity().name());
//        extendedBuild(builder);
//
//        Collection<ScenarioVariableEntity> children = scenarioBlock.getVariables().stream().filter(entity -> {
//            if (entity.getType() == ScenarioVariableType.SCRIPT &&
//                    entity.getProducerScript().getScript().contains(scenarioVariable.getConsumerVariable().getName())) {
//                return true;
//            }
//            if (entity.getType() == ScenarioVariableType.SIMPLE
//                    && entity.getProducerVariable().getId().equals(scenarioVariable.getConsumerVariable().getId())) {
//                return true;
//            }
//
//            if (entity.getType() == ScenarioVariableType.SOURCE_OUT) {
//                Collection<ScenarioVariableEntity> sourceVariables = sourceInVariables(entity.getProducerSource().getId());
//                for (ScenarioVariableEntity sourceVar : sourceVariables) {
//                    if (sourceVar.getProducerVariable().getId().equals(scenarioVariable.getConsumerVariable().getId())) {
//                        return true;
//                    }
//                }
//            }
//
//            return false;
//        }).toList();
//
//        Collection<ValueLoaderModel> childrenModels = children.stream().flatMap(entity -> {
//            Optional<ValueBuilder> b = BuilderConstructor.createBuilder(scenarioBlock, entity.getConsumerVariable().getId(), loaderModelMap);
//            if (b.isPresent()) {
//                return Stream.of(b.get().build());
//            }
//            return Stream.empty();
//        }).collect(Collectors.toList());
//        builder.children(childrenModels);
//
//        ValueLoaderModel model = builder.build();
//        loaderModelMap.put(scenarioVariable.getConsumerVariable().getId(), model);
//        return model;
        return null;
    }

    abstract void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder);

    Collection<ScenarioVariableEntity> sourceInVariables(Long sourceId) {
//        return scenarioBlock.getVariables().stream()
//                .filter(entity -> entity.getType() == ScenarioVariableType.SOURCE_IN && entity.getProducerSource().getId().equals(sourceId))
//                .toList();
        return null;
    }

}
