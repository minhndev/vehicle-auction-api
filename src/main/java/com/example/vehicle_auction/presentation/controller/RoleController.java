package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.usecase.role.CreateRoleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing user roles and permissions")
public class RoleController {
    private final CreateRoleUseCase createRoleUseCase;

    @Operation(summary = "Create a new role",
            description = "This endpoint allows Admin to create a custom role and link it with multiple permissions.")
    @ApiResponse(responseCode = "201", description = "Role created successfully")
    @ApiResponse(responseCode = "400", description = "Role name already exists")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid RoleRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRoleUseCase.execute(req));
    }
}
