package com.petralib.type.mapper;

import com.petralib.type.dto.TypeFullDto;
import com.petralib.type.entity.TypeEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = TypeVariableMapper.class)
public interface TypeMapper {
    TypeEntity dtoToEntity(TypeFullDto dto);

    TypeFullDto entityToDto(TypeEntity entity);

    Collection<TypeFullDto> map(Collection<TypeEntity> entities);



}
