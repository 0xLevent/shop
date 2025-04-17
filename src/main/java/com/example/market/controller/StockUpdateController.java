package com.example.market.controller;

import com.example.market.dto.requests.StockUpdateRequest;
import com.example.market.dto.responses.StockUpdateResponse;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class StockUpdateController {

    @MessageMapping("/updateStock")
    @SendTo("/topic/stockUpdates")
    public StockUpdateResponse updateStock(@Valid StockUpdateRequest stockUpdateRequest) {
        if (stockUpdateRequest.getProductName() == null || stockUpdateRequest.getAvailableStock() < 0) {
            throw new IllegalArgumentException("Geçersiz stok güncelleme isteği!");
        }
        return new StockUpdateResponse(stockUpdateRequest.getProductName(), stockUpdateRequest.getAvailableStock(), "Stok güncellendi!");
    }
}


