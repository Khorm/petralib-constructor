package com.petralib.file;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.repo.BlockRepository;
import com.petralib.file.builder.BuilderConstructor;
import com.petralib.file.enums.LoaderType;
import com.petralib.file.model.*;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import com.petralib.scenario.repo.ScenarioBlockRepo;
import com.petralib.scenario.repo.ScenarioVariableRepo;
import com.petralib.service.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FileConstructor {

    BlockRepository blockRepository;
    ScenarioVariableRepo scenarioVariableRepo;
    ScenarioBlockRepo scenarioBlockRepo;
    public void create(Long serviceId){
        Collection<BlockEntity> blocks = blockRepository.findBlocksByService(serviceId);

        ConstructorModel constructorModel = ConstructorModel.builder()
                .sources(sources(blocks.stream().filter(blockEntity -> blockEntity.getType() == BlockType.SOURCE).collect(Collectors.toList())))
                .consumers(consumers(serviceId))
                .build()
    }



    private Collection<LocalSourceModel> sources(Collection<BlockEntity> services){
        return services.stream().map(blockEntity -> new LocalSourceModel(blockEntity.getId(),
                "0",
                blockEntity.getName(),
                blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    private Collection<LocalConsumerModel> consumers(Long serviceId){
        return scenarioBlockRepo.findScenarioBlocksByService(serviceId).stream().map(scenarioBlockEntity -> {
            BlockEntity blockEntity = scenarioBlockEntity.getBlock();
            Map<Long, ValueLoaderModel> loaderModelMap = new HashMap<>();
            return new LocalConsumerModel(
                    blockEntity.getId(),
                    "0",
                    blockEntity.getType().name(),
                    blockEntity.getName(),
                    scenarioBlockEntity.getVariables().stream().map(entity -> BuilderConstructor.createBuilder(scenarioBlockEntity, entity.getId(),loaderModelMap).build()).toList(),
                    loaderModelMap.size(),
                    blockEntity.getOutVariables().stream().map(entity -> new ValueDto(entity.getId(), entity.getName(), entity.getMultiplicity().name())).toList()
            );
        }).collect(Collectors.toList());
    }

    private Collection<LocalProducerModel> producers(Long serviceId){
        дозаполнить продусеры
    }




}
