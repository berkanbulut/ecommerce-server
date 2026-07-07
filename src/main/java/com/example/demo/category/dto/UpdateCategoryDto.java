package com.example.demo.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCategoryDto {
    @NotBlank(message = "Name is required!")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters!")
    private String name;
    @NotBlank(message = "Description is required!")
    @Size(max = 255, message = "Description can be max 255 characters!")
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
