package com.example.vehicle_auction.application.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a new vehicle category")
public record CategoryRequest(
        @Schema(description = "Name of the category", example = "SUV 7 Chỗ")
        @NotBlank(message = "{category.name.required}")
        @Size(min = 2, max = 150, message = "{category.name.size}")
        String name,

        @Schema(description = "Description of the category", example = "Dòng xe thể thao đa dụng 7 chỗ ngồi")
        @Size(max = 250, message = "{category.description.size}")
        String description

) { }
