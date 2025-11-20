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
    void extendedBuild(ValueLoaderModel model) {
        model.setLoaderType(LoaderType.SOURCE_LOADER.name());
        model.setSourceName(getCurrentScenarioVariable().getProducerSource().getName());
        model.setProducerVariableId(getCurrentScenarioVariable().getProducerSource().getId());
        model.setSourceVersion("0");
        model.setSourceServiceName(getCurrentScenarioVariable().getProducerSource().getService().getPath());
    }
}
