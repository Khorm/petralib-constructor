package com.petralib.scenario.service;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.VariableMapper;
import com.petralib.block.service.BlockService;
import com.petralib.scenario.dto.CurrentVariableDto;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.ScenarioVariablesDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.enums.BeginEndType;
import com.petralib.scenario.mapper.ScenarioVariableMapper;
import com.petralib.scenario.repo.BeginEndRepo;
import com.petralib.scenario.repo.ScenarioBlockRepo;
import com.petralib.scenario.repo.ScenarioVariableRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ScenarioVariablesService {

    ScenarioVariableRepo scenarioVariableRepo;
    ScenarioBlockRepo scenarioBlockRepo;
    BeginEndRepo beginEndRepo;
    ScenarioVariableMapper scenarioVariableMapper;
    VariableMapper variableMapper;
    BlockService blockService;
    TypeDependencyService typeDependencyService;

    @Transactional(readOnly = true)
    public ScenarioVariablesDto getVariables(Long scenarioBlockId) {

        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockRepo.findById(scenarioBlockId).orElseThrow();
        Collection<VariableDto> previousVariables = variableMapper.map(getPreviousVariables(scenarioBlockEntity));
        Collection<VariableEntity> currentVariables = scenarioBlockEntity.getBlock().getInVariables();

        return new ScenarioVariablesDto(previousVariables, createCurrentVariables(currentVariables, scenarioBlockEntity.getVariables()));
    }

    @Transactional
    public void saveVariables(Collection<ScenarioVariableDto> dtos, Long scenarioBlockId) {

//        Collection<ScenarioVariableEntity> variables = scenarioVariableRepo.findVariables(scenarioBlockId);
//        Set<Long> dtoVariableIds = dtos.stream().flatMapToLong(scenarioVariableDto ->
//                        scenarioVariableDto.getScenarioVariableId() == null ? LongStream.empty() : LongStream.of(scenarioVariableDto.getScenarioVariableId()))
//                .boxed().collect(Collectors.toSet());
//
//        Collection<ScenarioVariableEntity> newScenarioVars = new ArrayList<>();
//        for (ScenarioVariableDto dto : dtos) {
//            if (dto.getScenarioVariableId() == null) {
//                Optional<ScenarioVariableEntity> parent = scenarioVariableRepo.findByLocalId(dto.getParentId());
//                if (parent.isEmpty()) {
//                    if (dto.getParentId() == 0) {
//                        parent = Optional.empty();
//                    } else {
//                        parent = newScenarioVars.stream()
//                                .filter(scenarioVariableEntity -> scenarioVariableEntity.getLocalId().equals(dto.getParentId()))
//                                .findFirst();
//                        if (parent.isEmpty()) {
//                            сделать парента если его нет
//                            throw new IllegalArgumentException("Parent variable not found");
//                        }
//
//                    }
//                }
//                ScenarioVariableEntity entity = ScenarioVariableFactory.createVar(dto, scenarioBlockId, parent.orElse(null));
//                newScenarioVars.add(entity);
//                Collection<TypeDependenceEntity> typeDependenceEntities = typeDependencyService.createTypeDependency(dto.getTypeInheritance(), entity);
//                entity.setTypeDependence(typeDependenceEntities);
//            }
//        }

        // Получаем существующие переменные блока
        Collection<ScenarioVariableEntity> existingVariables = scenarioVariableRepo.findVariables(scenarioBlockId);
        Set<Long> dtoVariableIds = dtos.stream()
                .map(ScenarioVariableDto::getScenarioVariableId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Список новых переменных для сохранения
        Collection<ScenarioVariableEntity> newScenarioVars = new ArrayList<>();

        // Карта: localId → ScenarioVariableEntity (для быстрого поиска родителей)
        Map<Long, ScenarioVariableEntity> localIdToEntity = existingVariables.stream()
                .collect(Collectors.toMap(ScenarioVariableEntity::getLocalId, Function.identity()));

        // Проходим по DTO и создаём сущности, восстанавливая иерархию
        for (ScenarioVariableDto dto : dtos) {

            // Если уже существует — пропускаем (обрабатывается как update)
            if (dto.getScenarioVariableId() != null) {
                continue;
            }

            // Рекурсивно создаём родителя, если нужно
            ScenarioVariableEntity parentEntity = null;
            if (dto.getParentId() != null && dto.getParentId() != 0) {
                parentEntity = getOrCreateParent(dto.getParentId(), dtos, newScenarioVars, localIdToEntity, scenarioBlockId);
            }

            // Создаём текущую сущность
            ScenarioVariableEntity entity = ScenarioVariableFactory.createVar(dto, scenarioBlockId, parentEntity);
            newScenarioVars.add(entity);
            localIdToEntity.put(entity.getLocalId(), entity); // Добавляем в карту для последующих детей

            // Настраиваем зависимости типов
            Collection<TypeDependenceEntity> typeDependenceEntities = typeDependencyService.createTypeDependency(dto.getTypeInheritance(), entity);
            entity.setTypeDependence(typeDependenceEntities);
        }

//        Collection<ScenarioVariableEntity> removeScenarioVars = new ArrayList<>();
//        for (ScenarioVariableEntity entity : variables) {
//            if (!dtoVariableIds.contains(entity.getId())) {
//                removeScenarioVars.add(entity);
//            }
//        }
        // Удаление удалённых переменных
        Collection<ScenarioVariableEntity> toRemove = existingVariables.stream()
                .filter(e -> !dtoVariableIds.contains(e.getId()))
                .collect(Collectors.toList());

        scenarioVariableRepo.saveAll(newScenarioVars);
        scenarioVariableRepo.deleteAll(toRemove);
    }

    @Transactional(readOnly = true)
    public ScenarioVariablesDto getWorkflowExitVariables(Long workflowId) {
        BlockEntity workflow = blockService.getBlockWithVariables(workflowId);
        if (workflow.getType() != BlockType.WORKFLOW) {
            throw new IllegalArgumentException("Block is not workflow");
        }

        //специальный блок для End блока Workflow
        Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findScenarioBlockForWorkflowExit(workflow.getId());
        ScenarioBlockEntity lastScenarioBlock = beginEndRepo.getEndByWorkflow(workflowId).getConnectedBlock();

        Collection<VariableDto> inputVariables = Collections.emptyList();

        if (lastScenarioBlock != null && lastScenarioBlock.getBlock() != null) {
            inputVariables = variableMapper.map(lastScenarioBlock.getBlock().getOutVariables());
        }
        if (scenarioBlockEntityOpt.isEmpty()) {
            return new ScenarioVariablesDto(inputVariables, createCurrentVariables(workflow.getOutVariables(), Collections.emptyList()));
        }

        return new ScenarioVariablesDto(inputVariables, createCurrentVariables(workflow.getOutVariables(), scenarioVariableRepo.findVariables(scenarioBlockEntityOpt.get().getId())));
//                variableMapper.map(blockEntity.getOutVariables()),
//                scenarioVariableMapper.simpleMap(scenarioVariableRepo.findVariables(scenarioBlockEntityOpt.get().getId())));

    }

    @Transactional
    public void saveVariablesExitWorkflow(Collection<ScenarioVariableDto> dtos, Long workflowId) {
        Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findScenarioBlockForWorkflowExit(workflowId);
        if (scenarioBlockEntityOpt.isEmpty()) {
            ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity();
            BlockEntity workflow = blockService.getBlockWithVariables(workflowId);
            scenarioBlockEntity.setParentWorkflow(workflow);
            scenarioBlockEntity.setBlock(workflow);
            scenarioBlockEntity.setX(0L);
            scenarioBlockEntity.setY(0L);
            ScenarioBlockEntity newEntity = scenarioBlockRepo.save(scenarioBlockEntity);
            saveVariables(dtos, newEntity.getId());
        } else {
            saveVariables(dtos, scenarioBlockEntityOpt.get().getId());
        }

    }

    /**
     * Рекурсивно находит или создаёт родительскую сущность по parentId.
     */
    private ScenarioVariableEntity getOrCreateParent(
            Long parentId,
            Collection<ScenarioVariableDto> dtos,
            Collection<ScenarioVariableEntity> newScenarioVars,
            Map<Long, ScenarioVariableEntity> localIdToEntity,
            Long scenarioBlockId) {

        // Проверяем в существующих
        ScenarioVariableEntity existing = localIdToEntity.get(parentId);
        if (existing != null) {
            return existing;
        }

        // Проверяем среди уже созданных новых
        Optional<ScenarioVariableEntity> created = newScenarioVars.stream()
                .filter(e -> e.getLocalId().equals(parentId))
                .findFirst();
        if (created.isPresent()) {
            return created.get();
        }

        // Ищем DTO родителя
        ScenarioVariableDto parentDto = dtos.stream()
                .filter(dto -> Objects.equals(dto.getLocalId(), parentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parent ScenarioVariableDto not found for localId=" + parentId));

        // Рекурсивно создаём родителя родителя
        ScenarioVariableEntity grandParent = null;
        if (parentDto.getParentId() != null && parentDto.getParentId() != 0) {
            grandParent = getOrCreateParent(parentDto.getParentId(), dtos, newScenarioVars, localIdToEntity, scenarioBlockId);
        }

        // Создаём родителя
        ScenarioVariableEntity parentEntity = ScenarioVariableFactory.createVar(parentDto, scenarioBlockId, grandParent);
        newScenarioVars.add(parentEntity);
        localIdToEntity.put(parentEntity.getLocalId(), parentEntity);

        // Настраиваем зависимости типов
        Collection<TypeDependenceEntity> typeDependenceEntities = typeDependencyService.createTypeDependency(parentDto.getTypeInheritance(), parentEntity);
        parentEntity.setTypeDependence(typeDependenceEntities);

        return parentEntity;
    }

    private Collection<VariableEntity> getPreviousVariables(ScenarioBlockEntity scenarioBlockEntity) {
        ScenarioBlockEntity previousBlock = scenarioBlockEntity.getPreviousScenarioBlock();
        if (previousBlock == null) {
            List<BeginEndEntity> beginEndList = beginEndRepo.getStartEndByWorkflow(scenarioBlockEntity.getParentWorkflow().getId());
            for (BeginEndEntity beginEnd : beginEndList) {
                if (beginEnd.getPointType() == BeginEndType.START && beginEnd.getConnectedBlock() != null) {
                    if (!beginEnd.getConnectedBlock().getId().equals(scenarioBlockEntity.getId())) {
                        return Collections.emptyList();
                    } else {
                        return beginEnd.getWorkflow().getInVariables();
                    }
                }
            }
        } else {
            return previousBlock.getBlock().getOutVariables();
        }
        return Collections.emptyList();
    }

    private Collection<CurrentVariableDto> createCurrentVariables(Collection<VariableEntity> currentVariables,
                                                                  Collection<ScenarioVariableEntity> scenarioVariableEntities) {
        Collection<CurrentVariableDto> currentVariableDtos = new ArrayList<>();
        for (VariableEntity blockVariable : currentVariables) {
            Collection<ScenarioVariableDto> scenarioVariableDtos = new ArrayList<>();
            Long maxLocalId = 0L;
            for (ScenarioVariableEntity scenarioVariable : scenarioVariableEntities) {
                if (scenarioVariable.getOwnerVariable().getId().equals(blockVariable.getId())) {
                    if (maxLocalId < scenarioVariable.getLocalId()) {
                        maxLocalId = scenarioVariable.getLocalId();
                    }
                    scenarioVariableDtos.add(scenarioVariableMapper.entityToDto(scenarioVariable));
                }
            }
            currentVariableDtos.add(new CurrentVariableDto(variableMapper.blockEntityToDto(blockVariable), maxLocalId, scenarioVariableDtos));
        }
        return currentVariableDtos;
    }


}
