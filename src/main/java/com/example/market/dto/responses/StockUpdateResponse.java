package com.example.market.dto.responses;

public class StockUpdateResponse {
    private String productName;
    private int availableStock;
    private String message;

    public StockUpdateResponse() {}

    public StockUpdateResponse(String productName, int availableStock, String message) {
        this.productName = productName;
        this.availableStock = availableStock;
        this.message = message;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
