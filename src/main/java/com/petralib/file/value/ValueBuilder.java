package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public abstract class ValueBuilder {
    ScenarioBlockEntity scenarioBlock;
    ScenarioVariableEntity currentScenarioVariable;
    List<Long> parentIds;

    ScenarioBlockEntity getScenarioBlock() {
        return scenarioBlock;
    }

    ScenarioVariableEntity getCurrentScenarioVariable() {
        return currentScenarioVariable;
    }

    public ValueModel build() {
        ValueModel model = createValueModel();

        extendedBuild(model);

        return model;
    }

    private ValueModel createValueModel() {
        ValueModel model = new ValueModel();
        model.setId(currentScenarioVariable.getConsumerVariable().getId());
        model.setName(currentScenarioVariable.getConsumerVariable().getName());
        model.setMultiplicity(currentScenarioVariable.getConsumerVariable().getMultiplicity().name());
        model.setLoaderType(LoaderType.fromScenarioVariableType(currentScenarioVariable.getType()).name());
        model.setExtractionString(currentScenarioVariable.getExtractionString());
        return model;
    }

    abstract void extendedBuild(ValueModel model);


}
