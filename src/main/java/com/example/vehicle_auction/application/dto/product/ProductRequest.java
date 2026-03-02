package com.example.vehicle_auction.application.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.UUID;

@Schema(description = "Request body for registering a new vehicle (product)")
public record ProductRequest(
        @Schema(description = "ID of the category (e.g., SUV, Sedan)", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "{product.category.required}")
        UUID categoryId,

        @Schema(description = "Name of the vehicle", example = "Toyota Camry 2.5Q 2022")
        @NotBlank(message = "{product.name.required}")
        String name,

        @Schema(example = "Toyota")
        @NotBlank(message = "{product.brand.required}")
        String brand,

        @Schema(example = "Camry")
        @NotBlank(message = "{product.model.required}")
        String model,

        @Schema(description = "Vehicle Identification Number - Must be unique", example = "JTDKB46K803123456")
        @NotBlank(message = "{product.vin.required}")
        String vinNumber,

        @Schema(example = "2022")
        @NotNull(message = "{product.year.required}")
        String manufactureYear,

        @Schema(description = "Current mileage in km", example = "15000")
        @NotNull(message = "{product.mileage.required}")
        @Positive(message = "{product.mileage.positive}")
        String mileage,

        @Schema(description = "Starting price for the auction", example = "850000000.00")
        @NotNull(message = "{product.price.required}")
        @Positive(message = "{product.price.positive}")
        String startPrice,

        @Schema(description = "List of image URLs uploaded via Cloudinary/S3. The first URL will be the main image.",
                example = "[\"https://cloud.com/img1.jpg\", \"https://cloud.com/img2.jpg\"]")
        @NotEmpty(message = "{product.images.required}")
        List<String> imageUrls) {

}
