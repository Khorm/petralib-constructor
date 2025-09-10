package com.petralib.ctype.mapper;

import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.entity.CTypeEntity;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ShortTypeMapper {
    CTypeShortDto entityToDto(CTypeEntity entity);
    Collection<CTypeShortDto> map(Collection<CTypeEntity> entities);

    CTypeEntity dtoToEntity(CTypeShortDto entity);

}
