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

        Collection<ScenarioVariableEntity> variables = scenarioVariableRepo.findVariables(scenarioBlockId);
        Set<Long> dtoVariableIds = dtos.stream().flatMapToLong(scenarioVariableDto ->
                        scenarioVariableDto.getScenarioVariableId() == null ? LongStream.empty() : LongStream.of(scenarioVariableDto.getScenarioVariableId()))
                .boxed().collect(Collectors.toSet());

        Collection<ScenarioVariableEntity> newScenarioVars = new ArrayList<>();
        for (ScenarioVariableDto dto : dtos) {
            if (dto.getScenarioVariableId() == null) {
                ScenarioVariableEntity entity = ScenarioVariableFactory.createVar(dto, scenarioBlockId);
                newScenarioVars.add(entity);
                Collection<TypeDependenceEntity> typeDependenceEntities = typeDependencyService.createTypeDependency(dto.getTypeInheritance(), entity);
                entity.setTypeDependence(typeDependenceEntities);
            }
        }

        Collection<ScenarioVariableEntity> removeScenarioVars = new ArrayList<>();
        for (ScenarioVariableEntity entity : variables) {
            if (!dtoVariableIds.contains(entity.getId())) {
                removeScenarioVars.add(entity);
            }
        }

        scenarioVariableRepo.saveAll(newScenarioVars);
        scenarioVariableRepo.deleteAll(removeScenarioVars);
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
            inputVariables = variableMapper.map(lastScenarioBlock.getBlock().getVariables());
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

    private Collection<VariableEntity> getPreviousVariables(ScenarioBlockEntity scenarioBlockEntity) {
        ScenarioBlockEntity previousBlock = scenarioBlockEntity.getPreviousScenarioBlock();
        if (previousBlock == null) {
            List<BeginEndEntity> beginEndList = beginEndRepo.getStartEndByWorkflow(scenarioBlockEntity.getParentWorkflow().getId());
            for (BeginEndEntity beginEnd : beginEndList) {
                if (beginEnd.getPointType() == BeginEndType.START && beginEnd.getConnectedBlock() != null) {
                    if (!beginEnd.getConnectedBlock().getId().equals(scenarioBlockEntity.getId())) {
                        return Collections.emptyList();
                    } else {
                        return beginEnd.getWorkflow().getVariables();
                    }
                }
            }
        } else {
            return previousBlock.getBlock().getVariables();
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
