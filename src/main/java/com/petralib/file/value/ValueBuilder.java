package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class ValueBuilder {
    final ScenarioBlockEntity scenarioBlock;
    final ScenarioVariableEntity currentScenarioVariable;
    final AtomicInteger counter;

    ScenarioBlockEntity getScenarioBlock() {
        return scenarioBlock;
    }

    ScenarioVariableEntity getCurrentScenarioVariable() {
        return currentScenarioVariable;
    }

    public ValueModel build() {
        ValueModel model = new ValueModel();
        model.setId(currentScenarioVariable.getConsumerVariable().getId());
        model.setName(currentScenarioVariable.getConsumerVariable().getName());
        model.setMultiplicity(currentScenarioVariable.getConsumerVariable().getMultiplicity().name());
        Long parent = getCurrentScenarioVariable().getParentId();
        if (parent != null) {
            model.setParents(List.of(parent));
        }
        model.setLoaderType(LoaderType.fromScenarioVariableType(currentScenarioVariable.getType()).name());
        model.setExtractionString(currentScenarioVariable.getExtractionString());

        extendedBuild(model);

        Collection<ScenarioVariableEntity> children = scenarioBlock.getVariables().stream()
                .filter(scenarioVariableEntity -> {
                    if (scenarioVariableEntity.getParentId() == null) {
                        return false;
                    }
                    return scenarioVariableEntity.getParentId().equals(currentScenarioVariable.getLocalId());
                }).toList();

        Collection<ValueModel> childrenModels = children.stream().flatMap(entity -> {
            Optional<ValueBuilder> b = BuilderConstructor.createBuilder(scenarioBlock, entity.getConsumerVariable().getId(), counter);
            if (b.isPresent()) {
                counter.incrementAndGet();
                return Stream.of(b.get().build());
            }
            return Stream.empty();
        }).collect(Collectors.toList());
        model.setChildren(childrenModels);
        return model;
    }

    abstract void extendedBuild(ValueModel model);


}
