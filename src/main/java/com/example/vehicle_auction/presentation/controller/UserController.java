package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.user.*;
import com.example.vehicle_auction.application.usecase.user.*;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for current profile and admin user management")
public class UserController {
    private final GetUserUseCase getUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final RestoreUserUseCase restoreUserUseCase;

    @Operation(
            summary = "Get current user profile",
            description = "Retrieves the profile information of the currently authenticated user."
    )
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID accountId = userDetails.getAccount().getId();

        UserResponse response = getUserUseCase.getUserByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get paginated users (Admin)")
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<Page<UserManagementResponse>> getUsers(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean verified,
            @RequestParam(required = false) Boolean deleted) {
        return ResponseEntity.ok(getUserUseCase.getAllUsers(pageable, keyword, active, verified, deleted));
    }

    @Operation(summary = "Get user details by id (Admin)")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW_DETAILS')")
    public ResponseEntity<UserManagementResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(getUserUseCase.getUserById(id));
    }

    @Operation(summary = "Update user active status (Admin)")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('USER_UPDATE_STATUS')")
    public ResponseEntity<UserManagementResponse> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserStatusRequest request) {
        return ResponseEntity.ok(getUserUseCase.updateStatus(id, request));
    }

    @Operation(summary = "Grant SELLER role to user (Admin)")
    @PatchMapping("/{id}/grant-seller")
    @PreAuthorize("hasAuthority('USER_GRANT_SELLER')")
    public ResponseEntity<UserManagementResponse> grantSellerRole(@PathVariable UUID id) {
        return ResponseEntity.ok(getUserUseCase.grantSellerRole(id));
    }

    @Operation(summary = "Create a new user with roles (Admin)")
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<UserManagementResponse> createUser(@RequestBody @Valid CreateUserRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createUserUseCase.execute(req));
    }

    @Operation(summary = "Update user details and roles (Admin)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<UserManagementResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest req) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateUserUseCase.execute(id, req));
    }

    @Operation(summary = "Soft delete a user (Admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> softDeleteUser(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore a soft-deleted user (Admin)")
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('USER_RESTORE')")
    public ResponseEntity<Void> restoreUser(@PathVariable UUID id) {
        restoreUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
