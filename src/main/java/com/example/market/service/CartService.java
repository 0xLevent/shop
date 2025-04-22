package com.example.market.service;

import com.example.market.dto.responses.CartItemResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;

import java.util.List;

public interface CartService {

    void addToCart(Long userId, int itemId, int quantity);

    void removeFromCart(Long userId, int itemId);

    void updateCartItemQuantity(Long userId, int itemId, int quantity);

    Cart getCartByUserId(Long userId);

    void clearCart(Long userId);

    List<CartItem> getCartItems(Long userId);
}