package com.petralib.scenario.mapper;

import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.TypeInheritanceDto;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ScenarioVariableMapper {

    @Mapping(target = "scenarioVariableId", source = "id")
    @Mapping(target = "type", source = "type", qualifiedByName = "toTypeStr")
    @Mapping(target = "consumerVariableId", source = "consumerVariable.id")
    @Mapping(target = "typeInheritance", source = "typeDependence", qualifiedByName = "typeVars")
    @Mapping(target = "script", source = "producerScript")
    @Mapping(target = "producerId", expression = "java(getProducer(entity))")
    @Mapping(target = "blockVariableId", source = "ownerVariable.id")
    ScenarioVariableDto entityToDto(ScenarioVariableEntity entity);


    Collection<ScenarioVariableDto> simpleMap(Collection<ScenarioVariableEntity> entities);

    @Named("toTypeStr")
    default String toTypeStr(ScenarioVariableType scenarioVariableType) {
        return scenarioVariableType.name();
    }


    @Named("typeVars")
    default List<TypeInheritanceDto> typeVars(Collection<TypeDependenceEntity> list) {
        List<TypeInheritanceDto> ret = new ArrayList<>();

        List<TypeDependenceEntity> sortedVariables = list.stream()
                .sorted(Comparator.comparingInt(TypeDependenceEntity::getCount)).toList();

        for (TypeDependenceEntity type : sortedVariables) {
            ret.add(new TypeInheritanceDto(
                    type.getId(),
                    type.getCurrentField().getOwner().getId(),
                    type.getCurrentField().getId(),
                    type.getCurrentField().getName(),
                    new CTypeShortDto(type.getCurrentField().getFieldType().getId(),
                            type.getCurrentField().getFieldType().getName(),
                            type.getCurrentField().getDescription())

            ));
        }
        return ret;
    }

    default Long getProducer(ScenarioVariableEntity entity) {
        if (entity.getProducerVariable() != null) return entity.getProducerVariable().getId();
        if (entity.getProducerSource() != null) return entity.getProducerSource().getId();
        return null;
    }

}
