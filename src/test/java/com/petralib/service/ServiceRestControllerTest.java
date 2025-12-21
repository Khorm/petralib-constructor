package com.petralib.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.service.dto.ServiceDto;
import com.petralib.test.annotation.AutoTest;
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

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@AutoTest
class ServiceRestControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    void testGetServicePage() throws Exception {
//        mockMvc.perform(get("/api/v1/service/page")
//                        .param("projectId", "1")
//                        .param("pageNumber", "0")
//                        .param("name", "")
//                        .param("pageElementsCount", "10"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void testGetServices() throws Exception {
//        mockMvc.perform(get("/api/v1/service")
//                        .param("projectId", "1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void testGetServiceById() throws Exception {
//        mockMvc.perform(get("/api/v1/service/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void testSaveService() throws Exception {
//        ServiceDto serviceDto = new ServiceDto();
//        serviceDto.setName("Test Service");
//
//        mockMvc.perform(post("/api/v1/service")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(serviceDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void testDeleteService() throws Exception {
//        mockMvc.perform(delete("/api/v1/service/1"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    void testDownloadServiceFile() throws Exception {
//        mockMvc.perform(get("/api/v1/service/file/1"))
//                .andExpect(status().isOk())
//                .andExpect(header().exists("Content-Disposition"));
//    }
}

