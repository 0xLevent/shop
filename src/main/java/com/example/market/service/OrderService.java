package com.example.market.service;

import com.example.market.dto.requests.OrderRequest;
import com.example.market.dto.responses.GenericResponse;
import com.example.market.dto.responses.OrderResponse;
import com.example.market.entity.User;

import java.util.List;

public interface OrderService {
    GenericResponse<Void> createOrder(OrderRequest orderRequest, User user);

    List<OrderResponse> getUserOrders(User user);

    OrderResponse getOrderDetails(Long orderId, User user);

    GenericResponse<Void> cancelOrder(Long orderId, User user);

    GenericResponse<Void> updateOrderStatus(Long orderId, String status);
}