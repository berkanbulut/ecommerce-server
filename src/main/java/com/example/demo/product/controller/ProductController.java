package com.example.demo.product.controller;

import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.PatchProductDto;
import com.example.demo.product.dto.ProductResponseDto;
import com.example.demo.product.dto.UpdateProductDto;
import com.example.demo.product.service.ProductService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProducts() {
        return ResponseEntity.ok(
                ApiResponse.success("Products retrieved successfully", productService.getProducts())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Product retrieved successfully", productService.getProductById(id))
        );
    }

    @PreAuthorize("hasAuthority('product:create')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductDto createProductDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", productService.createProduct(createProductDto)));
    }

    @PreAuthorize("hasAuthority('product:update')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto updateProductDto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Product updated successfully", productService.updateProduct(id, updateProductDto))
        );
    }

    @PreAuthorize("hasAuthority('product:update')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> patchProduct(
            @PathVariable Long id,
            @Valid @RequestBody PatchProductDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Product updated successfully", productService.patchProduct(id, dto))
        );
    }

    @PreAuthorize("hasAuthority('product:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }
}