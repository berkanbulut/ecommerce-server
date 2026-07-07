package com.example.demo.storefront.dto;

import java.math.BigDecimal;
import java.util.List;

public record StorefrontProductDetailDto(
        Long id,
        String name,
        String slug,
        String shortDescription,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        String currency,
        String mainImageUrl,
        List<StorefrontProductImageDto> images,
        String stockStatus,
        Double ratingAverage,
        Integer ratingCount,
        StorefrontCategoryDto category,
        StorefrontBrandDto brand
) {
}