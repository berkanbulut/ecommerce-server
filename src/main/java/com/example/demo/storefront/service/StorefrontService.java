package com.example.demo.storefront.service;

import com.example.demo.storefront.dto.StorefrontBrandDto;
import com.example.demo.storefront.dto.StorefrontCategoryDto;
import com.example.demo.storefront.dto.StorefrontProductCardDto;
import com.example.demo.storefront.dto.StorefrontProductDetailDto;

import java.math.BigDecimal;
import java.util.List;

public interface StorefrontService {

    List<StorefrontCategoryDto> getCategories();

    List<StorefrontBrandDto> getBrands();

    List<StorefrontProductCardDto> getProducts(
            String search,
            Long categoryId,
            Long brandId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean featured,
            String sort
    );

    StorefrontProductDetailDto getProductBySlug(String slug);

    List<StorefrontProductCardDto> getFeaturedProducts(int limit);

    List<StorefrontProductCardDto> getNewArrivals(int limit);
}