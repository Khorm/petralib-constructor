package com.petralib.block.service;

import com.petralib.block.BlockRepository;
import com.petralib.block.BlockType;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.project.service.ProjectService;
import com.petralib.variable.entity.VariableEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlockService {

    BlockRepository blockRepository;
    ProjectService projectService;

    @Transactional
    public BlockEntity saveBlock(BlockEntity blockEntity, BlockType blockType, Long projectId) {
        blockEntity.setType(blockType);
        blockEntity.setProject(projectService.getProject(projectId));
        for(VariableEntity variableEntity : blockEntity.getVariables()){
            variableEntity.setBlock(blockEntity);
        }
        return blockRepository.save(blockEntity);
    }

    @Transactional
    public BlockEntity getBlockWithVariables(Long blockId){
        Optional<BlockEntity> blockEntity = blockRepository.findById(blockId);
        if (blockEntity.isPresent()){
            return blockEntity.get();
        }else {
            throw new NullPointerException("Block not found");
        }
    }

    @Transactional
    public void deleteBlock(Long blockId){
        blockRepository.deleteById(blockId);
    }
}
