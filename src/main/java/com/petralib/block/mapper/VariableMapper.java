package com.petralib.block.mapper;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.PinType;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.entity.CTypeFieldEntity;
import com.petralib.ctype.enums.Multiplicity;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.dto.CTypeFieldDto;
import com.petralib.ctype.mapper.ShortTypeMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ShortTypeMapper.class)
public interface VariableMapper {
    CTypeFieldDto entityToDto(CTypeFieldEntity entity);

    @Mapping(target = "varType", source = "variableType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    @Mapping(target = "pinType", source = "pinType", qualifiedByName = "strPinTypeToEnum")
    VariableEntity dtoToEntity(VariableDto dto);

    @Mapping(source = "varType", target = "variableType")
    VariableDto blockEntityToDto(VariableEntity entity);

    Collection<VariableDto> map(Collection<VariableEntity> entities);


//    @Named("varTypeToType")
//    default CTypeEntity varTypeToType(CTypeShortDto dto) {
//        if (dto == null) return null;
//        CTypeEntity type = new CTypeEntity();
//        type.setId(dto.getId());
//        return type;
//    }

    @Named("strMultiplicityToEnum")
    default Multiplicity strMultiplicityToEnum(String val){
        return Multiplicity.valueOf(val);
    }

    @Named("strPinTypeToEnum")
    default PinType strPinTypeToEnum(String val){
        return PinType.valueOf(val);
    }
}
