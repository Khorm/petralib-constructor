package com.petralib.test.smoke;

import com.petralib.test.BaseApiTest;
import com.petralib.test.annotation.AutoTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Smoke тесты (дымовые тесты).
 * 
 * Быстрые базовые тесты для проверки критически важной функциональности.
 * Эти тесты должны проходить, чтобы приложение считалось работоспособным.
 * 
 * Smoke тесты проверяют:
 * - Приложение запускается
 * - API доступно
 * - Аутентификация работает
 * - Основные endpoints отвечают
 */
@AutoTest
@DisplayName("Smoke Tests - Критически важная функциональность")
class SmokeTest extends BaseApiTest {

    @Test
    @DisplayName("ST001: Приложение запускается и API доступно")
    void testApplicationIsRunning() throws Exception {
        // Проверяем, что можем сделать запрос к API
        // Даже если вернется 401, это значит, что приложение работает
        mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isUnauthorized()); // Ожидаем 401, т.к. нет токена
    }

    @Test
    @DisplayName("ST002: Аутентификация работает - можно получить JWT токен")
    void testAuthenticationWorks() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", TEST_EMAIL,
                "password", TEST_PASSWORD
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    @DisplayName("ST003: Защищенные endpoints требуют авторизацию")
    void testProtectedEndpointsRequireAuth() throws Exception {
        // Проверяем несколько критичных endpoints
        mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/project/current-user"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/block/workflow/page")
                        .param("projectId", "1")
                        .param("pageNumber", "0")
                        .param("pageElementsCount", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("ST004: С валидным токеном можно получить список проектов")
    void testCanGetProjectsWithValidToken() throws Exception {
        String token = getJwtToken();

        mockMvc.perform(get("/api/v1/project")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("ST005: С валидным токеном можно получить текущего пользователя")
    void testCanGetCurrentUserWithValidToken() throws Exception {
        String token = getJwtToken();

        mockMvc.perform(get("/api/v1/project/current-user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    @DisplayName("ST006: Можно создать проект")
    void testCanCreateProject() throws Exception {
        String token = getJwtToken();

        Map<String, String> projectData = Map.of(
                "name", "Smoke Test Project " + System.currentTimeMillis()
        );

        mockMvc.perform(post("/api/v1/project")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    @DisplayName("ST007: Login endpoint доступен без авторизации")
    void testLoginEndpointIsPublic() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", TEST_EMAIL,
                "password", TEST_PASSWORD
        );

        // Login должен быть доступен без токена
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ST008: Неверные учетные данные отклоняются")
    void testInvalidCredentialsAreRejected() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", "invalid@example.com",
                "password", "wrongpassword"
        );

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ST009: База данных доступна - можно получить данные")
    void testDatabaseIsAccessible() throws Exception {
        String token = getJwtToken();

        // Если можем получить проекты, значит БД работает
        mockMvc.perform(get("/api/v1/project")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("ST010: API возвращает правильные HTTP статусы")
    void testApiReturnsCorrectStatusCodes() throws Exception {
        // 401 для неавторизованных запросов
        mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isUnauthorized());

        // 200 для успешного login
        Map<String, String> loginRequest = Map.of(
                "email", TEST_EMAIL,
                "password", TEST_PASSWORD
        );
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // 403 для неверных учетных данных
        Map<String, String> invalidRequest = Map.of(
                "email", "wrong@example.com",
                "password", "wrong"
        );
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isForbidden());
    }
}

