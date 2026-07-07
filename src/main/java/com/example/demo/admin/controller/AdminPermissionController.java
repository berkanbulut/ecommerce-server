package com.example.demo.admin.controller;

import com.example.demo.admin.dto.PermissionDtos;
import com.example.demo.admin.service.AdminPermissionService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/permissions")
public class AdminPermissionController {

    private final AdminPermissionService adminPermissionService;

    public AdminPermissionController(AdminPermissionService adminPermissionService) {
        this.adminPermissionService = adminPermissionService;
    }

    @PreAuthorize("hasAuthority('permission:manage')")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<PermissionDtos.PermissionResponse>>> getPermissions(){
        return ResponseEntity.ok(
                ApiResponse.success("Permissions retrieved successfully!", adminPermissionService.getPermissions())
        );
    }

    @PreAuthorize("hasAuthority('permission:manage')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDtos.PermissionResponse>> getPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.success("Permission retrieved successfully!", adminPermissionService.getPermissionById(id))
        );
    }

    @PreAuthorize("hasAuthority('permission:manage')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionDtos.PermissionResponse>> createPermission(@Valid @RequestBody PermissionDtos.CreatePermissionRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Permission created successfully!", adminPermissionService.createPermission(request))
        );
    }

    @PreAuthorize("hasAuthority('permission:manage')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDtos.PermissionResponse>> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionDtos.UpdatePermissionRequest request){
        return ResponseEntity.ok(
                ApiResponse.success("Permission updated successfully!", adminPermissionService.updatePermission(id, request))
        );
    }

    @PreAuthorize("hasAuthority('permission:manage')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id){
        adminPermissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.success("Permission deleted successfully!"));
    }
}