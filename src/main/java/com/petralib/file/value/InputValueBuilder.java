package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

final class InputValueBuilder extends ValueBuilder{

    public InputValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable) {
        super(scenarioBlock, scenarioVariable);
    }

    @Override
    void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder) {
        builder.loaderType(LoaderType.INPUT_LOADER.name())
                .producerVariableId(getCurrentScenarioVariable().getProducerVariable().getId())
                .requiredBlockVariables(Collections.singletonList(getCurrentScenarioVariable().getProducerVariable().getId()))
                .extractionString(getCurrentScenarioVariable().getExtractionString());
    }
}
