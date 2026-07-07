package com.example.demo.order.controller;

import com.example.demo.order.dto.CreateOrderDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.dto.UpdateOrderDto;
import com.example.demo.order.service.OrderService;
import com.example.demo.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/orders")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Order created successfully!",
                        orderService.createOrder(dto)
                ));
    }

    @GetMapping("/api/v1/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getMyOrders() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders retrieved successfully!",
                        orderService.getMyOrders()
                )
        );
    }

    @GetMapping("/api/v1/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getMyOrderById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order retrieved successfully!",
                        orderService.getMyOrderById(id)
                )
        );
    }

    @PreAuthorize("hasAuthority('order:read')")
    @GetMapping("/api/v1/admin/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrders() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders retrieved successfully!",
                        orderService.getOrders()
                )
        );
    }

    @PreAuthorize("hasAuthority('order:read')")
    @GetMapping("/api/v1/admin/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order retrieved successfully!",
                        orderService.getOrderById(id)
                )
        );
    }

    @PreAuthorize("hasAuthority('order:update')")
    @PatchMapping("/api/v1/admin/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderDto dto
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order updated successfully!",
                        orderService.updateOrder(id, dto)
                )
        );
    }
}