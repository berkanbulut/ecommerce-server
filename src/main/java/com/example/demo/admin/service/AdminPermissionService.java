package com.example.demo.admin.service;

import com.example.demo.admin.dto.PermissionDtos;

import java.util.Set;

public interface AdminPermissionService {
        Set<PermissionDtos.PermissionResponse> getPermissions();
        PermissionDtos.PermissionResponse getPermissionById(Long id);
        PermissionDtos.PermissionResponse createPermission(PermissionDtos.CreatePermissionRequest createPermissionRequest);
        PermissionDtos.PermissionResponse updatePermission(Long id, PermissionDtos.UpdatePermissionRequest updatePermissionRequest);
        void deletePermission(Long id);
}
