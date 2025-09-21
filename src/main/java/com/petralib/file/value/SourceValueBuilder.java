package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

final class SourceValueBuilder extends ValueBuilder {


    public SourceValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable) {
        super(scenarioBlock, scenarioVariable);
    }

    @Override
    void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder) {
        builder
                .loaderType(LoaderType.SOURCE_LOADER.name())
                .sourceName(getCurrentScenarioVariable().getProducerSource().getName())
                .producerVariableId(getCurrentScenarioVariable().getProducerSource().getId())
                .sourceVersion("0")
                .sourceName(getCurrentScenarioVariable().getProducerSource().getName())
                .sourceServiceName(getCurrentScenarioVariable().getProducerSource().getService().getPath());
    }
}
