package com.example.demo.order.dto;

import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.entity.PaymentStatus;
import jakarta.validation.constraints.Size;

public class UpdateOrderDto {

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    @Size(max = 100, message = "Payment provider can be max 100 characters!")
    private String paymentProvider;

    @Size(max = 255, message = "Payment transaction id can be max 255 characters!")
    private String paymentTransactionId;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }
}