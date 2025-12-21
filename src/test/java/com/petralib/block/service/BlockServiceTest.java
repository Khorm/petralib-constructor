package com.petralib.block.service;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.enums.BlockType;
import com.petralib.block.mapper.BlockMapper;
import com.petralib.block.repo.BlockRepository;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//@AutoTest
class BlockServiceTest {

//    @Mock
//    private BlockRepository blockRepository;
//
//    @Mock
//    private BlockMapper blockMapper;
//
//    @InjectMocks
//    private BlockService blockService;
//
//    private BlockDto blockDto;
//
//    @BeforeEach
//    void setUp() {
//        blockDto = new BlockDto();
//        blockDto.setName("Test Block");
//    }
//
//    @Test
//    void testSaveBlock() {
//        // Given
//        Long projectId = 1L;
//        BlockType blockType = BlockType.WORKFLOW;
//
//        // When
//        blockService.save(blockDto, projectId, blockType);
//
//        // Then
//        verify(blockRepository, atLeastOnce()).save(any());
//    }
//
//    @Test
//    void testGetBlocksByProjectAndName() {
//        // Given
//        Long projectId = 1L;
//        Integer pageNumber = 0;
//        String name = "test";
//        Integer pageElementsCount = 10;
//        BlockType blockType = BlockType.WORKFLOW;
//
//        // When
//        var result = blockService.getBlocksByProjectAndName(pageElementsCount, pageNumber, projectId, name, blockType);
//
//        // Then
//        assertNotNull(result);
//    }
//
//    @Test
//    void testDeleteBlock() {
//        // Given
//        Long blockId = 1L;
//
//        // When
//        blockService.deleteBlock(blockId);
//
//        // Then
//        verify(blockRepository).deleteById(blockId);
//    }
}

