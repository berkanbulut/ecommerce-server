package com.example.demo.product.mapper;

import com.example.demo.product.dto.CreateProductImageDto;
import com.example.demo.product.dto.ProductImageResponseDto;
import com.example.demo.product.dto.ProductResponseDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.ProductImage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductResponseDto toResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getSku(),
                product.getShortDescription(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice(),
                product.getCurrency(),
                product.getStockQuantity(),
                product.getStockStatus().name(),
                product.isActive(),
                product.isFeatured(),
                product.getMainImageUrl(),
                toImageResponses(product.getImages()),
                product.getRatingAverage(),
                product.getRatingCount(),
                product.getCategory().getId(),
                product.getBrand().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public ProductImage toImage(CreateProductImageDto dto, Product product) {
        return new ProductImage(
                dto.getImageUrl(),
                dto.getSortOrder(),
                product
        );
    }

    public List<ProductImage> toImages(List<CreateProductImageDto> images, Product product) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .map(image -> toImage(image, product))
                .toList();
    }

    private List<ProductImageResponseDto> toImageResponses(List<ProductImage> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .map(image -> new ProductImageResponseDto(
                        image.getId(),
                        image.getImageUrl(),
                        image.getSortOrder()
                ))
                .toList();
    }
}