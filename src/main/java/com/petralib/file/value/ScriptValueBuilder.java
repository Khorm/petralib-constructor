package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.SourceInputVariableModel;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ScriptValueBuilder extends ValueBuilder {

    public ScriptValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable,
                              List<Long> parentIds) {
        super(scenarioBlock, scenarioVariable, parentIds);
    }

    @Override
    void extendedBuild(ValueModel model) {
        model.setLoaderType(LoaderType.SCRIPT_LOADER.name());
        model.setScript(getCurrentScenarioVariable().getProducerScript());

        Collection<VariableEntity> contextVars = getScenarioBlock().getContextVariables();


        model.setSourceInputVariableModels(contextVars.stream()
                .flatMap((Function<VariableEntity, Stream<Long>>) entity -> {
                    if (getCurrentScenarioVariable().getProducerScript().contains(entity.getName())) {
                        return Stream.of(entity.getId());
                    } else {
                        return Stream.empty();
                    }
                })
                .map(varId -> SourceInputVariableModel.builder()
                        .producerVariable(varId)
                        .build())
                .collect(Collectors.toList()));
    }
}
