package com.example.demo.storefront.service.impl;

import com.example.demo.brand.entity.Brand;
import com.example.demo.brand.repository.BrandRepository;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.exception.NotFoundException;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.storefront.dto.*;
import com.example.demo.storefront.service.StorefrontService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StorefrontServiceImpl implements StorefrontService {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public StorefrontServiceImpl(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            ProductRepository productRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<StorefrontCategoryDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toCategoryDto)
                .toList();
    }

    @Override
    public List<StorefrontBrandDto> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(this::toBrandDto)
                .toList();
    }

    @Override
    public List<StorefrontProductCardDto> getProducts(
            String search,
            Long categoryId,
            Long brandId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean featured,
            String sort
    ) {
        Specification<Product> specification = buildSpecification(
                search,
                categoryId,
                brandId,
                minPrice,
                maxPrice,
                featured
        );

        return productRepository.findAll(specification, resolveSort(sort))
                .stream()
                .map(this::toProductCardDto)
                .toList();
    }

    @Override
    public StorefrontProductDetailDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .filter(Product::isActive)
                .orElseThrow(() ->
                        new NotFoundException("Product not found with slug: " + slug)
                );

        return toProductDetailDto(product);
    }

    @Override
    public List<StorefrontProductCardDto> getFeaturedProducts(int limit) {
        return getProducts(
                null,
                null,
                null,
                null,
                null,
                true,
                "newest"
        ).stream().limit(limit).toList();
    }

    @Override
    public List<StorefrontProductCardDto> getNewArrivals(int limit) {
        return getProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                "newest"
        ).stream().limit(limit).toList();
    }

    private Specification<Product> buildSpecification(
            String search,
            Long categoryId,
            Long brandId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean featured
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    criteriaBuilder.isTrue(root.get("active"))
            );

            if (search != null && !search.isBlank()) {
                String keyword = "%" + search.toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")),
                                        keyword
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("shortDescription")),
                                        keyword
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")),
                                        keyword
                                )
                        )
                );
            }

            if (categoryId != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );
            }

            if (brandId != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("brand").get("id"),
                                brandId
                        )
                );
            }

            if (minPrice != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice
                        )
                );
            }

            if (maxPrice != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice
                        )
                );
            }

            if (featured != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("featured"),
                                featured
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }

    private Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        return switch (sort) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "popular" -> Sort.by(Sort.Direction.DESC, "ratingAverage");
            default -> Sort.by(Sort.Direction.DESC, "id");
        };
    }

    private StorefrontProductCardDto toProductCardDto(Product product) {
        return new StorefrontProductCardDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getPrice(),
                product.getSalePrice(),
                product.getCurrency(),
                product.getMainImageUrl(),
                product.getStockStatus().name(),
                product.getRatingAverage(),
                product.getRatingCount()
        );
    }

    private StorefrontProductDetailDto toProductDetailDto(Product product) {
        return new StorefrontProductDetailDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getShortDescription(),
                product.getDescription(),
                product.getPrice(),
                product.getSalePrice(),
                product.getCurrency(),
                product.getMainImageUrl(),
                product.getImages()
                        .stream()
                        .map(image ->
                                new StorefrontProductImageDto(
                                        image.getImageUrl(),
                                        image.getSortOrder()
                                )
                        )
                        .toList(),
                product.getStockStatus().name(),
                product.getStockQuantity(),
                product.getRatingAverage(),
                product.getRatingCount(),
                toCategoryDto(product.getCategory()),
                toBrandDto(product.getBrand())
        );
    }

    private StorefrontCategoryDto toCategoryDto(Category category) {
        return new StorefrontCategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private StorefrontBrandDto toBrandDto(Brand brand) {
        return new StorefrontBrandDto(
                brand.getId(),
                brand.getName(),
                brand.getDescription()
        );
    }
}