package com.petralib.file.value;

import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class ValueBuilder {
    final ScenarioBlockEntity scenarioBlock;
    final ScenarioVariableEntity currentScenarioVariable;
//    final Map<Long, ValueLoaderModel> loaderModelMap;

    ScenarioBlockEntity getScenarioBlock() {
        return scenarioBlock;
    }

    ScenarioVariableEntity getCurrentScenarioVariable() {
        return currentScenarioVariable;
    }

    public ValueLoaderModel build() {
        ValueLoaderModel.ValueLoaderModelBuilder builder = ValueLoaderModel.builder();
        builder
                .scenarioVariableId(currentScenarioVariable.getId())
                .consumerVariableId(currentScenarioVariable.getConsumerVariable().getId())
                .name(currentScenarioVariable.getConsumerVariable().getName())
                .multiplicity(currentScenarioVariable.getConsumerVariable().getMultiplicity().name())
                .parent(currentScenarioVariable.getParentId())
                .localId(currentScenarioVariable.getLocalId())
                ;
        extendedBuild(builder);

        Collection<ScenarioVariableEntity> children = scenarioBlock.getVariables().stream()
                .filter(entity -> entity.getParentId().equals(currentScenarioVariable.getLocalId())).toList();

        Collection<ValueLoaderModel> childrenModels = children.stream().flatMap(entity -> {
            Optional<ValueBuilder> b = BuilderConstructor.createBuilder(scenarioBlock, entity.getConsumerVariable().getId());
            if (b.isPresent()) {
                return Stream.of(b.get().build());
            }
            return Stream.empty();
        }).collect(Collectors.toList());
        builder.children(childrenModels);

        //        loaderModelMap.put(currentScenarioVariable.getConsumerVariable().getId(), model);
        return builder.build();
    }

    abstract void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder);

//    Collection<ScenarioVariableEntity> sourceInVariables(Long sourceId, Long parentId) {
//        return scenarioBlock.getVariables().stream()
//                .filter(entity -> entity.getProducerSource().getId().equals(sourceId) && entity.getParentId().equals(parentId))
//                .toList();
//    }

}
