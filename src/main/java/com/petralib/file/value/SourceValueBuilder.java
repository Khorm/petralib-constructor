package com.petralib.file.value;

import com.petralib.file.model.SourceInputVariableModel;
import com.petralib.file.model.ValueLoaderModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.Map;
import java.util.stream.LongStream;

final class SourceValueBuilder extends ValueBuilder {


    public SourceValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, Map<Long, ValueLoaderModel> loaderModelMap) {
        super(scenarioBlock, scenarioVariable, loaderModelMap);
    }

    @Override
    void extendedBuild(ValueLoaderModel.ValueLoaderModelBuilder builder) {
        builder.sourceName(getScenarioVariable().getProducerSource().getName())
                .sourceId(getScenarioVariable().getProducerSource().getId())
                .sourceVersion("0")
                .parents(sourceInVariables(getScenarioVariable().getProducerSource().getId()).stream()
                        .flatMapToLong(entity -> LongStream.of(entity.getProducerVariable().getId())).boxed().toList())
                .sourceInputVariableModels(sourceInVariables(getScenarioVariable().getProducerSource().getId()).stream()
                        .map(entity -> new SourceInputVariableModel(entity.getConsumerVariable().getId(),
                                entity.getProducerVariable().getId(),
                                entity.getExtractionString(),
                                entity.getConsumerVariable().getName(),
                                entity.getConsumerVariable().getMultiplicity().name())).toList());
    }
}
