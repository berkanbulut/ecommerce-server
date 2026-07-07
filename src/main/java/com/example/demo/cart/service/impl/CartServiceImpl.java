package com.example.demo.cart.service.impl;

import com.example.demo.cart.dto.AddCartItemDto;
import com.example.demo.cart.dto.CartResponseDto;
import com.example.demo.cart.dto.UpdateCartItemDto;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.entity.CartItem;
import com.example.demo.cart.mapper.CartMapper;
import com.example.demo.cart.repository.CartItemRepository;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.cart.service.CartService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    @Transactional
    public CartResponseDto getCart() {
        Cart cart = getOrCreateCart();
        return cartMapper.toCartResponseDto(cart);
    }

    @Override
    public CartResponseDto addItem(AddCartItemDto dto) {
        Cart cart = getOrCreateCart();

        Product product = findProductById(dto.getProductId());

        validateProductForCart(product);

        CartItem existingItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + dto.getQuantity();

            validateStock(product, newQuantity);

            existingItem.updateQuantity(newQuantity);
            existingItem.refreshPrice(resolveProductPrice(product));
        } else {
            validateStock(product, dto.getQuantity());

            CartItem cartItem = new CartItem(
                    product,
                    dto.getQuantity(),
                    resolveProductPrice(product)
            );

            cart.addItem(cartItem);
        }

        cart.recalculateTotal();

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toCartResponseDto(savedCart);
    }

    @Override
    public CartResponseDto updateItem(Long cartItemId, UpdateCartItemDto dto) {
        Cart cart = getOrCreateCart();

        CartItem cartItem = findCartItemById(cartItemId);

        validateCartItemBelongsToCart(cartItem, cart);

        Product product = cartItem.getProduct();

        validateProductForCart(product);
        validateStock(product, dto.getQuantity());

        cartItem.updateQuantity(dto.getQuantity());
        cartItem.refreshPrice(resolveProductPrice(product));

        cart.recalculateTotal();

        return cartMapper.toCartResponseDto(cart);
    }

    @Override
    public CartResponseDto removeItem(Long cartItemId) {
        Cart cart = getOrCreateCart();

        CartItem cartItem = findCartItemById(cartItemId);

        validateCartItemBelongsToCart(cartItem, cart);

        cart.removeItem(cartItem);
        cart.recalculateTotal();

        return cartMapper.toCartResponseDto(cart);
    }

    @Override
    public CartResponseDto clearCart() {
        Cart cart = getOrCreateCart();

        cart.clearItems();
        cart.recalculateTotal();

        return cartMapper.toCartResponseDto(cart);
    }

    private Cart getOrCreateCart() {
        User user = getCurrentUser();

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
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

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> NotFoundException.of("Product", productId));
    }

    private CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> NotFoundException.of("Cart item", cartItemId));
    }

    private void validateCartItemBelongsToCart(CartItem cartItem, Cart cart) {
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to current user cart!");
        }
    }

    private void validateProductForCart(Product product) {
        if (!product.isActive()) {
            throw new BadRequestException("Product is not active!");
        }

        if (product.getStockQuantity() == null || product.getStockQuantity() <= 0) {
            throw new BadRequestException("Product is out of stock!");
        }
    }

    private void validateStock(Product product, Integer requestedQuantity) {
        if (requestedQuantity > product.getStockQuantity()) {
            throw new BadRequestException(
                    "Not enough stock for product: " + product.getName()
            );
        }
    }

    private BigDecimal resolveProductPrice(Product product) {
        return product.getSalePrice() != null
                ? product.getSalePrice()
                : product.getPrice();
    }
}