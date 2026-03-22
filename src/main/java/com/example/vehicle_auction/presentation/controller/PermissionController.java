package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.permission.PermissionRequest;
import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.application.usecase.permission.CreatePermissionUseCase;
import com.example.vehicle_auction.application.usecase.permission.GetPermissionUseCase;
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
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Management", description = "APIs for managing user permissions")
public class PermissionController {
    private final CreatePermissionUseCase createPermissionUseCase;
    private final GetPermissionUseCase getPermissionUseCase;

    @Operation(summary = "Create a new permission",
            description = "This endpoint allows Admin to create a permission.")
    @ApiResponse(responseCode = "201", description = "Permission created successfully")
    @ApiResponse(responseCode = "400", description = "Permission name already exists")
    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<PermissionResponse> createPermission(@RequestBody @Valid PermissionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createPermissionUseCase.execute(req));
    }

    @Operation(summary = "Get paginated list of permissions",
            description = "Returns a paginated list of permissions. Use 'page, 'size', and 'sort' parameters.")
    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_VIEW')")
    public ResponseEntity<Page<PermissionResponse>> getAllPermissions(@ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(getPermissionUseCase.getAll(pageable));
    }

    @Operation(summary = "Get permission details by ID",
            description = "Returns detailed information about a specific permission.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION_VIEW_DETAILS')")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(getPermissionUseCase.getById(id));
    }
}
