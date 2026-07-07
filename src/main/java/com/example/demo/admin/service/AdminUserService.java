package com.example.demo.admin.service;

import com.example.demo.admin.dto.UserDtos;

import java.util.Set;

public interface AdminUserService {
    Set<UserDtos.UserResponse> getUsers();
    UserDtos.UserResponse getUser(Long id);
    UserDtos.UserResponse createUser(UserDtos.CreateUserRequest createUserRequest);
    UserDtos.UserResponse updateUser(Long id, UserDtos.UpdateUserRequest updateUserRequest);
    void setPassword(Long id, UserDtos.SetPasswordRequest setPasswordRequest);
    UserDtos.UserResponse setRoles(Long id, UserDtos.SetRolesRequest setRolesRequest);
    void deleteUser(Long id);
}
