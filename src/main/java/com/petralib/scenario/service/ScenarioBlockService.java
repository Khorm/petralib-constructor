package com.petralib.scenario.service;

import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.repo.BlockRepository;
import com.petralib.scenario.dto.ScenarioDto;
import com.petralib.scenario.entity.BeginEndEntity;
import com.petralib.scenario.entity.ScenarioBlockEntity;
import com.petralib.scenario.enums.BeginEndType;
import com.petralib.scenario.mapper.BeginEndMapper;
import com.petralib.scenario.mapper.ScenarioBlockMapper;
import com.petralib.scenario.repo.BeginEndRepo;
import com.petralib.scenario.repo.ScenarioBlockRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ScenarioBlockService {

    ScenarioBlockRepo scenarioBlockRepo;
    ScenarioBlockMapper scenarioBlockMapper;
    BeginEndMapper beginEndMapper;
    BlockRepository blockRepository;
    BeginEndRepo beginEndRepo;

    @Transactional(readOnly = true)
    public ScenarioDto getScenarioBlocks(Long workflowId) {
        Collection<ScenarioBlockEntity> scenarioBlockEntities = scenarioBlockRepo.findScenarioBlocksByWorkflow(workflowId);
        List<BeginEndEntity> beginEndEntities = beginEndRepo.getStartEndByWorkflow(workflowId);
        return new ScenarioDto(beginEndMapper.map(beginEndEntities), scenarioBlockMapper.mapEntity(scenarioBlockEntities));
    }

    @Transactional
    public void saveScenario(ScenarioDto dto, Long workflowId) {
        List<ScenarioBlockEntity> entities = scenarioBlockMapper.mapDto(dto.getScenarioBlocks());
        entities.forEach(entity -> entity.setParentWorkflow(blockRepository.getReferenceById(workflowId)));
        Set<Long> existingIds = entities.stream().flatMap((Function<ScenarioBlockEntity, Stream<Long>>) scenarioBlockEntity -> {
            if (scenarioBlockEntity.getId() == null) {
                return Stream.empty();
            } else {
                return Stream.of(scenarioBlockEntity.getId());
            }
        }).collect(Collectors.toSet());
        for (ScenarioBlockEntity scenarioBlockEntity : scenarioBlockRepo.findScenarioBlocksByWorkflow(workflowId)) {
            if (!existingIds.contains(scenarioBlockEntity.getId())) {
                scenarioBlockRepo.deleteById(scenarioBlockEntity.getId());
            }
        }
        scenarioBlockRepo.saveAll(entities);

        List<BeginEndEntity> beginEndEntities = beginEndMapper.mapDto(dto.getBeginEndDtoList());
        beginEndRepo.saveAll(beginEndEntities);
    }


    public void createStartEnd(BlockEntity workflow) {
        if (!beginEndRepo.getStartEndByWorkflow(workflow.getId()).isEmpty()) {
            return;
        }
        BeginEndEntity start = new BeginEndEntity();
        start.setX(100L);
        start.setY(100L);
        start.setPointType(BeginEndType.START);
        start.setWorkflow(workflow);

        BeginEndEntity end = new BeginEndEntity();
        end.setX(300L);
        end.setY(100L);
        end.setPointType(BeginEndType.END);
        end.setWorkflow(workflow);

        //сценанрий блок является конечным у воркфлоу если у него парент является воркфлоу и сам блок является воркфлоу
        ScenarioBlockEntity scenarioBlockEntity = new ScenarioBlockEntity();
        scenarioBlockEntity.setParentWorkflow(workflow);
        scenarioBlockEntity.setBlock(workflow);
        scenarioBlockEntity.setX(0L);
        scenarioBlockEntity.setY(0L);
        scenarioBlockRepo.save(scenarioBlockEntity);

        beginEndRepo.save(start);
        beginEndRepo.save(end);
    }

    @Transactional(readOnly = true)
    public ScenarioBlockEntity getExitWorkflowScenarioBlock(Long workflowId){
        return scenarioBlockRepo.findScenarioBlockForWorkflowExit(workflowId).get();
    }
}
