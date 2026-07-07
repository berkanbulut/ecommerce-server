package com.example.demo.category.controller;

import com.example.demo.category.dto.CategoryResponseDto;
import com.example.demo.category.dto.CreateCategoryDto;
import com.example.demo.category.dto.UpdateCategoryDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategories(){
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully!", categoryService.getCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully!", categoryService.getCategoryById(id)));
    }

    @PreAuthorize("hasAuthority('category:create')")
    @PostMapping()
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully!", categoryService.createCategory(createCategoryDto)));
    }

    @PreAuthorize("hasAuthority('category:update')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryDto updateCategoryDto){
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully!", categoryService.updateCategory(id, updateCategoryDto)));
    }

    @PreAuthorize("hasAuthority('category:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully!"));
    }
}

