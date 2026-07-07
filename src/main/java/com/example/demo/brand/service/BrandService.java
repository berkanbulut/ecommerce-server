package com.example.demo.brand.service;

import com.example.demo.brand.dto.BrandResponseDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.brand.dto.UpdateBrandDto;

import java.util.List;

public interface BrandService {

    List<BrandResponseDto> getBrands();

    BrandResponseDto getBrandById(Long id);

    BrandResponseDto createBrand(CreateBrandDto dto);

    BrandResponseDto updateBrand(Long id, UpdateBrandDto dto);

    void deleteBrand(Long id);
}
