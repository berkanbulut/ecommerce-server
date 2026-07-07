package com.example.demo.admin.controller;

import com.example.demo.admin.dto.UserDtos;
import com.example.demo.admin.service.AdminUserService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<UserDtos.UserResponse>>> getUsers(){
        return ResponseEntity.ok(
                ApiResponse.success("Users retrieved successfully!", adminUserService.getUsers())
        );
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtos.UserResponse>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success("User retrieved successfully!", adminUserService.getUser(id))
        );
    }

    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDtos.UserResponse>> createUser(@Valid @RequestBody UserDtos.CreateUserRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("User created successfully!", adminUserService.createUser(request))
        );
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtos.UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDtos.UpdateUserRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("User updated successfully!", adminUserService.updateUser(id, request))
        );
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PatchMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> setPassword(@PathVariable Long id, @Valid @RequestBody UserDtos.SetPasswordRequest request){
        adminUserService.setPassword(id, request);
        return ResponseEntity.ok(ApiResponse.success("Password updated successfully!"));
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @PatchMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<UserDtos.UserResponse>> setRoles(@PathVariable Long id, @Valid @RequestBody UserDtos.SetRolesRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("User roles updated!", adminUserService.setRoles(id, request))
        );
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id){
        adminUserService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully!"));
    }
}