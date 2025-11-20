package com.petralib.ctype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.ctype.dto.TypeFullDto;
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
class TypeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetTypesPage() throws Exception {
        mockMvc.perform(get("/api/v1/type/page")
                        .param("projectId", "1")
                        .param("pageNumber", "0")
                        .param("name", "")
                        .param("pageElementsCount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetAllTypes() throws Exception {
        mockMvc.perform(get("/api/v1/type")
                        .param("projectId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSaveType() throws Exception {
        TypeFullDto typeDto = new TypeFullDto();
        typeDto.setName("Test Type");

        mockMvc.perform(post("/api/v1/type")
                        .param("projectId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testDeleteType() throws Exception {
        mockMvc.perform(delete("/api/v1/type/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetTypeFields() throws Exception {
        mockMvc.perform(get("/api/v1/type/fields/1"))
                .andExpect(status().isOk());
    }
}

