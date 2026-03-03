package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.permission.PermissionRequest;
import com.example.vehicle_auction.application.dto.permission.PermissionResponse;
import com.example.vehicle_auction.application.usecase.permission.CreatePermissionUseCase;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Permission Management", description = "APIs for managing user permissions")
public class PermissionController {
    private final CreatePermissionUseCase createPermissionUseCase;

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
}
