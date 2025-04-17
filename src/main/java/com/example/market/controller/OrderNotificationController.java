package com.example.market.controller;
import com.example.market.dto.responses.OrderStatusResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class OrderNotificationController {
    @MessageMapping("/orderStatus")
    @SendTo("/topic/orderUpdates")
    public OrderStatusResponse sendOrderStatus(OrderStatusResponse orderStatusResponse) {
        return orderStatusResponse;
    }
}
