package com.petralib.scenario.mapper;

import com.petralib.ctype.mapper.TypeVariableMapper;
import com.petralib.scenario.dto.BeginEndDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.enums.BeginEndType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ScenarioBlockMapper.class)
public interface BeginEndMapper {

//    @Mapping(source = "connectedBlock", target = "connectedBlock")
    BeginEndDto entityToDto(BeginEndEntity entity);

//    @Mapping(target = "connectedBlock", ignore = true)
    BeginEndEntity dtoToEntity(BeginEndDto dto);

    @ValueMapping(source = "START", target = "START")
    @ValueMapping(source = "END", target = "END")
    BeginEndType strToEnum(String type);

    List<BeginEndDto> map(List<BeginEndEntity> entities);

    List<BeginEndEntity> mapDto(List<BeginEndDto> dtos);

//    @Named("toBlock")
//    default ScenarioBlockEntity toBlock(Long blockId) {
//        if (blockId == null) return null;
//        ScenarioBlockEntity blockEntity = new ScenarioBlockEntity();
//        blockEntity.setId(blockId);
//        return blockEntity;
//    }


//    @AfterMapping
//    default void connectBlock(BeginEndEntity entity, @MappingTarget BeginEndDto dto){
//
//        if (entity.getPointType() == BeginEndType.START
//                && entity.getConnectedBlock().getNextScenarioBlock() != null){
//            dto.setConnectedBlockId(entity.getConnectedBlock().getNextScenarioBlock().getId());
//        }else if (entity.getPointType() == BeginEndType.END
//                && entity.getConnectedBlock().getPreviousScenarioBlock() != null){
//            dto.setConnectedBlockId(entity.getConnectedBlock().getPreviousScenarioBlock().getId());
//        }
//    }


}
