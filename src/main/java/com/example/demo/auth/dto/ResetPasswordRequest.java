package com.example.demo.auth.dto;

public record ResetPasswordRequest(String token, String newPassword) {
}
