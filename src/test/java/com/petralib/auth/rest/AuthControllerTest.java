package com.petralib.auth.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petralib.auth.ConstructorUserRepository;
import com.petralib.auth.security.entity.ConstructorUserEntity;
import com.petralib.auth.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ConstructorUserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private AuthRequestDTO validRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        validRequest = new AuthRequestDTO();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("password123");

        authentication = new UsernamePasswordAuthenticationToken(
            validRequest.getEmail(),
            validRequest.getPassword(),
            null
        );
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtTokenProvider.createToken(validRequest.getEmail()))
            .thenReturn("test-jwt-token");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(validRequest.getEmail()))
            .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isForbidden())
            .andExpect(content().string("Invalid email/password combination"));
    }

    @Test
    void testLogin_MissingEmail() throws Exception {
        // Arrange
        AuthRequestDTO invalidRequest = new AuthRequestDTO();
        invalidRequest.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testLogout() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/logout"))
            .andExpect(status().isOk());
    }
}
