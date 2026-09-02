package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.category.CategoryRequest;
import com.example.vehicle_auction.application.dto.category.CategoryResponse;
import com.example.vehicle_auction.application.usecase.category.*;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing vehicle categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final RestoreCategoryUseCase restoreCategoryUseCase;

    @Operation(summary = "Create a new category", description = "Admin only. Auto-generates a unique URL slug.")
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategoryUseCase.execute(request));
    }

    @Operation(summary = "Get all active categories with pagination")
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getCategoryUseCase.getAll(pageable));
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCategoryUseCase.getById(id));
    }

    @Operation(summary = "Update category details")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.ok(updateCategoryUseCase.execute(id, request));
    }

    @Operation(summary = "Soft delete a category")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore a category")
    @ApiResponse(responseCode = "204", description = "Category Restored successfully")
    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('CATEGORY_RESTORE')")
    public ResponseEntity<Void> restoreCategory(@PathVariable UUID id) {
        restoreCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
