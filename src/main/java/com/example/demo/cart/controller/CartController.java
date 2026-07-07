package com.example.demo.cart.controller;

import com.example.demo.cart.dto.AddCartItemDto;
import com.example.demo.cart.dto.CartResponseDto;
import com.example.demo.cart.dto.UpdateCartItemDto;
import com.example.demo.cart.service.CartService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart retrieved successfully!",
                        cartService.getCart()
                )
        );
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItem(
            @Valid @RequestBody AddCartItemDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item added to cart successfully!",
                        cartService.addItem(dto)
                )
        );
    }
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart item updated successfully!",
                        cartService.updateItem(cartItemId, dto)
                )
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeItem(
            @PathVariable Long cartItemId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Item removed from cart successfully!",
                        cartService.removeItem(cartItemId)
                )
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<CartResponseDto>> clearCart() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cart cleared successfully!",
                        cartService.clearCart()
                )
        );
    }
}