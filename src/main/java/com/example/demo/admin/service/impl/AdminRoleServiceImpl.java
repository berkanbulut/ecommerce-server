package com.example.demo.admin.service.impl;

import com.example.demo.admin.dto.RoleDtos;
import com.example.demo.admin.service.AdminRoleService;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.permission.entity.Permission;
import com.example.demo.permission.repository.PermissionRepository;
import com.example.demo.role.entity.Role;
import com.example.demo.role.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public AdminRoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public Set<RoleDtos.RoleResponse> getRoles(){
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public RoleDtos.RoleResponse getRoleById(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Role", id));

        return toResponse(role);
    }

    @Transactional
    public RoleDtos.RoleResponse createRole(RoleDtos.CreateRoleRequest request){

        roleRepository.findByName(request.name())
                .ifPresent(r -> {
                    throw new ConflictException("Role already exists with name: " + request.name());
                });

        Role role = new Role(request.name(), request.description());
        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleDtos.RoleResponse updateRole(Long id, RoleDtos.UpdateRoleRequest request){

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Role", id));

        if (request.description() != null){
            role.setDescription(request.description());
        }

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleDtos.RoleResponse setRolePermissions(Long id, RoleDtos.SetRolePermissionsRequest request){

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Role", id));

        role.getPermissions().clear();

        if(request.permissions() != null){
            for(String permName : request.permissions()){
                Permission permission = permissionRepository.findByName(permName)
                        .orElseThrow(() -> new NotFoundException("Permission not found: " + permName));

                role.addPermission(permission);
            }
        }

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id){

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Role", id));

        // 🔥 ENTERPRISE BUSINESS VALIDATION
        if (!role.getUsers().isEmpty()) {
            throw new ConflictException(
                    "Role is assigned to one or more users and cannot be deleted"
            );
        }

        roleRepository.delete(role);
    }

    private RoleDtos.RoleResponse toResponse(Role role){
        Set<String> permissions = role.getPermissions()
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return new RoleDtos.RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                permissions
        );
    }
}