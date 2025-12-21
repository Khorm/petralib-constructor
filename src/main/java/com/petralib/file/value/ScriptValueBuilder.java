package com.petralib.file.value;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ScriptValueBuilder extends ValueBuilder {

    public ScriptValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, AtomicInteger counter) {
        super(scenarioBlock, scenarioVariable, counter);
    }

    @Override
    void extendedBuild(ValueModel model) {
        model.setLoaderType(LoaderType.SCRIPT_LOADER.name());
        model.setScript(getCurrentScenarioVariable().getProducerScript());
        model.setParents(getScenarioBlock().getBlock().getInVariables().stream()
                .flatMap((Function<VariableEntity, Stream<Long>>) entity -> {
                    if (getCurrentScenarioVariable().getProducerScript().contains(entity.getName())) {
                        return Stream.of(entity.getId());
                    } else {
                        return Stream.empty();
                    }
                }).collect(Collectors.toList()));
    }
}
