package com.example.demo.storefront.controller;

import com.example.demo.response.ApiResponse;
import com.example.demo.storefront.dto.StorefrontCategoryDto;
import com.example.demo.storefront.service.StorefrontService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class StorefrontCategoryController {

    private final StorefrontService storefrontService;

    public StorefrontCategoryController(StorefrontService storefrontService) {
        this.storefrontService = storefrontService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StorefrontCategoryDto>>> getCategories() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Categories retrieved successfully",
                        storefrontService.getCategories()
                )
        );
    }
}