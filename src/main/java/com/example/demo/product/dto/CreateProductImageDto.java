package com.example.demo.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateProductImageDto {
    // TODO: IS REQUIRED?
    @NotBlank(message = "Image URL is required!")
    private String imageUrl;
    @NotNull(message = "Sort order is required!")
    @PositiveOrZero(message = "Sort order must be zero or positive")
    private Integer sortOrder;

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}
