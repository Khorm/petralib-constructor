package com.petralib.variable;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.variable.dto.VariableDto;
import com.petralib.variable.entity.VariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VariableService {

    VariableRepository variableRepository;
    VariableMapper variableMapper;

    @Transactional
    public void updateVariables(Collection<VariableDto> variableEntitiesDto, BlockEntity block){
        Collection<VariableEntity> savedEntities = variableRepository.findVariablesForBlock(block.getId());
        Collection<VariableEntity> variableEntities = variableMapper.map(variableEntitiesDto);
        for (VariableEntity variableEntity :variableEntities){
            System.out.println("NewEntity " + variableEntity);
            boolean found = false;
            for (VariableEntity savedEntity: savedEntities){
                if (savedEntity.getId().equals(variableEntity.getId())){
                    updateEntity(savedEntity, variableEntity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                variableEntity.setBlock(block);
                variableRepository.save(variableEntity);
            }
        }

//        Collection<VariableEntity> removedEntities = new ArrayList<>();
//        for (VariableEntity savedEntity: savedEntities){
//            boolean found = false;
//            for (VariableEntity newEntity: variableEntities){
//                if (newEntity.getId() != null && savedEntity.getId().equals(newEntity.getId())){
//                    found = true;
//                }
//            }
//            if (!found){
//                removedEntities.add(savedEntity);
//            }
//        }
//        variableRepository.deleteAll(removedEntities);
    }

    private void updateEntity(VariableEntity oldEntity, VariableEntity newEntity){
        oldEntity.setName(newEntity.getName());
        oldEntity.setType(newEntity.getType());
        oldEntity.setMultiplicity(newEntity.getMultiplicity());
        oldEntity.setDescription(newEntity.getDescription());
        System.out.println(oldEntity);
        variableRepository.save(oldEntity);
    }

}
