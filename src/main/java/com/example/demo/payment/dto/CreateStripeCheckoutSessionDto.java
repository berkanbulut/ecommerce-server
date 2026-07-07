package com.example.demo.payment.dto;

import jakarta.validation.constraints.NotNull;

public class CreateStripeCheckoutSessionDto {

    @NotNull(message = "Order id is required!")
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }
}