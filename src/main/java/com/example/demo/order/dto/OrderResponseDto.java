package com.example.demo.order.dto;

import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.entity.PaymentMethod;
import com.example.demo.order.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        Long id,
        String orderNumber,

        Long userId,
        String username,
        String email,
        OrderStatus orderStatus,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,

        String paymentProvider,
        String paymentTransactionId,

        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal shippingAmount,
        BigDecimal taxAmount,
        BigDecimal grandTotal,
        String currency,

        String shippingFullName,
        String shippingPhone,
        String shippingAddressLine,
        String shippingCity,
        String shippingCountry,
        String shippingPostalCode,
        String customerNote,

        List<OrderItemResponseDto> items,

        Instant createdAt,
        Instant updatedAt
) {}