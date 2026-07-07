package com.example.demo.cart.mapper;

import com.example.demo.cart.dto.CartItemResponseDto;
import com.example.demo.cart.dto.CartResponseDto;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    default CartResponseDto toCartResponseDto(Cart cart) {

        List<CartItemResponseDto> items = cart.getItems()
                .stream()
                .map(this::toCartItemResponseDto)
                .toList();

        Integer totalItems = cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        String currency = cart.getItems().isEmpty()
                ? null
                : cart.getItems().get(0).getProduct().getCurrency();

        return new CartResponseDto(
                cart.getId(),
                items,
                totalItems,
                cart.getTotalPrice(),
                currency
        );
    }

    default CartItemResponseDto toCartItemResponseDto(CartItem item) {

        return new CartItemResponseDto(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getSlug(),
                item.getProduct().getMainImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getProduct().getCurrency(),
                item.getProduct().getStockQuantity()
        );
    }
}