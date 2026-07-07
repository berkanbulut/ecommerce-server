package com.example.demo.order.service;

import com.example.demo.order.dto.CreateOrderDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.dto.UpdateOrderDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(CreateOrderDto dto);

    List<OrderResponseDto> getMyOrders();

    OrderResponseDto getMyOrderById(Long id);

    List<OrderResponseDto> getOrders();

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrder(Long id, UpdateOrderDto dto);
}