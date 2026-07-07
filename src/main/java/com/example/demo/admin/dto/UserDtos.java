package com.example.demo.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UserDtos {

    public record UserResponse(
            Long id,
            String username,
            String email,
            boolean enabled,
            boolean accountLocked,
            Set<String> roles
    ){}

    public record CreateUserRequest(
            @NotBlank(message = "Username is required")
            @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
            String username,
            @Email(message = "Email should be valid")
            @NotBlank(message = "Email is required")
            String email,
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
            Set<String> roles
    ){}

    public record UpdateUserRequest(
                @Email(message = "Email should be valid")
                @NotBlank(message = "Email is required")
                String email,
                Boolean enabled,
                Boolean accountLocked
    ){}

    public record SetPasswordRequest(
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String newPassword
    ){}

    public record SetRolesRequest(
            Set<String> roles // Assuming roles are identified by their names (e.g., ["ROLE_ADMIN", "ROLE_USER"])
    ){}
}
