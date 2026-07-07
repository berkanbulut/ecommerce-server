package com.example.demo.order.mapper;

import com.example.demo.order.dto.OrderItemResponseDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default OrderResponseDto toOrderResponseDto(Order order) {

        List<OrderItemResponseDto> items = order.getItems()
                .stream()
                .map(this::toOrderItemResponseDto)
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getUser().getEmail(),

                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getPaymentMethod(),

                order.getPaymentProvider(),
                order.getPaymentTransactionId(),

                order.getSubtotal(),
                order.getDiscountAmount(),
                order.getShippingAmount(),
                order.getTaxAmount(),
                order.getGrandTotal(),
                order.getCurrency(),

                order.getShippingFullName(),
                order.getShippingPhone(),
                order.getShippingAddressLine(),
                order.getShippingCity(),
                order.getShippingCountry(),
                order.getShippingPostalCode(),
                order.getCustomerNote(),

                items,

                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    default OrderItemResponseDto toOrderItemResponseDto(OrderItem item) {
        return new OrderItemResponseDto(
                item.getId(),
                item.getProduct().getId(),
                item.getProductName(),
                item.getProductSlug(),
                item.getProductImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}