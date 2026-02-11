package com.petralib.scenario.service;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.VariableMapper;
import com.petralib.block.repo.BlockRepository;
import com.petralib.block.service.BlockService;
import com.petralib.scenario.dto.CurrentVariableDto;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.ScenarioVariablesDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
import com.petralib.scenario.entity.TypeDependenceEntity;
import com.petralib.scenario.enums.BeginEndType;
import com.petralib.scenario.enums.CurrentVariableType;
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
import java.util.stream.Collectors;

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
    BlockRepository blockRepo;

    @Transactional(readOnly = true)
    public ScenarioVariablesDto getVariables(Long scenarioBlockId) {

        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockRepo.findById(scenarioBlockId).orElseThrow();
        Collection<VariableDto> previousVariables = variableMapper.map(getPreviousVariables(scenarioBlockEntity));
        Collection<VariableEntity> inVariables = scenarioBlockEntity.getBlock().getInVariables();
        Collection<VariableEntity> localVariables = scenarioBlockEntity.getBlock().getLocalVariables(scenarioBlockId);

        // Создаем DTO для текущих переменных
        Collection<VariableEntity> allVariables = new ArrayList<>(inVariables.size() + localVariables.size());
        allVariables.addAll(inVariables);
        allVariables.addAll(localVariables);
        Collection<CurrentVariableDto> currentVariableDtos = createCurrentVariables(allVariables, scenarioBlockEntity.getVariables());

        return new ScenarioVariablesDto(previousVariables, currentVariableDtos,
                scenarioBlockEntity.getVarVersion());
    }

    /**
     * Сохраняет переменные для блока сценария.
     *
     * @param dtos               DTO переменных
     * @param scenarioBlockId    ID блока сценария
     * @param scenarioVarVersion версия переменных блока сценария
     */
    @Transactional
    public void saveVariables(Collection<ScenarioVariableDto> dtos, Long scenarioBlockId, Long scenarioVarVersion) {

        // Проверяем существование блока и версию
        Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findBlockByIdAndVersionForUpdate(scenarioBlockId, scenarioVarVersion);
        if (scenarioBlockEntityOpt.isEmpty()) {
            throw new IllegalStateException("Scenario block not found or version mismatch");
        }

        // Сохраняем локальные переменные
//        saveLocalVariables(localVariablesDto, scenarioBlockId);

        // Получаем существующие переменные блока
        Collection<ScenarioVariableEntity> existingVariables = scenarioVariableRepo.findVariables(scenarioBlockId);
        Set<Long> dtoVariableIds = dtos.stream()
                .map(ScenarioVariableDto::getScenarioVariableId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Список новых переменных для сохранения
        Collection<ScenarioVariableEntity> newScenarioVars = new ArrayList<>();

        // Проходим по DTO и создаём сущности, восстанавливая иерархию
        for (ScenarioVariableDto dto : dtos) {

            // Если уже существует — пропускаем (обрабатывается как update)
            if (dto.getScenarioVariableId() != null) {
                continue;
            }

            // Создаём текущую сущность
            ScenarioVariableEntity entity = ScenarioVariableFactory.createVar(dto, scenarioBlockId, dto.getParentId());
            newScenarioVars.add(entity);

            // Настраиваем зависимости типов
            Collection<TypeDependenceEntity> typeDependenceEntities = typeDependencyService.createTypeDependency(dto.getTypeInheritance(), entity);
            entity.setTypeDependence(typeDependenceEntities);
        }


        // Удаление переменных
        Collection<ScenarioVariableEntity> toRemove = existingVariables.stream()
                .filter(e -> !dtoVariableIds.contains(e.getId()))
                .collect(Collectors.toList());

        scenarioVariableRepo.saveAll(newScenarioVars);
        scenarioVariableRepo.deleteAll(toRemove);

        if (scenarioBlockRepo.updateVarVersion(scenarioBlockId, scenarioVarVersion) == 0) {
            throw new IllegalStateException("Variable version could not be updated");
        }
    }

    @Transactional
    public Collection<VariableEntity> saveLocalVariables(Collection<VariableDto> localVariablesDto, Long scenarioBlockId) {
        if (localVariablesDto == null) {
            return Collections.emptyList();
        }
        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockRepo.findById(scenarioBlockId).orElseThrow();
        Collection<VariableEntity> localVariables = variableMapper.mapDto(localVariablesDto);
        for (VariableEntity variableEntity : localVariables) {
            variableEntity.setBlock(scenarioBlockEntity.getBlock());
            variableEntity.setScenarioBlock(scenarioBlockEntity);
        }
        scenarioBlockEntity.getBlock().setLocalVariables(localVariables);
        blockRepo.save(scenarioBlockEntity.getBlock());
        return scenarioBlockEntity.getBlock().getLocalVariables(scenarioBlockId);
    }


    @Transactional
    public void deleteLocalVariable(Long scenarioBlockId, Long variableId) {
        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockRepo.findById(scenarioBlockId).orElseThrow();
        scenarioBlockEntity.getBlock().deleteLocalVariable(variableId);
    }

    private Collection<VariableEntity> getPreviousVariables(ScenarioBlockEntity scenarioBlockEntity) {

        ScenarioBlockEntity previousBlock = scenarioBlockEntity.getPreviousScenarioBlock();

        if (scenarioBlockEntity.isWorkflowEnd()) {
            previousBlock = beginEndRepo.getEndByWorkflow(scenarioBlockEntity.getParentWorkflow().getId()).getConnectedBlock();
        }

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
            CurrentVariableType currentVariableType = null;
            if (blockVariable.getScenarioBlock() != null) {
                currentVariableType = CurrentVariableType.LOCAL;
            }else {
                currentVariableType = CurrentVariableType.GLOBAL;
            }
            currentVariableDtos.add(new CurrentVariableDto(variableMapper.blockEntityToDto(blockVariable), maxLocalId, scenarioVariableDtos, currentVariableType));
        }
        return currentVariableDtos;
    }


}
