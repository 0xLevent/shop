package com.example.market.service;

import com.example.market.dto.responses.CartItemResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;

import java.util.List;

public interface CartService {

    void addToCart(int userId, int itemId, int quantity);

    void removeFromCart(int userId, int itemId);

    void updateCartItemQuantity(int userId, int itemId, int quantity);

    Cart getCartByUserId(int userId);

    void clearCart(int userId);

    List<CartItem> getCartItems(int userId);
}