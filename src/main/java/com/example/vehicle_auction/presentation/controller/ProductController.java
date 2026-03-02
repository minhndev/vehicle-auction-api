package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.usecase.product.ApproveProductUseCase;
import com.example.vehicle_auction.application.usecase.product.CreateProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing vehicles (products) before they go to auction")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final ApproveProductUseCase approveProductUseCase;

    @Operation(
            summary = "Register a new vehicle",
            description = "Allows SELLER or ADMIN to register a new vehicle into the system. " +
                    "The vehicle will be created with a DRAFT status and will await approval before being auctioned."
    )
    @ApiResponse(responseCode = "201", description = "Vehicle registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or VIN number already exists")
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Approve a vehicle (Admin only)",
            description = "Changes the status of a vehicle from DRAFT to APPROVED. " +
                    "Only approved vehicles can be added to an auction session."
    )
    @ApiResponse(responseCode = "200", description = "Vehicle approved successfully")
    @ApiResponse(responseCode = "400", description = "Vehicle is not in DRAFT status")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PRODUCT_APPROVE')")
    public ResponseEntity<ProductResponse> approveProduct(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(approveProductUseCase.execute(id));
    }
}
