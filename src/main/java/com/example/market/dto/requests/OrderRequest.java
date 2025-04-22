package com.example.market.dto.requests;

import lombok.Data;

@Data
public class OrderRequest {
    private Long addressId;
    private AddressRequest newAddress;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public AddressRequest getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(AddressRequest newAddress) {
        this.newAddress = newAddress;
    }
}
