package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.usecase.product.*;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing vehicles (products) before they go to auction")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final ApproveProductUseCase approveProductUseCase;
    private final RejectProductUseCase rejectProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final RestoreProductUseCase restoreProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @Operation(
            summary = "Register a new vehicle",
            description = "Allows SELLER or ADMIN to register a new vehicle into the system. " +
                    "The vehicle will be created with a DRAFT status and will await approval before being auctioned."
    )
    @ApiResponse(responseCode = "201", description = "Vehicle registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or VIN number already exists")
    @PostMapping
//    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        UUID sellerId = currentUser.getAccount().getId();
        ProductResponse response = createProductUseCase.execute(request, sellerId);
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
//    @PreAuthorize("hasAuthority('PRODUCT_APPROVE')")
    public ResponseEntity<ProductResponse> approveProduct(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(approveProductUseCase.execute(id));
    }

    @Operation(
            summary = "Approve a vehicle (Admin only)",
            description = "Changes the status of a vehicle from DRAFT to APPROVED. " +
                    "Only approved vehicles can be added to an auction session."
    )
    @ApiResponse(responseCode = "200", description = "Vehicle rejected successfully")
    @ApiResponse(responseCode = "400", description = "Vehicle is not in PENDING status")
    @ApiResponse(responseCode = "404", description = "Vehicle not found")
    @PatchMapping("/{id}/reject")
//    @PreAuthorize("hasAuthority('PRODUCT_REJECT')")
    public ResponseEntity<ProductResponse> rejectProduct(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(rejectProductUseCase.execute(id));
    }

    @Operation(summary = "Get all active products with pagination")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getProductUseCase.getAllProducts(pageable));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(getProductUseCase.getProductById(id));
    }

    @Operation(summary = "Update product details")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(updateProductUseCase.execute(id, request));
    }

    @Operation(summary = "Soft delete a product")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore a product")
    @ApiResponse(responseCode = "204", description = "Product Restored successfully")
    @PatchMapping("/{id}/restore")
//    @PreAuthorize("hasAuthority('PRODUCT_RESTORE')")
    public ResponseEntity<Void> restoreProduct(@PathVariable UUID id) {
        restoreProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
