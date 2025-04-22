package com.example.market.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;
import com.example.market.entity.Item;
import com.example.market.entity.User;
import com.example.market.mapper.CartItemMapper;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.CartRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.UserRepository;
import com.example.market.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @PersistenceContext
    private EntityManager entityManager;


    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartServiceImpl(CartItemMapper cartItemMapper, CartItemRepository cartItemRepository, CartRepository cartRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addToCart(Long userId, int itemId, int quantity) {
        System.out.println("DEBUG: addToCart fonksiyonu çağrıldı → userId: " + userId + ", itemId: " + itemId + ", quantity: " + quantity);

        User user = userRepository.findByIdWithCart(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cart = cartRepository.save(cart);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndItem(cart, item);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setItem(item);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);
            entityManager.flush();
        }
    }




    @Override
    @Transactional
    public void removeFromCart(Long userId, int itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new RuntimeException("Sepet bulunamadı");
        }

        List<CartItem> itemsToRemove = cart.getItems().stream()
                .filter(item -> item.getItem().getId() == itemId)
                .toList();

        if (!itemsToRemove.isEmpty()) {
            cart.getItems().removeAll(itemsToRemove);
            cartItemRepository.deleteAll(itemsToRemove);
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long userId, int itemId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new RuntimeException("Sepet bulunamadı");
        }

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getItem().getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sepette bu ürün bulunamadı"));

        if (quantity <= 0) {
            removeFromCart(userId, itemId);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            user.setCart(cart);
            cartRepository.save(cart);
            userRepository.save(user);
        }

        return cart;
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new RuntimeException("Sepet bulunamadı");
        }

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public List<CartItem> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Cart cart = cartRepository.findByUserWithItems(user)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));

        logger.info("Cart items count for user {}: {}", userId, cart.getItems().size());

        return cart.getItems();
    }

}