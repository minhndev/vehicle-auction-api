package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.user.UpdateUserStatusRequest;
import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.usecase.user.GetUserUseCase;
import com.example.vehicle_auction.infrastructure.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldReturn200WhenGettingUsers() throws Exception {
        UUID userId = UUID.randomUUID();
        UserManagementResponse response = new UserManagementResponse(
                userId,
                "John Doe",
                "John",
                "Doe",
                "john@example.com",
                "0901234567",
                true,
                true,
                false,
                "SYSTEM",
                null,
                null,
                List.of("USER")
        );

        when(getUserUseCase.getAllUsers(any(), eq("john"), eq(true), eq(null), eq(false)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/users")
                        .param("keyword", "john")
                        .param("active", "true")
                        .param("deleted", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("john@example.com"));
    }

    @Test
    void shouldReturn200WhenGettingUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        UserManagementResponse response = new UserManagementResponse(
                userId,
                "John Doe",
                "John",
                "Doe",
                "john@example.com",
                "0901234567",
                true,
                true,
                false,
                "SYSTEM",
                null,
                null,
                List.of("USER")
        );

        when(getUserUseCase.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    void shouldReturn200WhenUpdatingUserStatus() throws Exception {
        UUID userId = UUID.randomUUID();
        UpdateUserStatusRequest request = new UpdateUserStatusRequest(false);
        UserManagementResponse response = new UserManagementResponse(
                userId,
                "John Doe",
                "John",
                "Doe",
                "john@example.com",
                "0901234567",
                false,
                true,
                false,
                "SYSTEM",
                null,
                null,
                List.of("USER")
        );

        when(getUserUseCase.updateStatus(eq(userId), any(UpdateUserStatusRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/{id}/status", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        verify(getUserUseCase).updateStatus(eq(userId), any(UpdateUserStatusRequest.class));
    }
}
