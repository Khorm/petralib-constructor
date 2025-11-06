package com.petralib.ctype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.ctype.dto.TypeFullDto;
import com.petralib.ctype.dto.TypePage;
import com.petralib.ctype.entity.CTypeEntity;
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
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class TypeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TypeService typeService;

    private TypeFullDto testTypeDto;
    private TypePage testTypePage;

    @BeforeEach
    void setUp() {
        testTypeDto = new TypeFullDto();
        testTypeDto.setId(1L);
        testTypeDto.setName("Test Type");
        testTypeDto.setDescription("Test Description");

        List<TypeFullDto> types = new ArrayList<>();
        types.add(testTypeDto);
        testTypePage = new TypePage(1, types);
    }

    @Test
    void testGetTypesPage() throws Exception {
        // Arrange
        when(typeService.getTypesPage(anyInt(), anyInt(), anyLong(), anyString()))
            .thenReturn(testTypePage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/type/page")
                .param("projectId", "1")
                .param("pageNumber", "1")
                .param("name", "")
                .param("pageElementsCount", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageCount").value(1))
            .andExpect(jsonPath("$.blocks").isArray());
    }

    @Test
    void testGetTypesPage_WithNameFilter() throws Exception {
        // Arrange
        when(typeService.getTypesPage(anyInt(), anyInt(), anyLong(), eq("Test")))
            .thenReturn(testTypePage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/type/page")
                .param("projectId", "1")
                .param("pageNumber", "1")
                .param("name", "Test")
                .param("pageElementsCount", "10"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetTypes() throws Exception {
        // Arrange
        Collection<com.petralib.ctype.dto.CTypeShortDto> types = new ArrayList<>();
        when(typeService.getAllTypes(anyLong()))
            .thenReturn(types);

        // Act & Assert
        mockMvc.perform(get("/api/v1/type")
                .param("projectId", "1"))
            .andExpect(status().isOk());
    }

    @Test
    void testSave_ValidType() throws Exception {
        // Arrange
        CTypeEntity savedEntity = new CTypeEntity();
        savedEntity.setId(1L);
        savedEntity.setName(testTypeDto.getName());
        when(typeService.save(any(TypeFullDto.class), anyLong()))
            .thenReturn(savedEntity);

        // Act & Assert
        mockMvc.perform(post("/api/v1/type")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTypeDto)))
            .andExpect(status().isOk());
    }

    @Test
    void testSave_InvalidType() throws Exception {
        // Arrange
        TypeFullDto invalidType = new TypeFullDto();
        invalidType.setName(""); // Empty name should fail validation

        // Act & Assert
        mockMvc.perform(post("/api/v1/type")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidType)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSave_DuplicateName() throws Exception {
        // Arrange
        when(typeService.save(any(TypeFullDto.class), anyLong()))
            .thenThrow(new IllegalArgumentException("Type with same name already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/type")
                .param("projectId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTypeDto)))
            .andExpect(status().isNotAcceptable());
    }

    @Test
    void testDelete() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/type/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetTypeFields() throws Exception {
        // Arrange
        Collection<com.petralib.ctype.dto.CTypeFieldDto> fields = new ArrayList<>();
        when(typeService.getTypeVariables(anyLong()))
            .thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/api/v1/type/fields/1"))
            .andExpect(status().isOk());
    }
}
