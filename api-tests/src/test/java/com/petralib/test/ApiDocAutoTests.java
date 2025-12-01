package com.petralib.test;

import com.petralib.block.dto.BlockDto;
import com.petralib.block.dto.VariableDto;
import com.petralib.ctype.dto.CTypeShortDto;
import com.petralib.ctype.dto.TypeFullDto;
import com.petralib.project.dto.ProjectDto;
import com.petralib.service.dto.ServiceDto;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты по документации API (auth/projects/services/types/blocks)
 * на тестовом профиле (H2, тестовый пользователь из TestDataInitializer).
 */
@AutoTest
class ApiDocAutoTests extends BaseApiTest {

    @Test
    @DisplayName("Auth: успешный логин возвращает email и token")
    void authSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(TEST_EMAIL, TEST_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.token", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("Auth: неверный пароль дает 403")
    void authForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(TEST_EMAIL, "wrong-password"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Projects: создание и получение списка")
    void createProjectAndList() throws Exception {
        String token = getJwtToken();

        ProjectDto request = new ProjectDto();
        request.setName("Doc Project");
        request.setDescription("Created from API doc test");

        mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Doc Project"));

        mockMvc.perform(get("/api/v1/project")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Doc Project")));
    }

    @Test
    @DisplayName("Projects: current-user возвращает id и email")
    void currentUserReturnsIdAndEmail() throws Exception {
        String token = getJwtToken();

        mockMvc.perform(get("/api/v1/project/current-user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    @DisplayName("Services: создание и получение списка по projectId")
    void createServiceAndList() throws Exception {
        String token = getJwtToken();
        Long createdProjectId = createProject(token, "Service Project");

        ServiceDto service = new ServiceDto();
        service.setName("Doc Service");
        service.setDescription("From doc test");
        service.setProjectId(createdProjectId);
        service.setPath("/doc/test");

        String serviceJson = mockMvc.perform(post("/api/v1/service")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.projectId").value(createdProjectId))
                .andExpect(jsonPath("$.name").value("Doc Service"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdServiceId = objectMapper.readTree(serviceJson).get("id").asLong();

        mockMvc.perform(get("/api/v1/service/{id}", createdServiceId)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdServiceId))
                .andExpect(jsonPath("$.name").value("Doc Service"));

        mockMvc.perform(get("/api/v1/service")
                        .param("projectId", createdProjectId.toString())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));
    }

    @Test
    @DisplayName("Types: создание и листинг по projectId")
    void createTypeAndList() throws Exception {
        String token = getJwtToken();
        Long projectId = createProject(token, "Type Project");

        TypeFullDto type = new TypeFullDto();
        type.setName("DocType");
        type.setDescription("From doc test");
        type.setVariables(Collections.emptyList());

        String typeJson = mockMvc.perform(post("/api/v1/type")
                        .param("projectId", projectId.toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(type)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long typeId = objectMapper.readTree(typeJson).get("id").asLong();

        mockMvc.perform(get("/api/v1/type")
                        .param("projectId", projectId.toString())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(typeId.intValue())));

        mockMvc.perform(get("/api/v1/type/page")
                        .param("projectId", projectId.toString())
                        .param("pageNumber", "1")
                        .param("pageElementsCount", "10")
                        .param("name", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Blocks: создание workflow и получение страницы")
    void createBlockAndList() throws Exception {
        String token = getJwtToken();
        Long projectId = createProject(token, "Block Project");
        Long serviceId = createService(token, projectId, "Block Service");
        Long typeId = createType(token, projectId, "BlockType");

        BlockDto block = new BlockDto();
        block.setName("Workflow Doc");
        block.setDescription("From doc test");
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(serviceId);
        block.setService(serviceDto);

        VariableDto variable = new VariableDto();
        variable.setName("input");
        variable.setDescription("input var");
        variable.setMultiplicity("SINGLE");
        variable.setPinType("IN");
        CTypeShortDto varType = new CTypeShortDto();
        varType.setId(typeId);
        variable.setVariableType(varType);
        block.setVariables(List.of(variable));

        mockMvc.perform(post("/api/v1/block/workflow")
                        .param("projectId", projectId.toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(block)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Workflow Doc"));

        mockMvc.perform(get("/api/v1/block/workflow/page")
                        .param("projectId", projectId.toString())
                        .param("pageNumber", "1")
                        .param("pageElementsCount", "10")
                        .param("name", "")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocks[*].name", hasItem("Workflow Doc")));
    }

    private record LoginRequest(String email, String password) {}

    private Long createProject(String token, String name) throws Exception {
        ProjectDto project = new ProjectDto();
        project.setName(name);
        project.setDescription(name + " desc");

        String projectJson = mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(projectJson).get("id").asLong();
    }

    private Long createService(String token, Long projectId, String name) throws Exception {
        ServiceDto service = new ServiceDto();
        service.setName(name);
        service.setDescription(name + " desc");
        service.setProjectId(projectId);
        service.setPath("/" + name.toLowerCase().replace(" ", "-"));

        String serviceJson = mockMvc.perform(post("/api/v1/service")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(service)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(serviceJson).get("id").asLong();
    }

    private Long createType(String token, Long projectId, String name) throws Exception {
        TypeFullDto type = new TypeFullDto();
        type.setName(name);
        type.setDescription(name + " desc");
        type.setVariables(Collections.emptyList());

        String typeJson = mockMvc.perform(post("/api/v1/type")
                        .param("projectId", projectId.toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(type)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(typeJson).get("id").asLong();
    }
}
