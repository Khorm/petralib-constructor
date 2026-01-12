package com.petralib.scenario.service;

import com.petralib.ctype.dto.CTypeFieldDto;
import com.petralib.ctype.repository.TypeFieldRepo;
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


    public Collection<TypeDependenceEntity> createTypeDependency(List<CTypeFieldDto> dtos, ScenarioVariableEntity scenarioVariable) {
        Collection<TypeDependenceEntity> typeDependencyList = new ArrayList<>();
        Map<Long, TypeDependenceEntity> previous;

        if (scenarioVariable.getId() != null){
            previous = typeDependencyRepo.findTypeDependenciesByScenarioValue(scenarioVariable.getId())
                    .stream().collect(Collectors.toMap(typeDependenceEntity -> typeDependenceEntity.getCurrentField().getId(),
                            Function.identity()));
        }else {
            previous = new HashMap<>();
        }
        int count = 0;
        for (CTypeFieldDto cTypeFieldDto : dtos) {
            count++;
            if (previous.containsKey(cTypeFieldDto.getId()) && previous.get(cTypeFieldDto.getId()).getCount().equals(count)) {
                typeDependencyList.add(previous.get(cTypeFieldDto.getId()));
                continue;
            }

            TypeDependenceEntity typeDependenceEntity = new TypeDependenceEntity(
                    null,
                    scenarioVariable,
                    typeFieldRepo.getReferenceById(cTypeFieldDto.getId()),
                    count
            );
            typeDependencyList.add(typeDependenceEntity);
        }
        return typeDependencyList;

    }


}
