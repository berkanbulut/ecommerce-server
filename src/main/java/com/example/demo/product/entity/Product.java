package com.example.demo.product.entity;

import com.example.demo.brand.entity.Brand;
import com.example.demo.category.entity.Category;
import com.example.demo.common.base.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_slug", columnNames = "slug"),
                @UniqueConstraint(name = "uk_products_sku", columnNames = "sku")
        }
)
public class Product extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String slug;

    @Column(nullable = false, length = 150)
    private String sku;

    @Column(nullable = false, length = 300)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StockStatus stockStatus;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "is_featured", nullable = false)
    private boolean featured = false;

    @Column(nullable = false)
    private Double ratingAverage = 0.0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    @Column(nullable = false)
    private String mainImageUrl;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("sortOrder ASC")
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_category")
    )
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "brand_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_brand")
    )
    private Brand brand;

    protected Product() {
    }

    public Product(
            String name,
            String slug,
            String sku,
            String shortDescription,
            String description,
            BigDecimal price,
            BigDecimal salePrice,
            String currency,
            Integer stockQuantity,
            boolean active,
            boolean featured,
            String mainImageUrl,
            Category category,
            Brand brand
    ) {
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.currency = currency;
        this.stockQuantity = stockQuantity;
        this.active = active;
        this.featured = featured;
        this.mainImageUrl = mainImageUrl;
        this.category = category;
        this.brand = brand;

        this.ratingAverage = 0.0;
        this.ratingCount = 0;

        refreshStockStatus();
    }

    public void update(
            String name,
            String slug,
            String sku,
            String shortDescription,
            String description,
            BigDecimal price,
            BigDecimal salePrice,
            String currency,
            Integer stockQuantity,
            boolean active,
            boolean featured,
            String mainImageUrl,
            Category category,
            Brand brand
    ) {
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.currency = currency;
        this.stockQuantity = stockQuantity;
        this.active = active;
        this.featured = featured;
        this.mainImageUrl = mainImageUrl;
        this.category = category;
        this.brand = brand;

        refreshStockStatus();
    }

    public void patchBasicInfo(
            String name,
            String slug,
            String sku,
            String shortDescription,
            String description
    ) {
        if (name != null) this.name = name;
        if (slug != null) this.slug = slug;
        if (sku != null) this.sku = sku;
        if (shortDescription != null) this.shortDescription = shortDescription;
        if (description != null) this.description = description;
    }

    public void patchPrice(
            BigDecimal price,
            BigDecimal salePrice,
            String currency
    ) {
        if (price != null) this.price = price;
        if (salePrice != null) this.salePrice = salePrice;
        if (currency != null) this.currency = currency;
    }

    public void patchStock(Integer stockQuantity) {
        if (stockQuantity != null) {
            this.stockQuantity = stockQuantity;
            refreshStockStatus();
        }
    }

    public void patchFlags(Boolean active, Boolean featured) {
        if (active != null) this.active = active;
        if (featured != null) this.featured = featured;
    }

    public void patchMainImage(String mainImageUrl) {
        if (mainImageUrl != null) {
            this.mainImageUrl = mainImageUrl;
        }
    }

    public void patchCategory(Category category) {
        if (category != null) {
            this.category = category;
        }
    }

    public void patchBrand(Brand brand) {
        if (brand != null) {
            this.brand = brand;
        }
    }

    public void replaceImages(List<ProductImage> newImages) {

        this.images.clear();

        if (newImages != null) {
            for (ProductImage image : newImages) {
                addImage(
                        image.getImageUrl(),
                        image.getSortOrder()
                );
            }
        }
    }

    public void addImage(String imageUrl, Integer sortOrder) {
        this.images.add(
                new ProductImage(imageUrl, sortOrder, this)
        );
    }

    private void refreshStockStatus() {
        this.stockStatus =
                this.stockQuantity != null && this.stockQuantity > 0
                        ? StockStatus.IN_STOCK
                        : StockStatus.OUT_OF_STOCK;
    }

    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return;
        }

        this.stockQuantity -= quantity;
        refreshStockStatus();
    }
    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getSku() {
        return sku;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFeatured() {
        return featured;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public Category getCategory() {
        return category;
    }

    public Brand getBrand() {
        return brand;
    }

}
