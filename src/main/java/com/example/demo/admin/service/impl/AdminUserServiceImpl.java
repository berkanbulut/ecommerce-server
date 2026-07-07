package com.example.demo.admin.service.impl;

import com.example.demo.admin.dto.UserDtos;
import com.example.demo.admin.service.AdminUserService;
import com.example.demo.auth.repository.PasswordResetTokenRepository;
import com.example.demo.auth.repository.RefreshTokenRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.role.entity.Role;
import com.example.demo.role.repository.RoleRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AdminUserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenRepository refreshTokenRepository,
            PasswordResetTokenRepository passwordResetTokenRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Transactional(readOnly = true)
    public Set<UserDtos.UserResponse> getUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public UserDtos.UserResponse getUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));

        return toResponse(user);
    }

    @Transactional
    public UserDtos.UserResponse createUser(UserDtos.CreateUserRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists");
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password())
        );

        Set<String> roleNames = (request.roles() == null || request.roles().isEmpty())
                ? Set.of("ROLE_USER")
                : request.roles();

        for(String roleName: roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new BadRequestException("Role not found: " + roleName));

            user.addRole(role);
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserDtos.UserResponse updateUser(Long id, UserDtos.UpdateUserRequest request){

        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));

        if (!user.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {

            throw new ConflictException("Email already exists");
        }

        user.setEmail(request.email());

        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }

        if (request.accountLocked() != null) {
            user.setAccountLocked(request.accountLocked());
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void setPassword(Long id, UserDtos.SetPasswordRequest request){

        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserDtos.UserResponse setRoles(Long id, UserDtos.SetRolesRequest request){

        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));

        user.getRoles().clear();

        Set<String> roleNames = (request.roles() == null || request.roles().isEmpty())
                ? Set.of("ROLE_USER")
                : request.roles();

        for(String roleName: roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new BadRequestException("Role not found: " + roleName));

            user.addRole(role);
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("User", id));

        user.getRoles().clear();

        refreshTokenRepository.deleteAllByUserId(id);

        passwordResetTokenRepository.deleteAllByUserId(id);

        userRepository.delete(user);
    }

    private UserDtos.UserResponse toResponse(User user){
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserDtos.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEnabled(),
                user.isAccountLocked(),
                roles
        );
    }
}