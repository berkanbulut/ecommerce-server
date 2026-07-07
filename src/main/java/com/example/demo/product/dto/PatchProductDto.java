package com.example.demo.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class PatchProductDto {

    @Size(max = 150)
    private String name;

    @Size(max = 150)
    private String slug;

    @Size(max = 150)
    private String sku;

    @Size(max = 300)
    private String shortDescription;

    private String description;

    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @DecimalMin(value = "0.0")
    private BigDecimal salePrice;

    @Pattern(regexp = "TRY|USD|EUR|CHF", message = "Currency must be one of the following: TRY, USD, EUR, CHF")
    private String currency;

    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;

    private Boolean active;

    private Boolean featured;

    private String mainImageUrl;

    @Valid
    private List<CreateProductImageDto> images;

    private Long categoryId;

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

    public Boolean getActive() { return active; }

    public Boolean getFeatured() { return featured; }

    public String getMainImageUrl() { return mainImageUrl; }

    public List<CreateProductImageDto> getImages() { return images; }

    public Long getCategoryId() { return categoryId; }

    public Long getBrandId() { return brandId; }
}