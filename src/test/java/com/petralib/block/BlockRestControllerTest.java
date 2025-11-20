package com.petralib.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.block.dto.BlockDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlockRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetWorkflowPage() throws Exception {
        mockMvc.perform(get("/api/v1/block/workflow/page")
                        .param("projectId", "1")
                        .param("pageNumber", "0")
                        .param("name", "")
                        .param("pageElementsCount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetActionPage() throws Exception {
        mockMvc.perform(get("/api/v1/block/action/page")
                        .param("projectId", "1")
                        .param("pageNumber", "0")
                        .param("name", "")
                        .param("pageElementsCount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetSourcePage() throws Exception {
        mockMvc.perform(get("/api/v1/block/source/page")
                        .param("projectId", "1")
                        .param("pageNumber", "0")
                        .param("name", "")
                        .param("pageElementsCount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetAcceptedSources() throws Exception {
        mockMvc.perform(get("/api/v1/block/source/acceptedSources")
                        .param("projectId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetSourceById() throws Exception {
        mockMvc.perform(get("/api/v1/block/source/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveWorkflow() throws Exception {
        BlockDto blockDto = new BlockDto();
        blockDto.setName("Test Workflow");

        mockMvc.perform(post("/api/v1/block/workflow")
                        .param("projectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveAction() throws Exception {
        BlockDto blockDto = new BlockDto();
        blockDto.setName("Test Action");

        mockMvc.perform(post("/api/v1/block/action")
                        .param("projectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveSource() throws Exception {
        BlockDto blockDto = new BlockDto();
        blockDto.setName("Test Source");

        mockMvc.perform(post("/api/v1/block/source")
                        .param("projectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteBlock() throws Exception {
        mockMvc.perform(delete("/api/v1/block/workflow/1"))
                .andExpect(status().isOk());
    }
}

