package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.*;

public final class BuilderConstructor {
    BuilderConstructor() {
    }

    public static Collection<ValueLoaderModel> loaderModelMap(ScenarioBlockEntity scenarioBlock){
        Collection<ValueLoaderModel> ret = new ArrayList<>();
        for (VariableEntity variable: scenarioBlock.getBlock().getInVariables()){
            Optional<ValueBuilder> builder = createBuilder(scenarioBlock, variable.getId());
            if (builder.isEmpty()){
                continue;
            }
            ret.add(builder.get().build());
        }
        return ret;
    }


    static Optional<ValueBuilder> createBuilder(ScenarioBlockEntity scenarioBlock,
                                                Long variableId) {
        Optional<ScenarioVariableEntity> scenarioVariableOpt = scenarioBlock.getVariables().stream()
                .filter(entity -> entity.getConsumerVariable().getId().equals(variableId)).findFirst();
        if (scenarioVariableOpt.isEmpty()){
            return Optional.empty();
        }
        ScenarioVariableEntity scenarioVariable = scenarioVariableOpt.get();
        switch (scenarioVariable.getType()) {
            case SCRIPT -> {
                return Optional.of(new ScriptValueBuilder(scenarioBlock, scenarioVariable));
            }
            case SIMPLE -> {
                return Optional.of(new InputValueBuilder(scenarioBlock, scenarioVariable));
            }
            case SOURCE -> {
                return Optional.of(new SourceValueBuilder(scenarioBlock, scenarioVariable));
            }
            default -> throw new NullPointerException();
        }
    }
}
