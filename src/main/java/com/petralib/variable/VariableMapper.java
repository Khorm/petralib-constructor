package com.petralib.variable;

import com.petralib.variable.dto.VariableDto;
import com.petralib.variable.entity.VariableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface VariableMapper {

    @Mapping(source = "multiplicity", target = "multiplicity", qualifiedByName = "strMultiplicityToEnum")
    VariableEntity fromDtoToEntity(VariableDto variableDto);

    Collection<VariableEntity> map(Collection<VariableDto> dtos);

    @Named("strMultiplicityToEnum")
    default Multiplicity strMultiplicityToEnum(String strMultiplicity){
        return Multiplicity.valueOf(strMultiplicity);
    }
}
