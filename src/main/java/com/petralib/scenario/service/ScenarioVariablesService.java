package com.petralib.scenario.service;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.VariableMapper;
import com.petralib.block.service.BlockService;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.scenario.dto.ScenarioVariablesDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.entity.ScenarioVariableEntity;
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

    @Transactional(readOnly = true)
    public ScenarioVariablesDto getVariables(Long scenarioBlockId) {

        ScenarioBlockEntity scenarioBlockEntity = scenarioBlockRepo.findById(scenarioBlockId).orElseThrow();
        Collection<VariableDto> previousVariables = variableMapper.map(getPreviousVariables(scenarioBlockEntity));
        Collection<VariableDto> currentVariables = variableMapper.map(scenarioBlockEntity.getBlock().getVariables());

        return new ScenarioVariablesDto(previousVariables, currentVariables,
                scenarioVariableMapper.simpleMap(scenarioVariableRepo.findSimpleVariables(scenarioBlockId)));
    }

    @Transactional
    public void saveVariables(Collection<ScenarioVariableDto> dtos, Long scenarioBlockId) {

        Collection<ScenarioVariableEntity> variables = scenarioVariableRepo.findSimpleVariables(scenarioBlockId);
        Set<Long> dtoVariableIds = dtos.stream().flatMapToLong(scenarioVariableDto ->
                        scenarioVariableDto.getScenarioVariableId() == null ? LongStream.empty() : LongStream.of(scenarioVariableDto.getScenarioVariableId()))
                .boxed().collect(Collectors.toSet());

        Collection<ScenarioVariableEntity> newScenarioVars = new ArrayList<>();
        for (ScenarioVariableDto dto : dtos) {
            if (dto.getScenarioVariableId() == null) {
                newScenarioVars.add(ScenarioVariableFactory.createVar(dto, scenarioBlockId));
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
    public ScenarioVariablesDto getWorkflowExitVariables(Long blockId) {
        BlockEntity blockEntity = blockService.getBlockWithVariables(blockId);
        if (blockEntity.getType() != BlockType.WORKFLOW) {
            throw new IllegalArgumentException("Block is not workflow");
        }

        Optional<ScenarioBlockEntity> scenarioBlockEntityOpt = scenarioBlockRepo.findScenarioBlockForWorkflowExit(blockEntity.getId());
        ScenarioBlockEntity lastScenarioBlock = beginEndRepo.getEndByWorkflow(blockId).getConnectedBlock();

        Collection<VariableDto> inputVariables = Collections.emptyList();

        if (lastScenarioBlock != null && lastScenarioBlock.getBlock() != null){
            inputVariables = variableMapper.map(lastScenarioBlock.getBlock().getVariables());
        }
        if (scenarioBlockEntityOpt.isEmpty()) {
            return new ScenarioVariablesDto(inputVariables, variableMapper.map(blockEntity.getOutVariables()),
                    Collections.emptyList());
        }

        return new ScenarioVariablesDto(inputVariables, variableMapper.map(blockEntity.getOutVariables()),
                scenarioVariableMapper.simpleMap(scenarioVariableRepo.findSimpleVariables(scenarioBlockEntityOpt.get().getId())));

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
                if (beginEnd.getPointType() == BeginEndType.START) {
                    if (!beginEnd.getConnectedBlock().getId().equals(scenarioBlockEntity.getId())) {
                        throw new IllegalStateException("Block not connected");
                    } else {
                        return beginEnd.getWorkflow().getVariables();
                    }
                }
            }
        } else {
            return previousBlock.getBlock().getVariables();
        }
        throw new NoSuchElementException("Variables not found");
    }


}
