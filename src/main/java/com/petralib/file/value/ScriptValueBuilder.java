package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ScriptValueBuilder /*extends ValueBuilder */{

//    public ScriptValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, Map<Long, ValueLoaderModel> loaderModelMap) {
//        super(scenarioBlock, scenarioVariable, loaderModelMap);
//    }
//
//    @Override
//    void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder) {
//        builder.loaderType(LoaderType.SCRIPT_LOADER.name())
//                .script(getScenarioVariable().getProducerScript().getScript())
//                .parents(getScenarioBlock().getVariables().stream()
//                        .flatMap((Function<ScenarioVariableEntity, Stream<Long>>) entity -> {
//                            if (entity.getType() == ScenarioVariableType.SOURCE_IN) {
//                                return Stream.empty();
//                            }
//                            if (getScenarioVariable().getProducerScript().getScript().contains(entity.getConsumerVariable().getName())) {
//                                return Stream.of(entity.getConsumerVariable().getId());
//                            } else {
//                                return Stream.empty();
//                            }
//                        }).collect(Collectors.toList()));
//    }
}
