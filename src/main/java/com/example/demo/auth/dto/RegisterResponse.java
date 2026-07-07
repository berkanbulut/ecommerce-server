package com.example.demo.auth.dto;

public record RegisterResponse (
   Long userId,
   String username,
   String email
) {}
