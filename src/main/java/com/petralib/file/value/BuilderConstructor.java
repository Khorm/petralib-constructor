package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BuilderConstructor {
//    BuilderConstructor() {
//    }
//
//    public static Collection<ValueLoaderModel> loaderModelMap(ScenarioBlockEntity scenarioBlock){
//        Map<Long, ValueLoaderModel> loaderModelMap = new HashMap<>();
//        for (VariableEntity variable: scenarioBlock.getBlock().getInVariables()){
//            Optional<ValueBuilder> builder = createBuilder(scenarioBlock, variable.getId(), loaderModelMap);
//            if (builder.isEmpty()){
//                continue;
//            }
//            builder.get().build();
//        }
//        return loaderModelMap.values();
//    }
//
//
//    static Optional<ValueBuilder> createBuilder(ScenarioBlockEntity scenarioBlock,
//                                                Long variableId,
//                                                Map<Long, ValueLoaderModel> loaderModelMap) {
//        Optional<ScenarioVariableEntity> scenarioVariableOpt = scenarioBlock.getVariables().stream()
//                .filter(entity -> entity.getConsumerVariable().getId().equals(variableId)).findFirst();
//        if (scenarioVariableOpt.isEmpty()){
//            return Optional.empty();
//        }
//        ScenarioVariableEntity scenarioVariable = scenarioVariableOpt.get();
//        switch (scenarioVariable.getType()) {
//            case SCRIPT -> {
//                return Optional.of(new ScriptValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap));
//            }
//            case SIMPLE -> {
//                return Optional.of(new InputValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap));
//            }
//            case SOURCE -> {
//                return Optional.of(new SourceValueBuilder(scenarioBlock, scenarioVariable, loaderModelMap));
//            }
////            case SOURCE_IN -> {
////                throw new IllegalArgumentException("SOURCE_IN values cannot be created");
////            }
//            default -> throw new NullPointerException();
//        }
//    }
}
