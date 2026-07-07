package com.example.demo.admin.service;

import com.example.demo.admin.dto.RoleDtos;

import java.util.Set;

public interface AdminRoleService {
        Set<RoleDtos.RoleResponse> getRoles();
        RoleDtos.RoleResponse getRoleById(Long id);
        RoleDtos.RoleResponse createRole(RoleDtos.CreateRoleRequest createRoleRequest);
        RoleDtos.RoleResponse updateRole(Long id, RoleDtos.UpdateRoleRequest updateRoleRequest);
        RoleDtos.RoleResponse setRolePermissions(Long id, RoleDtos.SetRolePermissionsRequest setRolePermissionsRequest);
        void deleteRole(Long id);
}
