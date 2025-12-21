package com.petralib.auth.rest;

import com.petralib.test.BaseApiTest;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Автотесты для API аутентификации.
 * Тесты делают реальные HTTP запросы к API и проверяют ответы.
 */
//@AutoTest
class AuthControllerTest extends BaseApiTest {

//    private String jwtToken;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        // Получаем токен для тестов logout
//        jwtToken = getJwtToken();
//    }
//
//    @Test
//    void testLogin_WithValidCredentials_ReturnsToken() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", TEST_EMAIL,
//                "password", TEST_PASSWORD
//        );
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
//                .andExpect(jsonPath("$.token").exists())
//                .andExpect(jsonPath("$.token").isString())
//                .andExpect(jsonPath("$.token").isNotEmpty());
//    }
//
//    @Test
//    void testLogin_WithInvalidEmail_ReturnsForbidden() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", "invalid@example.com",
//                "password", TEST_PASSWORD
//        );
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void testLogin_WithInvalidPassword_ReturnsForbidden() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", TEST_EMAIL,
//                "password", "wrongpassword"
//        );
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void testLogin_WithEmptyEmail_ReturnsBadRequest() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", "",
//                "password", TEST_PASSWORD
//        );
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testLogin_WithEmptyPassword_ReturnsBadRequest() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", TEST_EMAIL,
//                "password", ""
//        );
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void testLogout_WithValidToken_ReturnsOk() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/logout")
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testLogout_WithoutToken_ReturnsUnauthorized() throws Exception {
//        mockMvc.perform(post("/api/v1/auth/logout"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void testLogin_ResponseContainsValidJwtToken() throws Exception {
//        Map<String, String> request = Map.of(
//                "email", TEST_EMAIL,
//                "password", TEST_PASSWORD
//        );
//
//        String response = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        // Проверяем, что токен можно использовать для авторизованных запросов
//        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
//        String token = (String) responseMap.get("token");
//
//        // Пробуем использовать токен для запроса к защищенному endpoint
//        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/project")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk());
//    }
}

