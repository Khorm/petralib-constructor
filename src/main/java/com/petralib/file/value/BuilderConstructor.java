package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.file.model.ValueModel;
import com.petralib.file.model.ValuesCollectionModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class BuilderConstructor {
    BuilderConstructor() {
    }

    public static ValuesCollectionModel loaderModelMap(ScenarioBlockEntity scenarioBlock) {

        Collection<ValueModel> ret = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        Collection<VariableEntity> variables = scenarioBlock.getBlock().getInVariables();
        variables.addAll(scenarioBlock.getBlock().getLocalVariables(scenarioBlock.getId()));
        for (VariableEntity variable : variables) {
            Optional<ValueBuilder> builder = createBuilder(scenarioBlock, variable.getId(), counter);
            if (builder.isEmpty()) {
                continue;
            }
            ret.add(builder.get().build());
        }
        return new ValuesCollectionModel(ret, counter.get());
    }


    static Optional<ValueBuilder> createBuilder(ScenarioBlockEntity scenarioBlock,
                                                Long newProducerVariableId, AtomicInteger counter) {
        Optional<ScenarioVariableEntity> scenarioVariableOpt = scenarioBlock.getVariables().stream()
                .filter(entity -> entity.getConsumerVariable().getId().equals(newProducerVariableId)).findFirst();
        if (scenarioVariableOpt.isEmpty()) {
            return Optional.empty();
        }
        ScenarioVariableEntity scenarioVariable = scenarioVariableOpt.get();
        counter.incrementAndGet();
        switch (scenarioVariable.getType()) {
            case SCRIPT -> {
                return Optional.of(new ScriptValueBuilder(scenarioBlock, scenarioVariable, counter));
            }
            case SIMPLE -> {
                return Optional.of(new InputValueBuilder(scenarioBlock, scenarioVariable, counter));
            }
            case SOURCE -> {
                return Optional.of(new SourceValueBuilder(scenarioBlock, scenarioVariable, counter));
            }
            default -> throw new NullPointerException();
        }
    }
}
