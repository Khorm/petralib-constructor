package com.petralib.block.mapper;

import com.petralib.block.enums.BlockType;
import com.petralib.block.dto.BlockDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.service.dto.ServiceDto;
import com.petralib.service.dto.ServiceMapper;
import com.petralib.service.entity.ServiceEntity;
import com.petralib.type.mapper.TypeVariableMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {VariableMapper.class, ServiceMapper.class})
public interface BlockMapper {

    @Mapping(target = "service", source = "service", qualifiedByName = "serviceFromDto")
    BlockEntity fromDtoToEntity(BlockDto blockDto);

    @Mapping(target = "type", source = "type", qualifiedByName = "typeEnumFromStr")
    BlockDto fromEntityToDto(BlockEntity blockEntity);


    Collection<BlockDto> map(Collection<BlockEntity> entities);

    @Named("serviceFromDto")
    default ServiceEntity serviceFromDto(ServiceDto dto) {
        if (dto == null) return null;
        ServiceEntity service = new ServiceEntity();
        service.setId(dto.getId());
        return service;
    }

    @Named("typeEnumFromStr")
    default BlockType typeEnumFromStr(String type) {
        return BlockType.valueOf(type);
    }

}
