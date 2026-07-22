package com.example.demo.product.service;

import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.PatchProductDto;
import com.example.demo.product.dto.ProductResponseDto;
import com.example.demo.product.dto.UpdateProductDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> getProducts();
    ProductResponseDto getProductById(Long id);
    ProductResponseDto createProduct(CreateProductDto createProductDto);
    ProductResponseDto updateProduct(Long id, UpdateProductDto updateProductDto);
    ProductResponseDto patchProduct(Long id, PatchProductDto patchProductDto);
    void deleteProduct(Long id);
    void regenerateAllProductImages();
}
