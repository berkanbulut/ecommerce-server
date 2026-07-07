package com.example.demo.storefront.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.storefront.dto.StorefrontBrandDto;
import com.example.demo.storefront.service.StorefrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
public class StorefrontBrandController {

    private final StorefrontService storefrontService;

    public StorefrontBrandController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StorefrontBrandDto>>> getBrands() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brands retrieved successfully",
                        storefrontService.getBrands()
                )
        );
    }
}