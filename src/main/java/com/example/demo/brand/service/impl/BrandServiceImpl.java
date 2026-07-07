package com.example.demo.brand.service.impl;

import com.example.demo.brand.dto.BrandResponseDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.brand.dto.UpdateBrandDto;
import com.example.demo.brand.entity.Brand;
import com.example.demo.brand.mapper.BrandMapper;
import com.example.demo.brand.repository.BrandRepository;
import com.example.demo.brand.service.BrandService;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository,
                            BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toBrandResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponseDto getBrandById(Long id) {
        return brandMapper.toBrandResponseDto(findBrandById(id));
    }

    @Override
    public BrandResponseDto createBrand(CreateBrandDto dto) {

        // 🔥 duplicate check
        brandRepository.findByName(dto.getName())
                .ifPresent(b -> {
                    throw new ConflictException("Brand already exists with name: " + dto.getName());
                });

        Brand brand = brandMapper.toEntityFromCreateBrandDto(dto);
        Brand saved = brandRepository.save(brand);

        return brandMapper.toBrandResponseDto(saved);
    }

    @Override
    public BrandResponseDto updateBrand(Long id, UpdateBrandDto dto) {
        Brand brand = findBrandById(id);

        brand.update(
                dto.getName(),
                dto.getDescription()
        );

        return brandMapper.toBrandResponseDto(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand brand = findBrandById(id);
        brandRepository.delete(brand);
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Brand", id));
    }
}