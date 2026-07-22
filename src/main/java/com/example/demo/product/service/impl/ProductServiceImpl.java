package com.example.demo.product.service.impl;

import com.example.demo.brand.entity.Brand;
import com.example.demo.brand.repository.BrandRepository;
import com.example.demo.category.entity.Category;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.PatchProductDto;
import com.example.demo.product.dto.ProductResponseDto;
import com.example.demo.product.dto.UpdateProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.mapper.ProductMapper;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.service.ImageCleanupService;
import com.example.demo.product.service.PexelsImageService;
import com.example.demo.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageCleanupService imageCleanupService;
    private final PexelsImageService pexelsImageService;


    public ProductServiceImpl(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            ProductRepository productRepository,
            ProductMapper productMapper,
            ImageCleanupService imageCleanupService,
            PexelsImageService pexelsImageService

    ) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.imageCleanupService = imageCleanupService;
        this.pexelsImageService = pexelsImageService;

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        return productMapper.toResponse(findProductById(id));
    }

    @Override
    public ProductResponseDto createProduct(CreateProductDto dto) {

        // slug hala frontend geliyor → validate devam
        validateUniqueSlug(dto.getSlug());

        Category category = findCategoryById(dto.getCategoryId());
        Brand brand = findBrandById(dto.getBrandId());

        // 🔥 SKU artık backend’de üretiliyor
        String sku = generateUniqueSku();

        Product product = new Product(
                dto.getName(),
                dto.getSlug(),
                sku,
                dto.getShortDescription(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getSalePrice(),
                dto.getCurrency(),
                dto.getStockQuantity(),
                dto.isActive(),
                dto.isFeatured(),
                dto.getMainImageUrl(),
                category,
                brand
        );

        // images (mevcut mapper mantığını bozmadım)
        product.replaceImages(productMapper.toImages(dto.getImages(), product));

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponseDto updateProduct(Long id, UpdateProductDto dto) {
        Product product = findProductById(id);

        validateUniqueSlugForUpdate(dto.getSlug(), id);
        validateUniqueSkuForUpdate(dto.getSku(), id);

        Category category = findCategoryById(dto.getCategoryId());
        Brand brand = findBrandById(dto.getBrandId());

        List<String> oldImages = collectAllImageUrls(product);

        product.update(
                dto.getName(),
                dto.getSlug(),
                dto.getSku(),
                dto.getShortDescription(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getSalePrice(),
                dto.getCurrency(),
                dto.getStockQuantity(),
                dto.isActive(),
                dto.isFeatured(),
                dto.getMainImageUrl(),
                category,
                brand
        );

        product.replaceImages(productMapper.toImages(dto.getImages(), product));

        cleanupUnusedImages(oldImages, collectAllImageUrls(product));

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponseDto patchProduct(Long id, PatchProductDto dto) {
        Product product = findProductById(id);

        if (dto.getSlug() != null) {
            validateUniqueSlugForUpdate(dto.getSlug(), id);
        }

        if (dto.getSku() != null) {
            validateUniqueSkuForUpdate(dto.getSku(), id);
        }

        Category category = dto.getCategoryId() == null ? null : findCategoryById(dto.getCategoryId());
        Brand brand = dto.getBrandId() == null ? null : findBrandById(dto.getBrandId());

        List<String> oldImages = collectAllImageUrls(product);

        product.patchBasicInfo(
                dto.getName(),
                dto.getSlug(),
                dto.getSku(),
                dto.getShortDescription(),
                dto.getDescription()
        );

        product.patchPrice(
                dto.getPrice(),
                dto.getSalePrice(),
                dto.getCurrency()
        );

        product.patchStock(dto.getStockQuantity());

        product.patchFlags(
                dto.getActive(),
                dto.getFeatured()
        );

        product.patchMainImage(dto.getMainImageUrl());
        product.patchCategory(category);
        product.patchBrand(brand);

        if (dto.getImages() != null) {
            product.replaceImages(productMapper.toImages(dto.getImages(), product));
            cleanupUnusedImages(oldImages, collectAllImageUrls(product));
        }

        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findProductById(id);

        List<String> imageUrls = collectAllImageUrls(product);

        productRepository.delete(product);

        imageUrls.forEach(imageCleanupService::deleteImage);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Product", id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Category", id));
    }

    private Brand findBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Brand", id));
    }

    private void validateUniqueSlug(String slug) {
        if (productRepository.existsBySlug(slug)) {
            throw new ConflictException("Product already exists with slug: " + slug);
        }
    }

    private void validateUniqueSku(String sku) {
        if (productRepository.existsBySku(sku)) {
            throw new ConflictException("Product already exists with sku: " + sku);
        }
    }

    private void validateUniqueSlugForUpdate(String slug, Long id) {
        if (productRepository.existsBySlugAndIdNot(slug, id)) {
            throw new ConflictException("Product already exists with slug: " + slug);
        }
    }

    private void validateUniqueSkuForUpdate(String sku, Long id) {
        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new ConflictException("Product already exists with sku: " + sku);
        }
    }

    private List<String> collectAllImageUrls(Product product) {
        List<String> urls = new java.util.ArrayList<>();

        if (product.getMainImageUrl() != null) {
            urls.add(product.getMainImageUrl());
        }

        if (product.getImages() != null) {
            urls.addAll(
                    product.getImages()
                            .stream()
                            .map(image -> image.getImageUrl())
                            .toList()
            );
        }

        return urls;
    }

    private void cleanupUnusedImages(List<String> oldImages, List<String> newImages) {
        oldImages.stream()
                .filter(oldImage -> !newImages.contains(oldImage))
                .forEach(imageCleanupService::deleteImage);
    }

    private String generateUniqueSku() {
        String sku;

        do {
            sku = "SKU-" + UUID.randomUUID().toString()
                    .substring(0, 8)
                    .toUpperCase();
        } while (productRepository.existsBySku(sku));

        return sku;
    }


    @Override
    @Transactional
    public void regenerateAllProductImages() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            List<String> imageUrls =
                    pexelsImageService.searchImageUrls(product.getName(), 4);

            if (imageUrls.isEmpty()) {
                continue;
            }

            product.patchMainImage(imageUrls.get(0));

            product.replaceImages(List.of());

            for (int i = 1; i < imageUrls.size(); i++) {
                product.addImage(imageUrls.get(i), i);
            }
        }
    }
}