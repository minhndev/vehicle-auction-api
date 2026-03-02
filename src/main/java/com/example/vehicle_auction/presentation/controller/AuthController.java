package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.auth.AuthResponse;
import com.example.vehicle_auction.application.dto.auth.LoginRequest;
import com.example.vehicle_auction.application.dto.auth.RefreshTokenRequest;
import com.example.vehicle_auction.application.dto.auth.RegisterRequest;
import com.example.vehicle_auction.application.usecase.auth.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs related to user authentication")
public class AuthController {
    private final AuthUseCase authUseCase;

    @PostMapping("/register")
    @Operation(summary = "Register a new account")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authUseCase.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login to the system")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authUseCase.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Reissue a new Access Token using Refresh Token.")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authUseCase.refreshToken(request.refreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
