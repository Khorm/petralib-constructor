package com.petralib.file;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.repo.BlockRepository;
import com.petralib.file.model.*;
import com.petralib.file.value.BuilderConstructor;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.repo.ScenarioBlockRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
                .map(blockEntity -> new LocalConsumerModel(
                        blockEntity.getId(),
                        "0",
                        blockEntity.getType().name(),
                        blockEntity.getName(),
                        blockEntity.getInVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                                .collect(Collectors.toList()),
                        blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                                .collect(Collectors.toList())

                )).toList();
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

        Collection<LocalProducerModel> localProducersModels = new ArrayList<>();
        workflowEntities.forEach((workflowId, scenarioBlockEntities) -> {
            Collection<ScenarioBlockEntity> workflowChildrenBlocks = scenarioBlockRepo.findScenarioBlocksByWorkflow(workflowId);
            Collection<RemoteConsumerModel> consumers = new ArrayList<>();
            for (ScenarioBlockEntity child : workflowChildrenBlocks) {
                Collection<ValueModel> valueParser = BuilderConstructor.loaderModelMap(child);
                consumers.add(new RemoteConsumerModel(
                        child.getBlock().getId(),
                        "0",
                        child.getBlock().getService().getPath(),
                        workflowId,
                        "0",
                        valueParser
                ));
            }

            ScenarioBlockEntity exit = null;
            for (ScenarioBlockEntity enterExit : scenarioBlockEntities) {
                if (enterExit.getPreviousScenarioBlock() != null) {
                    exit = enterExit;
                }
            }
            Collection<ValueModel> valueParser = BuilderConstructor.loaderModelMap(exit);
            LocalProducerModel localProducerModel = new LocalProducerModel(
                    workflowId,
                    "0",
                    exit.getBlock().getName(),
                    consumers,
                    valueParser
            );
            localProducersModels.add(localProducerModel);
        });
        return localProducersModels;


//            Map<Long, Collection<ScenarioBlockEntity>> workflowEntities = scenarioBlockRepo.findScenarioBlocksByService(serviceId)
//                    .stream()
//                    .filter(scenarioBlockEntity -> scenarioBlockEntity.getBlock().getType() == BlockType.WORKFLOW)
//                    .collect(Collectors.toMap(new Function<ScenarioBlockEntity, Long>() {
//                    }))


//                    .map(scenarioBlockEntity -> {
//
//                        Collection<ScenarioBlockEntity> workflowChildrenBlocks = scenarioBlockRepo.findScenarioBlocksByWorkflow(scenarioBlockEntity.getBlock().getId());
//                        Collection<RemoteConsumerModel> consumers = new ArrayList<>();
//                        for (ScenarioBlockEntity child : workflowChildrenBlocks) {
//                            Collection<ValueModel> valueParser = BuilderConstructor.loaderModelMap(child);
//                            consumers.add(new RemoteConsumerModel(
//                                    child.getBlock().getId(),
//                                    "0",
//                                    child.getBlock().getService().getPath(),
//                                    scenarioBlockEntity.getBlock().getId(),
//                                    "0",
//                                    valueParser
//                            ));
//                        }
//
//                        Collection<ValueModel> lastWorkflowBlockValueParser = null;
//                        Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findScenarioBlockForWorkflowExit(scenarioBlockEntity.getBlock().getId());
//                        if (scenarioBlockEntityOpt.isPresent()) {
//                            ScenarioBlockEntity exit = scenarioBlockEntityOpt.get();
//                            lastWorkflowBlockValueParser = BuilderConstructor.loaderModelMap(exit);
//                        }
//                        return new LocalProducerModel(
//                                scenarioBlockEntity.getBlock().getId(),
//                                "0",
//                                scenarioBlockEntity.getBlock().getName(),
//                                consumers,
//                                lastWorkflowBlockValueParser
//                        );
//                    })
//                    .toList();
        }


    }
