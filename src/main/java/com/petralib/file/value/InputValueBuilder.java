package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Collections;
import java.util.Map;

final class InputValueBuilder extends ValueBuilder{

    public InputValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, Map<Long, ValueLoaderModel> loaderModelMap) {
        super(scenarioBlock, scenarioVariable, loaderModelMap);
    }

    @Override
    void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder) {
        builder.loaderType(LoaderType.INPUT_LOADER.name())
                .parents(Collections.singletonList(getScenarioVariable().getProducerVariable().getId()))
                .extractionString(getScenarioVariable().getExtractionString());
    }
}
