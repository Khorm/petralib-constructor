package com.petralib.scenario.mapper;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.scenario.dto.BeginEndDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.enums.BeginEndType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeginEndMapper {

    @Mapping(source = "connectedBlock.id", target = "connectedBlockId")
    BeginEndDto entityToDto(BeginEndEntity entity);

    @Mapping(source = "connectedBlockId", target = "connectedBlock", qualifiedByName = "toBlock")
    BeginEndEntity dtoToEntity(BeginEndDto dto);

    @ValueMapping(source = "START", target = "START")
    @ValueMapping(source = "END", target = "END")
    BeginEndType strToEnum(String type);

    List<BeginEndDto> map(List<BeginEndEntity> entities);
    List<BeginEndEntity> mapDto(List<BeginEndDto> dtos);

    @Named("toBlock")
    default ScenarioBlockEntity toBlock(Long blockId) {
        if (blockId == null) return null;
        ScenarioBlockEntity blockEntity = new ScenarioBlockEntity();
        blockEntity.setId(blockId);
        return blockEntity;
    }


}
