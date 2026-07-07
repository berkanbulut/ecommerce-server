package com.example.demo.admin.controller;

import com.example.demo.admin.dto.RoleDtos;
import com.example.demo.admin.service.AdminRoleService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/roles")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<RoleDtos.RoleResponse>>> getRoles(){
        return ResponseEntity.ok(
                ApiResponse.success("Roles retrieved successfully!", adminRoleService.getRoles())
        );
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDtos.RoleResponse>> getRoleById(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success("Role retrieved successfully!", adminRoleService.getRoleById(id))
        );
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleDtos.RoleResponse>> createRole(@Valid @RequestBody RoleDtos.CreateRoleRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Role created successfully!", adminRoleService.createRole(request))
        );
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDtos.RoleResponse>> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDtos.UpdateRoleRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Role updated successfully!", adminRoleService.updateRole(id, request))
        );
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @PatchMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<RoleDtos.RoleResponse>> setPermissions(@PathVariable Long id, @Valid @RequestBody RoleDtos.SetRolePermissionsRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Role permissions updated!", adminRoleService.setRolePermissions(id, request))
        );
    }

    @PreAuthorize("hasAuthority('role:manage')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id){
        adminRoleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted successfully!"));
    }
}