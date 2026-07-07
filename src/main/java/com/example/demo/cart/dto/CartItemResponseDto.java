package com.example.demo.cart.dto;

import java.math.BigDecimal;

public record CartItemResponseDto(

        Long id,

        Long productId,

        String productName,

        String productSlug,

        String productImageUrl,

        Integer quantity,

        BigDecimal unitPrice,

        BigDecimal totalPrice,

        String currency,

        Integer availableStock

) {}