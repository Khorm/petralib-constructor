package com.petralib.block;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.block.dto.BlockDto;
import com.petralib.project.dto.ProjectDto;
import com.petralib.test.BaseApiTest;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Автотесты для API управления блоками.
 * Тесты делают реальные HTTP запросы к API и проверяют ответы.
 */
@AutoTest
class BlockRestControllerTest extends BaseApiTest {

//    private String jwtToken;
//    private Long projectId;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        // Получаем JWT токен
//        jwtToken = getJwtToken();
//
//        // Создаем проект для тестов
//        ProjectDto projectDto = new ProjectDto();
//        projectDto.setName("Test Project for Blocks " + System.currentTimeMillis());
//
//        String createResponse = mockMvc.perform(post("/api/v1/project")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(projectDto)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        ProjectDto createdProject = objectMapper.readValue(createResponse, ProjectDto.class);
//        projectId = createdProject.getId();
//    }
//
//    @Test
//    void testGetWorkflowPage_WithValidProjectId_ReturnsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/block/workflow/page")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .param("pageNumber", "0")
//                        .param("name", "")
//                        .param("pageElementsCount", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//    }
//
//    @Test
//    void testGetWorkflowPage_WithoutToken_ReturnsUnauthorized() throws Exception {
//        mockMvc.perform(get("/api/v1/block/workflow/page")
//                        .param("projectId", projectId.toString())
//                        .param("pageNumber", "0")
//                        .param("pageElementsCount", "10"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testGetActionPage_WithValidProjectId_ReturnsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/block/action/page")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .param("pageNumber", "0")
//                        .param("name", "")
//                        .param("pageElementsCount", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//    }
//
//    @Test
//    void testGetSourcePage_WithValidProjectId_ReturnsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/block/source/page")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .param("pageNumber", "0")
//                        .param("name", "")
//                        .param("pageElementsCount", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//    }
//
//    @Test
//    void testGetAcceptedSources_WithValidProjectId_ReturnsOk() throws Exception {
//        mockMvc.perform(get("/api/v1/block/source/acceptedSources")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//    }
//
//    @Test
//    void testSaveWorkflow_WithValidData_ReturnsCreated() throws Exception {
//        BlockDto blockDto = new BlockDto();
//        blockDto.setName("Test Workflow " + System.currentTimeMillis());
//
//        mockMvc.perform(post("/api/v1/block/workflow")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(blockDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value(blockDto.getName()));
//    }
//
//    @Test
//    void testSaveWorkflow_WithoutProjectId_ReturnsBadRequest() throws Exception {
//        BlockDto blockDto = new BlockDto();
//        blockDto.setName("Test Workflow");
//
//        mockMvc.perform(post("/api/v1/block/workflow")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(blockDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testSaveAction_WithValidData_ReturnsCreated() throws Exception {
//        BlockDto blockDto = new BlockDto();
//        blockDto.setName("Test Action " + System.currentTimeMillis());
//
//        mockMvc.perform(post("/api/v1/block/action")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(blockDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value(blockDto.getName()));
//    }
//
//    @Test
//    void testSaveSource_WithValidData_ReturnsCreated() throws Exception {
//        BlockDto blockDto = new BlockDto();
//        blockDto.setName("Test Source " + System.currentTimeMillis());
//
//        mockMvc.perform(post("/api/v1/block/source")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(blockDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.name").value(blockDto.getName()));
//    }
//
//    @Test
//    void testDeleteBlock_WithValidId_ReturnsOk() throws Exception {
//        // Сначала создаем блок для удаления
//        BlockDto blockDto = new BlockDto();
//        blockDto.setName("Block to Delete " + System.currentTimeMillis());
//
//        String createResponse = mockMvc.perform(post("/api/v1/block/workflow")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .param("projectId", projectId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(blockDto)))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        BlockDto createdBlock = objectMapper.readValue(createResponse, BlockDto.class);
//        Long blockId = createdBlock.getId();
//
//        // Удаляем блок
//        mockMvc.perform(delete("/api/v1/block/workflow/" + blockId)
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteBlock_WithoutToken_ReturnsUnauthorized() throws Exception {
//        mockMvc.perform(delete("/api/v1/block/workflow/1"))
//                .andExpect(status().isUnauthorized());
//    }
}

