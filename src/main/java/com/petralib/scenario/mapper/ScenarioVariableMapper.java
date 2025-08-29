package com.petralib.scenario.mapper;

import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.TypeInheritanceDto;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.enums.ScenarioVariableType;
import com.petralib.type.dto.TypeShortDto;
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
    @Mapping(target = "producerVariableId", source = "producerVariable.id")
    @Mapping(target = "consumerVariableId", source = "consumerVariable.id")
    @Mapping(target = "typeInheritance", source = "typeDependence", qualifiedByName = "typeVars")
    @Mapping(target = "sourceId", source = "source.id")
    @Mapping(target = "script", source = "script.script")
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
            ret.add(new TypeInheritanceDto(type.getCurrentTypeVariable().getId(),
                    type.getCurrentTypeVariable().getOwner().getId(),
                    type.getCurrentTypeVariable().getName(),
                    new TypeShortDto(type.getCurrentTypeVariable().getVarType())));
        }
        return ret;
    }

}
