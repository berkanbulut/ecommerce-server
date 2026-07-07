package com.example.demo.brand.mapper;

import com.example.demo.brand.dto.BrandResponseDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.brand.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toEntityFromCreateBrandDto(CreateBrandDto dto);

    BrandResponseDto toBrandResponseDto(Brand brand);
}