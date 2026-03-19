package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.auth.AuthResponse;
import com.example.vehicle_auction.application.dto.auth.LoginRequest;
import com.example.vehicle_auction.application.dto.auth.RegisterRequest;
import com.example.vehicle_auction.application.usecase.auth.AuthUseCase;
import com.example.vehicle_auction.domain.enums.Gender;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthUseCase authUseCase;
    @MockitoBean
    private JwtService jwtService;

    @Test
    void should_Return201_When_RegisteringSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "test@test.com", "pass123", "pass123", "John", "Doe",
                "123456789", LocalDate.of(1990, 1, 1), Gender.MALE, "1234567890", "Address", "url"
        );
        AuthResponse response = new AuthResponse("access-token", "refresh-token", "Bearer");

        when(authUseCase.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void should_Return200_When_LoggingInSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "pass123");
        AuthResponse response = new AuthResponse("access-token", "refresh-token", "Bearer");

        when(authUseCase.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"));
    }

    @Test
    void should_Return400_When_LoginRequestIsInvalid() throws Exception {
        LoginRequest request = new LoginRequest("", "");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_Return200_When_AccountIsVerified() throws Exception {
        doNothing().when(authUseCase).verifyAccount(anyString());

        mockMvc.perform(get("/auth/verify")
                        .param("token", "valid-uuid-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully!"));
    }
}
