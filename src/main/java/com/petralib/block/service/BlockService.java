package com.petralib.block.service;

import com.petralib.block.repo.BlockRepository;
import com.petralib.block.enums.BlockType;
import com.petralib.block.dto.BlockDto;
import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.dto.BlockPage;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enitity.VariableEntity;
import com.petralib.project.repository.ProjectRepository;
import com.petralib.scenario.service.ScenarioBlockService;
import com.petralib.type.enums.Multiplicity;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlockService {

    BlockRepository blockRepository;
    ProjectRepository projectRepository;
    BlockMapper blockMapper;
    ScenarioBlockService scenarioBlockService;

    @Transactional
    public BlockEntity save(BlockDto dto,Long projectId, BlockType blockType) {
        BlockEntity blockEntity = blockMapper.fromDtoToEntity(dto);
        blockEntity.setType(blockType);

        blockEntity.setProject(projectRepository.getReferenceById(projectId));

        Set<String> namesSet = new HashSet<>();
        for (VariableEntity variable : blockEntity.getVariables()) {
            System.out.println(variable);
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
        blockRepository.deleteById(blockId);
    }

    @Transactional(readOnly = true)
    public Collection<BlockEntity> getSourcesWithAcceptableReturnType(Long returnTypeId, Multiplicity multiplicity, Long projectId){
        return blockRepository.findSourcesWithMultiplicityAndTypeRetVariable(multiplicity, returnTypeId, projectId);
    }


}
