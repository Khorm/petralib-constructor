package com.petralib.file.value;

import com.petralib.file.model.SourceInputVariableModel;
import com.petralib.file.model.ValueModel;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;

import java.util.List;

final class SourceValueBuilder extends ValueBuilder {


    public SourceValueBuilder(ScenarioBlockEntity scenarioBlock, ScenarioVariableEntity scenarioVariable, List<Long> parentIds) {
        super(scenarioBlock, scenarioVariable, parentIds);
    }

    @Override
    void extendedBuild(ValueModel model) {
        model.setSourceName(getCurrentScenarioVariable().getProducerSource().getName());
        model.setSourceId(getCurrentScenarioVariable().getProducerSource().getId());
        model.setSourceServicePath(getCurrentScenarioVariable().getProducerSource().getService().getPath());
        model.setSourceVersion("0");

        model.setSourceInputVariableModels(
                getCurrentScenarioVariable().getProducerSource().getInVariables().stream().
                        map(sourceVariable -> {


                            // Ищем существующую сценарную переменную, соответствующую:
                            // - принадлежит текущему блоку сценария
                            // - её владелец (owner) — это переменная из модели (вход блока)
                            // - её потребитель (consumer) — это переменная источника (sourceVariableEntity)
                            // Это позволяет найти точку подключения значения из источника к входу текущего блока
                            ScenarioVariableEntity resultCurBlockVar = getScenarioBlock().getVariables().stream()
                                    .filter(scenarioVar -> scenarioVar.getOwnerVariable().getId().equals(model.getId()))
                                    .filter(scenarioCurrentVar -> sourceVariable.getId().equals(scenarioCurrentVar.getConsumerVariable().getId()))
                                    .findFirst().orElseThrow();

                            return SourceInputVariableModel.builder()
                                    .sourceVariable(sourceVariable.getId())
                                    .sourceValueName(sourceVariable.getName())
                                    .sourceValueMultiplicity(sourceVariable.getMultiplicity().name())
                                    .producerVariable(resultCurBlockVar.getProducerVariable().getId())
                                    .extractionString(resultCurBlockVar.getExtractionString())
                                    .build();
                        }).toList());
    }
}
