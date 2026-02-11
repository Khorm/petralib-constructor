package com.petralib.block.mapper;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.PinType;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.entity.CTypeEntity;
import com.petralib.ctype.enums.Multiplicity;
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

    @Mapping(target = "varType", source = "variableType", qualifiedByName = "varTypeToType")
    @Mapping(target = "multiplicity", source = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    @Mapping(target = "pinType", source = "pinType", qualifiedByName = "strPinTypeToEnum")
//    @Mapping(target = "block", source = "blockId", qualifiedByName = "blockIdToEntity")
    VariableEntity dtoToEntity(VariableDto dto);

    @Mapping(source = "varType", target = "variableType")
//    @Mapping(source = "block.id", target = "blockId")
    VariableDto blockEntityToDto(VariableEntity entity);

    Collection<VariableDto> map(Collection<VariableEntity> entities);

    Collection<VariableEntity> mapDto(Collection<VariableDto> entities);


    @Named("varTypeToType")
    default CTypeEntity varTypeToType(CTypeShortDto dto) {
        if (dto == null) return null;
        CTypeEntity type = new CTypeEntity();
        type.setId(dto.getId());
        return type;
    }

    @Named("strMultiplicityToEnum")
    default Multiplicity strMultiplicityToEnum(String val) {
        return Multiplicity.valueOf(val);
    }

    @Named("strPinTypeToEnum")
    default PinType strPinTypeToEnum(String val) {
        return PinType.valueOf(val);
    }

//    @Named("blockIdToEntity")
//    default BlockEntity blockIdToEntity(Long id) {
//        if (id == null) return null;
//        else {
//            BlockEntity entity = new BlockEntity();
//            entity.setId(id);
//            return entity;
//        }
//    }
}
