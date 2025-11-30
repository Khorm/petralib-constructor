package com.petralib.project;

import com.petralib.project.dto.ProjectDto;
import com.petralib.test.BaseApiTest;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Автотесты для API управления проектами.
 * Тесты делают реальные HTTP запросы к API и проверяют ответы.
 */
@AutoTest
class ProjectRestControllerTest extends BaseApiTest {

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        // Получаем реальный JWT токен через API login
        jwtToken = getJwtToken();
    }

    @Test
    void testGetProjects_WithValidToken_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/project")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void testGetProjects_WithoutToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCurrentUser_WithValidToken_ReturnsUserData() throws Exception {
        mockMvc.perform(get("/api/v1/project/current-user")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    void testGetCurrentUser_WithoutToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/project/current-user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateProject_WithValidData_ReturnsCreated() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Auto Test Project " + System.currentTimeMillis());
        projectDto.setDescription("Test Description");

        mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(projectDto.getName()))
                .andExpect(jsonPath("$.description").value(projectDto.getDescription()));
    }

    @Test
    void testCreateProject_WithEmptyName_ReturnsBadRequest() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("");
        projectDto.setDescription("Test Description");

        mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProject_WithoutToken_ReturnsUnauthorized() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Test Project");

        mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteProject_WithValidId_ReturnsOk() throws Exception {
        // Сначала создаем проект для удаления
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project to Delete " + System.currentTimeMillis());

        String createResponse = mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Извлекаем ID созданного проекта
        ProjectDto createdProject = objectMapper.readValue(createResponse, ProjectDto.class);
        Long projectId = createdProject.getId();

        // Удаляем проект
        mockMvc.perform(delete("/api/v1/project/" + projectId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProject_WithoutToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/project/1"))
                .andExpect(status().isUnauthorized());
    }
}

