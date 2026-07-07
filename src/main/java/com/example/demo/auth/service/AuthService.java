package com.example.demo.auth.service;

import com.example.demo.auth.dto.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
    AuthResult login(LoginRequest loginRequest);
}
