package com.example.demo.payment.controller;

import com.example.demo.payment.dto.CreateStripeCheckoutSessionDto;
import com.example.demo.payment.dto.StripeCheckoutSessionResponseDto;
import com.example.demo.payment.service.StripePaymentService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final StripePaymentService stripePaymentService;

    public PaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/stripe/checkout-session")
    public ResponseEntity<ApiResponse<StripeCheckoutSessionResponseDto>> createStripeCheckoutSession(
            @Valid @RequestBody CreateStripeCheckoutSessionDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Stripe checkout session created successfully!",
                        stripePaymentService.createCheckoutSession(dto.getOrderId())
                )
        );
    }
}