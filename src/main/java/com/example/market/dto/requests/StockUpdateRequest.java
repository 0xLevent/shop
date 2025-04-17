package com.example.market.dto.requests;

public class StockUpdateRequest {
    private String productName;
    private int availableStock;

    public StockUpdateRequest() {}

    public StockUpdateRequest(String productName, int availableStock) {
        this.productName = productName;
        this.availableStock = availableStock;
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
}
