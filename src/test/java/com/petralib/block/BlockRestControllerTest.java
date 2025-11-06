package com.petralib.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.BlockPage;
import com.petralib.block.dto.VariableDto;
import com.petralib.block.enums.BlockType;
import com.petralib.block.service.BlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class BlockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlockService blockService;

    private BlockDto testBlockDto;
    private BlockPage testBlockPage;

    @BeforeEach
    void setUp() {
        testBlockDto = new BlockDto();
        testBlockDto.setId(1L);
        testBlockDto.setName("Test Block");
        testBlockDto.setDescription("Test Description");
        testBlockDto.setType(BlockType.ACTION);
        testBlockDto.setVariables(new ArrayList<>());

        List<BlockDto> blocks = new ArrayList<>();
        blocks.add(testBlockDto);
        testBlockPage = new BlockPage(1, blocks);
    }

    @Test
    void testGetWorkflowPage() throws Exception {
        // Arrange
        when(blockService.getBlocksByProjectAndName(anyInt(), anyInt(), anyLong(), anyString(), eq(BlockType.WORKFLOW)))
            .thenReturn(testBlockPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/block/workflow/page")
                .param("projectId", "1")
                .param("pageNumber", "1")
                .param("name", "")
                .param("pageElementsCount", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageCount").value(1))
            .andExpect(jsonPath("$.blocks").isArray());
    }

    @Test
    void testGetActionPage() throws Exception {
        // Arrange
        when(blockService.getBlocksByProjectAndName(anyInt(), anyInt(), anyLong(), anyString(), eq(BlockType.ACTION)))
            .thenReturn(testBlockPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/block/action/page")
                .param("projectId", "1")
                .param("pageNumber", "1")
                .param("name", "Test")
                .param("pageElementsCount", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageCount").value(1));
    }

    @Test
    void testGetSourcePage() throws Exception {
        // Arrange
        when(blockService.getBlocksByProjectAndName(anyInt(), anyInt(), anyLong(), anyString(), eq(BlockType.SOURCE)))
            .thenReturn(testBlockPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/block/source/page")
                .param("projectId", "1")
                .param("pageNumber", "1")
                .param("name", "")
                .param("pageElementsCount", "10"))
            .andExpect(status().isOk());
    }

    @Test
    void testSaveAction_ValidBlock() throws Exception {
        // Arrange
        when(blockService.save(any(BlockDto.class), anyLong(), eq(BlockType.ACTION)))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/block/action")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBlockDto)))
            .andExpect(status().isOk());
    }

    @Test
    void testSaveAction_InvalidBlock() throws Exception {
        // Arrange
        BlockDto invalidBlock = new BlockDto();
        invalidBlock.setName(""); // Empty name should fail validation

        // Act & Assert
        mockMvc.perform(post("/api/v1/block/action")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBlock)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveWorkflow_ValidBlock() throws Exception {
        // Arrange
        testBlockDto.setType(BlockType.WORKFLOW);
        when(blockService.save(any(BlockDto.class), anyLong(), eq(BlockType.WORKFLOW)))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/block/workflow")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBlockDto)))
            .andExpect(status().isOk());
    }

    @Test
    void testSaveSource_ValidBlock() throws Exception {
        // Arrange
        testBlockDto.setType(BlockType.SOURCE);
        when(blockService.save(any(BlockDto.class), anyLong(), eq(BlockType.SOURCE)))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/v1/block/source")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBlockDto)))
            .andExpect(status().isOk());
    }

    @Test
    void testDeleteBlock() throws Exception {
        // Arrange
        // No need to mock anything for delete

        // Act & Assert
        mockMvc.perform(delete("/api/v1/block/action/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetSourceById() throws Exception {
        // Arrange
        when(blockService.getBlockWithVariables(1L))
            .thenReturn(null);
        when(blockService.getBlockWithVariables(anyLong()))
            .thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/block/source/1"))
            .andExpect(status().isOk());
    }
}
