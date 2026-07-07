package com.example.demo.cart.service;

import com.example.demo.cart.dto.AddCartItemDto;
import com.example.demo.cart.dto.CartResponseDto;
import com.example.demo.cart.dto.UpdateCartItemDto;

public interface CartService {

    CartResponseDto getCart();

    CartResponseDto addItem(AddCartItemDto dto);

    CartResponseDto updateItem(Long cartItemId, UpdateCartItemDto dto);

    CartResponseDto removeItem(Long cartItemId);

    CartResponseDto clearCart();
}