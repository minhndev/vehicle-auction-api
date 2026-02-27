package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.role.RoleRequest;
import com.example.vehicle_auction.application.dto.role.RoleResponse;
import com.example.vehicle_auction.application.dto.role.RoleUpdateRequest;
import com.example.vehicle_auction.application.usecase.role.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "APIs for managing user roles and permissions")
public class RoleController {
    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final RestoreRoleUseCase restoreRoleUseCase;

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

    @Operation(summary = "Get paginated list of roles",
            description = "Returns a paginated list of roles. Use 'page, 'size', and 'sort' parameters.")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    public ResponseEntity<Page<RoleResponse>> getAllRoles(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(getRoleUseCase.getAll(pageable));
    }

    @Operation(summary = "Get role details by ID",
            description = "Returns detailed information about a specific role, including its permissions.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_DETAILS')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(getRoleUseCase.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @Operation(summary = "Update an existing role",
            description = "This endpoint allows Admin to update role details and its associated permissions.")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID id, @RequestBody @Valid RoleUpdateRequest req) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateRoleUseCase.execute(id, req));
    }

    @Operation(summary = "Soft delete a role",
            description = "This endpoint allows Admin to soft delete a role. The role will be marked as deleted but not removed from the database.")
    @ApiResponse(responseCode = "204", description = "Role deleted successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        deleteRoleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore a soft-deleted role",
            description = "This endpoint allows Admin to restore a previously soft-deleted role.")
    @ApiResponse(responseCode = "204", description = "Role restored successfully")
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('ROLE_RESTORE')")
    public ResponseEntity<Void> restoreRole(@PathVariable UUID id) {
        restoreRoleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
