package com.petralib.file.builder;

import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Map;

public final class BuilderConstructor {
    BuilderConstructor() {
    }

    public static ValueBuilder createBuilder(ScenarioBlockEntity scenarioBlock,
                                             Long variableId,
                                             Map<Long, ValueLoaderModel> loaderModelMap) {
        ScenarioVariableEntity scenarioVariable = scenarioBlock.getVariables().stream().filter(entity -> entity.getConsumerVariable().getId().equals(variableId)).findFirst().get();
        switch (scenarioVariable.getType()) {
            case SCRIPT -> {
                return new ScriptValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap);
            }
            case SIMPLE -> {
                return new InputValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap);
            }
            case SOURCE_OUT -> {
                return new SourceValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap);
            }
            case SOURCE_IN -> {
                throw new IllegalArgumentException("SOURCE_IN values cannot be created");
            }
            default -> throw new NullPointerException();
        }
    }
}
