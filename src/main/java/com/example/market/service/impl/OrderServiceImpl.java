package com.example.market.service.impl;

import com.example.market.dto.requests.OrderRequest;
import com.example.market.dto.responses.GenericResponse;
import com.example.market.dto.responses.OrderResponse;
import com.example.market.entity.*;
import com.example.market.mapper.AddressMapper;
import com.example.market.mapper.OrderMapper;
import com.example.market.repository.*;
import com.example.market.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(AddressMapper addressMapper, AddressRepository addressRepository, CartItemRepository cartItemRepository, CartRepository cartRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper, OrderRepository orderRepository, UserRepository userRepository) {

        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public GenericResponse<Void> createOrder(OrderRequest orderRequest, User user) {
        Address shippingAddress = processShippingAddress(orderRequest, user);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        validateCart(cartItems);

        double totalAmount = calculateTotalAmount(cartItems);

        validateUserBalance(user, totalAmount);

        Order order = createOrderEntity(user, shippingAddress, totalAmount);
        Order savedOrder = orderRepository.save(order);

        createOrderItems(cartItems, savedOrder);

        updateUserBalanceAndClearCart(user, totalAmount, cartItems);

        return new GenericResponse<>(true, "Order created successfully", null);
    }

    private Address processShippingAddress(OrderRequest orderRequest, User user) {
        if (orderRequest.getAddressId() != null) {
            return addressRepository.findById(orderRequest.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
        } else {
            Address address = addressMapper.toEntity(orderRequest.getNewAddress());
            address.setUser(user);
            return addressRepository.save(address);
        }
    }

    private void validateCart(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
    }

    private double calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                .sum();
    }

    private void validateUserBalance(User user, double totalAmount) {
        if (user.getBalance() < totalAmount) {
            throw new RuntimeException("Insufficient balance");
        }
    }

    private Order createOrderEntity(User user, Address shippingAddress, double totalAmount) {
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        return order;
    }

    private void createOrderItems(List<CartItem> cartItems, Order savedOrder) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setItem(cartItem.getItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPriceAtPurchase(cartItem.getItem().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
    }

    private void updateUserBalanceAndClearCart(User user, double totalAmount, List<CartItem> cartItems) {
        user.setBalance(user.getBalance() - totalAmount);
        userRepository.save(user);
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    public List<OrderResponse> getUserOrders(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId, User user) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or not belongs to user"));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public GenericResponse<Void> cancelOrder(Long orderId, User user) {
        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or not belongs to user"));

        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Only CREATED orders can be canceled");
        }

        // Bakiyeyi iade et
        user.setBalance(user.getBalance() + order.getTotalAmount());
        userRepository.save(user);

        // Sipariş durumunu güncelle
        order.setStatus("CANCELED");
        orderRepository.save(order);

        return new GenericResponse<>(true, "Order canceled successfully", null);
    }

    @Override
    @Transactional
    public GenericResponse<Void> updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!List.of("CREATED", "PROCESSING", "SHIPPED", "DELIVERED").contains(status)) {
            throw new RuntimeException("Invalid status");
        }

        order.setStatus(status);
        orderRepository.save(order);

        return new GenericResponse<>(true, "Order status updated to " + status, null);
    }
}