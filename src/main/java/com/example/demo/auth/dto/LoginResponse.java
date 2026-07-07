package com.example.demo.auth.dto;

public record LoginResponse (
        String accessToken,
        // String refreshToken,
        String tokenType
){
}
