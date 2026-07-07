package com.example.demo.order.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        Long id,
        Long productId,
        String productName,
        String productSlug,
        String productImageUrl,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {}