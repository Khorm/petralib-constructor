package com.petralib.block.mapper;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.PinType;
import com.petralib.type.enums.Multiplicity;
import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.dto.TypeVariableDto;
import com.petralib.type.entity.TypeEntity;
import com.petralib.type.entity.TypeVariableEntity;
import com.petralib.type.mapper.ShortTypeMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ShortTypeMapper.class)
public interface VariableMapper {
    TypeVariableDto entityToDto(TypeVariableEntity entity);

    @Mapping(target = "varType", source = "varType", qualifiedByName = "varTypeToType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    @Mapping(target = "pinType", source = "pinType", qualifiedByName = "strPinTypeToEnum")
    VariableEntity dtoToEntity(VariableDto dto);

    VariableDto blockEntityToDto(VariableEntity entity);

    Collection<VariableDto> map(Collection<VariableEntity> entities);


    @Named("varTypeToType")
    default TypeEntity varTypeToType(TypeShortDto dto) {
        if (dto == null) return null;
        TypeEntity type = new TypeEntity();
        type.setId(dto.getId());
        return type;
    }

    @Named("strMultiplicityToEnum")
    default Multiplicity strMultiplicityToEnum(String val){
        return Multiplicity.valueOf(val);
    }

    @Named("strPinTypeToEnum")
    default PinType strPinTypeToEnum(String val){
        return PinType.valueOf(val);
    }
}
