package com.example.demo.order.service.impl;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.entity.CartItem;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.order.dto.CreateOrderDto;
import com.example.demo.order.dto.OrderResponseDto;
import com.example.demo.order.dto.UpdateOrderDto;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderItem;
import com.example.demo.order.mapper.OrderMapper;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.order.service.OrderService;
import com.example.demo.product.entity.Product;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository,
            OrderMapper orderMapper
    ) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponseDto createOrder(CreateOrderDto dto) {
        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new BadRequestException("Cart not found!"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty!");
        }

        validateCartItems(cart);

        BigDecimal subtotal = cart.getTotalPrice();
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal shippingAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal
                .subtract(discountAmount)
                .add(shippingAmount)
                .add(taxAmount);

        String currency = cart.getItems().get(0).getProduct().getCurrency();

        Order order = new Order(
                generateOrderNumber(),
                user,
                dto.getPaymentMethod(),
                subtotal,
                discountAmount,
                shippingAmount,
                taxAmount,
                grandTotal,
                currency,
                dto.getShippingFullName(),
                dto.getShippingPhone(),
                dto.getShippingAddressLine(),
                dto.getShippingCity(),
                dto.getShippingCountry(),
                dto.getShippingPostalCode(),
                dto.getCustomerNote()
        );

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            product.decreaseStock(cartItem.getQuantity());

            OrderItem orderItem = new OrderItem(
                    product,
                    product.getName(),
                    product.getSlug(),
                    product.getMainImageUrl(),
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice(),
                    cartItem.getTotalPrice()
            );

            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        cart.clearItems();

        return orderMapper.toOrderResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders() {
        User user = getCurrentUser();

        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getMyOrderById(Long id) {
        User user = getCurrentUser();

        Order order = orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> NotFoundException.of("Order", id));

        return orderMapper.toOrderResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        Order order = findOrderById(id);
        return orderMapper.toOrderResponseDto(order);
    }

    @Override
    public OrderResponseDto updateOrder(Long id, UpdateOrderDto dto) {
        Order order = findOrderById(id);

        if (dto.getOrderStatus() != null) {
            order.updateOrderStatus(dto.getOrderStatus());
        }

        if (dto.getPaymentStatus() != null) {
            order.updatePaymentStatus(
                    dto.getPaymentStatus(),
                    dto.getPaymentProvider(),
                    dto.getPaymentTransactionId()
            );
        }

        return orderMapper.toOrderResponseDto(order);
    }

    private void validateCartItems(Cart cart) {
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            if (!product.isActive()) {
                throw new BadRequestException(
                        "Product is not active: " + product.getName()
                );
            }

            if (product.getStockQuantity() == null || product.getStockQuantity() <= 0) {
                throw new BadRequestException(
                        "Product is out of stock: " + product.getName()
                );
            }

            if (item.getQuantity() > product.getStockQuantity()) {
                throw new BadRequestException(
                        "Not enough stock for product: " + product.getName()
                );
            }
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Order", id));
    }

    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated!");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        NotFoundException.of("User", "username", username)
                );
    }

    private String generateOrderNumber() {
        return "ORD-" +
                Instant.now().toEpochMilli() +
                "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}