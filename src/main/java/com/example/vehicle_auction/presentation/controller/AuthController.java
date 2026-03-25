package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.auth.*;
import com.example.vehicle_auction.application.dto.common.MessageResponse;
import com.example.vehicle_auction.application.usecase.auth.AuthUseCase;
import com.example.vehicle_auction.application.usecase.auth.GoogleAuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs related to user authentication")
public class AuthController {
    private final AuthUseCase authUseCase;
    private final GoogleAuthUseCase googleAuthUseCase;

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

    @PostMapping("/google-login")
    @Operation(summary = "Login with Google ID Token fron Frontend")
    public ResponseEntity<AuthResponse> authenticateWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {
        AuthResponse response = googleAuthUseCase.authenticate(request.idToken());
        return ResponseEntity.ok(response);
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

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset link")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        authUseCase.forgotPassword(request.email());
        return ResponseEntity.ok(new MessageResponse("If the email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password with token")
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authUseCase.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully."));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        authUseCase.verifyAccount(token);
        return ResponseEntity.ok("Account verified successfully!");
    }
}
