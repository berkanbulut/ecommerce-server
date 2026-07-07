package com.example.demo.payment.dto;

public record StripeCheckoutSessionResponseDto(
        String sessionId,
        String url
) {}