package com.petralib.ctype.mapper;

import com.petralib.ctype.dto.CTypeFieldDto;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.entity.CTypeFieldEntity;
import com.petralib.ctype.enums.Multiplicity;
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

    CTypeFieldDto entityToDto(CTypeFieldEntity entity);

    @Mapping(target = "fieldType", source = "fieldType", qualifiedByName = "varTypeToType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    @Mapping(target = "owner", source = "ownerId", ignore = true)
    CTypeFieldEntity dtoToEntity(CTypeFieldDto dto);

    Collection<CTypeFieldDto> map(Collection<CTypeFieldEntity> entities);


//    TypeVariableDto blockEntityToDto(VariableEntity entity);

//    @Mapping(target = "varType", source = "varType", qualifiedByName = "varTypeToType")
//    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
//    VariableEntity dtoToBlockEntity(TypeVariableDto dto);

//    Collection<TypeVariableDto> mapBlock(Collection<VariableEntity> entities);

    @Named("varTypeToType")
    default CTypeEntity varTypeToType(CTypeShortDto dto) {
        if (dto == null) return null;
        CTypeEntity type = new CTypeEntity();
        type.setId(dto.getId());
        return type;
    }

    @Named("strMultiplicityToEnum")
    default Multiplicity strMultiplicityToEnum(String val){
        return Multiplicity.valueOf(val);
    }
}
