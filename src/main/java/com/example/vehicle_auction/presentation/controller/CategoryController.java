package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.category.CategoryRequest;
import com.example.vehicle_auction.application.dto.category.CategoryResponse;
import com.example.vehicle_auction.application.usecase.category.CreateCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing vehicle categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;

    @Operation(summary = "Create a new category", description = "Admin only. Auto-generates a unique URL slug.")
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategoryUseCase.execute(request));
    }
}
