package com.example.demo.product.entity;

import com.example.demo.common.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_images_product"))
    private Product product;

    protected ProductImage() {
    }

    public ProductImage(String imageUrl, Integer sortOrder, Product product) {
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.product = product;
    }

    public String getImageUrl() { return imageUrl; }

    public Integer getSortOrder() { return sortOrder; }

    public Product getProduct() { return product; }
}