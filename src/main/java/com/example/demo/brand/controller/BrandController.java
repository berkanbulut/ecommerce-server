package com.example.demo.brand.controller;

import com.example.demo.brand.dto.BrandResponseDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.brand.dto.UpdateBrandDto;
import com.example.demo.brand.service.BrandService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponseDto>>> getBrands() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brands retrieved successfully!",
                        brandService.getBrands()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand retrieved successfully!",
                        brandService.getBrandById(id)
                )
        );
    }

    @PreAuthorize("hasAuthority('brand:create')")
    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDto>> createBrand(
            @Valid @RequestBody CreateBrandDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Brand created successfully!",
                        brandService.createBrand(dto)
                ));
    }

    @PreAuthorize("hasAuthority('brand:update')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBrandDto dto) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Brand updated successfully!",
                        brandService.updateBrand(id, dto)
                )
        );
    }

    @PreAuthorize("hasAuthority('brand:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @PathVariable Long id) {

        brandService.deleteBrand(id);
        return ResponseEntity.ok(
                ApiResponse.success("Brand deleted successfully!")
        );
    }
}
