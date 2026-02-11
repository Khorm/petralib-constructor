package com.petralib.block.service;

import com.petralib.block.dto.VariableDto;
import com.petralib.block.mapper.VariableMapper;
import com.petralib.block.repo.BlockRepository;
import com.petralib.block.enums.BlockType;
import com.petralib.block.dto.BlockDto;
import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.dto.BlockPage;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.project.repository.ProjectRepository;
import com.petralib.scenario.repo.ScenarioVariableRepo;
import com.petralib.scenario.service.ScenarioBlockService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlockService {

    BlockRepository blockRepository;
    ProjectRepository projectRepository;
    BlockMapper blockMapper;
    VariableMapper variableMapper;
    ScenarioBlockService scenarioBlockService;
    ScenarioVariableRepo scenarioVariableRepo;

    @Transactional
    public BlockEntity save(BlockDto dto,Long projectId, BlockType blockType) {
        dto.getVariables().forEach(variable -> System.out.println("SAVE VAR : " + variable.getName() + " " + variable.getVariableType().getId()));
        BlockEntity blockEntity = blockMapper.fromDtoToEntity(dto);
        blockEntity.setType(blockType);

        blockEntity.setProject(projectRepository.getReferenceById(projectId));

        Set<String> namesSet = new HashSet<>();
        for (VariableEntity variable : blockEntity.getVariables()) {

            variable.setBlock(blockEntity);
            if (!namesSet.contains(variable.getName())){
                namesSet.add(variable.getName());
            }else {
                throw new IllegalArgumentException("Same variable name is not allowed");
            }
        }
        blockEntity = blockRepository.save(blockEntity);

        if (blockType == BlockType.WORKFLOW){
            scenarioBlockService.createStartEnd(blockEntity);
        }

        return blockEntity;
    }

    @Transactional(readOnly = true)
    public BlockEntity getBlockWithVariables(Long blockId){
        Optional<BlockEntity> blockEntity = blockRepository.findById(blockId);
        if (blockEntity.isPresent()){
            return blockEntity.get();
        }else {
            throw new NullPointerException("Block not found");
        }
    }

    @Transactional(readOnly = true)
    public BlockPage getBlocksByProjectAndName(int pageSize, int lastPageNumber, Long projectId, String blockName, BlockType blockType) {
        Page<BlockEntity> blockEntities;
        if (null != blockName && !blockName.isBlank()){
            blockEntities = blockRepository.findBlocksByName(projectId, blockName, blockType,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }else {
            blockEntities = blockRepository.findBlocks(projectId, blockType,
                    PageRequest.of(lastPageNumber - 1, pageSize, Sort.by("name")));
        }
        Collection<BlockDto> dtos = blockMapper.map(blockEntities.toList());
        return new BlockPage(blockEntities.getTotalPages(), dtos);
    }

    @Transactional
    public void deleteBlock(Long blockId){
        for (VariableEntity variable : blockRepository.findById(blockId).get().getVariables()){
            scenarioVariableRepo.deleteByVariable(variable.getId());
        }
        blockRepository.deleteById(blockId);
    }

    @Transactional(readOnly = true)
    public Collection<BlockEntity> getSourcesByName(Long projectId){
//        Collection<BlockEntity> blockEntities = blockRepository.findSourcesByNameLike(projectId, name);
//        if (blockEntities.size() > 15){
//            return Collections.emptyList();
//        }
        return blockRepository.findBlocksByType(projectId, BlockType.SOURCE);
    }


}
