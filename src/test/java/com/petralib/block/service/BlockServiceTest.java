package com.petralib.block.service;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.VariableDto;
import com.petralib.block.enitity.BlockEntity;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.repo.BlockRepository;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.project.entity.ProjectEntity;
import com.petralib.project.repository.ProjectRepository;
import com.petralib.scenario.repo.ScenarioVariableRepo;
import com.petralib.scenario.service.ScenarioBlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private BlockMapper blockMapper;

    @Mock
    private ScenarioBlockService scenarioBlockService;

    @Mock
    private ScenarioVariableRepo scenarioVariableRepo;

    @InjectMocks
    private BlockService blockService;

    private BlockDto testBlockDto;
    private BlockEntity testBlockEntity;
    private ProjectEntity testProject;

    @BeforeEach
    void setUp() {
        testProject = new ProjectEntity();
        testProject.setId(1L);

        testBlockDto = new BlockDto();
        testBlockDto.setId(1L);
        testBlockDto.setName("Test Block");
        testBlockDto.setDescription("Test Description");
        testBlockDto.setType(BlockType.ACTION);
        
        VariableDto variable = new VariableDto();
        variable.setName("testVar");
        CTypeShortDto typeDto = new CTypeShortDto();
        typeDto.setId(1L);
        variable.setVariableType(typeDto);
        testBlockDto.setVariables(List.of(variable));

        testBlockEntity = new BlockEntity();
        testBlockEntity.setId(1L);
        testBlockEntity.setName("Test Block");
        testBlockEntity.setType(BlockType.ACTION);
    }

    @Test
    void testSave_ActionBlock_Success() {
        // Arrange
        when(blockMapper.fromDtoToEntity(any(BlockDto.class))).thenReturn(testBlockEntity);
        when(projectRepository.getReferenceById(anyLong())).thenReturn(testProject);
        when(blockRepository.save(any(BlockEntity.class))).thenReturn(testBlockEntity);

        // Act
        BlockEntity result = blockService.save(testBlockDto, 1L, BlockType.ACTION);

        // Assert
        assertNotNull(result);
        verify(blockRepository, times(1)).save(any(BlockEntity.class));
        verify(scenarioBlockService, never()).createStartEnd(any(BlockEntity.class));
    }

    @Test
    void testSave_WorkflowBlock_CreatesStartEnd() {
        // Arrange
        testBlockDto.setType(BlockType.WORKFLOW);
        testBlockEntity.setType(BlockType.WORKFLOW);
        when(blockMapper.fromDtoToEntity(any(BlockDto.class))).thenReturn(testBlockEntity);
        when(projectRepository.getReferenceById(anyLong())).thenReturn(testProject);
        when(blockRepository.save(any(BlockEntity.class))).thenReturn(testBlockEntity);

        // Act
        BlockEntity result = blockService.save(testBlockDto, 1L, BlockType.WORKFLOW);

        // Assert
        assertNotNull(result);
        verify(blockRepository, times(1)).save(any(BlockEntity.class));
        verify(scenarioBlockService, times(1)).createStartEnd(any(BlockEntity.class));
    }

    @Test
    void testSave_DuplicateVariableNames_ThrowsException() {
        // Arrange
        VariableDto var1 = new VariableDto();
        var1.setName("duplicate");
        CTypeShortDto type1 = new CTypeShortDto();
        type1.setId(1L);
        var1.setVariableType(type1);

        VariableDto var2 = new VariableDto();
        var2.setName("duplicate"); // Same name
        CTypeShortDto type2 = new CTypeShortDto();
        type2.setId(2L);
        var2.setVariableType(type2);

        testBlockDto.setVariables(List.of(var1, var2));
        
        when(blockMapper.fromDtoToEntity(any(BlockDto.class))).thenReturn(testBlockEntity);
        when(projectRepository.getReferenceById(anyLong())).thenReturn(testProject);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            blockService.save(testBlockDto, 1L, BlockType.ACTION);
        });
    }

    @Test
    void testGetBlockWithVariables_Success() {
        // Arrange
        when(blockRepository.findById(1L)).thenReturn(Optional.of(testBlockEntity));

        // Act
        BlockEntity result = blockService.getBlockWithVariables(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBlockEntity, result);
    }

    @Test
    void testGetBlockWithVariables_NotFound_ThrowsException() {
        // Arrange
        when(blockRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            blockService.getBlockWithVariables(1L);
        });
    }

    @Test
    void testGetBlocksByProjectAndName_WithNameFilter() {
        // Arrange
        List<BlockEntity> blocks = List.of(testBlockEntity);
        Page<BlockEntity> page = new PageImpl<>(blocks);
        when(blockRepository.findBlocksByName(eq(1L), eq("Test"), eq(BlockType.ACTION), any(PageRequest.class)))
            .thenReturn(page);
        when(blockMapper.map(anyList())).thenReturn(List.of(testBlockDto));

        // Act
        var result = blockService.getBlocksByProjectAndName(10, 1, 1L, "Test", BlockType.ACTION);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPageCount());
        verify(blockRepository, times(1)).findBlocksByName(anyLong(), anyString(), any(BlockType.class), any(PageRequest.class));
    }

    @Test
    void testGetBlocksByProjectAndName_WithoutNameFilter() {
        // Arrange
        List<BlockEntity> blocks = List.of(testBlockEntity);
        Page<BlockEntity> page = new PageImpl<>(blocks);
        when(blockRepository.findBlocks(eq(1L), eq(BlockType.ACTION), any(PageRequest.class)))
            .thenReturn(page);
        when(blockMapper.map(anyList())).thenReturn(List.of(testBlockDto));

        // Act
        var result = blockService.getBlocksByProjectAndName(10, 1, 1L, "", BlockType.ACTION);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPageCount());
        verify(blockRepository, times(1)).findBlocks(anyLong(), any(BlockType.class), any(PageRequest.class));
    }

    @Test
    void testGetSourcesByName() {
        // Arrange
        Collection<BlockEntity> sources = List.of(testBlockEntity);
        when(blockRepository.findBlocksByType(1L, BlockType.SOURCE)).thenReturn(sources);

        // Act
        Collection<BlockEntity> result = blockService.getSourcesByName(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blockRepository, times(1)).findBlocksByType(anyLong(), eq(BlockType.SOURCE));
    }

    @Test
    void testDeleteBlock() {
        // Arrange
        when(blockRepository.findById(1L)).thenReturn(Optional.of(testBlockEntity));

        // Act
        blockService.deleteBlock(1L);

        // Assert
        verify(scenarioVariableRepo, atLeastOnce()).deleteByVariable(anyLong());
        verify(blockRepository, times(1)).deleteById(1L);
    }
}
