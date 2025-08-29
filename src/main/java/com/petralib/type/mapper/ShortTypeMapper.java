package com.petralib.type.mapper;

import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.entity.TypeEntity;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ShortTypeMapper {
    TypeShortDto entityToDto(TypeEntity entity);
    Collection<TypeShortDto> map(Collection<TypeEntity> entities);

}
