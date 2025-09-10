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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileConstructor {

//    BlockRepository blockRepository;
//    ScenarioBlockRepo scenarioBlockRepo;
//
//    public ConstructorModel create(Long serviceId) {
//        return ConstructorModel.builder()
//                .sources(sources(serviceId))
//                .consumers(consumers(serviceId))
//                .producers(producers(serviceId))
//                .build();
//    }
//
//
//    private Collection<LocalSourceModel> sources(Long serviceId) {
//        Collection<BlockEntity> blocks = blockRepository.findBlocksByService(serviceId);
//        Collection<BlockEntity> services = blocks.stream().filter(blockEntity -> blockEntity.getType() == BlockType.SOURCE).toList();
//        return services.stream().map(blockEntity -> new LocalSourceModel(blockEntity.getId(),
//                        "0",
//                        blockEntity.getName(),
//                        blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
//                                .collect(Collectors.toList())))
//                .toList();
//    }
//
//    private Collection<LocalConsumerModel> consumers(Long serviceId) {
//        return scenarioBlockRepo.findScenarioBlocksByService(serviceId)
//                .stream().map(scenarioBlockEntity -> {
//                    BlockEntity blockEntity = scenarioBlockEntity.getBlock();
//                    Collection<ValueLoaderModel> lastWorkflowBlockValueParser = BuilderConstructor.loaderModelMap(scenarioBlockEntity);
//
//                    return new LocalConsumerModel(
//                            blockEntity.getId(),
//                            "0",
//                            blockEntity.getType().name(),
//                            blockEntity.getName(),
//                            lastWorkflowBlockValueParser,
//                            lastWorkflowBlockValueParser.size(),
//                            blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name())).toList()
//                    );
//                }).toList();
//    }
//
//    private Collection<LocalProducerModel> producers(Long serviceId) {
//        return scenarioBlockRepo.findScenarioBlocksByService(serviceId)
//                .stream()
//                .filter(scenarioBlockEntity -> scenarioBlockEntity.getBlock().getType() == BlockType.WORKFLOW)
//                .map(scenarioBlockEntity -> {
//                    Collection<ScenarioBlockEntity> workflowChildrenBlocks = scenarioBlockRepo.findScenarioBlocksByWorkflow(scenarioBlockEntity.getBlock().getId());
//                    Collection<RemoteConsumerModel> consumers = new ArrayList<>();
//                    for (ScenarioBlockEntity child : workflowChildrenBlocks) {
//                        consumers.add(new RemoteConsumerModel(
//                                child.getBlock().getId(),
//                                "0",
//                                child.getBlock().getService().getName()
//                        ));
//                    }
//
//                    Collection<ValueLoaderModel> lastWorkflowBlockValueParser;
//                    Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findScenarioBlockForWorkflowExit(scenarioBlockEntity.getBlock().getId());
//                    if (scenarioBlockEntityOpt.isPresent()) {
//                        ScenarioBlockEntity exit = scenarioBlockEntityOpt.get();
//                        lastWorkflowBlockValueParser = BuilderConstructor.loaderModelMap(exit);
//                    } else {
//                        lastWorkflowBlockValueParser = new ArrayList<>();
//                    }
//
//
//                    return new LocalProducerModel(
//                            scenarioBlockEntity.getBlock().getId(),
//                            "0",
//                            scenarioBlockEntity.getBlock().getName(),
//                            consumers,
//                            lastWorkflowBlockValueParser,
//                            lastWorkflowBlockValueParser.size()
//                    );
//                })
//                .toList();
//    }


}
