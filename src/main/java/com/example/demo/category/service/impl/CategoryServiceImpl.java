package com.example.demo.category.service.impl;

import com.example.demo.category.dto.CategoryResponseDto;
import com.example.demo.category.dto.CreateCategoryDto;
import com.example.demo.category.dto.UpdateCategoryDto;
import com.example.demo.category.entity.Category;
import com.example.demo.category.mapper.CategoryMapper;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.service.CategoryService;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        return categoryMapper.toCategoryResponseDto(findCategoryById(id));
    }

    @Override
    public CategoryResponseDto createCategory(CreateCategoryDto createCategoryDto) {

        // 🔥 duplicate check (enterprise)
        categoryRepository.findByName(createCategoryDto.getName())
                .ifPresent(c -> {
                    throw new ConflictException("Category already exists with name: " + createCategoryDto.getName());
                });

        Category category = categoryMapper.toEntity(createCategoryDto);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto) {
        Category category = findCategoryById(id);

        category.update(
                updateCategoryDto.getName(),
                updateCategoryDto.getDescription()
        );

        return categoryMapper.toCategoryResponseDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Category", id));
    }
}