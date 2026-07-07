package com.example.demo.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class RoleDtos {
    public record RoleResponse(
            Long id,
            String name,
            String description,
            Set<String> permissions
    ) {
    }

    public record CreateRoleRequest(
            @NotBlank(message = "Role name must not be blank")
            @Size(min = 6, max = 50, message = "Role name must be between 6 and 50 characters" )
            String name,
            @Size(max = 255, message = "Description must not exceed 255 characters")
            String description
    ) {
    }

    public record UpdateRoleRequest(
            @Size(max = 255, message = "Description must not exceed 255 characters")
            String description
    ) {
    }

    public record SetRolePermissionsRequest(
            Set<String> permissions // ["category:create", "category:read", "category:update", "category:delete"]
    ) {
    }
}
