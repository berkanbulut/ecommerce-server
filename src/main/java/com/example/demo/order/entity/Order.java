package com.example.demo.order.entity;

import com.example.demo.common.base.BaseEntity;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_orders_order_number", columnNames = "order_number")
        }
)
public class Order extends BaseEntity {

    @Column(name = "order_number", nullable = false, length = 50)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_orders_user")
    )
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PaymentMethod paymentMethod = PaymentMethod.CASH_ON_DELIVERY;

    @Column(length = 100)
    private String paymentProvider;

    @Column(length = 255)
    private String paymentTransactionId;

    @Column(length = 255)
    private String stripeSessionId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 150)
    private String shippingFullName;

    @Column(nullable = false, length = 30)
    private String shippingPhone;

    @Column(nullable = false, length = 255)
    private String shippingAddressLine;

    @Column(nullable = false, length = 100)
    private String shippingCity;

    @Column(nullable = false, length = 100)
    private String shippingCountry;

    @Column(nullable = false, length = 30)
    private String shippingPostalCode;

    @Column(length = 500)
    private String customerNote;

    protected Order() {
    }

    public Order(
            String orderNumber,
            User user,
            PaymentMethod paymentMethod,
            BigDecimal subtotal,
            BigDecimal discountAmount,
            BigDecimal shippingAmount,
            BigDecimal taxAmount,
            BigDecimal grandTotal,
            String currency,
            String shippingFullName,
            String shippingPhone,
            String shippingAddressLine,
            String shippingCity,
            String shippingCountry,
            String shippingPostalCode,
            String customerNote
    ) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.shippingAmount = shippingAmount;
        this.taxAmount = taxAmount;
        this.grandTotal = grandTotal;
        this.currency = currency;
        this.shippingFullName = shippingFullName;
        this.shippingPhone = shippingPhone;
        this.shippingAddressLine = shippingAddressLine;
        this.shippingCity = shippingCity;
        this.shippingCountry = shippingCountry;
        this.shippingPostalCode = shippingPostalCode;
        this.customerNote = customerNote;

        this.orderStatus = OrderStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.assignOrder(this);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void markPaymentPaid(String paymentProvider, String paymentTransactionId) {
        this.paymentStatus = PaymentStatus.PAID;
        this.paymentProvider = paymentProvider;
        this.paymentTransactionId = paymentTransactionId;
    }

    public void markPaymentFailed(String paymentProvider, String paymentTransactionId) {
        this.paymentStatus = PaymentStatus.FAILED;
        this.paymentProvider = paymentProvider;
        this.paymentTransactionId = paymentTransactionId;
    }

    public void markPaymentRefunded(String paymentProvider, String paymentTransactionId) {
        this.paymentStatus = PaymentStatus.REFUNDED;
        this.paymentProvider = paymentProvider;
        this.paymentTransactionId = paymentTransactionId;
    }




    public void updatePaymentStatus(
            PaymentStatus paymentStatus,
            String paymentProvider,
            String paymentTransactionId
    ) {
        this.paymentStatus = paymentStatus;
        this.paymentProvider = paymentProvider;
        this.paymentTransactionId = paymentTransactionId;
    }

    public void attachStripeSession(String stripeSessionId) {
        this.stripeSessionId = stripeSessionId;
    }



    public String getOrderNumber() { return orderNumber; }
    public User getUser() { return user; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getPaymentProvider() { return paymentProvider; }
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public String getStripeSessionId() {
        return stripeSessionId;
    }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getShippingAmount() { return shippingAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public String getCurrency() { return currency; }
    public String getShippingFullName() { return shippingFullName; }
    public String getShippingPhone() { return shippingPhone; }
    public String getShippingAddressLine() { return shippingAddressLine; }
    public String getShippingCity() { return shippingCity; }
    public String getShippingCountry() { return shippingCountry; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public String getCustomerNote() { return customerNote; }
}