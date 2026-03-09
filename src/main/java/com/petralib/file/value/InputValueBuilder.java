package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

final class InputValueBuilder extends ValueBuilder {

    public InputValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, List<Long> parentIds) {
        super(scenarioBlock, scenarioVariable, parentIds);
    }

    @Override
    void extendedBuild(ValueModel model) {
        model.setLoaderType(LoaderType.INPUT_LOADER.name());
        model.setInputValueId(getCurrentScenarioVariable().getProducerVariable().getId());
        model.setExtractionString(getCurrentScenarioVariable().getExtractionString());
    }
}
