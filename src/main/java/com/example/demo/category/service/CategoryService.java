package com.example.demo.category.service;

import com.example.demo.category.dto.CategoryResponseDto;
import com.example.demo.category.dto.CreateCategoryDto;
import com.example.demo.category.dto.UpdateCategoryDto;

import java.util.List;


public interface CategoryService {
    List<CategoryResponseDto> getCategories();
    CategoryResponseDto getCategoryById(Long id);
    CategoryResponseDto createCategory(CreateCategoryDto createCategoryDto);
    CategoryResponseDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto);
    void deleteCategory(Long id);
}
