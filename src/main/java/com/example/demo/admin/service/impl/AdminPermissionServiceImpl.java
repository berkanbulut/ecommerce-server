package com.example.demo.admin.service.impl;

import com.example.demo.admin.dto.PermissionDtos;
import com.example.demo.admin.service.AdminPermissionService;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.permission.entity.Permission;
import com.example.demo.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminPermissionServiceImpl implements AdminPermissionService {

    private final PermissionRepository permissionRepository;

    public AdminPermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public Set<PermissionDtos.PermissionResponse> getPermissions(){
        return permissionRepository.findAll()
                .stream()
                .map(p -> new PermissionDtos.PermissionResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription()
                ))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public PermissionDtos.PermissionResponse getPermissionById(Long id){
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Permission", id));

        return new PermissionDtos.PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }

    @Transactional
    public PermissionDtos.PermissionResponse createPermission(PermissionDtos.CreatePermissionRequest request){

        permissionRepository.findByName(request.name())
                .ifPresent(p -> {
                    throw new ConflictException("Permission already exists with name: " + request.name());
                });

        Permission permission = new Permission(request.name(), request.description());
        Permission saved = permissionRepository.save(permission);

        return new PermissionDtos.PermissionResponse(
                saved.getId(),
                saved.getName(),
                saved.getDescription()
        );
    }

    @Transactional
    public PermissionDtos.PermissionResponse updatePermission(Long id, PermissionDtos.UpdatePermissionRequest request){

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Permission", id));

        if (request.description() != null){
            permission.setDescription(request.description());
        }

        return new PermissionDtos.PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }

    @Transactional
    public void deletePermission(Long id){

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Permission", id));

        // 🔥 ENTERPRISE BUSINESS VALIDATION
        if (!permission.getRoles().isEmpty()) {
            throw new ConflictException(
                    "Permission is assigned to one or more roles and cannot be deleted"
            );
        }

        permissionRepository.delete(permission);
    }
}