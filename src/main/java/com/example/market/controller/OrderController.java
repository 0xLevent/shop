package com.example.market.controller;

import com.example.market.dto.requests.OrderRequest;
import com.example.market.dto.responses.GenericResponse;
import com.example.market.dto.responses.OrderResponse;
import com.example.market.entity.User;
import com.example.market.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public GenericResponse createOrder(@RequestBody OrderRequest orderRequest,
                                       @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequest, user);
    }

    @GetMapping
    public List<OrderResponse> getUserOrders(@AuthenticationPrincipal User user) {
        return orderService.getUserOrders(user);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderDetails(@PathVariable Long id,
                                         @AuthenticationPrincipal User user) {
        return orderService.getOrderDetails(id, user);
    }

    @PostMapping("/{id}/cancel")
    public GenericResponse cancelOrder(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        return orderService.cancelOrder(id, user);
    }

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
}