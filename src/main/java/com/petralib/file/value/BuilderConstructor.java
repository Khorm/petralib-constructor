package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.PinType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.enums.FunctionVariableType;

import java.util.*;

public final class BuilderConstructor {
    BuilderConstructor() {
    }

    public static Collection<ValueModel> loaderModelMap(ScenarioBlockEntity scenarioBlock) {

        Set<ValueModel> ret = new HashSet<>();

        Collection<VariableEntity> variables = scenarioBlock.getContextVariables();

        for (VariableEntity variable : variables) {
            Optional<ValueBuilder> builder = createBuilder(scenarioBlock, variable.getId(), ret);
            if (builder.isEmpty()) {
                continue;
            }
            ret.add(builder.get().build());
        }
        return ret;
    }


    static Optional<ValueBuilder> createBuilder(ScenarioBlockEntity scenarioBlock,
                                                Long variableId, Set<ValueModel> ret) {
        Optional<ScenarioVariableEntity> scenarioContextVariableOpt = scenarioBlock.getVariables().stream()
                .filter(entity -> entity.getConsumerVariable().getId().equals(variableId)).findFirst();
        if (scenarioContextVariableOpt.isEmpty()) {
            return Optional.empty();
        }
        ScenarioVariableEntity scenarioVariable = scenarioContextVariableOpt.get();

        List<ScenarioVariableEntity> scenarioContextVariableParams = scenarioBlock.getVariables().stream()
                .filter(entity -> entity.getOwnerVariable().getId().equals(scenarioVariable.getId())
                        && entity.getFunctionVariableType() == FunctionVariableType.PARAMETER)
                .toList();

        List<Long> parentIds = scenarioContextVariableParams.stream()
                .mapToLong(value -> value.getProducerVariable().getId())
                .boxed().toList();



        switch (scenarioVariable.getType()) {
            case SCRIPT -> {
                return Optional.of(new ScriptValueBuilder(scenarioBlock, scenarioVariable, parentIds));
            }
            case SIMPLE -> {
                return Optional.of(new InputValueBuilder(scenarioBlock, scenarioVariable, parentIds));
            }
            case SOURCE -> {
                return Optional.of(new SourceValueBuilder(scenarioBlock, scenarioVariable, parentIds));
            }
            default -> throw new NullPointerException();
        }
    }
}
