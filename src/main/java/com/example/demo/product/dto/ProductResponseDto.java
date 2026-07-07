package com.example.demo.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductResponseDto(
        Long id,
        String name,
        String slug,
        String sku,
        String shortDescription,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        String currency,
        Integer stockQuantity,
        String stockStatus,
        boolean active,
        boolean featured,
        String mainImageUrl,
        List<ProductImageResponseDto> images,
        Double ratingAverage,
        Integer ratingCount,
        Long categoryId,
        Long brandId,
        Instant createdAt,
        Instant updatedAt
) {
}