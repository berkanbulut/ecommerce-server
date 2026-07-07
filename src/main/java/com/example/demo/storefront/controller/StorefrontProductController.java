package com.example.demo.storefront.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.storefront.dto.StorefrontProductCardDto;
import com.example.demo.storefront.dto.StorefrontProductDetailDto;
import com.example.demo.storefront.service.StorefrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class StorefrontProductController {

    private final StorefrontService storefrontService;

    public StorefrontProductController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StorefrontProductCardDto>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String sort
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        storefrontService.getProducts(
                                search,
                                categoryId,
                                brandId,
                                minPrice,
                                maxPrice,
                                featured,
                                sort
                        )
                )
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<StorefrontProductCardDto>>> getFeaturedProducts(
            @RequestParam(defaultValue = "8") int limit
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Featured products retrieved successfully",
                        storefrontService.getFeaturedProducts(limit)
                )
        );
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<List<StorefrontProductCardDto>>> getNewArrivals(
            @RequestParam(defaultValue = "8") int limit
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "New arrivals retrieved successfully",
                        storefrontService.getNewArrivals(limit)
                )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<StorefrontProductDetailDto>> getProductBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product retrieved successfully",
                        storefrontService.getProductBySlug(slug)
                )
        );
    }
}