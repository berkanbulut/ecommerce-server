package com.example.demo.storefront.dto;

import java.math.BigDecimal;

public record StorefrontProductCardDto(
        Long id,
        String name,
        String slug,
        BigDecimal price,
        BigDecimal salePrice,
        String currency,
        String mainImageUrl,
        String stockStatus,
        Double ratingAverage,
        Integer ratingCount
) {
}