package com.petralib.ctype.mapper;

import com.petralib.ctype.dto.TypeFullDto;
import com.petralib.ctype.entity.CTypeEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = TypeVariableMapper.class)
public interface TypeMapper {
    @Mapping(target = "project", ignore = true)
    CTypeEntity dtoToEntity(TypeFullDto dto);

    TypeFullDto entityToDto(CTypeEntity entity);

    Collection<TypeFullDto> map(Collection<CTypeEntity> entities);



}
