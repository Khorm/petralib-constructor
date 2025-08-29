package com.petralib.type.mapper;

import com.petralib.block.enitity.VariableEntity;
import com.petralib.type.dto.TypeShortDto;
import com.petralib.type.dto.TypeVariableDto;
import com.petralib.type.entity.TypeEntity;
import com.petralib.type.entity.TypeVariableEntity;
import com.petralib.type.enums.Multiplicity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ShortTypeMapper.class)
public interface TypeVariableMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    TypeVariableDto entityToDto(TypeVariableEntity entity);

    @Mapping(target = "varType", source = "varType", qualifiedByName = "varTypeToType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    TypeVariableEntity dtoToEntity(TypeVariableDto dto);

    Collection<TypeVariableDto> map(Collection<TypeVariableEntity> entities);


    TypeVariableDto blockEntityToDto(VariableEntity entity);

    @Mapping(target = "varType", source = "varType", qualifiedByName = "varTypeToType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    VariableEntity dtoToBlockEntity(TypeVariableDto dto);

    Collection<TypeVariableDto> mapBlock(Collection<VariableEntity> entities);

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
}
