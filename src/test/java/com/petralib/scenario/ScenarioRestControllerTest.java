package com.petralib.scenario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.scenario.dto.ScenarioDto;
import com.petralib.scenario.dto.ScenarioVariableDto;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoTest
class ScenarioRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetScenarioBlocksForWorkflow() throws Exception {
        mockMvc.perform(get("/api/v1/scenario")
                        .param("workflowId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveScenario() throws Exception {
        ScenarioDto scenarioDto = new ScenarioDto();

        mockMvc.perform(post("/api/v1/scenario")
                        .param("workflowId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scenarioDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetScenarioVariables() throws Exception {
        mockMvc.perform(get("/api/v1/scenario/1/variables"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveScenarioVariables() throws Exception {
        ScenarioVariableDto variableDto = new ScenarioVariableDto();
        variableDto.setType("SIMPLE");

        mockMvc.perform(post("/api/v1/scenario/1/variables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(variableDto))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetWorkflowExitVariables() throws Exception {
        mockMvc.perform(get("/api/v1/scenario/1/variables/exit"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveWorkflowExitVariables() throws Exception {
        ScenarioVariableDto variableDto = new ScenarioVariableDto();
        variableDto.setType("SIMPLE");

        mockMvc.perform(put("/api/v1/scenario/1/variables/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(variableDto))))
                .andExpect(status().isOk());
    }
}

