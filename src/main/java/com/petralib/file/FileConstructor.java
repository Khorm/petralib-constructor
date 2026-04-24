package com.petralib.file;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.repo.BlockRepository;
import com.petralib.file.model.*;
import com.petralib.file.value.ValueModelConstructor;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.repo.ScenarioBlockRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileConstructor {

    BlockRepository blockRepository;
    ScenarioBlockRepo scenarioBlockRepo;

    public ConstructorModel create(Long serviceId) {
        ConstructorModel constructorModel = ConstructorModel.builder()
                .sources(sources(serviceId))
                .consumers(consumers(serviceId))
                .producers(producers(serviceId))
                .build();
        return constructorModel;
    }


    private Collection<LocalSourceModel> sources(Long serviceId) {
        Collection<BlockEntity> blocks = blockRepository.findBlocksByServiceAndType(serviceId, BlockType.SOURCE);
        Collection<BlockEntity> services = blocks.stream().filter(blockEntity -> blockEntity.getType() == BlockType.SOURCE).toList();
        return services.stream().map(blockEntity -> new LocalSourceModel(blockEntity.getId(),
                "0",
                blockEntity.getName(),
                blockEntity.getInVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                        .collect(Collectors.toList()),

                blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                        .findFirst().orElse(null))).toList();

    }

    private Collection<LocalConsumerModel> consumers(Long serviceId) {
        Collection<BlockEntity> consumers = blockRepository.findBlocksByServiceAndType(serviceId, BlockType.ACTION);
        consumers.addAll(blockRepository.findBlocksByServiceAndType(serviceId, BlockType.WORKFLOW));

        return consumers.stream()
                .map(blockEntity -> {
                    List<VariableEntity> inAndTimedVariables = new ArrayList<>();
                    inAndTimedVariables.addAll(blockEntity.getInVariables());
                    inAndTimedVariables.addAll(blockEntity.getAllTimedVariables());
                    return new LocalConsumerModel(
                            blockEntity.getId(),
                            "0",
                            blockEntity.getType().name(),
                            blockEntity.getName(),
                            inAndTimedVariables.stream()
                                    .map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                                    .collect(Collectors.toList()),
                            blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                                    .collect(Collectors.toList())

                    );
                }).toList();
    }

    private Collection<LocalProducerModel> producers(Long serviceId) {
        Collection<ScenarioBlockEntity> workflowScenarios = scenarioBlockRepo.findScenarioBlocksByService(serviceId)
                .stream()
                .filter(scenarioBlockEntity -> scenarioBlockEntity.getBlock().getType() == BlockType.WORKFLOW)
                .toList();

        Map<Long, Collection<ScenarioBlockEntity>> workflowEntities = new HashMap<>();
        for (ScenarioBlockEntity scen : workflowScenarios) {
            if (!workflowEntities.containsKey(scen.getBlock().getId())) {
                workflowEntities.put(scen.getBlock().getId(), new ArrayList<>());
            }
            workflowEntities.get(scen.getBlock().getId()).add(scen);
        }

        //заполнить модели удаленных консюмеров
        Collection<LocalProducerModel> localProducersModels = new ArrayList<>();
        workflowEntities.forEach((workflowId, scenarioBlockEntities) -> {
            Collection<ScenarioBlockEntity> workflowChildrenBlocks = scenarioBlockRepo.findScenarioBlocksByWorkflow(workflowId);
            Collection<RemoteConsumerModel> consumers = new ArrayList<>();
            for (ScenarioBlockEntity child : workflowChildrenBlocks) {
                Collection<ValueModel> valueParser = ValueModelConstructor.loaderModelMap(child);
                Collection<ValueDto> contextValues = child.getContextVariables().stream()
                        .map(variableEntity -> new ValueDto(variableEntity.getId(), variableEntity.getName(),
                                variableEntity.getMultiplicity().name())).toList();
                consumers.add(new RemoteConsumerModel(
                        child.getBlock().getId(),
                        "0",
                        child.getBlock().getService().getPath(),
                        workflowId,
                        "0",
                        child.getBlock().getName(),
                        valueParser,
                        contextValues,
                        child.getNextScenarioBlock() != null ? child.getNextScenarioBlock().getBlock().getId() : null,
                        child.getPreviousScenarioBlock() != null ? child.getPreviousScenarioBlock().getBlock().getId() : null
                ));
            }

            ScenarioBlockEntity exit = null;
            for (ScenarioBlockEntity enterExit : scenarioBlockEntities) {
                if (enterExit.getPreviousScenarioBlock() != null) {
                    exit = enterExit;
                }
            }
            Collection<ValueModel> valueParser = ValueModelConstructor.loaderModelMap(exit);
            Collection<ValueDto> contextValues = exit.getContextVariables().stream()
                    .map(variableEntity -> new ValueDto(variableEntity.getId(), variableEntity.getName(),
                            variableEntity.getMultiplicity().name())).toList();
            LocalProducerModel localProducerModel = new LocalProducerModel(
                    workflowId,
                    "0",
                    exit.getBlock().getName(),
                    consumers,
                    valueParser,
                    contextValues
            );
            localProducersModels.add(localProducerModel);
        });
        return localProducersModels;
    }


}
