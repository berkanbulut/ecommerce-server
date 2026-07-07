package com.example.demo.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class UpdateProductDto {

    @NotBlank(message = "Name is required!")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Slug is required!")
    @Size(max = 150)
    private String slug;

    @NotBlank(message = "SKU is required!")
    @Size(max = 150)
    private String sku;

    @NotBlank(message = "Short description is required!")
    @Size(max = 300)
    private String shortDescription;

    @NotBlank(message = "Description is required!")
    private String description;

    @NotNull(message = "Price is required!")
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @DecimalMin(value = "0.0")
    private BigDecimal salePrice;

    @NotBlank(message = "Currency is required!")
    @Pattern(regexp = "TRY|USD|EUR|CHF", message = "Currency must be one of the following: TRY, USD, EUR, CHF")
    private String currency;

    @NotNull(message = "Stock quantity is required!")
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;

    private boolean active;

    private boolean featured;

    @NotBlank(message = "Main image url is required!")
    private String mainImageUrl;

    @Valid
    private List<CreateProductImageDto> images;

    @NotNull(message = "Category id is required!")
    private Long categoryId;

    @NotNull(message = "Brand id is required!")
    private Long brandId;

    public String getName() { return name; }

    public String getSlug() { return slug; }

    public String getSku() { return sku; }

    public String getShortDescription() { return shortDescription; }

    public String getDescription() { return description; }

    public BigDecimal getPrice() { return price; }

    public BigDecimal getSalePrice() { return salePrice; }

    public String getCurrency() { return currency; }

    public Integer getStockQuantity() { return stockQuantity; }

    public boolean isActive() { return active; }

    public boolean isFeatured() { return featured; }

    public String getMainImageUrl() { return mainImageUrl; }

    public List<CreateProductImageDto> getImages() { return images; }

    public Long getCategoryId() { return categoryId; }

    public Long getBrandId() { return brandId; }
}