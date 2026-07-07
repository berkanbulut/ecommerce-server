package com.example.demo.auth.dto;

public record AuthResult(
        String accessToken,
        String refreshToken
) {}