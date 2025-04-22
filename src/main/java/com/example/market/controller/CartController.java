package com.example.market.controller;

import com.example.market.Security.jwt.CustomUserDetails;
import com.example.market.dto.requests.CartItemRequest;
import com.example.market.dto.responses.CartItemResponse;
import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;
import com.example.market.mapper.CartItemMapper;
import com.example.market.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart/")
@CrossOrigin(origins = "http://localhost:8282", allowCredentials = "true")

public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService, CartItemMapper cartItemMapper) {
        this.cartService = cartService;
        this.cartItemMapper = cartItemMapper;
    }

    private final CartItemMapper cartItemMapper;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemRequest request) {
        System.out.println("🧪 addToCart LOG → request geldi");
        System.out.println("🧪 itemId: " + request.getItemId());
        System.out.println("🧪 quantity: " + request.getQuantity());

        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            logger.warn("User not authenticated");
            return ResponseEntity.status(401).body("User not authenticated");
        }
        System.out.println("🚀 addToCart() çağrıldı → userId: " + userId + ", itemId: " + request.getItemId() + ", quantity: " + request.getQuantity());

        try {
            cartService.addToCart(userId, request.getItemId(), request.getQuantity());
            return ResponseEntity.ok("Ürün sepete eklendi");
        } catch (Exception e) {
            logger.error("Error adding to cart: ", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam int itemId) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.ok("Ürün sepetten çıkarıldı");
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<String> updateCartItemQuantity(@RequestParam int itemId, @RequestParam int quantity) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok("Sepet güncellendi");
    }

    @GetMapping("/user")
    public ResponseEntity<Cart> getCartByUserId() {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }

        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        cartService.clearCart(userId);
        return ResponseEntity.ok("Sepet temizlendi");
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems() {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }

        List<CartItem> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItemMapper.toResponseList(cartItems));

    }

}
