package com.example.demo.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDtos {

    public record PermissionResponse(
            Long id,
            String name,
            String description
    ) {
    }

    public record CreatePermissionRequest(
            @NotBlank(message = "Permission name must not be blank")
            @Size(min = 6, max = 50, message = "Permission name must be between 6 and 50 characters" )
            String name,
            @Size(max = 255, message = "Description must not exceed 255 characters")
            String description
    ) {
    }

    public record UpdatePermissionRequest(
            @Size(max = 255, message = "Description must not exceed 255 characters")
            String description
    ) {
    }
}
