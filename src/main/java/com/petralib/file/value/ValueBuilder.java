package com.petralib.file.value;

import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        ValueModel model = getValueModel();

        extendedBuild(model);

        return model;
    }

    private ValueModel getValueModel() {
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
