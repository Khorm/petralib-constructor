package com.petralib.file.value;

import com.petralib.file.model.SourceInputVariableModel;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.concurrent.atomic.AtomicInteger;

final class SourceValueBuilder extends ValueBuilder {


    public SourceValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, AtomicInteger counter) {
        super(scenarioBlock, scenarioVariable, counter);
    }

    @Override
    void extendedBuild(ValueModel model) {
        model.setSourceName(getCurrentScenarioVariable().getProducerSource().getName());
        model.setSourceId(getCurrentScenarioVariable().getProducerSource().getId());
        model.setSourceVersion("0");
        model.setSourceInputVariableModels(getCurrentScenarioVariable()
                .getProducerSource().getInVariables().stream().map(sourceVariableEntity -> {
                    // Ищем существующую сценарную переменную, соответствующую:
                    // - принадлежит текущему блоку сценария
                    // - её владелец (owner) — это переменная из модели (вход блока)
                    // - её потребитель (consumer) — это переменная источника (sourceVariableEntity)
                    // Это позволяет найти точку подключения значения из источника к входу текущего блока
                    ScenarioVariableEntity resultCurBlockVar = getScenarioBlock().getVariables().stream()
                            .filter(scenarioVar -> scenarioVar.getOwnerVariable().getId().equals(model.getId()))
                            .filter(scenarioCurrentVar -> sourceVariableEntity.getId().equals(scenarioCurrentVar.getConsumerVariable().getId()))
                            .findFirst().orElseThrow();

                    return SourceInputVariableModel.builder()
                            .sourceVariable(sourceVariableEntity.getId())
                            .sourceValueName(sourceVariableEntity.getName())
                            .sourceValueMultiplicity(sourceVariableEntity.getMultiplicity().name())
                            .currentBlockVariable(resultCurBlockVar.getProducerVariable().getId())
                            .extractionString(resultCurBlockVar.getExtractionString())
                            .build();
                }).toList());
    }
}
