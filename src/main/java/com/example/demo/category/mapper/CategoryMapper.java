package com.example.demo.category.mapper;

import com.example.demo.category.dto.CategoryResponseDto;
import com.example.demo.category.dto.CreateCategoryDto;
import com.example.demo.category.dto.UpdateCategoryDto;
import com.example.demo.category.entity.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryDto createCategoryDto);
    CategoryResponseDto toCategoryResponseDto(Category category);
}
