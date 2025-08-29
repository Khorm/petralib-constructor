package com.petralib.scenario.mapper;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.scenario.dto.ScenarioBlockDto;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScenarioBlockMapper {

    @Mapping(target = "name", source = "block.name")
    @Mapping(target = "blockType", source = "block.type")
    @Mapping(target = "blockId", source = "block.id")
    @Mapping(target = "previousBlock", source = "previousScenarioBlock.id")
    @Mapping(target = "nextBlock", source = "nextScenarioBlock.id")
    ScenarioBlockDto entityToDto(ScenarioBlockEntity entity);

    @Mapping(target = "previousScenarioBlock", source = "previousBlock", qualifiedByName = "toScenarioBlock")
    @Mapping(target = "nextScenarioBlock", source = "nextBlock", qualifiedByName = "toScenarioBlock")
    @Mapping(target = "block", source = "blockId", qualifiedByName = "toBlock")
    ScenarioBlockEntity dtoToEntity(ScenarioBlockDto dto);

    List<ScenarioBlockDto> mapEntity(List<ScenarioBlockEntity> entities);

    List<ScenarioBlockEntity> mapDto(List<ScenarioBlockDto> dtos);

    @Named("toScenarioBlock")
    default ScenarioBlockEntity toScenarioBlock(Long scenarioBlockId) {
        if (scenarioBlockId == null) return null;
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity();
        scenarioBlockEntity.setId(scenarioBlockId);
        return scenarioBlockEntity;
    }

    @Named("toBlock")
    default BlockEntity toBlock(Long blockId) {
        if (blockId == null) return null;
        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setId(blockId);
        return blockEntity;
    }

}
