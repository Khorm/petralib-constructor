package com.petralib.scenario.service;

import com.petralib.ctype.repository.TypeFieldRepo;
import com.petralib.scenario.dto.TypeInheritanceDto;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.repo.TypeDependencyRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TypeDependencyService {

    TypeFieldRepo typeFieldRepo;
    TypeDependencyRepo typeDependencyRepo;


    public Collection<TypeDependenceEntity> createTypeDependency(List<TypeInheritanceDto> dtos, ScenarioVariableEntity scenarioVariable) {
        Collection<TypeDependenceEntity> typeDependencyList = new ArrayList<>();
        Map<Long, TypeDependenceEntity> previous;

        if (scenarioVariable.getId() != null){
            previous = typeDependencyRepo.findTypeDependenciesByScenarioValue(scenarioVariable.getId())
                    .stream().collect(Collectors.toMap(TypeDependenceEntity::getId, Function.identity()));
        }else {
            previous = new HashMap<>();
        }
        for (TypeInheritanceDto typeInheritanceDto : dtos) {
            if (previous.containsKey(typeInheritanceDto.getId())) {
                typeDependencyList.add(previous.get(typeInheritanceDto.getId()));
                continue;
            }

            TypeDependenceEntity typeDependenceEntity = new TypeDependenceEntity(
                    null,
                    scenarioVariable,
                    typeFieldRepo.getReferenceById(typeInheritanceDto.getFieldTypeId()),
                    typeInheritanceDto.getCount()
            );
            typeDependencyList.add(typeDependenceEntity);
        }
        return typeDependencyList;

    }


}
