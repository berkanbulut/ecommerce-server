package com.example.demo.order.entity;

import com.example.demo.common.base.BaseEntity;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_items_order")
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_items_product")
    )
    private Product product;

    @Column(nullable = false, length = 150)
    private String productName;

    @Column(nullable = false, length = 150)
    private String productSlug;

    @Column(nullable = false, length = 255)
    private String productImageUrl;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    protected OrderItem() {
    }

    public OrderItem(
            Product product,
            String productName,
            String productSlug,
            String productImageUrl,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice
    ) {
        this.product = product;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productImageUrl = productImageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() { return order; }
    public Product getProduct() { return product; }
    public String getProductName() { return productName; }
    public String getProductSlug() { return productSlug; }
    public String getProductImageUrl() { return productImageUrl; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
}