package com.example.demo.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDto(

        Long id,

        List<CartItemResponseDto> items,

        Integer totalItems,

        BigDecimal subtotal,

        String currency

) {}