package com.example.demo.payment.service;

import com.example.demo.payment.dto.StripeCheckoutSessionResponseDto;

public interface StripePaymentService {

    StripeCheckoutSessionResponseDto createCheckoutSession(Long orderId);

}